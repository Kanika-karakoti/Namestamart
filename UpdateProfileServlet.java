package com.namastemart.srv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.namastemart.beans.UserBean;
import com.namastemart.service.UserService;
import com.namastemart.service.impl.UserServiceImpl;

@WebServlet("/UpdateProfileServlet")
@MultipartConfig(maxFileSize = 16177215) // File size up to 16MB
public class UpdateProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobileStr = request.getParameter("mobile");
        String address = request.getParameter("address");
        String pincodeStr = request.getParameter("pincode");

        // Validate and convert inputs as needed
        long mobile = 0;
        int pincode = 0;

        try {
            mobile = Long.parseLong(mobileStr);
            pincode = Integer.parseInt(pincodeStr);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid phone number or pin code");
            request.getRequestDispatcher("userProfile.jsp").forward(request, response);
            return;
        }

        Part filePart = request.getPart("profileImage");
        byte[] userImage = null;

        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream inputStream = filePart.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                userImage = outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Get the current user details from session
        String sessionEmail = (String) request.getSession().getAttribute("username");
        String password = (String) request.getSession().getAttribute("password");

        if (sessionEmail == null || password == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
            return;
        }

        // Prepare user data for update
        boolean isActive = true;
        UserBean user = new UserBean(name, mobile, email, address, pincode, password,
                userImage, isActive);

        UserService userService = new UserServiceImpl();
        boolean isUpdated = userService.updateUserDetails(user);

        if (isUpdated) {
            if (!sessionEmail.equals(email)) {
                request.getSession().setAttribute("username", email);
            }
            response.sendRedirect("userProfile.jsp?message=Profile Updated Successfully!");
        } else {
            request.setAttribute("message", "Profile update failed, please try again.");
            request.getRequestDispatcher("userProfile.jsp").forward(request, response);
        }
    }
}