package com.smartcollege.erp.ui;

import com.smartcollege.erp.model.DataStore;
import com.smartcollege.erp.model.DataStore.FeeHead;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class FeesModule extends JPanel {
    private final Dashboard dashboard;
    private final DefaultTableModel structureModel;
    private final DefaultTableModel paymentModel;
    private final JLabel structureTotalValue;
    private final JLabel collectedValue;
    private final JLabel pendingValue;
    private final JLabel progressValue;

    public FeesModule(Dashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(14, 16, 22));
        setBorder(new EmptyBorder(18, 18, 18, 18));

        structureModel = createStructureModel();
        paymentModel = createPaymentModel();
        structureTotalValue = new JLabel();
        collectedValue = new JLabel();
        pendingValue = new JLabel();
        progressValue = new JLabel();

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        loadStructure();
        refreshSummary();
        loadPayments();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 12));
        header.setOpaque(false);

        JLabel title = new JLabel("Fee Structure", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel subtitle = new JLabel("Structured fee heads, live collection status, and payment tracking.");
        subtitle.setForeground(new Color(176, 184, 196));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel textBlock = new JPanel();
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));
        textBlock.setOpaque(false);
        textBlock.add(title);
        textBlock.add(Box.createVerticalStrut(4));
        textBlock.add(subtitle);

        header.add(textBlock, BorderLayout.WEST);
        return header;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new GridLayout(1, 2, 16, 16));
        center.setOpaque(false);
        center.add(buildStructureCard());
        center.add(buildPaymentCard());
        return center;
    }

    private JPanel buildStructureCard() {
        JPanel card = createCardPanel("Fee Breakdown");
        JTable table = new JTable(structureModel);
        styleTable(table);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private JPanel buildPaymentCard() {
        JPanel card = createCardPanel("Collect Fee Payment");

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setOpaque(false);

        JTextField rollField = new JTextField();
        JTextField amountField = new JTextField();
        JButton collectButton = new JButton("Collect Payment");
        collectButton.setBackground(new Color(70, 130, 180));
        collectButton.setForeground(Color.WHITE);
        collectButton.setFocusPainted(false);

        form.add(label("Student Roll No"));
        form.add(rollField);
        form.add(label("Amount"));
        form.add(amountField);
        form.add(Box.createHorizontalStrut(1));
        form.add(collectButton);

        JTable paymentTable = new JTable(paymentModel);
        styleTable(paymentTable);

        collectButton.addActionListener(event -> {
            String rollNo = rollField.getText().trim();
            String amountText = amountField.getText().trim();

            if (rollNo.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Roll No and Amount required");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                DataStore.getInstance().addFee(rollNo, amount);
                loadPayments();
                refreshSummary();
                dashboard.refreshMetrics();
                rollField.setText("");
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid amount");
            }
        });

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setOpaque(false);
        top.add(form, BorderLayout.NORTH);

        JLabel recentLabel = label("Recent Payments");
        recentLabel.setBorder(new EmptyBorder(10, 0, 4, 0));
        top.add(recentLabel, BorderLayout.CENTER);

        JPanel paymentContainer = new JPanel(new BorderLayout(8, 8));
        paymentContainer.setOpaque(false);
        paymentContainer.add(top, BorderLayout.NORTH);
        paymentContainer.add(new JScrollPane(paymentTable), BorderLayout.CENTER);
        card.add(paymentContainer, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new GridLayout(1, 4, 12, 12));
        footer.setOpaque(false);
        footer.add(metricCard("Structure Total", structureTotalValue));
        footer.add(metricCard("Collected", collectedValue));
        footer.add(metricCard("Pending", pendingValue));
        footer.add(metricCard("Collection %", progressValue));
        return footer;
    }

    private JPanel createCardPanel(String titleText) {
        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setBackground(new Color(22, 26, 34));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 52, 64)),
                new EmptyBorder(16, 16, 16, 16)));

        JLabel title = new JLabel(titleText);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(title, BorderLayout.NORTH);
        return card;
    }

    private JPanel metricCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(6, 6));
        card.setBackground(new Color(22, 26, 34));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 52, 64)),
                new EmptyBorder(16, 16, 16, 16)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(176, 184, 196));
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }

    private DefaultTableModel createStructureModel() {
        return new DefaultTableModel(new Object[]{"Fee Head", "Cycle", "Amount"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private DefaultTableModel createPaymentModel() {
        return new DefaultTableModel(new Object[]{"Student Roll No", "Amount"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(new Color(28, 33, 42));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(70, 130, 180));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(34, 40, 52));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void loadStructure() {
        structureModel.setRowCount(0);
        for (FeeHead feeHead : DataStore.getInstance().getFeeStructure()) {
            structureModel.addRow(new Object[]{feeHead.getName(), feeHead.getCycle(), feeHead.getAmount()});
        }
    }

    private void loadPayments() {
        paymentModel.setRowCount(0);
        for (DataStore.FeePayment payment : DataStore.getInstance().getFeePayments()) {
            paymentModel.addRow(new Object[]{payment.getStudentRollNo(), payment.getAmount()});
        }
    }

    private void refreshSummary() {
        DataStore store = DataStore.getInstance();
        structureTotalValue.setText(String.format("%.2f", store.totalFeeStructureAmount()));
        collectedValue.setText(String.format("%.2f", store.totalFeesCollected()));
        pendingValue.setText(String.format("%.2f", store.totalFeesPending()));
        progressValue.setText(String.format("%.1f%%", store.feeCollectionProgress()));
    }
}
