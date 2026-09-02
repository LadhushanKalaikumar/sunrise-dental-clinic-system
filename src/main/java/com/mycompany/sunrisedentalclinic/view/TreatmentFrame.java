package com.mycompany.sunrisedentalclinic.view;

import com.mycompany.sunrisedentalclinic.dao.TreatmentDAO;
import com.mycompany.sunrisedentalclinic.model.Treatment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class TreatmentFrame extends JFrame {

    private JTextField txtTreatmentId;
    private JTextField txtTreatmentName;
    private JTextField txtDescription;
    private JTextField txtTreatmentCost;
    private JComboBox<String> cmbStatus;

    private JButton btnAdd;
    private JButton btnSearch;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnClose;

    private JTable treatmentTable;
    private DefaultTableModel tableModel;

    private TreatmentDAO treatmentDAO;

    public TreatmentFrame() {

        treatmentDAO = new TreatmentDAO();

        setTitle("Sunrise Dental Clinic - Treatment Management");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadTreatments();
    }

    private void initializeUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("TREATMENT MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        formPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Treatment ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Treatment ID:"), gbc);

        txtTreatmentId = new JTextField();
        txtTreatmentId.setEditable(false);

        gbc.gridx = 1;
        formPanel.add(txtTreatmentId, gbc);

        // Treatment Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Treatment Name:"), gbc);

        txtTreatmentName = new JTextField();

        gbc.gridx = 1;
        formPanel.add(txtTreatmentName, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Description:"), gbc);

        txtDescription = new JTextField();

        gbc.gridx = 1;
        formPanel.add(txtDescription, gbc);

        // Treatment Cost
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Treatment Cost:"), gbc);

        txtTreatmentCost = new JTextField();

        gbc.gridx = 1;
        formPanel.add(txtTreatmentCost, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Status:"), gbc);

        cmbStatus = new JComboBox<>(
                new String[]{"ACTIVE", "INACTIVE"}
        );

        gbc.gridx = 1;
        formPanel.add(cmbStatus, gbc);

        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel = new JPanel(new FlowLayout());

        btnAdd = new JButton("Add Treatment");
        btnSearch = new JButton("Search");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnClose = new JButton("Close");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnClose);

        // =========================
        // TABLE
        // =========================

        tableModel = new DefaultTableModel(
                new Object[]{
                        "Treatment ID",
                        "Treatment Name",
                        "Description",
                        "Treatment Cost",
                        "Status"
                }, 0
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        treatmentTable = new JTable(tableModel);

        JScrollPane tableScrollPane =
                new JScrollPane(treatmentTable);

        // =========================
        // ADD COMPONENTS
        // =========================

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // =========================
        // BUTTON EVENTS
        // =========================

        btnAdd.addActionListener(e -> addTreatment());

        btnSearch.addActionListener(e -> searchTreatment());

        btnUpdate.addActionListener(e -> updateTreatment());

        btnDelete.addActionListener(e -> deleteTreatment());

        btnClear.addActionListener(e -> clearFields());

        btnClose.addActionListener(e -> dispose());

        // =========================
        // TABLE ROW SELECTION
        // =========================

        treatmentTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int row = treatmentTable.getSelectedRow();

                        if (row >= 0) {

                            txtTreatmentId.setText(
                                    tableModel.getValueAt(row, 0).toString()
                            );

                            txtTreatmentName.setText(
                                    tableModel.getValueAt(row, 1).toString()
                            );

                            txtDescription.setText(
                                    tableModel.getValueAt(row, 2).toString()
                            );

                            txtTreatmentCost.setText(
                                    tableModel.getValueAt(row, 3).toString()
                            );

                            cmbStatus.setSelectedItem(
                                    tableModel.getValueAt(row, 4).toString()
                            );
                        }
                    }
                });
    }

    // =========================
    // ADD TREATMENT
    // =========================

    private void addTreatment() {

        if (!validateFields()) {
            return;
        }

        try {

            BigDecimal cost = new BigDecimal(
                    txtTreatmentCost.getText().trim()
            );

            Treatment treatment = new Treatment(
                    txtTreatmentName.getText().trim(),
                    txtDescription.getText().trim(),
                    cost,
                    cmbStatus.getSelectedItem().toString()
            );

            boolean success = treatmentDAO.addTreatment(treatment);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Treatment added successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                loadTreatments();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to add treatment. "
                        + "The treatment name may already exist.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Treatment cost must be a valid number.",
                    "Invalid Cost",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =========================
    // SEARCH
    // =========================

    private void searchTreatment() {

        String idText = JOptionPane.showInputDialog(
                this,
                "Enter Treatment ID:"
        );

        if (idText == null || idText.trim().isEmpty()) {
            return;
        }

        try {

            int treatmentId = Integer.parseInt(idText.trim());

            Treatment treatment =
                    treatmentDAO.getTreatmentById(treatmentId);

            if (treatment != null) {

                txtTreatmentId.setText(
                        String.valueOf(treatment.getTreatmentId())
                );

                txtTreatmentName.setText(
                        treatment.getTreatmentName()
                );

                txtDescription.setText(
                        treatment.getDescription() == null
                        ? ""
                        : treatment.getDescription()
                );

                txtTreatmentCost.setText(
                        treatment.getTreatmentCost().toString()
                );

                cmbStatus.setSelectedItem(
                        treatment.getStatus()
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Treatment not found.",
                        "Search Result",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Treatment ID must be a valid number.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =========================
    // UPDATE
    // =========================

    private void updateTreatment() {

        if (txtTreatmentId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select or search for a treatment first.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!validateFields()) {
            return;
        }

        try {

            int treatmentId = Integer.parseInt(
                    txtTreatmentId.getText().trim()
            );

            BigDecimal cost = new BigDecimal(
                    txtTreatmentCost.getText().trim()
            );

            Treatment treatment = new Treatment(
                    treatmentId,
                    txtTreatmentName.getText().trim(),
                    txtDescription.getText().trim(),
                    cost,
                    cmbStatus.getSelectedItem().toString()
            );

            boolean success =
                    treatmentDAO.updateTreatment(treatment);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Treatment updated successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                loadTreatments();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to update treatment.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Treatment ID and cost must be valid numbers.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =========================
    // DELETE
    // =========================

    private void deleteTreatment() {

        if (txtTreatmentId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a treatment first.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this treatment?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int treatmentId = Integer.parseInt(
                    txtTreatmentId.getText().trim()
            );

            boolean success =
                    treatmentDAO.deleteTreatment(treatmentId);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Treatment deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                loadTreatments();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to delete treatment.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid treatment ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // VALIDATION
    // =========================

    private boolean validateFields() {

        String name =
                txtTreatmentName.getText().trim();

        String costText =
                txtTreatmentCost.getText().trim();

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Treatment name is required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            txtTreatmentName.requestFocus();
            return false;
        }

        if (costText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Treatment cost is required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            txtTreatmentCost.requestFocus();
            return false;
        }

        try {

            BigDecimal cost = new BigDecimal(costText);

            if (cost.compareTo(BigDecimal.ZERO) < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Treatment cost cannot be negative.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                txtTreatmentCost.requestFocus();
                return false;
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Treatment cost must be a valid number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            txtTreatmentCost.requestFocus();
            return false;
        }

        return true;
    }

    // =========================
    // LOAD TREATMENTS
    // =========================

    private void loadTreatments() {

        tableModel.setRowCount(0);

        List<Treatment> treatments =
                treatmentDAO.getAllTreatments();

        for (Treatment treatment : treatments) {

            tableModel.addRow(
                    new Object[]{
                            treatment.getTreatmentId(),
                            treatment.getTreatmentName(),
                            treatment.getDescription(),
                            treatment.getTreatmentCost(),
                            treatment.getStatus()
                    }
            );
        }
    }

    // =========================
    // CLEAR
    // =========================

    private void clearFields() {

        txtTreatmentId.setText("");
        txtTreatmentName.setText("");
        txtDescription.setText("");
        txtTreatmentCost.setText("");

        cmbStatus.setSelectedItem("ACTIVE");

        treatmentTable.clearSelection();

        txtTreatmentName.requestFocus();
    }
}
