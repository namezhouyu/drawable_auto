package com.fish;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

/**
 * 输入文件名的对话框
 */
public class SetFileNameDialog extends JDialog {
    private OnInputFinishListener onInputFinishListener;

    public SetFileNameDialog setOnInputFinishListener(OnInputFinishListener onInputFinishListener) {
        this.onInputFinishListener = onInputFinishListener;
        return this;
    }

    public SetFileNameDialog(List<String> labels) {
        InputPanel inputPanel = new InputPanel(labels);
        setModal(true);
        if (labels.size() > 1) {
            setSize(400, (labels.size() - 1) * 38 + 60);
        } else {
            setSize(400, 80);
        }
        setLocation(400, 400);
        setTitle("请给图标命名");
        JButton btnOk = new JButton("确定");
        inputPanel.add(btnOk);
        setContentPane(inputPanel);
        getRootPane().setDefaultButton(btnOk);
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (null != onInputFinishListener) {
                    List<SourceNameModel> data = inputPanel.getData();
                    if (null != data) {
                        onInputFinishListener.finish(data);
                        dispose();
                    }
                }
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        inputPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onCancel() {
        dispose();
    }

    public interface OnInputFinishListener {
        void finish(List<SourceNameModel> names);
    }
}
