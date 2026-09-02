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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class BillingFrame extends JFrame {

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

public BillingFrame() {

    setTitle("Sunrise Dental Clinic - Calculate / Print Bill");
    setSize(700, 650);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    createInterface();
}

private void createInterface() {

    JPanel mainPanel =
            new JPanel(new BorderLayout(10, 10));

    mainPanel.setBorder(
            BorderFactory.createEmptyBorder(
                    20, 20, 20, 20
            )
    );

    // =========================
    // TITLE
    // =========================

    JLabel lblTitle =
            new JLabel(
                    "CALCULATE / PRINT BILL",
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

    // =========================
    // SEARCH PANEL
    // =========================

    JPanel searchPanel =
            new JPanel(new GridLayout(1, 3, 10, 10));

    searchPanel.add(
            new JLabel("Appointment Number:")
    );

    txtAppointmentNumber =
            new JTextField();

    searchPanel.add(
            txtAppointmentNumber
    );

    JButton btnSearch =
            new JButton("Search");

    searchPanel.add(
            btnSearch
    );

    // =========================
    // DETAILS PANEL
    // =========================

    JPanel detailsPanel =
            new JPanel(new GridLayout(8, 2, 10, 10));

    detailsPanel.setBorder(
            BorderFactory.createTitledBorder(
                    "Patient / Appointment Details"
            )
    );

    detailsPanel.add(
            new JLabel("Patient Name:")
    );

    lblPatient =
            new JLabel("-");

    detailsPanel.add(lblPatient);

    detailsPanel.add(
            new JLabel("Contact Number:")
    );

    lblContact =
            new JLabel("-");

    detailsPanel.add(lblContact);

    detailsPanel.add(
            new JLabel("Dentist:")
    );

    lblDentist =
            new JLabel("-");

    detailsPanel.add(lblDentist);

    detailsPanel.add(
            new JLabel("Treatment:")
    );

    lblTreatment =
            new JLabel("-");

    detailsPanel.add(lblTreatment);

    detailsPanel.add(
            new JLabel("Treatment Cost:")
    );

    lblTreatmentCost =
            new JLabel("-");

    detailsPanel.add(lblTreatmentCost);

    detailsPanel.add(
            new JLabel("Appointment Date:")
    );

    lblDate =
            new JLabel("-");

    detailsPanel.add(lblDate);

    detailsPanel.add(
            new JLabel("Status:")
    );

    lblStatus =
            new JLabel("-");

    detailsPanel.add(lblStatus);

    detailsPanel.add(
            new JLabel("TOTAL BILL:")
    );

    lblTotal =
            new JLabel("Rs. 0.00");

    lblTotal.setFont(
            new Font("Arial", Font.BOLD, 18)
    );

    lblTotal.setForeground(Color.RED);

    detailsPanel.add(lblTotal);

    // =========================
    // CENTER PANEL
    // =========================

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

    // =========================
    // BILL PREVIEW
    // =========================

    txtBill =
            new JTextArea();

    txtBill.setEditable(false);

    txtBill.setFont(
            new Font("Monospaced", Font.PLAIN, 13)
    );

    txtBill.setText(
            "Bill preview will appear here."
    );

    JScrollPane scrollPane =
            new JScrollPane(txtBill);

    scrollPane.setBorder(
            BorderFactory.createTitledBorder(
                    "Bill Preview"
            )
    );

    scrollPane.setPreferredSize(
            new java.awt.Dimension(600, 220)
    );

    centerPanel.add(
            scrollPane,
            BorderLayout.SOUTH
    );

    mainPanel.add(
            centerPanel,
            BorderLayout.CENTER
    );

    // =========================
    // BOTTOM BUTTONS
    // =========================

    JPanel buttonPanel =
            new JPanel(new GridLayout(1, 3, 10, 10));

    JButton btnCalculate =
            new JButton("Calculate Bill");

    JButton btnPrint =
            new JButton("Print Bill");

    JButton btnClose =
            new JButton("Close");

    buttonPanel.add(btnCalculate);
    buttonPanel.add(btnPrint);
    buttonPanel.add(btnClose);

    mainPanel.add(
            buttonPanel,
            BorderLayout.SOUTH
    );

    add(mainPanel);

    // =========================
    // ACTION LISTENERS
    // =========================

    btnSearch.addActionListener(e ->
            searchAppointment()
    );

    btnCalculate.addActionListener(e ->
            calculateBill()
    );

    btnPrint.addActionListener(e ->
            printBill()
    );

    btnClose.addActionListener(e ->
            dispose()
    );
}

// =========================
// SEARCH APPOINTMENT
// =========================

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

        stmt.setString(
                1,
                appointmentNumber
        );

        try (ResultSet rs =
                stmt.executeQuery()) {

            if (rs.next()) {

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

                double cost =
                        rs.getDouble("treatment_cost");

                lblTreatmentCost.setText(
                        String.format(
                                "Rs. %.2f",
                                cost
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

                lblTotal.setText(
                        String.format(
                                "Rs. %.2f",
                                cost
                        )
                );

                createBillPreview(
                        appointmentNumber,
                        cost
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

// =========================
// CALCULATE BILL
// =========================

private void calculateBill() {

    String appointmentNumber =
            txtAppointmentNumber.getText().trim();

    if (appointmentNumber.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Please search for an appointment first.",
                "Billing",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    String total =
            lblTotal.getText();

    if (total.equals("Rs. 0.00")) {

        JOptionPane.showMessageDialog(
                this,
                "Please search for a valid appointment first.",
                "Billing",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    JOptionPane.showMessageDialog(
            this,
            "Bill calculated successfully.\n\n"
            + "Total: " + total,
            "Bill Calculated",
            JOptionPane.INFORMATION_MESSAGE
    );
}

// =========================
// CREATE BILL PREVIEW
// =========================

private void createBillPreview(
        String appointmentNumber,
        double cost) {

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

    bill.append(
            appointmentNumber
    );

    bill.append("\n");

    bill.append(
            "Patient        : "
    );

    bill.append(
            lblPatient.getText()
    );

    bill.append("\n");

    bill.append(
            "Contact        : "
    );

    bill.append(
            lblContact.getText()
    );

    bill.append("\n");

    bill.append(
            "Dentist        : "
    );

    bill.append(
            lblDentist.getText()
    );

    bill.append("\n\n");

    bill.append(
            "Treatment      : "
    );

    bill.append(
            lblTreatment.getText()
    );

    bill.append("\n");

    bill.append(
            "Treatment Cost : Rs. "
    );

    bill.append(
            String.format("%.2f", cost)
    );

    bill.append("\n\n");

    bill.append(
            "----------------------------------------\n"
    );

    bill.append(
            "TOTAL          : Rs. "
    );

    bill.append(
            String.format("%.2f", cost)
    );

    bill.append("\n");

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

// =========================
// PRINT BILL
// =========================

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

// =========================
// HELPER
// =========================

private String valueOrDash(String value) {

    if (value == null
            || value.trim().isEmpty()) {

        return "-";
    }

    return value;
}

// =========================
// CLEAR
// =========================

private void clearFields() {

    txtAppointmentNumber.setText("");

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

// =========================
// MAIN
// =========================

public static void main(String[] args) {

    java.awt.EventQueue.invokeLater(() -> {

        new BillingFrame().setVisible(true);

    });
}
