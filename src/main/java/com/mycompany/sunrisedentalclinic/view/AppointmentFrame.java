package com.mycompany.sunrisedentalclinic.view;

import com.mycompany.sunrisedentalclinic.dao.AppointmentDAO;
import com.mycompany.sunrisedentalclinic.dao.DentistDAO;
import com.mycompany.sunrisedentalclinic.dao.PatientDAO;
import com.mycompany.sunrisedentalclinic.dao.TreatmentDAO;
import com.mycompany.sunrisedentalclinic.model.Appointment;
import com.mycompany.sunrisedentalclinic.model.Dentist;
import com.mycompany.sunrisedentalclinic.model.Patient;
import com.mycompany.sunrisedentalclinic.model.Treatment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AppointmentFrame extends JFrame {

    private JTextField txtAppointmentNumber;
    private JComboBox<PatientItem> cmbPatient;
    private JComboBox<DentistItem> cmbDentist;
    private JComboBox<TreatmentItem> cmbTreatment;
    private JTextField txtDate;
    private JTextField txtTime;
    private JComboBox<String> cmbStatus;
    private JTextArea txtNotes;

    private JButton btnRegister;
    private JButton btnClear;
    private JButton btnClose;

    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    private AppointmentDAO appointmentDAO;
    private PatientDAO patientDAO;
    private DentistDAO dentistDAO;
    private TreatmentDAO treatmentDAO;

    private DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("HH:mm");

    public AppointmentFrame() {

        appointmentDAO = new AppointmentDAO();
        patientDAO = new PatientDAO();
        dentistDAO = new DentistDAO();
        treatmentDAO = new TreatmentDAO();

        setTitle("Sunrise Dental Clinic - Appointment Registration");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadPatients();
        loadDentists();
        loadTreatments();
        loadAppointments();
    }

    private void initializeUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel lblTitle =
                new JLabel("APPOINTMENT REGISTRATION");

        lblTitle.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        JPanel formPanel =
                new JPanel(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(5, 5, 5, 5);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                new JLabel("Appointment Number:"),
                gbc
        );

        txtAppointmentNumber =
                new JTextField();

        gbc.gridx = 1;

        formPanel.add(
                txtAppointmentNumber,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
                new JLabel("Patient:"),
                gbc
        );

        cmbPatient =
                new JComboBox<>();

        gbc.gridx = 1;

        formPanel.add(
                cmbPatient,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 2;

        formPanel.add(
                new JLabel("Dentist:"),
                gbc
        );

        cmbDentist =
                new JComboBox<>();

        gbc.gridx = 1;

        formPanel.add(
                cmbDentist,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 3;

        formPanel.add(
                new JLabel("Treatment:"),
                gbc
        );

        cmbTreatment =
                new JComboBox<>();

        gbc.gridx = 1;

        formPanel.add(
                cmbTreatment,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 4;

        formPanel.add(
                new JLabel("Appointment Date:"),
                gbc
        );

        txtDate =
                new JTextField();

        txtDate.setToolTipText(
                "Format: yyyy-MM-dd"
        );

        gbc.gridx = 1;

        formPanel.add(
                txtDate,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 5;

        formPanel.add(
                new JLabel("Appointment Time:"),
                gbc
        );

        txtTime =
                new JTextField();

        txtTime.setToolTipText(
                "Format: HH:mm"
        );

        gbc.gridx = 1;

        formPanel.add(
                txtTime,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 6;

        formPanel.add(
                new JLabel("Status:"),
                gbc
        );

        cmbStatus =
                new JComboBox<>(
                        new String[]{
                                "SCHEDULED",
                                "COMPLETED",
                                "CANCELLED"
                        }
                );

        gbc.gridx = 1;

        formPanel.add(
                cmbStatus,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 7;

        formPanel.add(
                new JLabel("Notes:"),
                gbc
        );

        txtNotes =
                new JTextArea(3, 20);

        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);

        JScrollPane notesScroll =
                new JScrollPane(txtNotes);

        gbc.gridx = 1;

        formPanel.add(
                notesScroll,
                gbc
        );

        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        btnRegister =
                new JButton("Register Appointment");

        btnClear =
                new JButton("Clear");

        btnClose =
                new JButton("Close");

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnClose);

        tableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Appointment No",
                                "Patient ID",
                                "Dentist ID",
                                "Treatment ID",
                                "Date",
                                "Time",
                                "Status"
                        },
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        appointmentTable =
                new JTable(tableModel);

        JScrollPane tableScroll =
                new JScrollPane(appointmentTable);

        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.add(
                lblTitle,
                BorderLayout.NORTH
        );

        topPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        topPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                topPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                tableScroll,
                BorderLayout.CENTER
        );

        add(mainPanel);

        btnRegister.addActionListener(
                e -> registerAppointment()
        );

        btnClear.addActionListener(
                e -> clearFields()
        );

        btnClose.addActionListener(
                e -> dispose()
        );
    }

    private void loadPatients() {

        cmbPatient.removeAllItems();

        try {

            List<Patient> patients =
                    patientDAO.getAllPatients();

            for (Patient patient : patients) {

                cmbPatient.addItem(
                        new PatientItem(patient)
                );
            }

        } catch (Exception ex) {

            showError(
                    "Unable to load patient information."
            );
        }
    }

    private void loadDentists() {

        cmbDentist.removeAllItems();

        try {

            List<Dentist> dentists =
                    dentistDAO.getAllDentists();

            for (Dentist dentist : dentists) {

                if ("ACTIVE".equalsIgnoreCase(
                        dentist.getStatus())) {

                    cmbDentist.addItem(
                            new DentistItem(dentist)
                    );
                }
            }

        } catch (Exception ex) {

            showError(
                    "Unable to load dentist information."
            );
        }
    }

    private void loadTreatments() {

        cmbTreatment.removeAllItems();

        try {

            List<Treatment> treatments =
                    treatmentDAO.getAllTreatments();

            for (Treatment treatment : treatments) {

                if ("ACTIVE".equalsIgnoreCase(
                        treatment.getStatus())) {

                    cmbTreatment.addItem(
                            new TreatmentItem(treatment)
                    );
                }
            }

        } catch (Exception ex) {

            showError(
                    "Unable to load treatment information."
            );
        }
    }

    private void registerAppointment() {

        if (!validateFields()) {
            return;
        }

        PatientItem patientItem =
                (PatientItem) cmbPatient.getSelectedItem();

        DentistItem dentistItem =
                (DentistItem) cmbDentist.getSelectedItem();

        TreatmentItem treatmentItem =
                (TreatmentItem) cmbTreatment.getSelectedItem();

        if (patientItem == null ||
                dentistItem == null ||
                treatmentItem == null) {

            showWarning(
                    "Please select a patient, dentist and treatment."
            );

            return;
        }

        String appointmentNumber =
                txtAppointmentNumber.getText().trim();

        try {

            if (appointmentDAO.appointmentNumberExists(
                    appointmentNumber)) {

                showWarning(
                        "Appointment number already exists."
                );

                txtAppointmentNumber.requestFocus();

                return;
            }

            LocalDate appointmentDate =
                    LocalDate.parse(
                            txtDate.getText().trim(),
                            dateFormatter
                    );

            LocalTime appointmentTime =
                    LocalTime.parse(
                            txtTime.getText().trim(),
                            timeFormatter
                    );

            Appointment appointment =
                    new Appointment(
                            appointmentNumber,
                            patientItem.patient.getPatientId(),
                            dentistItem.dentist.getDentistId(),
                            treatmentItem.treatment.getTreatmentId(),
                            appointmentDate,
                            appointmentTime,
                            cmbStatus.getSelectedItem().toString(),
                            txtNotes.getText().trim()
                    );

            boolean success =
                    appointmentDAO.addAppointment(
                            appointment
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Appointment registered successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                loadAppointments();

            } else {

                showError(
                        "Unable to register appointment."
                );
            }

        } catch (DateTimeParseException ex) {

            showWarning(
                    "Please enter a valid date or time."
            );

        } catch (Exception ex) {

            showError(
                    "An unexpected error occurred while registering the appointment."
            );
        }
    }

    private boolean validateFields() {

        String appointmentNumber =
                txtAppointmentNumber.getText().trim();

        String date =
                txtDate.getText().trim();

        String time =
                txtTime.getText().trim();

        if (appointmentNumber.isEmpty()) {

            showWarning(
                    "Appointment number is required."
            );

            txtAppointmentNumber.requestFocus();

            return false;
        }

        if (appointmentNumber.length() > 20) {

            showWarning(
                    "Appointment number cannot exceed 20 characters."
            );

            txtAppointmentNumber.requestFocus();

            return false;
        }

        if (!appointmentNumber.matches("[A-Za-z0-9-]+")) {

            showWarning(
                    "Appointment number can contain only letters, numbers and hyphens."
            );

            txtAppointmentNumber.requestFocus();

            return false;
        }

        if (cmbPatient.getSelectedItem() == null) {

            showWarning(
                    "Please select a patient."
            );

            return false;
        }

        if (cmbDentist.getSelectedItem() == null) {

            showWarning(
                    "Please select a dentist."
            );

            return false;
        }

        if (cmbTreatment.getSelectedItem() == null) {

            showWarning(
                    "Please select a treatment."
            );

            return false;
        }

        if (date.isEmpty()) {

            showWarning(
                    "Appointment date is required."
            );

            txtDate.requestFocus();

            return false;
        }

        if (time.isEmpty()) {

            showWarning(
                    "Appointment time is required."
            );

            txtTime.requestFocus();

            return false;
        }

        LocalDate appointmentDate;

        try {

            appointmentDate =
                    LocalDate.parse(
                            date,
                            dateFormatter
                    );

        } catch (DateTimeParseException ex) {

            showWarning(
                    "Invalid date. Use the format yyyy-MM-dd."
            );

            txtDate.requestFocus();

            return false;
        }

        if (appointmentDate.isBefore(LocalDate.now())) {

            showWarning(
                    "Appointment date cannot be in the past."
            );

            txtDate.requestFocus();

            return false;
        }

        LocalTime appointmentTime;

        try {

            appointmentTime =
                    LocalTime.parse(
                            time,
                            timeFormatter
                    );

        } catch (DateTimeParseException ex) {

            showWarning(
                    "Invalid time. Use the format HH:mm."
            );

            txtTime.requestFocus();

            return false;
        }

        LocalTime openingTime =
                LocalTime.of(8, 0);

        LocalTime closingTime =
                LocalTime.of(18, 0);

        if (appointmentTime.isBefore(openingTime) ||
                appointmentTime.isAfter(closingTime)) {

            showWarning(
                    "Appointment time must be between 08:00 and 18:00."
            );

            txtTime.requestFocus();

            return false;
        }

        return true;
    }

    private void loadAppointments() {

        tableModel.setRowCount(0);

        try {

            List<Appointment> appointments =
                    appointmentDAO.getAllAppointments();

            for (Appointment appointment :
                    appointments) {

                tableModel.addRow(
                        new Object[]{
                                appointment.getAppointmentNumber(),
                                appointment.getPatientId(),
                                appointment.getDentistId(),
                                appointment.getTreatmentId(),
                                appointment.getAppointmentDate(),
                                appointment.getAppointmentTime(),
                                appointment.getStatus()
                        }
                );
            }

        } catch (Exception ex) {

            showError(
                    "Unable to load appointments."
            );
        }
    }

    private void clearFields() {

        txtAppointmentNumber.setText("");
        txtDate.setText("");
        txtTime.setText("");
        txtNotes.setText("");

        cmbStatus.setSelectedItem(
                "SCHEDULED"
        );

        if (cmbPatient.getItemCount() > 0) {
            cmbPatient.setSelectedIndex(0);
        }

        if (cmbDentist.getItemCount() > 0) {
            cmbDentist.setSelectedIndex(0);
        }

        if (cmbTreatment.getItemCount() > 0) {
            cmbTreatment.setSelectedIndex(0);
        }

        txtAppointmentNumber.requestFocus();
    }

    private void showWarning(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "System Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private static class PatientItem {

        private Patient patient;

        public PatientItem(Patient patient) {
            this.patient = patient;
        }

        @Override
        public String toString() {

            return patient.getPatientId()
                    + " - "
                    + patient.getPatientName();
        }
    }

    private static class DentistItem {

        private Dentist dentist;

        public DentistItem(Dentist dentist) {
            this.dentist = dentist;
        }

        @Override
        public String toString() {

            return dentist.getDentistId()
                    + " - "
                    + dentist.getDentistName();
        }
    }

    private static class TreatmentItem {

        private Treatment treatment;

        public TreatmentItem(Treatment treatment) {
            this.treatment = treatment;
        }

        @Override
        public String toString() {

            return treatment.getTreatmentId()
                    + " - "
                    + treatment.getTreatmentName()
                    + " ("
                    + treatment.getTreatmentCost()
                    + ")";
        }
    }
}

