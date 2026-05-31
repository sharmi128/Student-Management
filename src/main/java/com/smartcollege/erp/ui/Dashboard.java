package com.smartcollege.erp.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.smartcollege.erp.model.DataStore;

public class Dashboard extends JFrame {
    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private final JLabel totalStudentsValue;
    private final JLabel totalTeachersValue;
    private final JLabel attendanceValue;
    private final JLabel feesValue;

    public Dashboard() {
        super("Smart College Management - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(9, 13, 23));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(9, 13, 23));

        totalStudentsValue = new JLabel("0");
        totalTeachersValue = new JLabel("0");
        attendanceValue = new JLabel("0");
        feesValue = new JLabel("0.0");

        // Dashboard view
        JPanel dashboardView = new JPanel(new BorderLayout(16, 16));
        dashboardView.setBackground(new Color(9, 13, 23));
        dashboardView.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel hero = new JPanel(new BorderLayout());
        hero.setBackground(new Color(18, 25, 40));
        hero.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 54, 79)),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        JLabel heroTitle = new JLabel("Command Center");
        heroTitle.setForeground(Color.WHITE);
        heroTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel heroSub = new JLabel("Live campus metrics, finance status, and operational modules");
        heroSub.setForeground(new Color(180, 190, 210));
        heroSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JPanel heroText = new JPanel();
        heroText.setOpaque(false);
        heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));
        heroText.add(heroTitle);
        heroText.add(Box.createVerticalStrut(4));
        heroText.add(heroSub);
        hero.add(heroText, BorderLayout.WEST);
        dashboardView.add(hero, BorderLayout.NORTH);

        JPanel metricGrid = new JPanel(new GridLayout(1, 4, 12, 12));
        metricGrid.setBackground(new Color(9, 13, 23));
        metricGrid.add(createMetricCard("Total Students", totalStudentsValue));
        metricGrid.add(createMetricCard("Total Teachers", totalTeachersValue));
        metricGrid.add(createMetricCard("Attendance %", attendanceValue));
        metricGrid.add(createMetricCard("Fees Collected", feesValue));
        dashboardView.add(metricGrid, BorderLayout.CENTER);

        mainPanel.add(dashboardView, "Dashboard");

        // add functional modules
        mainPanel.add(new StudentModule(this), "Students");
        mainPanel.add(new TeacherModule(this), "Teachers");
        mainPanel.add(new AttendanceModule(this), "Attendance");
        mainPanel.add(new FeesModule(this), "Fees");
        mainPanel.add(new NoticeModule(this), "Notices");

        JSplitPane split = new JSplitPane();
        split.setDividerLocation(220);
        split.setEnabled(false);
        split.setBorder(null);
        split.setBackground(new Color(9, 13, 23));

        JPanel sidebar = new JPanel(new GridLayout(8, 1, 6, 6));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(14, 19, 34));
        sidebar.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel brand = new JLabel("Smart College", SwingConstants.CENTER);
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brand.setBorder(BorderFactory.createEmptyBorder(12, 8, 20, 8));
        sidebar.add(brand);

        String[] sections = {"Dashboard", "Students", "Teachers", "Attendance", "Fees", "Notices", "Logout"};
        for (String section : sections) {
            JButton button = new JButton(section);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(27, 36, 58));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
            button.addActionListener(new SidebarAction());
            sidebar.add(button);
        }

        split.setLeftComponent(sidebar);
        split.setRightComponent(mainPanel);
        add(split, BorderLayout.CENTER);

        refreshMetrics();
        cardLayout.show(mainPanel, "Dashboard");
    }

    private JPanel createMetricCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(new Color(18, 25, 40));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(58, 76, 112)),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(180, 190, 210));
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    public final void refreshMetrics() {
        DataStore store = DataStore.getInstance();
        totalStudentsValue.setText(String.valueOf(store.getStudents().size()));
        totalTeachersValue.setText(String.valueOf(store.getTeachers().size()));
        attendanceValue.setText(String.valueOf(store.attendancePercentage()));
        feesValue.setText(String.valueOf(store.totalFeesCollected()));
    }

    private class SidebarAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String command = ((JButton) event.getSource()).getText();
            if ("Logout".equals(command)) {
                dispose();
                SwingUtilities.invokeLater(() -> new Login().setVisible(true));
                return;
            }

            cardLayout.show(mainPanel, command);
            refreshMetrics();
        }
    }
}
