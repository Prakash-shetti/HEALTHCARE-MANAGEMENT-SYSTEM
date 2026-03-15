package com.srm.hms.web;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.srm.hms.dao.AppointmentDao;
import com.srm.hms.dao.PaymentDao;
import com.srm.hms.model.Payments;
import com.srm.hms.model.Users;

/**
 * Servlet implementation class PaymentController
 */
@WebServlet("/payment")
public class PaymentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private PaymentDao paymentDAO;
    
    public void init() {
        paymentDAO = new PaymentDao();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        insertPayment(request, response);
    }
    
    private void insertPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("user");
        
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        
        int user_id = user.getUser_id();
        float amount = Float.parseFloat(req.getParameter("amount"));
        LocalDate payment_date = LocalDate.parse(req.getParameter("payment_date"));
        String payment_mode = req.getParameter("payment_mode");
        long card_number = Long.parseLong(req.getParameter("card_number"));
        String expire_date = req.getParameter("expire_date");
        int cvv_number = Integer.parseInt(req.getParameter("cvv_number"));
        String transaction_id = "BMC" + Math.round((Math.random() * 1000) + 20000);
        
        int appointmentId = Integer.parseInt(req.getParameter("appointmentId"));

        
        Payments newPaymentObj = new Payments(user_id, amount, payment_date, payment_mode, card_number, expire_date, cvv_number, transaction_id);
        
        try {
            int result = paymentDAO.insertPayment(newPaymentObj);
            if (result == 1) {
            	 AppointmentDao appointmentDao = new AppointmentDao();
                 boolean statusUpdated = appointmentDao.updateStatus(appointmentId, "Paid");
            	 req.setAttribute("payment", newPaymentObj);
                 RequestDispatcher dispatcher = req.getRequestDispatcher("Customer/payment-success.jsp");
                 dispatcher.forward(req, resp);
            } else {
                RequestDispatcher dispatcher = req.getRequestDispatcher("Customer/error-page.jsp");
                dispatcher.forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/error.jsp");
        }
    }
}
