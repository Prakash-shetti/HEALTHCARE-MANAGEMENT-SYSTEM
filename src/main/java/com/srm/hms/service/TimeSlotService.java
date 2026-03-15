package com.srm.hms.service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.srm.hms.dao.TimeSlotDao;
import com.srm.hms.model.TimeSlot;

public class TimeSlotService {
    private TimeSlotDao dao;

    public TimeSlotService() {
        dao = new TimeSlotDao();
    }

    // Get all active slots
    public List<TimeSlot> getAllActiveSlots() throws SQLException {
        return dao.getAllSlots();
    }

    // Get available slots by date
    public List<TimeSlot> getAvailableSlotsByDate(Date date) throws SQLException {
        return dao.getAvailableSlotsByDate(date);
    }

    // Add new slot
    public boolean addSlot(TimeSlot slot) {
        return dao.addSlot(slot);
    }

    // Deactivate slot
    public boolean deactivateSlot(int slotId) {
        return dao.deactivateSlot(slotId);
    }
}
