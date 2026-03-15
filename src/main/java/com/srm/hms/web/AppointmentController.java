package com.srm.hms.web;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.srm.hms.model.Appointment;
import com.srm.hms.model.TimeSlot;
import com.srm.hms.service.AppointmentService;
import com.srm.hms.service.TimeSlotService;

@WebServlet("/appointment")
public class AppointmentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentService appointmentService;
    private TimeSlotService timeSlotService;

    public void init() {
        appointmentService = new AppointmentService();
        timeSlotService = new TimeSlotService();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "details";

        switch (action) {
            case "details":
                showPatientDetailsForm(request, response);
                break;
            case "saveDetails":
                savePatientDetails(request, response);
                break;
            case "slot":
                showSlotSelectionForm(request, response);
                break;
            case "saveSlot":
                saveSlotSelection(request, response);
                break;
            default:
                showPatientDetailsForm(request, response);
                break;
        }
    }

    /** STEP 1: Show patient details form */
    private void showPatientDetailsForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("appointment/details.jsp");
        rd.forward(request, response);
    }

    /** STEP 1: Save details and create draft appointment */
    private void savePatientDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int patientId = Integer.parseInt(request.getParameter("patientId"));
        String description = request.getParameter("description");

        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        appointment.setDescription(description);

        try {
            boolean success = appointmentService.createDraft(appointment);
            if (success) {
                response.sendRedirect(request.getContextPath()
                        + "/appointment?action=slot&appointmentId=" + appointment.getAppointmentId());
            } else {
                request.setAttribute("errorMessage", "Failed to create appointment.");
                showPatientDetailsForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            showPatientDetailsForm(request, response);
        }
    }

    /** STEP 2: Show slot selection form */
    private void showSlotSelectionForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String dateParam = request.getParameter("slotDate");
            List<TimeSlot> slots;

            if (dateParam != null && !dateParam.isEmpty()) {
                Date selectedDate = Date.valueOf(dateParam);
                slots = timeSlotService.getAvailableSlotsByDate(selectedDate);
                request.setAttribute("selectedDate", selectedDate);
            } else {
                slots = timeSlotService.getAllActiveSlots();
            }

            request.setAttribute("slotList", slots);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Unable to load slots.");
        }

        RequestDispatcher rd = request.getRequestDispatcher("appointment/selectSlot.jsp");
        rd.forward(request, response);
    }

    /** STEP 2: Save selected slot */
    private void saveSlotSelection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        int slotId = Integer.parseInt(request.getParameter("slotId"));
        Date slotDate = Date.valueOf(request.getParameter("slotDate"));

        try {
            boolean success = appointmentService.updateSlot(appointmentId, slotId, slotDate);
            if (success) {
                response.sendRedirect(request.getContextPath()
                        + "/payment/payment.jsp?appointmentId=" + appointmentId);
            } else {
                request.setAttribute("errorMessage", "Failed to update slot.");
                showSlotSelectionForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            showSlotSelectionForm(request, response);
        }
    }
}
