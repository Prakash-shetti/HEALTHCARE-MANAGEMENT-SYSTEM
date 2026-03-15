package com.srm.hms.web;

import com.srm.hms.model.Prescription;
import com.srm.hms.model.Users;
import com.srm.hms.model.Appointment;
import com.srm.hms.service.PrescriptionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/doctor/prescription")
public class PrescriptionController extends HttpServlet {
    private PrescriptionService service = new PrescriptionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String appointmentIdStr = request.getParameter("appointmentId");
        int appointmentId = Integer.parseInt(appointmentIdStr);

        try {
            Prescription prescription = service.getPrescriptionByAppointment(appointmentId);
            Appointment appointment = service.getAppointmentById(appointmentId);

            request.setAttribute("prescription", prescription);
            request.setAttribute("appointmentId", appointmentId);
            request.setAttribute("patientId", appointment != null ? appointment.getPatientId() : 0);

            request.getRequestDispatcher("/Doctor/PrescriptionForm.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(500, "Database error.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users doctor = (Users) session.getAttribute("user");

        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        int patientId = Integer.parseInt(request.getParameter("patientId"));
        String medicineDetails = request.getParameter("medicineDetails");
        String notes = request.getParameter("notes");

        Prescription prescription = new Prescription();
        prescription.setAppointmentId(appointmentId);
        prescription.setDoctorId(doctor.getUser_id());
        prescription.setPatientId(patientId);
        prescription.setMedicineDetails(medicineDetails);
        prescription.setNotes(notes);

        try {
            service.savePrescription(prescription);

            // Show success message on same page
            request.setAttribute("successMessage", "Prescription saved successfully!");
            request.setAttribute("prescription", null); // Clear form
            request.setAttribute("appointmentId", appointmentId);
            request.setAttribute("patientId", patientId);

            request.getRequestDispatcher("/Doctor/PrescriptionForm.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error saving prescription.");
            request.getRequestDispatcher("/Doctor/PrescriptionForm.jsp").forward(request, response);
        }
    }
}
