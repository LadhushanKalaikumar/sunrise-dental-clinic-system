package com.mycompany.sunrisedentalclinic.dao;

import com.mycompany.sunrisedentalclinic.database.DatabaseConnection;
import com.mycompany.sunrisedentalclinic.model.Treatment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO {

    // Add a new treatment
    public boolean addTreatment(Treatment treatment) {

        String sql = "INSERT INTO treatments "
                   + "(treatment_name, description, treatment_cost, status) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, treatment.getTreatmentName());
            stmt.setString(2, treatment.getDescription());
            stmt.setBigDecimal(3, treatment.getTreatmentCost());
            stmt.setString(4, treatment.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding treatment: " + e.getMessage());
            return false;
        }
    }

    // Find treatment by ID
    public Treatment getTreatmentById(int treatmentId) {

        String sql = "SELECT treatment_id, treatment_name, description, "
                   + "treatment_cost, status "
                   + "FROM treatments WHERE treatment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, treatmentId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Treatment(
                    rs.getInt("treatment_id"),
                    rs.getString("treatment_name"),
                    rs.getString("description"),
                    rs.getBigDecimal("treatment_cost"),
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error finding treatment: " + e.getMessage());
        }

        return null;
    }

    // Get all treatments
    public List<Treatment> getAllTreatments() {

        List<Treatment> treatments = new ArrayList<>();

        String sql = "SELECT treatment_id, treatment_name, description, "
                   + "treatment_cost, status "
                   + "FROM treatments ORDER BY treatment_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Treatment treatment = new Treatment(
                    rs.getInt("treatment_id"),
                    rs.getString("treatment_name"),
                    rs.getString("description"),
                    rs.getBigDecimal("treatment_cost"),
                    rs.getString("status")
                );

                treatments.add(treatment);
            }

        } catch (SQLException e) {
            System.out.println("Error loading treatments: " + e.getMessage());
        }

        return treatments;
    }

    // Update treatment
    public boolean updateTreatment(Treatment treatment) {

        String sql = "UPDATE treatments SET "
                   + "treatment_name = ?, "
                   + "description = ?, "
                   + "treatment_cost = ?, "
                   + "status = ? "
                   + "WHERE treatment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, treatment.getTreatmentName());
            stmt.setString(2, treatment.getDescription());
            stmt.setBigDecimal(3, treatment.getTreatmentCost());
            stmt.setString(4, treatment.getStatus());
            stmt.setInt(5, treatment.getTreatmentId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating treatment: " + e.getMessage());
            return false;
        }
    }

    // Delete treatment
    public boolean deleteTreatment(int treatmentId) {

        String sql = "DELETE FROM treatments WHERE treatment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, treatmentId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting treatment: " + e.getMessage());
            return false;
        }
    }
}
