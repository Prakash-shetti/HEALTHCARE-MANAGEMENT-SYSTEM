package com.srm.hms.dao;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.srm.hms.model.Appointment;
import com.srm.hms.util.JdbcUtils;

public class AppointmentDao {

    // Step 1: Create draft appointment
    public boolean createDraft(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, description, status) VALUES (?, ?, 'Draft')";
        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, appointment.getPatientId());
            ps.setString(2, appointment.getDescription());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    appointment.setAppointmentId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
        }
        return false;
    }

    // Step 2: Update slot
    public boolean updateSlot(int appointmentId, int slotId, Date slotDate) {
        String sql = "UPDATE appointments SET slot_id=?, slot_date=?, status='Pending' WHERE appointment_id=?";
        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, slotId);
            ps.setDate(2, slotDate);
            ps.setInt(3, appointmentId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
            return false;
        }
    }

    // Assign doctor (Admin)
    public boolean assignDoctor(int appointmentId, int doctorId) {
        String sql = "UPDATE appointments SET doctor_id=?, status='Assigned' WHERE appointment_id=?";
        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setInt(2, appointmentId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
            return false;
        }
    }

    // Update status
    public boolean updateStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status=? WHERE appointment_id=?";
        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, appointmentId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
            return false;
        }
    }

 // Get Appointments for Patient (excluding Pending and Draft, with Doctor details)
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.description, a.slot_date, a.status, "
                   + "d.user_first_name AS doctor_first_name, d.user_last_name AS doctor_last_name, "
                   + "t.slot_time "
                   + "FROM appointments a "
                   + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                   + "JOIN time_slots t ON a.slot_id = t.slot_id "
                   + "WHERE a.patient_id = ? AND a.status NOT IN ('Pending', 'Draft') "
                   + "ORDER BY a.slot_date DESC";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPatientResult(rs));
            }
        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
        }
        return list;
    }


    // Get Appointments for Doctor (with Patient details)
    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.description, a.slot_date, a.status, "
                   + "p.user_first_name AS patient_first_name, p.user_last_name AS patient_last_name, "
                   + "t.slot_time "
                   + "FROM appointments a "
                   + "JOIN users p ON a.patient_id = p.user_id "
                   + "JOIN time_slots t ON a.slot_id = t.slot_id "
                   + "WHERE a.doctor_id = ? "
                   + "ORDER BY a.slot_date DESC";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapDoctorResult(rs));
            }
        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
        }
        return list;
    }

    // Admin view Paid appointments
    public List<Appointment> getPaidAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.description, a.slot_date, a.status, "
                   + "u.user_first_name, u.user_last_name, "
                   + "t.slot_id, t.slot_time "
                   + "FROM appointments a "
                   + "JOIN users u ON a.patient_id = u.user_id "
                   + "JOIN time_slots t ON a.slot_id = t.slot_id "
                   + "WHERE a.status = 'Paid'";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPaidResult(rs));
            }
        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
        }
        return list;
    }

    // Mapping for Patient View
    private Appointment mapPatientResult(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setSlotDate(rs.getDate("slot_date"));
        a.setDescription(rs.getString("description"));
        a.setStatus(rs.getString("status"));
        a.setSlotTime(rs.getTime("slot_time"));
        if (rs.getString("doctor_first_name") != null) {
            a.setDoctorName(rs.getString("doctor_first_name") + " " + rs.getString("doctor_last_name"));
        }else {
        	a.setDoctorName(null);
        }
        return a;
    }

    // Mapping for Doctor View
    private Appointment mapDoctorResult(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setSlotDate(rs.getDate("slot_date"));
        a.setDescription(rs.getString("description"));
        a.setStatus(rs.getString("status"));
        a.setSlotTime(rs.getTime("slot_time"));
        if (rs.getString("patient_first_name") != null) {
            a.setPatientName(rs.getString("patient_first_name") + " " + rs.getString("patient_last_name"));
        }else {
        	a.setPatientName(null);
        }
        return a;
    }

    // Mapping for Admin Paid Appointments
    private Appointment mapPaidResult(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setPatientId(rs.getInt("patient_id"));
        a.setDoctorId(rs.getInt("doctor_id"));
        a.setDescription(rs.getString("description"));
        a.setSlotDate(rs.getDate("slot_date"));
        a.setStatus(rs.getString("status"));
        a.setPatientName(rs.getString("user_first_name") + " " + rs.getString("user_last_name"));
        a.setSlotTime(rs.getTime("slot_time"));
        return a;
    }

    public Appointment getAppointmentById(int appointmentId) throws SQLException, ClassNotFoundException {
        Appointment appointment = null;
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                appointment.setDoctorId(rs.getInt("doctor_id"));
                appointment.setPatientId(rs.getInt("patient_id"));
                appointment.setDescription(rs.getString("description"));
                appointment.setSlotDate(rs.getDate("slot_date"));
                appointment.setStatus(rs.getString("status"));
            }
        }
        return appointment;
    }

    public boolean hasPrescription(int appointmentId) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM prescriptions WHERE appointment_id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

}
