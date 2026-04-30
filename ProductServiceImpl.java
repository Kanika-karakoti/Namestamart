package com.namastemart.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.namastemart.beans.ProductBean;
import com.namastemart.service.ProductService;
import com.namastemart.utility.DBUtil;

public class ProductServiceImpl implements ProductService {

    // ================= HELPER: InputStream -> byte[] =================
    private byte[] toByteArray(InputStream is) {
        if (is == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = is.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= ADD PRODUCT (by fields with InputStream) =================
    @Override
    public String addProduct(String name, String type, String info,
                             double price, int quantity, InputStream image) {

        String status = "Product Registration Failed!";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO products(prod_name, prod_type, prod_info, prod_price, prod_quantity, prod_image) VALUES(?,?,?,?,?,?)")) {

            ps.setString(1, name);
            ps.setString(2, type);
            ps.setString(3, info);
            ps.setDouble(4, price);
            ps.setInt(5, quantity);
            if (image != null) {
                ps.setBlob(6, image);
            } else {
                ps.setNull(6, java.sql.Types.BLOB);
            }

            int k = ps.executeUpdate();
            if (k > 0) {
                status = "Product Registered Successfully!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    // ================= ADD PRODUCT (by ProductBean) =================
    @Override
    public String addProduct(ProductBean product) {

        String status = "Product Registration Failed!";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO products(prod_name, prod_type, prod_info, prod_price, prod_quantity, prod_image) VALUES(?,?,?,?,?,?)")) {

            ps.setString(1, product.getProdName());
            ps.setString(2, product.getProdType());
            ps.setString(3, product.getProdInfo());
            ps.setDouble(4, product.getProdPrice());
            ps.setInt(5, product.getProdQuantity());
            byte[] imgBytes = product.getProdImage();
            if (imgBytes != null) {
                ps.setBlob(6, new ByteArrayInputStream(imgBytes));
            } else {
                ps.setNull(6, java.sql.Types.BLOB);
            }

            int k = ps.executeUpdate();
            if (k > 0) {
                status = "Product Registered Successfully!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    // ================= GET PRODUCT DETAILS =================
    @Override
    public ProductBean getProductDetails(String prodId) {

        ProductBean product = null;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM products WHERE prod_id=?")) {

            ps.setString(1, prodId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = new ProductBean(
                            rs.getString("prod_id"),
                            rs.getString("prod_name"),
                            rs.getString("prod_type"),
                            rs.getString("prod_info"),
                            rs.getDouble("prod_price"),
                            rs.getInt("prod_quantity"),
                            toByteArray(rs.getBinaryStream("prod_image"))
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    // ================= GET ALL PRODUCTS =================
    @Override
    public List<ProductBean> getAllProducts() {

        List<ProductBean> products = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM products");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(new ProductBean(
                        rs.getString("prod_id"),
                        rs.getString("prod_name"),
                        rs.getString("prod_type"),
                        rs.getString("prod_info"),
                        rs.getDouble("prod_price"),
                        rs.getInt("prod_quantity"),
                        toByteArray(rs.getBinaryStream("prod_image"))
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    // ================= GET PRODUCTS BY TYPE =================
    @Override
    public List<ProductBean> getAllProductsByType(String type) {

        List<ProductBean> products = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM products WHERE prod_type=?")) {

            ps.setString(1, type);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(new ProductBean(
                            rs.getString("prod_id"),
                            rs.getString("prod_name"),
                            rs.getString("prod_type"),
                            rs.getString("prod_info"),
                            rs.getDouble("prod_price"),
                            rs.getInt("prod_quantity"),
                            toByteArray(rs.getBinaryStream("prod_image"))
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    // ================= SEARCH PRODUCTS =================
    @Override
    public List<ProductBean> searchAllProducts(String search) {

        List<ProductBean> products = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM products WHERE prod_name LIKE ? OR prod_type LIKE ? OR prod_info LIKE ?")) {

            String likeParam = "%" + search + "%";
            ps.setString(1, likeParam);
            ps.setString(2, likeParam);
            ps.setString(3, likeParam);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(new ProductBean(
                            rs.getString("prod_id"),
                            rs.getString("prod_name"),
                            rs.getString("prod_type"),
                            rs.getString("prod_info"),
                            rs.getDouble("prod_price"),
                            rs.getInt("prod_quantity"),
                            toByteArray(rs.getBinaryStream("prod_image"))
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    // ================= GET IMAGE =================
    @Override
    public byte[] getImage(String prodId) {

        byte[] image = null;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT prod_image FROM products WHERE prod_id=?")) {

            ps.setString(1, prodId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    image = toByteArray(rs.getBinaryStream("prod_image"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    // ================= REMOVE PRODUCT =================
    @Override
    public String removeProduct(String prodId) {

        String status = "Product Removal Failed!";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM products WHERE prod_id=?")) {

            ps.setString(1, prodId);

            int k = ps.executeUpdate();
            if (k > 0) {
                status = "Product Removed Successfully!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    // ================= UPDATE PRODUCT WITH IMAGE =================
    @Override
    public String updateProduct(ProductBean prevProduct, ProductBean updatedProduct) {

        String status = "Product Update Failed!";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE products SET prod_name=?, prod_type=?, prod_info=?, prod_price=?, prod_quantity=?, prod_image=? WHERE prod_id=?")) {

            ps.setString(1, updatedProduct.getProdName());
            ps.setString(2, updatedProduct.getProdType());
            ps.setString(3, updatedProduct.getProdInfo());
            ps.setDouble(4, updatedProduct.getProdPrice());
            ps.setInt(5, updatedProduct.getProdQuantity());
            byte[] imgBytes = updatedProduct.getProdImage();
            if (imgBytes != null) {
                ps.setBlob(6, new ByteArrayInputStream(imgBytes));
            } else {
                ps.setNull(6, java.sql.Types.BLOB);
            }
            ps.setString(7, prevProduct.getProdId());

            int k = ps.executeUpdate();
            if (k > 0) {
                status = "Product Updated Successfully!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    // ================= UPDATE PRODUCT WITHOUT IMAGE =================
    @Override
    public String updateProductWithoutImage(String prevProductId, ProductBean updatedProduct) {

        String status = "Product Update Failed!";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE products SET prod_name=?, prod_type=?, prod_info=?, prod_price=?, prod_quantity=? WHERE prod_id=?")) {

            ps.setString(1, updatedProduct.getProdName());
            ps.setString(2, updatedProduct.getProdType());
            ps.setString(3, updatedProduct.getProdInfo());
            ps.setDouble(4, updatedProduct.getProdPrice());
            ps.setInt(5, updatedProduct.getProdQuantity());
            ps.setString(6, prevProductId);

            int k = ps.executeUpdate();
            if (k > 0) {
                status = "Product Updated Successfully!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    // ================= UPDATE PRODUCT PRICE =================
    @Override
    public String updateProductPrice(String prodId, double updatedPrice) {

        String status = "Price Update Failed!";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE products SET prod_price=? WHERE prod_id=?")) {

            ps.setDouble(1, updatedPrice);
            ps.setString(2, prodId);

            int k = ps.executeUpdate();
            if (k > 0) {
                status = "Price Updated Successfully!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    // ================= GET PRODUCT PRICE =================
    @Override
    public double getProductPrice(String prodId) {

        double price = 0;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT prod_price FROM products WHERE prod_id=?")) {

            ps.setString(1, prodId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    price = rs.getDouble(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return price;
    }

    // ================= SELL N PRODUCT =================
    @Override
    public boolean sellNProduct(String prodId, int n) {

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE products SET prod_quantity = prod_quantity - ? WHERE prod_id=?")) {

            ps.setInt(1, n);
            ps.setString(2, prodId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= GET PRODUCT QUANTITY =================
    @Override
    public int getProductQuantity(String prodId) {

        int qty = 0;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT prod_quantity FROM products WHERE prod_id=?")) {

            ps.setString(1, prodId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    qty = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return qty;
    }

    // ================= GET TOTAL PRODUCTS =================
    @Override
    public int getTotalProducts() {

        int count = 0;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM products");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}