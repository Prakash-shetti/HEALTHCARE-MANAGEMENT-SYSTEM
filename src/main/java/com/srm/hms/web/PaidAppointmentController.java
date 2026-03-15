package com.srm.hms.web;

import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.srm.hms.dao.AppointmentDao;
import com.srm.hms.model.Appointment;
import com.srm.hms.model.Users;
import com.srm.hms.service.AppointmentService;
import com.srm.hms.service.UserService;

@WebServlet("/paidAppointments")
public class PaidAppointmentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentService appointmentService;
    private UserService userService;
    private AppointmentDao appointmentDao;

    public void init() {
        appointmentService = new AppointmentService();
        userService = new UserService();
        appointmentDao=new AppointmentDao();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "list":
                    listPaidAppointments(request, response);
                    break;
                case "assignDoctor":
                    assignDoctor(request, response);
                    break;

                //Added - show appointments for patient
                case "patientAppointments":
                    showPatientAppointments(request, response);
                    break;

                //Added - show appointments for doctor
                case "doctorAppointments":
                    showDoctorAppointments(request, response);
                    break;

                default:
                    listPaidAppointments(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("Customer/error-page.jsp").forward(request, response);
        }
    }

    private void listPaidAppointments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Appointment> paidList = appointmentService.getPaidAppointments();
            request.setAttribute("paidList", paidList);

            List<Users> doctors = userService.getAllDoctors();
            request.setAttribute("doctorList", doctors);

            RequestDispatcher rd = request.getRequestDispatcher("Admin/paidAppointments.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void assignDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        int doctorId = Integer.parseInt(request.getParameter("doctorId"));

        try {
            boolean updated = appointmentService.assignDoctor(appointmentId, doctorId);
            if (updated) {
                response.sendRedirect(request.getContextPath() + "/paidAppointments?action=list");
            } else {
                request.setAttribute("errorMessage", "Failed to assign doctor.");
                listPaidAppointments(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            listPaidAppointments(request, response);
        }
    }

    // New method for Patient - view own appointments with doctor details
    private void showPatientAppointments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            Users user = (Users) session.getAttribute("user");
            List<Appointment> appointments = appointmentService.getAppointmentsByPatient(user.getUser_id());
            
         // For each appointment, check if a prescription exists
            for (Appointment appt : appointments) {
                boolean exists = appointmentDao.hasPrescription(appt.getAppointmentId());
                appt.setPrescriptionExists(exists);
            }
            
            request.setAttribute("appointments", appointments);
            RequestDispatcher rd = request.getRequestDispatcher("Customer/patientAppointments.jsp");
            rd.forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    // New method for Doctor - view own appointments with patient details
    private void showDoctorAppointments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            Users user = (Users) session.getAttribute("user");
            List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(user.getUser_id());
            request.setAttribute("appointments", appointments);
            RequestDispatcher rd = request.getRequestDispatcher("Doctor/doctorAppointments.jsp");
            rd.forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}
