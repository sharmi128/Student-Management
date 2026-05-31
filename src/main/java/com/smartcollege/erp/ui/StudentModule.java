package com.smartcollege.erp.ui;

import com.smartcollege.erp.model.DataStore;
import com.smartcollege.erp.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Student management panel: add, list, delete
 */
public class StudentModule extends JPanel {
    private DefaultTableModel tableModel;
    private Dashboard dashboard;

    public StudentModule(Dashboard dashboard) {
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
        JTextField rollF = new JTextField();
        JTextField deptF = new JTextField();
        JButton addBtn = new JButton("Add Student");
        addBtn.setBackground(new Color(30,144,255));
        addBtn.setForeground(Color.WHITE);
        JButton delBtn = new JButton("Delete Selected");

        form.add(new JLabel("Name") {{ setForeground(Color.WHITE); }});
        form.add(nameF);
        form.add(new JLabel("Roll No") {{ setForeground(Color.WHITE); }});
        form.add(rollF);
        form.add(new JLabel("Dept") {{ setForeground(Color.WHITE); }});
        form.add(deptF);
        form.add(addBtn);
        form.add(delBtn);

        add(form, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Roll No", "Dept"},0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        loadStudents();

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameF.getText().trim();
                String roll = rollF.getText().trim();
                String dept = deptF.getText().trim();
                if (name.isEmpty() || roll.isEmpty()) {
                    JOptionPane.showMessageDialog(StudentModule.this, "Name and Roll No required");
                    return;
                }
                DataStore.getInstance().addStudent(name, roll, dept);
                loadStudents();
                dashboard.refreshMetrics();
                nameF.setText(""); rollF.setText(""); deptF.setText("");
            }
        });

        delBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sel = table.getSelectedRow();
                if (sel == -1) return;
                int id = (int) tableModel.getValueAt(sel, 0);
                DataStore.getInstance().removeStudent(id);
                loadStudents();
                dashboard.refreshMetrics();
            }
        });
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        for (Student s : DataStore.getInstance().getStudents()) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getRollNo(), s.getDepartment()});
        }
    }
}
