/**
 * 管理员数据访问对象
 */
package com.library.dao;

import com.library.model.Admin;
import com.library.util.DBUtil;
import java.sql.*;

public class AdminDAO {
    // 验证管理员登录
    public Admin login(String adminId, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Admin admin = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Admin WHERE admin_id = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, adminId);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                admin = new Admin();
                admin.setAdminId(rs.getString("admin_id"));
                admin.setPassword(rs.getString("password"));
                admin.setName(rs.getString("name"));
                admin.setContact(rs.getString("contact"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, rs);
        }
        
        return admin;
    }
    
    // 添加管理员
    public boolean addAdmin(Admin admin) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO Admin (admin_id, password, name, contact) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, admin.getAdminId());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getName());
            stmt.setString(4, admin.getContact());
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, null);
        }
        
        return success;
    }
}