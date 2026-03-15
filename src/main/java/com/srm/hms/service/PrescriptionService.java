package com.srm.hms.service;

import com.srm.hms.dao.PrescriptionDao;
import com.srm.hms.dao.AppointmentDao;
import com.srm.hms.model.Appointment;
import com.srm.hms.model.Prescription;
import java.sql.SQLException;
import java.util.List;

public class PrescriptionService {
    private PrescriptionDao dao = new PrescriptionDao();
    private AppointmentDao appointmentDao = new AppointmentDao();

    public void savePrescription(Prescription prescription) throws SQLException, ClassNotFoundException {
        dao.save(prescription);
    }

    public List<Prescription> getPrescriptionsByDoctor(int doctorId) throws SQLException, ClassNotFoundException {
        return dao.getByDoctor(doctorId);
    }

    public Prescription getPrescriptionByAppointment(int appointmentId) throws SQLException, ClassNotFoundException {
        return dao.getByAppointment(appointmentId);
    }

    public Appointment getAppointmentById(int appointmentId) throws SQLException, ClassNotFoundException {
        return appointmentDao.getAppointmentById(appointmentId);
    }
}
