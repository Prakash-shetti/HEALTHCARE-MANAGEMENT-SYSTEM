package com.srm.hms.model;

import java.sql.Time;

public class TimeSlot {
    private int slotId;
    private Time slotTime;
    private boolean active;

    // Getters & Setters
    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public Time getSlotTime() { return slotTime; }
    public void setSlotTime(Time slotTime) { this.slotTime = slotTime; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
