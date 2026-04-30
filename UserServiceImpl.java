package com.namastemart.service.impl;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.namastemart.beans.UserBean;
import com.namastemart.constants.IUserConstants;
import com.namastemart.service.UserService;
import com.namastemart.utility.DBUtil;
import com.namastemart.utility.MailMessage;

public class UserServiceImpl implements UserService {

    @Override
    public String registerUser(String userName, Long mobileNo, String emailId, String address,
            int pinCode, String password, byte[] userImage) {
        UserBean user = new UserBean(userName, mobileNo, emailId, address, pinCode, password,
                userImage, true);
        return registerUser(user);
    }

    @Override
    public String registerUser(UserBean user) {
        String status = "User Registration Failed!";
        boolean isRegtd = isRegistered(user.getEmail());

        if (isRegtd) {
            return "Email Id Already Registered!";
        }

        String query = "INSERT INTO user (email, name, mobile, address, pincode, password, "
                + "userimage, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.provideConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getName());
            ps.setLong(3, user.getMobile());
            ps.setString(4, user.getAddress());
            ps.setInt(5, user.getPinCode());
            ps.setString(6, user.getPassword());

            if (user.getUserImage() != null) {
                ps.setBlob(7, new ByteArrayInputStream(user.getUserImage()));
            } else {
                ps.setNull(7, java.sql.Types.BLOB);
            }

            ps.setBoolean(8, user.isActive());

            int k = ps.executeUpdate();
            if (k > 0) {
                status = "User Registered Successfully!";
                MailMessage.registrationSuccess(user.getEmail(), user.getName().split(" ")[0]);
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public boolean isRegistered(String emailId) {
        String query = "SELECT 1 FROM user WHERE email=?";

        try (Connection con = DBUtil.provideConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, emailId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String isValidCredential(String emailId, String password) {
        String query = "SELECT 1 FROM user WHERE email=? AND password=? AND is_active=true";
        String status = "Login Denied! Incorrect Username or Password";

        try (Connection con = DBUtil.provideConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, emailId);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    status = "valid";
                }
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public UserBean getUserDetails(String emailId, String password) {
        String query = "SELECT * FROM user WHERE email=? AND password=? AND is_active=true";
        UserBean user = null;

        try (Connection con = DBUtil.provideConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, emailId);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new UserBean();
                    user.setName(rs.getString("name"));
                    user.setMobile(rs.getLong("mobile"));
                    user.setEmail(rs.getString("email"));
                    user.setAddress(rs.getString("address"));
                    user.setPinCode(rs.getInt("pincode"));
                    user.setPassword(rs.getString("password"));
                    user.setUserImage(rs.getBytes("userimage"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public boolean updateUserDetails(UserBean user) {
        String currentUserQuery = "SELECT email, userimage FROM user WHERE email=?";
        String updateQuery = "UPDATE user SET name=?, mobile=?, address=?, pincode=?, "
                + "password=?, userimage=? WHERE email=?";
        boolean isUpdated = false;

        try (Connection conn = DBUtil.provideConnection();
                PreparedStatement psCurrentUser = conn.prepareStatement(currentUserQuery)) {

            psCurrentUser.setString(1, user.getEmail());

            try (ResultSet rsCurrentUser = psCurrentUser.executeQuery()) {
                String currentEmail = null;
                byte[] currentImage = null;

                if (rsCurrentUser.next()) {
                    currentEmail = rsCurrentUser.getString("email");
                    currentImage = rsCurrentUser.getBytes("userimage");
                }

                try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
                    ps.setString(1, user.getName());
                    ps.setLong(2, user.getMobile());
                    ps.setString(3, user.getAddress());
                    ps.setInt(4, user.getPinCode());
                    ps.setString(5, user.getPassword());

                    if (user.getUserImage() != null && user.getUserImage().length > 0) {
                        ps.setBlob(6, new ByteArrayInputStream(user.getUserImage()));
                    } else {
                        if (currentImage != null) {
                            ps.setBlob(6, new ByteArrayInputStream(currentImage));
                        } else {
                            ps.setNull(6, java.sql.Types.BLOB);
                        }
                    }

                    ps.setString(7, currentEmail);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        isUpdated = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    @Override
    public String getFName(String emailId) {
        String query = "SELECT name FROM user WHERE email=?";
        String fname = "";

        try (Connection con = DBUtil.provideConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, emailId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fname = rs.getString("name").split(" ")[0];
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fname;
    }

    @Override
    public String getUserAddr(String userId) {
        String query = "SELECT address FROM user WHERE email=?";
        String userAddr = "";

        try (Connection con = DBUtil.provideConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userAddr = rs.getString("address");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userAddr;
    }

    @Override
    public List<UserBean> getAllUsers() {
        String query = "SELECT * FROM user";
        List<UserBean> userList = new ArrayList<>();

        try (Connection con = DBUtil.provideConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserBean user = new UserBean();
                    user.setName(rs.getString("name"));
                    user.setMobile(rs.getLong("mobile"));
                    user.setEmail(rs.getString("email"));
                    user.setAddress(rs.getString("address"));
                    user.setPinCode(rs.getInt("pincode"));
                    user.setPassword(rs.getString("password"));
                    user.setUserImage(rs.getBytes("userimage"));
                    user.setActive(rs.getBoolean("is_active"));
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public void activateUser(String userId) {
        changeUserActivationStatus(userId, true);
    }

    @Override
    public void deactivateUser(String userId) {
        changeUserActivationStatus(userId, false);
    }

    private void changeUserActivationStatus(String userId, boolean isActive) {
        String query = "UPDATE user SET is_active=? WHERE email=?";

        try (Connection con = DBUtil.provideConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setBoolean(1, isActive);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(String userId) {
        String query = "DELETE FROM user WHERE email=?";

        try (Connection con = DBUtil.provideConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}