package com.mycompany.sunrisedentalclinic.view;

import com.mycompany.sunrisedentalclinic.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SearchAppointmentFrame extends JFrame {

    private JTextField txtAppointmentNumber;

    private JLabel lblPatient;
    private JLabel lblAddress;
    private JLabel lblContact;
    private JLabel lblDentist;
    private JLabel lblTreatment;
    private JLabel lblDate;
    private JLabel lblTime;
    private JLabel lblStatus;
    private JLabel lblNotes;

    public SearchAppointmentFrame() {

        setTitle("Sunrise Dental Clinic - Search Appointment");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createInterface();
    }

    private void createInterface() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

  
        JLabel lblTitle = new JLabel(
                "SEARCH APPOINTMENT",
                SwingConstants.CENTER
        );

        lblTitle.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        lblTitle.setForeground(
                new Color(0, 105, 148)
        );

        mainPanel.add(
                lblTitle,
                BorderLayout.NORTH
        );

        JPanel searchPanel =
                new JPanel(new BorderLayout(10, 10));

        searchPanel.add(
                new JLabel("Appointment Number:"),
                BorderLayout.WEST
        );

        txtAppointmentNumber =
                new JTextField();

        searchPanel.add(
                txtAppointmentNumber,
                BorderLayout.CENTER
        );

        JButton btnSearch =
                new JButton("Search");

        searchPanel.add(
                btnSearch,
                BorderLayout.EAST
        );


        JPanel detailsPanel =
                new JPanel(new GridLayout(9, 2, 10, 10));

        detailsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Appointment Details"
                )
        );

        detailsPanel.add(
                new JLabel("Patient Name:")
        );

        lblPatient = new JLabel("-");
        detailsPanel.add(lblPatient);

        detailsPanel.add(
                new JLabel("Address:")
        );

        lblAddress = new JLabel("-");
        detailsPanel.add(lblAddress);

        detailsPanel.add(
                new JLabel("Contact Number:")
        );

        lblContact = new JLabel("-");
        detailsPanel.add(lblContact);

        detailsPanel.add(
                new JLabel("Dentist:")
        );

        lblDentist = new JLabel("-");
        detailsPanel.add(lblDentist);

        detailsPanel.add(
                new JLabel("Treatment:")
        );

        lblTreatment = new JLabel("-");
        detailsPanel.add(lblTreatment);

        detailsPanel.add(
                new JLabel("Appointment Date:")
        );

        lblDate = new JLabel("-");
        detailsPanel.add(lblDate);

        detailsPanel.add(
                new JLabel("Appointment Time:")
        );

        lblTime = new JLabel("-");
        detailsPanel.add(lblTime);

        detailsPanel.add(
                new JLabel("Status:")
        );

        lblStatus = new JLabel("-");
        detailsPanel.add(lblStatus);

        detailsPanel.add(
                new JLabel("Notes:")
        );

        lblNotes = new JLabel("-");
        detailsPanel.add(lblNotes);

        JPanel centerPanel =
                new JPanel(new BorderLayout(10, 10));

        centerPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                detailsPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                5
                        )
                );

        JButton btnClear =
                new JButton("Clear");

        JButton btnClose =
                new JButton("Close");

        buttonPanel.add(btnClear);
        buttonPanel.add(btnClose);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        add(mainPanel);


        btnSearch.addActionListener(e ->
                searchAppointment()
        );

        btnClear.addActionListener(e ->
                clearFields()
        );

        btnClose.addActionListener(e ->
                dispose()
        );
    }

    private void searchAppointment() {

        String appointmentNumber =
                txtAppointmentNumber.getText().trim();

        if (appointmentNumber.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter an appointment number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            txtAppointmentNumber.requestFocus();

            return;
        }

        String sql =
                "SELECT a.appointment_number, "
                + "p.patient_name, "
                + "p.address, "
                + "p.contact_number, "
                + "d.dentist_name, "
                + "t.treatment_name, "
                + "a.appointment_date, "
                + "a.appointment_time, "
                + "a.status, "
                + "a.notes "
                + "FROM appointments a "
                + "INNER JOIN patients p "
                + "ON a.patient_id = p.patient_id "
                + "INNER JOIN dentists d "
                + "ON a.dentist_id = d.dentist_id "
                + "INNER JOIN treatments t "
                + "ON a.treatment_id = t.treatment_id "
                + "WHERE a.appointment_number = ?";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    appointmentNumber
            );

            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()) {

                    lblPatient.setText(
                            valueOrDash(
                                    rs.getString(
                                            "patient_name"
                                    )
                            )
                    );

                    lblAddress.setText(
                            valueOrDash(
                                    rs.getString(
                                            "address"
                                    )
                            )
                    );

                    lblContact.setText(
                            valueOrDash(
                                    rs.getString(
                                            "contact_number"
                                    )
                            )
                    );

                    lblDentist.setText(
                            valueOrDash(
                                    rs.getString(
                                            "dentist_name"
                                    )
                            )
                    );

                    lblTreatment.setText(
                            valueOrDash(
                                    rs.getString(
                                            "treatment_name"
                                    )
                            )
                    );

                    lblDate.setText(
                            valueOrDash(
                                    rs.getString(
                                            "appointment_date"
                                    )
                            )
                    );

                    lblTime.setText(
                            valueOrDash(
                                    rs.getString(
                                            "appointment_time"
                                    )
                            )
                    );

                    lblStatus.setText(
                            valueOrDash(
                                    rs.getString(
                                            "status"
                                    )
                            )
                    );

                    lblNotes.setText(
                            valueOrDash(
                                    rs.getString(
                                            "notes"
                                    )
                            )
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Appointment found successfully.",
                            "Search Result",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No appointment found for: "
                            + appointmentNumber,
                            "Appointment Not Found",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    clearDetails();
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database search error:\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String valueOrDash(String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "-";
        }

        return value;
    }

    private void clearFields() {

        txtAppointmentNumber.setText("");

        clearDetails();

        txtAppointmentNumber.requestFocus();
    }

    private void clearDetails() {

        lblPatient.setText("-");
        lblAddress.setText("-");
        lblContact.setText("-");
        lblDentist.setText("-");
        lblTreatment.setText("-");
        lblDate.setText("-");
        lblTime.setText("-");
        lblStatus.setText("-");
        lblNotes.setText("-");
    }
}