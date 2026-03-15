package com.srm.hms.dao;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.srm.hms.model.TimeSlot;
import com.srm.hms.util.JdbcUtils;

public class TimeSlotDao {

    // Get all active slots
    public List<TimeSlot> getAllSlots() {
        List<TimeSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM time_slots WHERE is_active = true ORDER BY slot_time";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TimeSlot slot = new TimeSlot();
                slot.setSlotId(rs.getInt("slot_id"));
                slot.setSlotTime(rs.getTime("slot_time"));
                slot.setActive(rs.getBoolean("is_active"));
                list.add(slot);
            }
        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
        }
        return list;
    }

    // Get available slots for a specific date (exclude booked ones)
    public List<TimeSlot> getAvailableSlotsByDate(Date date) {
        List<TimeSlot> list = new ArrayList<>();
        String sql = "SELECT ts.slot_id, ts.slot_time, ts.is_active FROM time_slots ts WHERE ts.is_active = true AND ts.slot_id NOT IN (SELECT a.slot_id FROM appointments a WHERE CAST(a.slot_date AS DATE) = ?) ORDER BY ts.slot_time";


        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TimeSlot slot = new TimeSlot();
                slot.setSlotId(rs.getInt("slot_id"));
                slot.setSlotTime(rs.getTime("slot_time"));
                slot.setActive(rs.getBoolean("is_active"));
                list.add(slot);
            }
        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
        }
        return list;
    }

    // Add new slot
    public boolean addSlot(TimeSlot slot) {
        String sql = "INSERT INTO time_slots (slot_time, is_active) VALUES (?, ?)";
        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTime(1, slot.getSlotTime());
            ps.setBoolean(2, slot.isActive());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
            return false;
        }
    }

    // Deactivate slot
    public boolean deactivateSlot(int slotId) {
        String sql = "UPDATE time_slots SET is_active = false WHERE slot_id = ?";
        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, slotId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JdbcUtils.printSQLException(e);
            return false;
        }
    }
}
