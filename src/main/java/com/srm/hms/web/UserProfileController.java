package com.srm.hms.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.srm.hms.dao.UserDao;
import com.srm.hms.model.Users;


/**
 * Servlet implementation class U8serProfileController
 */
@WebServlet("/profile")
public class UserProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
private UserDao userDAO;
	
	public void init(){
		userDAO = new UserDao();
	}
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Get current user from session
	        Users currentUser = (Users) request.getSession().getAttribute("user");
	        
	        // Get form parameters
	        String firstName = request.getParameter("user_firstName");
	        String lastName = request.getParameter("user_lastName");
	        String mobileNumberStr = request.getParameter("user_mobile_number");
	        String password = request.getParameter("user_password");
	        if (firstName != null && !firstName.isEmpty()) {
	            currentUser.setUser_firstName(firstName);
	        }
	        if (lastName != null && !lastName.isEmpty()) {
	            currentUser.setUser_lastName(lastName);
	        }
	        if (mobileNumberStr!=null) {
	            currentUser.setUser_mobile_number(mobileNumberStr);
	        }
	        if (password!=null) {
	            currentUser.setUser_password(password);
	        }

	        // Save the updated user back to the database
	        boolean updateSuccess = userDAO.updateUser(currentUser);
	        if (updateSuccess) {
	            // Set success message and redirect or forward to a success page
	            request.getSession().setAttribute("user", currentUser);
	            String contextPath = request.getContextPath();
	            response.sendRedirect(contextPath + "/register/register-update.jsp?success=true");

	        } 
	    }
}