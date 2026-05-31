package com.smartcollege.erp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.smartcollege.erp.model.DataStore;

public class Login extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public Login() {
        setTitle("Smart College Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(420, 300));
        setResizable(false);

        usernameField = new JTextField(18);
        passwordField = new JPasswordField(18);
        usernameField.setText(DataStore.DEFAULT_USERNAME);
        passwordField.setText(DataStore.DEFAULT_PASSWORD);

        buildUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        root.setBackground(new Color(9, 13, 23));

        JLabel title = new JLabel("Smart College ERP", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 6, 0));

        JLabel subtitle = new JLabel("Premium campus operations, finance, attendance, and notices in one place", SwingConstants.CENTER);
        subtitle.setForeground(new Color(180, 190, 210));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel titleBlock = new JPanel(new BorderLayout(0, 8));
        titleBlock.setOpaque(false);
        titleBlock.add(title, BorderLayout.NORTH);
        titleBlock.add(subtitle, BorderLayout.SOUTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 54, 79)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        form.setBackground(new Color(18, 25, 40));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(Color.WHITE);
        form.add(usernameLabel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        form.add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        form.add(passwordLabel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        form.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 1;

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(84, 144, 247));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        form.add(loginButton, constraints);

        constraints.gridy = 3;
        JLabel hintLabel = new JLabel("Use: admin / admin123");
        hintLabel.setForeground(new Color(155, 168, 188));
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        form.add(hintLabel, constraints);

        root.add(titleBlock, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        add(root);

        getRootPane().setDefaultButton(loginButton);
        usernameField.requestFocusInWindow();

        loginButton.addActionListener(event -> login());
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        boolean isValid = DataStore.getInstance().authenticate(username, password)
                || (DataStore.DEFAULT_USERNAME.equalsIgnoreCase(username)
                && DataStore.DEFAULT_PASSWORD.equals(password));

        if (isValid) {
            launchDashboard();
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Invalid username or password",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
    }

    private void launchDashboard() {
        try {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
            dashboard.toFront();
            dashboard.requestFocus();
            dispose();
        } catch (Throwable ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Dashboard open aagala. Error: " + ex.getMessage(),
                    "Launch Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}