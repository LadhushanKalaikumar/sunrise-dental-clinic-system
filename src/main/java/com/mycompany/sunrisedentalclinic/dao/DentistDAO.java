package com.mycompany.sunrisedentalclinic.dao;

import com.mycompany.sunrisedentalclinic.database.DatabaseConnection;
import com.mycompany.sunrisedentalclinic.model.Dentist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DentistDAO {

    // Add a new dentist
    public boolean addDentist(Dentist dentist) {

        String sql = "INSERT INTO dentists "
                   + "(dentist_name, specialization, contact_number, status) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dentist.getDentistName());
            stmt.setString(2, dentist.getSpecialization());
            stmt.setString(3, dentist.getContactNumber());
            stmt.setString(4, dentist.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding dentist: " + e.getMessage());
            return false;
        }
    }

    // Find dentist by ID
    public Dentist getDentistById(int dentistId) {

        String sql = "SELECT dentist_id, dentist_name, specialization, "
                   + "contact_number, status "
                   + "FROM dentists WHERE dentist_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dentistId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Dentist(
                    rs.getInt("dentist_id"),
                    rs.getString("dentist_name"),
                    rs.getString("specialization"),
                    rs.getString("contact_number"),
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error finding dentist: " + e.getMessage());
        }

        return null;
    }

    // Get all dentists
    public List<Dentist> getAllDentists() {

        List<Dentist> dentists = new ArrayList<>();

        String sql = "SELECT dentist_id, dentist_name, specialization, "
                   + "contact_number, status "
                   + "FROM dentists ORDER BY dentist_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Dentist dentist = new Dentist(
                    rs.getInt("dentist_id"),
                    rs.getString("dentist_name"),
                    rs.getString("specialization"),
                    rs.getString("contact_number"),
                    rs.getString("status")
                );

                dentists.add(dentist);
            }

        } catch (SQLException e) {
            System.out.println("Error loading dentists: " + e.getMessage());
        }

        return dentists;
    }

    // Update dentist
    public boolean updateDentist(Dentist dentist) {

        String sql = "UPDATE dentists SET dentist_name = ?, "
                   + "specialization = ?, contact_number = ?, status = ? "
                   + "WHERE dentist_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dentist.getDentistName());
            stmt.setString(2, dentist.getSpecialization());
            stmt.setString(3, dentist.getContactNumber());
            stmt.setString(4, dentist.getStatus());
            stmt.setInt(5, dentist.getDentistId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating dentist: " + e.getMessage());
            return false;
        }
    }

    // Delete dentist
    public boolean deleteDentist(int dentistId) {

        String sql = "DELETE FROM dentists WHERE dentist_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dentistId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting dentist: " + e.getMessage());
            return false;
        }
    }
}
