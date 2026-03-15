package com.srm.hms.model;

import java.sql.Timestamp;

public class Prescription {
    private int prescriptionId;
    private int appointmentId;
    private int doctorId;
    private int patientId;
    private String medicineDetails;
    private String notes;
    private Timestamp createdAt;

    // Getters and Setters
    public int getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(int prescriptionId) { this.prescriptionId = prescriptionId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getMedicineDetails() { return medicineDetails; }
    public void setMedicineDetails(String medicineDetails) { this.medicineDetails = medicineDetails; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
