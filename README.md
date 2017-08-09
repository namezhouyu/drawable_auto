# drawable_auto
自动移动不同的像素文件到andorid对应的mipmap-*／drawable-*文件夹
这是一款andorid studio插件 用idea开发

在android开发中需要大量的添加图标文件，不同分辨率需要适配。当设计给你一堆不同分辨率的图 你一个一个去复制粘贴重命名的时候会不会觉得累？
这个插件旨在解决这个问题，以下是该插件规范：
1.设计给你图标文件命名必须规范，比如 测试图片@2x 测试图片@3x，分别对应-xhdpi和-xxhdpi
2.文件名必须包含‘@’字符，用于不同分辨率图片的归类
3.仅支持jpg和png格式的文件

总的来说只有一个图片文件命名规范 某某某@2x，某某某@3x，分别对应-xhdpi和-xxhdpi

功能：
1.允许选择图标文件导入到drawable-*或者mipmap-*
在该插件运行一次后（即安装完点击Edit->DrawableAuto）,会在当前项目根目录生成drawable_auto.config文件，默认内容为mipmap，即插件会把图片文件导入到mipmap-*
文件夹下，如需修改，请将mipmap删掉，输入drawable，切记修改完要保存。
2.允许多选。比如同时选择 测试图片1@2x 测试图片1@3x 测试图片2@2x 测试图片2@3x,下一步可以在对话框中为测试图片1和测试图片2设置图标名称

src/drawable_auto.jar 可直接通过本地插件安装的方式直接在android studio中安装。重启后Edit菜单下会出现DrawableAuto
