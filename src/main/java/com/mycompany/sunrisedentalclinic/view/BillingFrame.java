package com.mycompany.sunrisedentalclinic.view;

import com.mycompany.sunrisedentalclinic.database.DatabaseConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BillingFrame extends JFrame {

    private static final double CONSULTATION_FEE = 2500.00;

    private JTextField txtAppointmentNumber;

    private JLabel lblPatient;
    private JLabel lblContact;
    private JLabel lblDentist;
    private JLabel lblTreatment;
    private JLabel lblTreatmentCost;
    private JLabel lblDate;
    private JLabel lblStatus;
    private JLabel lblTotal;

    private JTextArea txtBill;

    private int currentAppointmentId = -1;
    private double currentTreatmentCost = 0.00;

    public BillingFrame() {
        setTitle("Sunrise Dental Clinic - Billing");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createInterface();
    }

    private void createInterface() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel lblTitle = new JLabel(
                "CALCULATE / PRINT BILL",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 105, 148));

        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(
                new GridLayout(1, 3, 10, 10)
        );

        searchPanel.add(new JLabel("Appointment Number:"));

        txtAppointmentNumber = new JTextField();
        searchPanel.add(txtAppointmentNumber);

        JButton btnSearch = new JButton("Search");
        searchPanel.add(btnSearch);

        JPanel detailsPanel = new JPanel(
                new GridLayout(8, 2, 10, 10)
        );

        detailsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Patient / Appointment Details"
                )
        );

        detailsPanel.add(new JLabel("Patient Name:"));
        lblPatient = new JLabel("-");
        detailsPanel.add(lblPatient);

        detailsPanel.add(new JLabel("Contact Number:"));
        lblContact = new JLabel("-");
        detailsPanel.add(lblContact);

        detailsPanel.add(new JLabel("Dentist:"));
        lblDentist = new JLabel("-");
        detailsPanel.add(lblDentist);

        detailsPanel.add(new JLabel("Treatment:"));
        lblTreatment = new JLabel("-");
        detailsPanel.add(lblTreatment);

        detailsPanel.add(new JLabel("Treatment Cost:"));
        lblTreatmentCost = new JLabel("-");
        detailsPanel.add(lblTreatmentCost);

        detailsPanel.add(new JLabel("Appointment Date:"));
        lblDate = new JLabel("-");
        detailsPanel.add(lblDate);

        detailsPanel.add(new JLabel("Status:"));
        lblStatus = new JLabel("-");
        detailsPanel.add(lblStatus);

        detailsPanel.add(new JLabel("TOTAL BILL:"));

        lblTotal = new JLabel("Rs. 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(Color.RED);

        detailsPanel.add(lblTotal);

        JPanel centerPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(detailsPanel, BorderLayout.CENTER);

        txtBill = new JTextArea();
        txtBill.setEditable(false);
        txtBill.setFont(
                new Font("Monospaced", Font.PLAIN, 13)
        );
        txtBill.setText("Bill preview will appear here.");

        JScrollPane scrollPane = new JScrollPane(txtBill);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Bill Preview"
                )
        );

        scrollPane.setPreferredSize(
                new java.awt.Dimension(600, 260)
        );

        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(
                new GridLayout(1, 3, 10, 10)
        );

        JButton btnCalculate =
                new JButton("Calculate / Save Bill");

        JButton btnPrint =
                new JButton("Print Bill");

        JButton btnClose =
                new JButton("Close");

        buttonPanel.add(btnCalculate);
        buttonPanel.add(btnPrint);
        buttonPanel.add(btnClose);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnSearch.addActionListener(e ->
                searchAppointment()
        );

        btnCalculate.addActionListener(e ->
                calculateAndSaveBill()
        );

        btnPrint.addActionListener(e ->
                printBill()
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
                "SELECT "
                + "a.appointment_id, "
                + "a.appointment_number, "
                + "p.patient_name, "
                + "p.contact_number, "
                + "d.dentist_name, "
                + "t.treatment_name, "
                + "t.treatment_cost, "
                + "a.appointment_date, "
                + "a.status "
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

            stmt.setString(1, appointmentNumber);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    currentAppointmentId =
                            rs.getInt("appointment_id");

                    currentTreatmentCost =
                            rs.getDouble("treatment_cost");

                    lblPatient.setText(
                            valueOrDash(
                                    rs.getString("patient_name")
                            )
                    );

                    lblContact.setText(
                            valueOrDash(
                                    rs.getString("contact_number")
                            )
                    );

                    lblDentist.setText(
                            valueOrDash(
                                    rs.getString("dentist_name")
                            )
                    );

                    lblTreatment.setText(
                            valueOrDash(
                                    rs.getString("treatment_name")
                            )
                    );

                    lblTreatmentCost.setText(
                            String.format(
                                    "Rs. %.2f",
                                    currentTreatmentCost
                            )
                    );

                    lblDate.setText(
                            valueOrDash(
                                    rs.getString("appointment_date")
                            )
                    );

                    lblStatus.setText(
                            valueOrDash(
                                    rs.getString("status")
                            )
                    );

                    double total =
                            CONSULTATION_FEE
                            + currentTreatmentCost;

                    lblTotal.setText(
                            String.format(
                                    "Rs. %.2f",
                                    total
                            )
                    );

                    createBillPreview(
                            appointmentNumber,
                            currentTreatmentCost,
                            total
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Appointment found successfully.",
                            "Billing",
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

                    clearFields();
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

    private void calculateAndSaveBill() {

        if (currentAppointmentId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please search for an appointment first.",
                    "Billing",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double total =
                CONSULTATION_FEE
                + currentTreatmentCost;

        String checkSql =
                "SELECT bill_id, payment_status "
                + "FROM bills "
                + "WHERE appointment_id = ?";

        String insertSql =
                "INSERT INTO bills "
                + "(appointment_id, consultation_fee, "
                + "treatment_cost, total_amount, payment_status) "
                + "VALUES (?, ?, ?, ?, 'UNPAID')";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement checkStmt =
                        conn.prepareStatement(checkSql)
        ) {

            checkStmt.setInt(
                    1,
                    currentAppointmentId
            );

            try (ResultSet rs =
                    checkStmt.executeQuery()) {

                if (rs.next()) {

                    String paymentStatus =
                            rs.getString("payment_status");

                    JOptionPane.showMessageDialog(
                            this,
                            "A bill already exists for this appointment.\n\n"
                            + "Total: Rs. "
                            + String.format("%.2f", total)
                            + "\n"
                            + "Payment Status: "
                            + paymentStatus,
                            "Bill Already Exists",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    return;
                }
            }

            try (
                    PreparedStatement insertStmt =
                            conn.prepareStatement(insertSql)
            ) {

                insertStmt.setInt(
                        1,
                        currentAppointmentId
                );

                insertStmt.setDouble(
                        2,
                        CONSULTATION_FEE
                );

                insertStmt.setDouble(
                        3,
                        currentTreatmentCost
                );

                insertStmt.setDouble(
                        4,
                        total
                );

                insertStmt.executeUpdate();
            }

            lblTotal.setText(
                    String.format(
                            "Rs. %.2f",
                            total
                    )
            );

            createBillPreview(
                    txtAppointmentNumber.getText().trim(),
                    currentTreatmentCost,
                    total
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Bill saved successfully.\n\n"
                    + "Consultation Fee: Rs. "
                    + String.format(
                            "%.2f",
                            CONSULTATION_FEE
                    )
                    + "\n"
                    + "Treatment Cost: Rs. "
                    + String.format(
                            "%.2f",
                            currentTreatmentCost
                    )
                    + "\n"
                    + "Total: Rs. "
                    + String.format(
                            "%.2f",
                            total
                    )
                    + "\n"
                    + "Payment Status: UNPAID",
                    "Bill Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to save bill:\n"
                    + e.getMessage(),
                    "Billing Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createBillPreview(
            String appointmentNumber,
            double treatmentCost,
            double total) {

        StringBuilder bill =
                new StringBuilder();

        bill.append(
                "========================================\n"
        );

        bill.append(
                "        SUNRISE DENTAL CLINIC\n"
        );

        bill.append(
                "              PATIENT BILL\n"
        );

        bill.append(
                "========================================\n\n"
        );

        bill.append(
                "Appointment No : "
        );

        bill.append(appointmentNumber);
        bill.append("\n");

        bill.append(
                "Patient        : "
        );

        bill.append(lblPatient.getText());
        bill.append("\n");

        bill.append(
                "Contact        : "
        );

        bill.append(lblContact.getText());
        bill.append("\n");

        bill.append(
                "Dentist        : "
        );

        bill.append(lblDentist.getText());
        bill.append("\n");

        bill.append(
                "Date           : "
        );

        bill.append(lblDate.getText());
        bill.append("\n\n");

        bill.append(
                "Treatment      : "
        );

        bill.append(lblTreatment.getText());
        bill.append("\n");

        bill.append(
                "Consultation   : Rs. "
        );

        bill.append(
                String.format(
                        "%.2f",
                        CONSULTATION_FEE
                )
        );

        bill.append("\n");

        bill.append(
                "Treatment Cost : Rs. "
        );

        bill.append(
                String.format(
                        "%.2f",
                        treatmentCost
                )
        );

        bill.append("\n\n");

        bill.append(
                "----------------------------------------\n"
        );

        bill.append(
                "TOTAL          : Rs. "
        );

        bill.append(
                String.format(
                        "%.2f",
                        total
                )
        );

        bill.append("\n");

        bill.append(
                "PAYMENT STATUS : UNPAID\n"
        );

        bill.append(
                "----------------------------------------\n\n"
        );

        bill.append(
                "Thank you for visiting\n"
        );

        bill.append(
                "Sunrise Dental Clinic\n"
        );

        bill.append(
                "========================================\n"
        );

        txtBill.setText(
                bill.toString()
        );
    }

    private void printBill() {

        if (txtBill.getText().trim().isEmpty()
                || txtBill.getText().equals(
                        "Bill preview will appear here."
                )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please search for an appointment first.",
                    "Print Bill",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            boolean printed =
                    txtBill.print();

            if (printed) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bill sent to printer successfully.",
                        "Print Bill",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (PrinterException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to print bill:\n"
                    + e.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String valueOrDash(String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "-";
        }

        return value;
    }

    private void clearFields() {

        txtAppointmentNumber.setText("");

        currentAppointmentId = -1;
        currentTreatmentCost = 0.00;

        lblPatient.setText("-");
        lblContact.setText("-");
        lblDentist.setText("-");
        lblTreatment.setText("-");
        lblTreatmentCost.setText("-");
        lblDate.setText("-");
        lblStatus.setText("-");
        lblTotal.setText("Rs. 0.00");

        txtBill.setText(
                "Bill preview will appear here."
        );

        txtAppointmentNumber.requestFocus();
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {

            new BillingFrame().setVisible(true);

        });
    }
}
