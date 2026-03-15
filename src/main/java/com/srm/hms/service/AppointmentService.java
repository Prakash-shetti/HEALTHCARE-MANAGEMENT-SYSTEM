package com.srm.hms.service;

import java.sql.Date;
import java.util.List;

import com.srm.hms.dao.AppointmentDao;
import com.srm.hms.model.Appointment;

public class AppointmentService {

    private AppointmentDao appointmentDao;

    public AppointmentService() {
        this.appointmentDao = new AppointmentDao();
    }

    // Step 1: Create draft
    public boolean createDraft(Appointment appointment) {
        return appointmentDao.createDraft(appointment);
    }

    // Step 2: Update slot and set status Pending
    public boolean updateSlot(int appointmentId, int slotId, Date slotDate) {
        return appointmentDao.updateSlot(appointmentId, slotId, slotDate);
    }

    // Step 3: Assign doctor (Admin)
    public boolean assignDoctor(int appointmentId, int doctorId) {
        return appointmentDao.assignDoctor(appointmentId, doctorId);
    }

    // Update appointment status
    public boolean updateStatus(int appointmentId, String status) {
        return appointmentDao.updateStatus(appointmentId, status);
    }

    // Get appointments by patient
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentDao.getAppointmentsByPatient(patientId);
    }

    // Get appointments by doctor
    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        return appointmentDao.getAppointmentsByDoctor(doctorId);
    }

    // Get paid appointments (Admin)
    public List<Appointment> getPaidAppointments() {
        return appointmentDao.getPaidAppointments();
    }
}
