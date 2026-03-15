package com.srm.hms.dao;

import com.srm.hms.model.Prescription;
import com.srm.hms.util.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDao {

    

    // Save prescription
    public void save(Prescription prescription) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO prescriptions (appointment_id, doctor_id, patient_id, medicine_details, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JdbcUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescription.getAppointmentId());
            ps.setInt(2, prescription.getDoctorId());
            ps.setInt(3, prescription.getPatientId());
            ps.setString(4, prescription.getMedicineDetails());
            ps.setString(5, prescription.getNotes());
            ps.executeUpdate();
        }
    }

    // Get all prescriptions for a doctor
    public List<Prescription> getByDoctor(int doctorId) throws SQLException, ClassNotFoundException {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE doctor_id = ?";
        try (Connection conn = JdbcUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setPrescriptionId(rs.getInt("prescription_id"));
                p.setAppointmentId(rs.getInt("appointment_id"));
                p.setDoctorId(rs.getInt("doctor_id"));
                p.setPatientId(rs.getInt("patient_id"));
                p.setMedicineDetails(rs.getString("medicine_details"));
                p.setNotes(rs.getString("notes"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(p);
            }
        }
        return list;
    }

    // Get prescription by appointment
    public Prescription getByAppointment(int appointmentId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM prescriptions WHERE appointment_id = ?";
        try (Connection conn = JdbcUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Prescription p = new Prescription();
                p.setPrescriptionId(rs.getInt("prescription_id"));
                p.setAppointmentId(rs.getInt("appointment_id"));
                p.setDoctorId(rs.getInt("doctor_id"));
                p.setPatientId(rs.getInt("patient_id"));
                p.setMedicineDetails(rs.getString("medicine_details"));
                p.setNotes(rs.getString("notes"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                return p;
            }
        }
        return null;
    }
}
