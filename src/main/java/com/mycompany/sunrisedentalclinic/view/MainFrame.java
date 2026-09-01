package com.mycompany.sunrisedentalclinic.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final String fullName;

    public MainFrame(String fullName) {
        this.fullName = fullName;
        setupInterface();
    }

    private void setupInterface() {

        setTitle("Sunrise Dental Clinic - Main Dashboard");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main background
        getContentPane().setBackground(
                new Color(225, 245, 254)
        );

        // Title
        JLabel lblTitle = new JLabel(
                "SUNRISE DENTAL CLINIC",
                SwingConstants.CENTER
        );

        lblTitle.setFont(
                new Font("Arial", Font.BOLD, 26)
        );

        lblTitle.setForeground(
                new Color(0, 105, 148)
        );

        // Welcome message
        JLabel lblWelcome = new JLabel(
                "Welcome, " + fullName,
                SwingConstants.CENTER
        );

        lblWelcome.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        // Buttons
        JButton btnAppointment =
                new JButton("Register Appointment");

        JButton btnSearch =
                new JButton("Search Appointment");

        JButton btnPatients =
                new JButton("Patients");

        JButton btnDentists =
                new JButton("Dentists");

        JButton btnTreatments =
                new JButton("Treatments");

        JButton btnBilling =
                new JButton("Calculate / Print Bill");

        JButton btnHelp =
                new JButton("Help");

        JButton btnLogout =
                new JButton("Logout");

        // Button panel
        JPanel buttonPanel = new JPanel(
                new GridLayout(4, 2, 15, 15)
        );

        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 50, 30, 50
                )
        );

        buttonPanel.add(btnAppointment);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnPatients);
        buttonPanel.add(btnDentists);
        buttonPanel.add(btnTreatments);
        buttonPanel.add(btnBilling);
        buttonPanel.add(btnHelp);
        buttonPanel.add(btnLogout);

        // Header
        JPanel headerPanel = new JPanel(
                new GridLayout(2, 1)
        );

        headerPanel.setBackground(
                new Color(225, 245, 254)
        );

        headerPanel.add(lblTitle);
        headerPanel.add(lblWelcome);

        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Temporary button messages
        btnAppointment.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Appointment Registration module will be developed next."
            );
        });

        btnSearch.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Appointment Search module will be developed next."
            );
        });

        btnPatients.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Patient Management module will be developed next."
            );
        });

        btnDentists.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Dentist Management module will be developed next."
            );
        });

        btnTreatments.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Treatment Management module will be developed next."
            );
        });

        btnBilling.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Billing module will be developed next."
            );
        });

        btnHelp.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Sunrise Dental Clinic Help\n\n"
                    + "1. Register a new appointment.\n"
                    + "2. Search appointment details.\n"
                    + "3. Manage patients, dentists and treatments.\n"
                    + "4. Calculate and print patient bills.\n"
                    + "5. Use Logout to safely exit the system.",
                    "Help",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        btnLogout.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
    }
}
