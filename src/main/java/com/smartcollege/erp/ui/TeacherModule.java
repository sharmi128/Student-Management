package com.smartcollege.erp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.smartcollege.erp.model.DataStore;
import com.smartcollege.erp.model.Teacher;

public class TeacherModule extends JPanel {
    private DefaultTableModel tableModel;
    private final Dashboard dashboard;

    public TeacherModule(Dashboard dashboard) {
        this.dashboard = dashboard;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        JPanel form = new JPanel(new GridLayout(1, 6, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.setBackground(Color.DARK_GRAY);

        JTextField nameF = new JTextField();
        JTextField deptF = new JTextField();
        JTextField emailF = new JTextField();
        JButton addBtn = new JButton("Add Teacher");
        addBtn.setBackground(new Color(30,144,255));
        addBtn.setForeground(Color.WHITE);

        form.add(new JLabel("Name") {{ setForeground(Color.WHITE); }});
        form.add(nameF);
        form.add(new JLabel("Dept") {{ setForeground(Color.WHITE); }});
        form.add(deptF);
        form.add(new JLabel("Email") {{ setForeground(Color.WHITE); }});
        form.add(emailF);
        form.add(addBtn);

        add(form, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Dept", "Email"},0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTeachers();

        addBtn.addActionListener((ActionEvent e) -> {
            String name = nameF.getText().trim();
            String dept = deptF.getText().trim();
            String email = emailF.getText().trim();
            if (name.isEmpty()) { JOptionPane.showMessageDialog(TeacherModule.this, "Name required"); return; }
            DataStore.getInstance().addTeacher(name, dept, email);
            loadTeachers();
            dashboard.refreshMetrics();
            nameF.setText(""); deptF.setText(""); emailF.setText("");
        });
    }

    private void loadTeachers() {
        tableModel.setRowCount(0);
        for (Teacher t : DataStore.getInstance().getTeachers()) {
            tableModel.addRow(new Object[]{t.getId(), t.getName(), t.getDepartment(), t.getEmail()});
        }
    }
}
