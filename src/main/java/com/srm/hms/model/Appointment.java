package com.srm.hms.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private int slotId;
    private Date slotDate;
    private String description;
    private String status;
    private Timestamp createdAt;
    
    private String patientName;
    private Time slotTime;  
    private String doctorName;
    
    private boolean prescriptionExists;

    public boolean isPrescriptionExists() {
        return prescriptionExists;
    }

    public void setPrescriptionExists(boolean prescriptionExists) {
        this.prescriptionExists = prescriptionExists;
    }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    // getters and setters
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Time getSlotTime() { return slotTime; }
    public void setSlotTime(Time slotTime) { this.slotTime = slotTime; }


    // Getters & Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public Date getSlotDate() { return slotDate; }
    public void setSlotDate(Date slotDate) { this.slotDate = slotDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
