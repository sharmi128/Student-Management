package com.smartcollege.erp;

import javax.swing.SwingUtilities;

import com.smartcollege.erp.ui.Login;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
