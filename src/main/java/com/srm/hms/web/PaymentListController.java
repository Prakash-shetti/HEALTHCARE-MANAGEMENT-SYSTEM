package com.srm.hms.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.srm.hms.dao.PaymentDao;
import com.srm.hms.model.Payments;


@WebServlet("/paymentlist")
public class PaymentListController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PaymentDao paymentDAO = new PaymentDao();
            List<Payments> payments = paymentDAO.getAllPayments();
            request.setAttribute("payments", payments);
            request.getRequestDispatcher("/Customer/payment-list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while fetching the payments.");
        }
    }
}
