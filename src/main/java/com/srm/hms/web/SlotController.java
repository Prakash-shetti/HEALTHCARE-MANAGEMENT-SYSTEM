package com.srm.hms.web;

import com.srm.hms.model.TimeSlot;
import com.srm.hms.service.TimeSlotService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Time;
import java.util.List;

@WebServlet("/slots")
public class SlotController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TimeSlotService slotService;

    public void init() {
        slotService = new TimeSlotService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<TimeSlot> slots = slotService.getAllActiveSlots();
            request.setAttribute("slotList", slots);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error loading slots");
        }
        RequestDispatcher rd = request.getRequestDispatcher("Admin/manage-slots.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String timeStr = request.getParameter("slotTime");
            Time slotTime = Time.valueOf(timeStr + ":00"); // "09:00"  "09:00:00"

            TimeSlot slot = new TimeSlot();
            slot.setSlotTime(slotTime);
            slot.setActive(true);

            slotService.addSlot(slot);
        } else if ("deactivate".equals(action)) {
            int slotId = Integer.parseInt(request.getParameter("slotId"));
            slotService.deactivateSlot(slotId);
        }

        response.sendRedirect(request.getContextPath() + "/slots");
    }
}
