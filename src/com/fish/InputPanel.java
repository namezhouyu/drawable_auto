package com.fish;

import org.apache.http.util.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by zhouxiaodong on 2017/8/9.
 * 输入文件名的 JLabel+JTextField组合的 JPanel
 */
public class InputPanel extends JPanel {
    private JTextField[] fields;

    public InputPanel(java.util.List<String> data) {
        JPanel panel = new JPanel(new GridLayout(data.size(), 1));
        add(panel, BorderLayout.CENTER);
        fields = new JTextField[data.size()];
        for (int i = 0; i < data.size(); i += 1) {
            fields[i] = new JTextField();
            fields[i].setColumns(8);
            String labName = data.get(i);
            fields[i].setName(labName);
            JLabel lab = new JLabel(labName, JLabel.RIGHT);
            lab.setLabelFor(fields[i]);
            panel.add(lab);
            panel.add(fields[i]);
        }
    }

    public List<SourceNameModel> getData() {
        List<SourceNameModel> names = new ArrayList<>();
        for (JTextField field : fields) {
            if (TextUtils.isEmpty(field.getText())) {
                return null;
            }
            SourceNameModel name = new SourceNameModel();
            name.setOriginName(field.getName());
            name.setNewName(field.getText());
            names.add(name);
        }
        return names;
    }
}

