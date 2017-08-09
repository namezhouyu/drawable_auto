package com.fish;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.http.util.TextUtils;

import java.io.*;
import java.util.*;

/**
 * Created by zhouxiaodong on 2017/8/4.
 */
public class DrawableAutoAction extends AnAction implements SetFileNameDialog.OnInputFinishListener {
    String resRootPath;//资源文件res目录
    String fileHeader;//drawable-* or mipmap-*
    String typeSplitChar = "@";//不同分辨率图片命名分隔符 如:测试图片@2x 测试图片@3x,'@'前认为是真实的图片名字
    String splitChar1 = "@2x";//通过文件名区分不同分辨率 对应xh-dpi
    String splitChar2 = "@3x";//通过文件名区分不同分辨率 对应xxh-dpi
    Map<String, Set<VirtualFile>> maps;//key：文件名字（'@'之前字符串） val:不同分辨率的多个虚拟文件

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (null == e.getProject()) {
            return;
        }
        String productRootPath = e.getProject().getBasePath();
        resRootPath = productRootPath + "/app/src/main/res/";
        String configFileName = productRootPath + "/drawable_auto.config";

        initConfig(configFileName);

        fileHeader = getHeaderName(configFileName);

        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createMultipleFilesNoJarsDescriptor();
        descriptor.setTitle("请选择图标文件");
        VirtualFile[] virtualFile = FileChooser.chooseFiles(descriptor, null, null);

        File dir = new File(resRootPath + fileHeader + "-xhdpi");
        if (!dir.exists()) {
            dir.mkdir();
        }
        dir = new File(resRootPath + fileHeader + "-xxhdpi");
        if (!dir.exists()) {
            dir.mkdir();
        }

        //筛选合法文件
        Set<VirtualFile> files = new HashSet<>();
        for (int i = 0; i < virtualFile.length; i++) {
            String name = virtualFile[i].getName();
            if (!(name.endsWith(".png") || name.endsWith(".jpg"))) {
                Messages.showMessageDialog(name + "不是png或者jpg类型的文件", "文件被忽略", null);
            } else if (name.contains(typeSplitChar)) {
                files.add(virtualFile[i]);
            } else {
                Messages.showMessageDialog(name + "文件名称中不包含分隔符\"" + typeSplitChar + "\"", "文件被忽略", null);
            }
        }

        //分类
        List<String> keys = new ArrayList<>();
        maps = new HashMap<>();
        for (VirtualFile file : files) {
            String name = file.getName();
            name = name.substring(0, name.indexOf(typeSplitChar));
            if (!keys.contains(name)) {
                keys.add(name);
            }
        }
        for (String key : keys) {
            Set<VirtualFile> val = new HashSet<>();
            maps.put(key, val);
            for (VirtualFile file : files) {
                String name = file.getName();
                if (name.contains(key)) {
                    val.add(file);
                }
            }
        }
        if (keys.size() > 0) {
            new SetFileNameDialog(keys).setOnInputFinishListener(this).show();
        }
    }

    /**
     * 初始化配置 导入到mipmap或者drawable
     *
     * @param fileName 配置文件路径
     */
    private void initConfig(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                FileWriter writer = new FileWriter(fileName);
                writer.write("mipmap");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取导入文件  mipmap或者drawable
     *
     * @param fileName 配置文件路径
     * @return
     */
    private String getHeaderName(String fileName) {
        String headName = "";
        int c;
        try {
            FileReader reader = new FileReader(fileName);
            c = reader.read();
            while (c != -1) {
                headName += (char) c;
                c = reader.read();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!(headName.equals("mipmap") || headName.equals("drawable"))) {
            headName = "mipmap";
            Messages.showMessageDialog("目标文件夹仅支持mipmap-*或者drawable-*", "配置重置为mipmap-*", null);
            //重置配置
            File file = new File(fileName);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            initConfig(fileName);
        }
        return headName;
    }

    @Override
    public void finish(List<SourceNameModel> names) {
        try {
            String msg = "";
            for (SourceNameModel sourceName : names) {
                Set<VirtualFile> val = maps.get(sourceName.getOriginName());
                if (null != val) {
                    for (VirtualFile file : val) {
                        InputStream initialStream = file.getInputStream();
                        byte[] buffer = new byte[initialStream.available()];
                        initialStream.read(buffer);
                        if (file.getName().contains(splitChar1)) {
                            File targetFile = new File(resRootPath + fileHeader + "-xhdpi/" + sourceName.getNewName() + ".png");
                            OutputStream outStream = new FileOutputStream(targetFile);
                            outStream.write(buffer);

                            msg += sourceName.getOriginName() + "成功导入到" + fileHeader + "-xhdpi/文件夹，名字为" + sourceName.getNewName() + "\n";
                        } else if (file.getName().contains(splitChar2)) {
                            File targetFile = new File(resRootPath + fileHeader + "-xxhdpi/" + sourceName.getNewName() + ".png");
                            OutputStream outStream = new FileOutputStream(targetFile);
                            outStream.write(buffer);

                            msg += sourceName.getOriginName() + "成功导入到" + fileHeader + "-xxhdpi/文件夹，名字为" + sourceName.getNewName() + "\n";
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(msg)) {
                Messages.showMessageDialog(msg, "导入完成", null);
            }
        } catch (IOException e1) {
            Messages.showMessageDialog(e1.getMessage(), "遇到异常了~", null);
        }
    }
}
