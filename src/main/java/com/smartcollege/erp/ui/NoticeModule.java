package com.smartcollege.erp.ui;

import com.smartcollege.erp.model.DataStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NoticeModule extends JPanel {
    private Dashboard dashboard;
    private DefaultListModel<String> listModel;

    public NoticeModule(Dashboard dashboard) {
        this.dashboard = dashboard;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.DARK_GRAY);
        JTextField noticeField = new JTextField();
        JButton addBtn = new JButton("Add Notice");
        addBtn.setBackground(new Color(30,144,255)); addBtn.setForeground(Color.WHITE);
        top.add(noticeField, BorderLayout.CENTER);
        top.add(addBtn, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        list.setBackground(Color.DARK_GRAY); list.setForeground(Color.WHITE);
        add(new JScrollPane(list), BorderLayout.CENTER);

        loadNotices();

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = noticeField.getText().trim();
                if (text.isEmpty()) return;
                DataStore.getInstance().addNotice(text);
                loadNotices();
                dashboard.refreshMetrics();
                noticeField.setText("");
            }
        });
    }

    private void loadNotices() {
        listModel.clear();
        for (String n : DataStore.getInstance().getNotices()) listModel.addElement(n);
    }
}
