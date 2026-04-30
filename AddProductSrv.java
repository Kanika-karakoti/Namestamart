package com.namastemart.srv;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.namastemart.service.impl.ProductServiceImpl;

@WebServlet("/AddProductSrv")
@MultipartConfig(maxFileSize = 16177215)
public class AddProductSrv extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userType = (String) session.getAttribute("usertype");
        String userName = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");

        // Security Check
        if (userType == null || !"admin".equals(userType)) {
            response.sendRedirect("login.jsp?message=Access Denied!");
            return;
        }

        if (userName == null || password == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again!");
            return;
        }

        String status = "Product Registration Failed!";

        try {
            String prodName = request.getParameter("name");
            String prodType = request.getParameter("type");
            String prodInfo = request.getParameter("info");
            double prodPrice = Double.parseDouble(request.getParameter("price"));
            int prodQuantity = Integer.parseInt(request.getParameter("quantity"));

            Part part = request.getPart("image");
            InputStream prodImage = null;
            if (part != null) {
                prodImage = part.getInputStream();
            }

            ProductServiceImpl productService = new ProductServiceImpl();
            status = productService.addProduct(prodName, prodType, prodInfo,
                    prodPrice, prodQuantity, prodImage);

        } catch (Exception e) {
            e.printStackTrace();
            status = "Invalid Input Data!";
        }

        RequestDispatcher rd =
                request.getRequestDispatcher("addProduct.jsp?message=" + status);
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}