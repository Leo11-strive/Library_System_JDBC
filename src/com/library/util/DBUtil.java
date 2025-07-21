/**
 * 数据库连接工具类
 * 提供数据库连接和关闭功能
 */
package com.library.util;

import java.sql.*;

public class DBUtil {
    // 数据库连接信息
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Xing_xuan1";

    // 获取数据库连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭数据库连接和相关资源
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 初始化数据库表
    public static void initDatabase() {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            
            // 创建Book表
            String createBookTable = "CREATE TABLE IF NOT EXISTS Book (" +
                    "book_id VARCHAR(20) PRIMARY KEY," +
                    "category VARCHAR(50) NOT NULL," +
                    "title VARCHAR(100) NOT NULL," +
                    "publisher VARCHAR(100) NOT NULL," +
                    "publish_year INT NOT NULL," +
                    "author VARCHAR(100) NOT NULL," +
                    "price DECIMAL(10,2) NOT NULL," +
                    "total INT NOT NULL," +
                    "stock INT NOT NULL" +
                    ")";
            stmt.executeUpdate(createBookTable);
            
            // 创建Card表
            String createCardTable = "CREATE TABLE IF NOT EXISTS Card (" +
                    "card_id VARCHAR(20) PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "department VARCHAR(100) NOT NULL," +
                    "type VARCHAR(20) NOT NULL" +
                    ")";
            stmt.executeUpdate(createCardTable);
            
            // 创建Admin表
            String createAdminTable = "CREATE TABLE IF NOT EXISTS Admin (" +
                    "admin_id VARCHAR(20) PRIMARY KEY," +
                    "password VARCHAR(50) NOT NULL," +
                    "name VARCHAR(50) NOT NULL," +
                    "contact VARCHAR(100) NOT NULL" +
                    ")";
            stmt.executeUpdate(createAdminTable);
            
            // 创建Borrow表
            String createBorrowTable = "CREATE TABLE IF NOT EXISTS Borrow (" +
                    "borrow_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "book_id VARCHAR(20) NOT NULL," +
                    "card_id VARCHAR(20) NOT NULL," +
                    "borrow_date DATE NOT NULL," +
                    "return_date DATE," +
                    "admin_id VARCHAR(20) NOT NULL," +
                    "FOREIGN KEY (book_id) REFERENCES Book(book_id)," +
                    "FOREIGN KEY (card_id) REFERENCES Card(card_id)," +
                    "FOREIGN KEY (admin_id) REFERENCES Admin(admin_id)" +
                    ")";
            stmt.executeUpdate(createBorrowTable);
            
            // 插入默认管理员账号
            String checkAdmin = "SELECT COUNT(*) FROM Admin";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            rs.next();
            if (rs.getInt(1) == 0) {
                String insertAdmin = "INSERT INTO Admin (admin_id, password, name, contact) " +
                        "VALUES ('admin', 'admin123', '系统管理员', '13800138000')";
                stmt.executeUpdate(insertAdmin);
            }
            rs.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
    }
}