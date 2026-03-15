package com.srm.hms.web;

import com.srm.hms.model.Prescription;
import com.srm.hms.model.Users;
import com.srm.hms.service.PrescriptionService;
import com.srm.hms.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@WebServlet("/patient/downloadPrescription")
public class DownloadPrescriptionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PrescriptionService service = new PrescriptionService();
    private UserService user = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        HttpSession session = request.getSession();
        Users patient = (Users) session.getAttribute("user");

        if (patient == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            Prescription prescription = service.getPrescriptionByAppointment(appointmentId);

            if (prescription == null || prescription.getPatientId() != patient.getUser_id()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot access this prescription.");
                return;
            }

            Users doctor = user.selectUserById(prescription.getDoctorId());
            Users patientUser = user.selectUserById(prescription.getPatientId());

            // Set PDF response
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Prescription_" + appointmentId + ".pdf");

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDPageContentStream content = new PDPageContentStream(document, page);

                float yPosition = 780;
                float leftMargin = 50;
                float lineHeight = 18;

                // Helper function to write text
                java.util.function.BiFunction<String, Float, Float> writeLines = (text, yPos) -> {
                    try {
                        if (text == null) text = "";
                        String[] lines = text.split("\\r?\\n");
                        for (String line : lines) {
                            content.beginText();
                            content.newLineAtOffset(leftMargin, yPos);
                            content.showText(line);
                            content.endText();
                            yPos -= lineHeight;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return yPos;
                };

                // ===== HEADER SECTION =====
                content.setFont(PDType1Font.HELVETICA_BOLD, 20);
                String hospitalName = "HOSPITAL CORE";
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(hospitalName) / 1000 * 20;
                float centerX = (PDRectangle.A4.getWidth() - titleWidth) / 2;
                content.beginText();
                content.newLineAtOffset(centerX, yPosition);
                content.showText(hospitalName);
                content.endText();

                yPosition -= lineHeight * 2;

                // Draw line under header
                content.setStrokingColor(0, 0, 0);
                content.moveTo(leftMargin, yPosition);
                content.lineTo(PDRectangle.A4.getWidth() - leftMargin, yPosition);
                content.stroke();

                yPosition -= lineHeight * 2;

                // ===== PRESCRIPTION TITLE =====
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.newLineAtOffset(leftMargin, yPosition);
                content.showText("Prescription Details");
                content.endText();

                yPosition -= lineHeight * 1.5;

                // ===== CONTENT SECTION =====
                content.setFont(PDType1Font.HELVETICA, 12);
                yPosition = writeLines.apply("Appointment ID: " + prescription.getAppointmentId(), yPosition);
                yPosition = writeLines.apply("Doctor: " + doctor.getUser_firstName() + " " + doctor.getUser_lastName(), yPosition);
                yPosition = writeLines.apply("Patient: " + patientUser.getUser_firstName() + " " + patientUser.getUser_lastName(), yPosition);
                yPosition = writeLines.apply("Date: " + prescription.getCreatedAt(), yPosition);

                yPosition -= lineHeight;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 13);
                content.newLineAtOffset(leftMargin, yPosition);
                content.showText("Medicine Details:");
                content.endText();

                yPosition -= lineHeight;
                content.setFont(PDType1Font.HELVETICA, 12);
                yPosition = writeLines.apply(prescription.getMedicineDetails(), yPosition);

                yPosition -= lineHeight;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 13);
                content.newLineAtOffset(leftMargin, yPosition);
                content.showText("Doctor Notes:");
                content.endText();

                yPosition -= lineHeight;
                content.setFont(PDType1Font.HELVETICA, 12);
                yPosition = writeLines.apply(prescription.getNotes() != null ? prescription.getNotes() : "N/A", yPosition);

                yPosition -= lineHeight * 2;

                // ===== FOOTER / SIGNATURE SECTION =====
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_OBLIQUE, 12);
                content.newLineAtOffset(leftMargin, yPosition);
                content.showText("Signature:");
                content.endText();

                yPosition -= lineHeight;
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_OBLIQUE, 11);
                content.newLineAtOffset(leftMargin, yPosition);
                content.showText(doctor.getUser_firstName() + " " + doctor.getUser_lastName());
                content.endText();

                // ===== NEW FOOTER AT PAGE BOTTOM =====
                String footerText = "Thank you for visiting Hospital Core";
                content.setFont(PDType1Font.HELVETICA_OBLIQUE, 11);
                float footerWidth = PDType1Font.HELVETICA_OBLIQUE.getStringWidth(footerText) / 1000 * 11;
                float footerX = (PDRectangle.A4.getWidth() - footerWidth) / 2;
                float footerY = 40; // position above page bottom
                content.beginText();
                content.newLineAtOffset(footerX, footerY);
                content.showText(footerText);
                content.endText();

                content.close();

                // Save PDF
                document.save(response.getOutputStream());
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating PDF.");
        }
    }
}