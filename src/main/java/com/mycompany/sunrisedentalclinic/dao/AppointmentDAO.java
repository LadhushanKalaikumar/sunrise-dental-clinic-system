package com.mycompany.sunrisedentalclinic.dao;

import com.mycompany.sunrisedentalclinic.database.DatabaseConnection;
import com.mycompany.sunrisedentalclinic.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // Add a new appointment
    public boolean addAppointment(Appointment appointment) {

        String sql = "INSERT INTO appointments "
                + "(appointment_number, patient_id, dentist_id, treatment_id, "
                + "appointment_date, appointment_time, status, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointment.getAppointmentNumber());
            stmt.setInt(2, appointment.getPatientId());
            stmt.setInt(3, appointment.getDentistId());
            stmt.setInt(4, appointment.getTreatmentId());
            stmt.setDate(5, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(6, Time.valueOf(appointment.getAppointmentTime()));
            stmt.setString(7, appointment.getStatus());
            stmt.setString(8, appointment.getNotes());

            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println(
                    "Appointment number already exists or "
                    + "a referenced record is invalid: "
                    + e.getMessage()
            );

            return false;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding appointment: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // Search appointment by appointment number
    public Appointment getAppointmentByNumber(String appointmentNumber) {

        String sql = "SELECT appointment_id, appointment_number, "
                + "patient_id, dentist_id, treatment_id, "
                + "appointment_date, appointment_time, status, notes "
                + "FROM appointments "
                + "WHERE appointment_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentNumber);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new Appointment(
                            rs.getInt("appointment_id"),
                            rs.getString("appointment_number"),
                            rs.getInt("patient_id"),
                            rs.getInt("dentist_id"),
                            rs.getInt("treatment_id"),
                            rs.getDate("appointment_date").toLocalDate(),
                            rs.getTime("appointment_time").toLocalTime(),
                            rs.getString("status"),
                            rs.getString("notes")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error searching appointment: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // Check whether an appointment number already exists
    public boolean appointmentNumberExists(String appointmentNumber) {

        String sql = "SELECT appointment_id "
                + "FROM appointments "
                + "WHERE appointment_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error checking appointment number: "
                    + e.getMessage()
            );
        }

        return false;
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {

        List<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT appointment_id, appointment_number, "
                + "patient_id, dentist_id, treatment_id, "
                + "appointment_date, appointment_time, status, notes "
                + "FROM appointments "
                + "ORDER BY appointment_date, appointment_time";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getString("appointment_number"),
                        rs.getInt("patient_id"),
                        rs.getInt("dentist_id"),
                        rs.getInt("treatment_id"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        rs.getString("status"),
                        rs.getString("notes")
                );

                appointments.add(appointment);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error loading appointments: "
                    + e.getMessage()
            );
        }

        return appointments;
    }

    // Update appointment
    public boolean updateAppointment(Appointment appointment) {

        String sql = "UPDATE appointments SET "
                + "patient_id = ?, "
                + "dentist_id = ?, "
                + "treatment_id = ?, "
                + "appointment_date = ?, "
                + "appointment_time = ?, "
                + "status = ?, "
                + "notes = ? "
                + "WHERE appointment_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDentistId());
            stmt.setInt(3, appointment.getTreatmentId());
            stmt.setDate(4, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(5, Time.valueOf(appointment.getAppointmentTime()));
            stmt.setString(6, appointment.getStatus());
            stmt.setString(7, appointment.getNotes());
            stmt.setString(8, appointment.getAppointmentNumber());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error updating appointment: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // Delete appointment
    public boolean deleteAppointment(String appointmentNumber) {

        String sql = "DELETE FROM appointments "
                + "WHERE appointment_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentNumber);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error deleting appointment: "
                    + e.getMessage()
            );

            return false;
        }
    }
}
