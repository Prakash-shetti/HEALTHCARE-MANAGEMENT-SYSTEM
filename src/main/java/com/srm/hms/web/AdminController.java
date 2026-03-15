package com.srm.hms.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.srm.hms.model.Users;
import com.srm.hms.service.UserService;

@WebServlet("/")
public class AdminController extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	private UserService userService;
 
	public void init() {
		userService = new UserService();
	}
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
 
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getServletPath();
		switch (action) {
		case "/patientlist" :
			listPatient(req,resp);
			break;
		case "/doctorslist" :
			listDoctors(req,resp);
			break;
		default:
			RequestDispatcher rd = req.getRequestDispatcher("Admin/Admin-list.jsp");
			rd.forward(req, resp);
			break;
		}
	}

	 private void listPatient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        try {
	            List<Users> patients = userService.getAllPatients();
	            req.setAttribute("patientList", patients);
	            RequestDispatcher dispatcher = req.getRequestDispatcher("Admin/patient-list.jsp");
	            dispatcher.forward(req, resp);
	        } catch (Exception e) {
	            e.printStackTrace();
	            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching patient list");
	        }
	    }

	    private void listDoctors(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        try {
	            List<Users> doctors = userService.getAllDoctors();
	            req.setAttribute("doctorList", doctors);
	            RequestDispatcher dispatcher = req.getRequestDispatcher("Admin/doctors-list.jsp");
	            dispatcher.forward(req, resp);
	        } catch (Exception e) {
	            e.printStackTrace();
	            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching doctor list");
	        }
	    }
	

}
