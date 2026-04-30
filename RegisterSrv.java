package com.namastemart.srv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.namastemart.beans.UserBean;
import com.namastemart.service.impl.UserServiceImpl;

@WebServlet("/RegisterSrv")
@MultipartConfig // Required for handling file uploads
public class RegisterSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        String userName = request.getParameter("username");
        Long mobileNo = Long.parseLong(request.getParameter("mobile"));
        String emailId = request.getParameter("email");
        String address = request.getParameter("address");
        int pinCode = Integer.parseInt(request.getParameter("pincode"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String status = "";

        byte[] userImage = null;
        Part filePart = request.getPart("image"); // Retrieves <input type="file" name="image">
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream inputStream = filePart.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                userImage = outputStream.toByteArray();
            }
        }

        if (password != null && password.equals(confirmPassword)) {
            boolean isActive = true;
            UserBean user = new UserBean(userName, mobileNo, emailId, address, pinCode,
                    password, userImage, isActive);

            UserServiceImpl dao = new UserServiceImpl();
            status = dao.registerUser(user);
        } else {
            status = "Password not matching!";
        }

        request.setAttribute("message", status);
        RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}