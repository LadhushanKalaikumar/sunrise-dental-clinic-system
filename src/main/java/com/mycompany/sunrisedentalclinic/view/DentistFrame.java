package com.mycompany.sunrisedentalclinic.view;

import com.mycompany.sunrisedentalclinic.dao.DentistDAO;
import com.mycompany.sunrisedentalclinic.model.Dentist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DentistFrame extends JFrame {

    private JTextField txtDentistId;
    private JTextField txtDentistName;
    private JTextField txtSpecialization;
    private JTextField txtContactNumber;
    private JComboBox<String> cmbStatus;

    private JButton btnAdd;
    private JButton btnSearch;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnClose;

    private JTable dentistTable;
    private DefaultTableModel tableModel;

    private DentistDAO dentistDAO;

    public DentistFrame() {

        dentistDAO = new DentistDAO();

        setTitle("Sunrise Dental Clinic - Dentist Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadDentists();
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

        JLabel lblTitle = new JLabel("DENTIST MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        formPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Dentist ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Dentist ID:"), gbc);

        txtDentistId = new JTextField();
        txtDentistId.setEditable(false);

        gbc.gridx = 1;
        formPanel.add(txtDentistId, gbc);

        // Dentist Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Dentist Name:"), gbc);

        txtDentistName = new JTextField();

        gbc.gridx = 1;
        formPanel.add(txtDentistName, gbc);

        // Specialization
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Specialization:"), gbc);

        txtSpecialization = new JTextField();

        gbc.gridx = 1;
        formPanel.add(txtSpecialization, gbc);

        // Contact Number
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Contact Number:"), gbc);

        txtContactNumber = new JTextField();

        gbc.gridx = 1;
        formPanel.add(txtContactNumber, gbc);

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

        btnAdd = new JButton("Add Dentist");
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
                "Dentist ID",
                "Dentist Name",
                "Specialization",
                "Contact Number",
                "Status"
            }, 0
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        dentistTable = new JTable(tableModel);

        JScrollPane tableScrollPane =
            new JScrollPane(dentistTable);

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

        btnAdd.addActionListener(e -> addDentist());

        btnSearch.addActionListener(e -> searchDentist());

        btnUpdate.addActionListener(e -> updateDentist());

        btnDelete.addActionListener(e -> deleteDentist());

        btnClear.addActionListener(e -> clearFields());

        btnClose.addActionListener(e -> dispose());

        // =========================
        // TABLE ROW SELECTION
        // =========================

        dentistTable.getSelectionModel()
            .addListSelectionListener(e -> {

                if (!e.getValueIsAdjusting()) {

                    int row = dentistTable.getSelectedRow();

                    if (row >= 0) {

                        txtDentistId.setText(
                            tableModel.getValueAt(row, 0).toString()
                        );

                        txtDentistName.setText(
                            tableModel.getValueAt(row, 1).toString()
                        );

                        txtSpecialization.setText(
                            tableModel.getValueAt(row, 2).toString()
                        );

                        txtContactNumber.setText(
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
    // ADD DENTIST
    // =========================

    private void addDentist() {

        if (!validateFields()) {
            return;
        }

        Dentist dentist = new Dentist(
            txtDentistName.getText().trim(),
            txtSpecialization.getText().trim(),
            txtContactNumber.getText().trim(),
            cmbStatus.getSelectedItem().toString()
        );

        boolean success = dentistDAO.addDentist(dentist);

        if (success) {

            JOptionPane.showMessageDialog(
                this,
                "Dentist registered successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
            loadDentists();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Unable to register dentist.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // SEARCH
    // =========================

    private void searchDentist() {

        String idText = JOptionPane.showInputDialog(
            this,
            "Enter Dentist ID:"
        );

        if (idText == null || idText.trim().isEmpty()) {
            return;
        }

        try {

            int dentistId = Integer.parseInt(idText.trim());

            Dentist dentist =
                dentistDAO.getDentistById(dentistId);

            if (dentist != null) {

                txtDentistId.setText(
                    String.valueOf(dentist.getDentistId())
                );

                txtDentistName.setText(
                    dentist.getDentistName()
                );

                txtSpecialization.setText(
                    dentist.getSpecialization()
                );

                txtContactNumber.setText(
                    dentist.getContactNumber()
                );

                cmbStatus.setSelectedItem(
                    dentist.getStatus()
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Dentist not found.",
                    "Search Result",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Dentist ID must be a valid number.",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =========================
    // UPDATE
    // =========================

    private void updateDentist() {

        if (txtDentistId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please select or search for a dentist first.",
                "Validation",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!validateFields()) {
            return;
        }

        try {

            int dentistId = Integer.parseInt(
                txtDentistId.getText().trim()
            );

            Dentist dentist = new Dentist(
                dentistId,
                txtDentistName.getText().trim(),
                txtSpecialization.getText().trim(),
                txtContactNumber.getText().trim(),
                cmbStatus.getSelectedItem().toString()
            );

            boolean success =
                dentistDAO.updateDentist(dentist);

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Dentist updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                loadDentists();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Unable to update dentist.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Invalid dentist ID.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // DELETE
    // =========================

    private void deleteDentist() {

        if (txtDentistId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a dentist first.",
                "Validation",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this dentist?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int dentistId = Integer.parseInt(
                txtDentistId.getText().trim()
            );

            boolean success =
                dentistDAO.deleteDentist(dentistId);

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Dentist deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                loadDentists();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete dentist.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Invalid dentist ID.",
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
            txtDentistName.getText().trim();

        String contact =
            txtContactNumber.getText().trim();

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Dentist name is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );

            txtDentistName.requestFocus();
            return false;
        }

        if (!contact.isEmpty()
                && !contact.matches("\\d{10}")) {

            JOptionPane.showMessageDialog(
                this,
                "Contact number must contain exactly 10 digits.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );

            txtContactNumber.requestFocus();
            return false;
        }

        return true;
    }

    // =========================
    // LOAD DENTISTS
    // =========================

    private void loadDentists() {

        tableModel.setRowCount(0);

        List<Dentist> dentists =
            dentistDAO.getAllDentists();

        for (Dentist dentist : dentists) {

            tableModel.addRow(
                new Object[]{
                    dentist.getDentistId(),
                    dentist.getDentistName(),
                    dentist.getSpecialization(),
                    dentist.getContactNumber(),
                    dentist.getStatus()
                }
            );
        }
    }

    // =========================
    // CLEAR
    // =========================

    private void clearFields() {

        txtDentistId.setText("");
        txtDentistName.setText("");
        txtSpecialization.setText("");
        txtContactNumber.setText("");

        cmbStatus.setSelectedItem("ACTIVE");

        dentistTable.clearSelection();

        txtDentistName.requestFocus();
    }
}
