package com.smartcollege.erp.ui;

import com.smartcollege.erp.model.DataStore;
import com.smartcollege.erp.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AttendanceModule extends JPanel {
    private DefaultTableModel tableModel;
    private Dashboard dashboard;

    public AttendanceModule(Dashboard dashboard) {
        this.dashboard = dashboard;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        tableModel = new DefaultTableModel(new Object[]{"ID","Name","Roll No","Mark Present"},0) {
            public boolean isCellEditable(int r, int c) { return c == 3; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.setBackground(Color.DARK_GRAY);
        JButton markPresent = new JButton("Mark Selected Present");
        JButton markAbsent = new JButton("Mark Selected Absent");
        controls.add(markPresent); controls.add(markAbsent);

        add(controls, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);

        loadStudents();

        markPresent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();
                for (int r : rows) {
                    int id = (int) tableModel.getValueAt(r,0);
                    DataStore.getInstance().recordAttendance(id, true);
                }
                JOptionPane.showMessageDialog(AttendanceModule.this, "Marked present for selected students.");
                dashboard.refreshMetrics();
            }
        });

        markAbsent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();
                for (int r : rows) {
                    int id = (int) tableModel.getValueAt(r,0);
                    DataStore.getInstance().recordAttendance(id, false);
                }
                JOptionPane.showMessageDialog(AttendanceModule.this, "Marked absent for selected students.");
                dashboard.refreshMetrics();
            }
        });
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        for (Student s : DataStore.getInstance().getStudents()) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getRollNo(), ""});
        }
    }
}
