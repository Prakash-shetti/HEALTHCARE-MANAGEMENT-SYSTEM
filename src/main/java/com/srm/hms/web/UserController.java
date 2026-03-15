package com.srm.hms.web;

import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.srm.hms.dao.UserDao;
import com.srm.hms.exception.UserExistException;
import com.srm.hms.model.Users;
import com.srm.hms.service.UserService;

 
@WebServlet("/register")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;
 
	public void init() {
		userService = new UserService();
	}
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		register(request, response);
	}
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("register/register.jsp");
	}
 
	private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String userRole = request.getParameter("userRole");
		String userEmail = request.getParameter("userEmail");
		String password = request.getParameter("password");
		String mobile = request.getParameter("mobile");
 
		Users user = new Users();
		user.setUser_firstName(firstName);
		user.setUser_lastName(lastName);
		user.setUser_role(userRole);
		user.setUser_email(userEmail);
		user.setUser_password(password);
		user.setUser_mobile_number(mobile);
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		try {
//			if(userDao.userExists(userEmail)) {
//				request.setAttribute("errorMessage", "user already exist");
//				request.getRequestDispatcher("register/register.jsp").forward(request, response);
//			}
//			else {
//				int result = userDao.registerUser(user);
//				if(result == 1) {
//					request.setAttribute("NOTIFICATION", "User Registered Successfully!");
//					RequestDispatcher dispatcher = request.getRequestDispatcher("register/register.jsp");
//					dispatcher.forward(request, response);
//				}
//			}
			  boolean success = userService.registerUser(user);
	            if (success) {
	            	 
	                 
	                     request.setAttribute("NOTIFICATION", "User Registered Successfully!");
	                 }
	            
			
		} catch (UserExistException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
		RequestDispatcher dispatcher = request.getRequestDispatcher("register/register.jsp");
        dispatcher.forward(request, response);
		
	}
}