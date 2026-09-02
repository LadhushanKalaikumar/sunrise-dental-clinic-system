package com.mycompany.sunrisedentalclinic.view;

import com.mycompany.sunrisedentalclinic.dao.PatientDAO;
import com.mycompany.sunrisedentalclinic.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientFrame extends JFrame {

    private JTextField txtPatientId;
    private JTextField txtPatientName;
    private JTextField txtAddress;
    private JTextField txtContactNumber;

    private JButton btnAdd;
    private JButton btnSearch;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnClose;

    private JTable patientTable;
    private DefaultTableModel tableModel;

    private PatientDAO patientDAO;

    public PatientFrame() {

        patientDAO = new PatientDAO();

        setTitle("Sunrise Dental Clinic - Patient Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadPatients();
    }

    private void initializeUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("PATIENT MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(lblTitle, gbc);

        // Patient ID
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Patient ID:"), gbc);

        txtPatientId = new JTextField();
        txtPatientId.setEditable(false);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtPatientId, gbc);

        // Patient Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Patient Name:"), gbc);

        txtPatientName = new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtPatientName, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Address:"), gbc);

        txtAddress = new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(txtAddress, gbc);

        // Contact
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Contact Number:"), gbc);

        txtContactNumber = new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(txtContactNumber, gbc);

        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel = new JPanel(new FlowLayout());

        btnAdd = new JButton("Add Patient");
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
                "Patient ID",
                "Patient Name",
                "Address",
                "Contact Number"
            }, 0
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(patientTable);

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

        btnAdd.addActionListener(e -> addPatient());

        btnSearch.addActionListener(e -> searchPatient());

        btnUpdate.addActionListener(e -> updatePatient());

        btnDelete.addActionListener(e -> deletePatient());

        btnClear.addActionListener(e -> clearFields());

        btnClose.addActionListener(e -> dispose());

        // Select table row
        patientTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = patientTable.getSelectedRow();

                if (row >= 0) {

                    txtPatientId.setText(
                        tableModel.getValueAt(row, 0).toString()
                    );

                    txtPatientName.setText(
                        tableModel.getValueAt(row, 1).toString()
                    );

                    txtAddress.setText(
                        tableModel.getValueAt(row, 2).toString()
                    );

                    txtContactNumber.setText(
                        tableModel.getValueAt(row, 3).toString()
                    );
                }
            }
        });
    }

    // =========================
    // ADD PATIENT
    // =========================

    private void addPatient() {

        if (!validateFields()) {
            return;
        }

        Patient patient = new Patient(
            txtPatientName.getText().trim(),
            txtAddress.getText().trim(),
            txtContactNumber.getText().trim()
        );

        boolean success = patientDAO.addPatient(patient);

        if (success) {

            JOptionPane.showMessageDialog(
                this,
                "Patient registered successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
            loadPatients();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Unable to register patient.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // SEARCH PATIENT
    // =========================

    private void searchPatient() {

        String idText = JOptionPane.showInputDialog(
            this,
            "Enter Patient ID:"
        );

        if (idText == null || idText.trim().isEmpty()) {
            return;
        }

        try {

            int patientId = Integer.parseInt(idText.trim());

            Patient patient = patientDAO.getPatientById(patientId);

            if (patient != null) {

                txtPatientId.setText(
                    String.valueOf(patient.getPatientId())
                );

                txtPatientName.setText(
                    patient.getPatientName()
                );

                txtAddress.setText(
                    patient.getAddress()
                );

                txtContactNumber.setText(
                    patient.getContactNumber()
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Patient not found.",
                    "Search Result",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Patient ID must be a valid number.",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =========================
    // UPDATE PATIENT
    // =========================

    private void updatePatient() {

        if (txtPatientId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please select or search for a patient first.",
                "Validation",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!validateFields()) {
            return;
        }

        try {

            int patientId = Integer.parseInt(
                txtPatientId.getText().trim()
            );

            Patient patient = new Patient(
                patientId,
                txtPatientName.getText().trim(),
                txtAddress.getText().trim(),
                txtContactNumber.getText().trim()
            );

            boolean success = patientDAO.updatePatient(patient);

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Patient updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                loadPatients();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Unable to update patient.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Invalid patient ID.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // DELETE PATIENT
    // =========================

    private void deletePatient() {

        if (txtPatientId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a patient first.",
                "Validation",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this patient?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int patientId = Integer.parseInt(
                txtPatientId.getText().trim()
            );

            boolean success = patientDAO.deletePatient(patientId);

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Patient deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                loadPatients();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete patient.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Invalid patient ID.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // VALIDATION
    // =========================

    private boolean validateFields() {

        String name = txtPatientName.getText().trim();
        String address = txtAddress.getText().trim();
        String contact = txtContactNumber.getText().trim();

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Patient name is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );

            txtPatientName.requestFocus();
            return false;
        }

        if (address.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Address is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );

            txtAddress.requestFocus();
            return false;
        }

        if (contact.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Contact number is required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );

            txtContactNumber.requestFocus();
            return false;
        }

        if (!contact.matches("\\d{10}")) {

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
    // LOAD PATIENTS
    // =========================

    private void loadPatients() {

        tableModel.setRowCount(0);

        List<Patient> patients = patientDAO.getAllPatients();

        for (Patient patient : patients) {

            tableModel.addRow(
                new Object[]{
                    patient.getPatientId(),
                    patient.getPatientName(),
                    patient.getAddress(),
                    patient.getContactNumber()
                }
            );
        }
    }

    // =========================
    // CLEAR
    // =========================

    private void clearFields() {

        txtPatientId.setText("");
        txtPatientName.setText("");
        txtAddress.setText("");
        txtContactNumber.setText("");

        patientTable.clearSelection();

        txtPatientName.requestFocus();
    }
}
