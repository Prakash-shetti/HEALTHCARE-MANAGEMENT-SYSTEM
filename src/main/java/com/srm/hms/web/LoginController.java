package com.srm.hms.web;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.srm.hms.dao.LoginDao;
import com.srm.hms.model.Users;


@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginDao loginDao;

    @Override
    public void init() throws ServletException {
        super.init();
        loginDao = new LoginDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            authenticate(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void authenticate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String userEmail = request.getParameter("userEmail");
        String password = request.getParameter("password");

        // Validate inputs
        if (userEmail == null || password == null || userEmail.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and password are required.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login/login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Basic email validation
        if (!userEmail.matches("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$")) {
            request.setAttribute("errorMessage", "Invalid email format.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login/login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Users user = new Users();
        user.setUser_email(userEmail);
        user.setUser_password(password);

        boolean isValidUser = loginDao.validate(user);
        String userRole = loginDao.getRole(user);

        if (isValidUser) {
            Users loggedInUser = loginDao.getUserByEmail(userEmail);
            HttpSession session = request.getSession();
            session.setAttribute("user", loggedInUser);
            session.setAttribute("userRole", userRole);
            
            if ("Admin".equalsIgnoreCase(userRole)) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("Admin/Admin-list.jsp");
                dispatcher.forward(request, response);
            } else if ("Customer".equalsIgnoreCase(userRole)) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("Customer/Customer-list.jsp");
                dispatcher.forward(request, response);
            }else if ("Doctor".equalsIgnoreCase(userRole)) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("Doctor/Doctor-list.jsp");
                dispatcher.forward(request, response);
            }else {
                request.setAttribute("errorMessage", "Unknown user role.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("login/login.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Invalid email or password.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login/login.jsp");
            dispatcher.forward(request, response);
        }
    }
}
