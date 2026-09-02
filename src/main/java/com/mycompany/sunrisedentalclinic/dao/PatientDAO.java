package com.mycompany.sunrisedentalclinic.dao;

import com.mycompany.sunrisedentalclinic.database.DatabaseConnection;
import com.mycompany.sunrisedentalclinic.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // Add a new patient
    public boolean addPatient(Patient patient) {

        String sql = "INSERT INTO patients "
                   + "(patient_name, address, contact_number) "
                   + "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getPatientName());
            stmt.setString(2, patient.getAddress());
            stmt.setString(3, patient.getContactNumber());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
            return false;
        }
    }

    // Find patient by ID
    public Patient getPatientById(int patientId) {

        String sql = "SELECT patient_id, patient_name, address, contact_number "
                   + "FROM patients WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("address"),
                    rs.getString("contact_number")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error finding patient: " + e.getMessage());
        }

        return null;
    }

    // Get all patients
    public List<Patient> getAllPatients() {

        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT patient_id, patient_name, address, contact_number "
                   + "FROM patients ORDER BY patient_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("address"),
                    rs.getString("contact_number")
                );

                patients.add(patient);
            }

        } catch (SQLException e) {
            System.out.println("Error loading patients: " + e.getMessage());
        }

        return patients;
    }

    // Update patient
    public boolean updatePatient(Patient patient) {

        String sql = "UPDATE patients SET patient_name = ?, "
                   + "address = ?, contact_number = ? "
                   + "WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getPatientName());
            stmt.setString(2, patient.getAddress());
            stmt.setString(3, patient.getContactNumber());
            stmt.setInt(4, patient.getPatientId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating patient: " + e.getMessage());
            return false;
        }
    }

    // Delete patient
    public boolean deletePatient(int patientId) {

        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            return false;
        }
    }
}
