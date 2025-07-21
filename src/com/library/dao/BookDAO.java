/**
 * 图书数据访问对象
 */
package com.library.dao;

import com.library.model.Book;
import com.library.util.DBUtil;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    // 添加图书
    public boolean addBook(Book book) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // 检查书号是否已存在
            String checkSql = "SELECT * FROM Book WHERE book_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, book.getBookId());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // 图书已存在，更新库存和总量
                String updateSql = "UPDATE Book SET total = total + ?, stock = stock + ? WHERE book_id = ?";
                stmt = conn.prepareStatement(updateSql);
                stmt.setInt(1, book.getTotal());
                stmt.setInt(2, book.getTotal());
                stmt.setString(3, book.getBookId());
            } else {
                // 图书不存在，添加新记录
                String insertSql = "INSERT INTO Book (book_id, category, title, publisher, publish_year, author, price, total, stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(insertSql);
                stmt.setString(1, book.getBookId());
                stmt.setString(2, book.getCategory());
                stmt.setString(3, book.getTitle());
                stmt.setString(4, book.getPublisher());
                stmt.setInt(5, book.getPublishYear());
                stmt.setString(6, book.getAuthor());
                stmt.setBigDecimal(7, book.getPrice());
                stmt.setInt(8, book.getTotal());
                stmt.setInt(9, book.getTotal()); // 初始库存等于总量
            }
            
            rs.close();
            checkStmt.close();
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, null);
        }
        
        return success;
    }
    
    // 批量添加图书
    public int addBooks(List<Book> books) {
        int successCount = 0;
        
        for (Book book : books) {
            if (addBook(book)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    // 查询图书
    public List<Book> searchBooks(String category, String title, String publisher, 
                                  Integer minYear, Integer maxYear, String author,
                                  BigDecimal minPrice, BigDecimal maxPrice, String sortBy) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Book WHERE 1=1");
            List<Object> params = new ArrayList<>();
            
            if (category != null && !category.isEmpty()) {
                sqlBuilder.append(" AND category LIKE ?");
                params.add("%" + category + "%");
            }
            
            if (title != null && !title.isEmpty()) {
                sqlBuilder.append(" AND title LIKE ?");
                params.add("%" + title + "%");
            }
            
            if (publisher != null && !publisher.isEmpty()) {
                sqlBuilder.append(" AND publisher LIKE ?");
                params.add("%" + publisher + "%");
            }
            
            if (minYear != null) {
                sqlBuilder.append(" AND publish_year >= ?");
                params.add(minYear);
            }
            
            if (maxYear != null) {
                sqlBuilder.append(" AND publish_year <= ?");
                params.add(maxYear);
            }
            
            if (author != null && !author.isEmpty()) {
                sqlBuilder.append(" AND author LIKE ?");
                params.add("%" + author + "%");
            }
            
            if (minPrice != null) {
                sqlBuilder.append(" AND price >= ?");
                params.add(minPrice);
            }
            
            if (maxPrice != null) {
                sqlBuilder.append(" AND price <= ?");
                params.add(maxPrice);
            }
            
            // 默认按书名排序
            if (sortBy == null || sortBy.isEmpty()) {
                sortBy = "title";
            }
            sqlBuilder.append(" ORDER BY ").append(sortBy);
            
            // 限制返回最多50条记录
            sqlBuilder.append(" LIMIT 50");
            
            stmt = conn.prepareStatement(sqlBuilder.toString());
            
            // 设置参数
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getString("book_id"));
                book.setCategory(rs.getString("category"));
                book.setTitle(rs.getString("title"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishYear(rs.getInt("publish_year"));
                book.setAuthor(rs.getString("author"));
                book.setPrice(rs.getBigDecimal("price"));
                book.setTotal(rs.getInt("total"));
                book.setStock(rs.getInt("stock"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, rs);
        }
        
        return books;
    }
    
    // 根据书号查询图书
    public Book getBookById(String bookId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Book book = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Book WHERE book_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, bookId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                book = new Book();
                book.setBookId(rs.getString("book_id"));
                book.setCategory(rs.getString("category"));
                book.setTitle(rs.getString("title"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishYear(rs.getInt("publish_year"));
                book.setAuthor(rs.getString("author"));
                book.setPrice(rs.getBigDecimal("price"));
                book.setTotal(rs.getInt("total"));
                book.setStock(rs.getInt("stock"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, rs);
        }
        
        return book;
    }
    
    // 更新图书库存
    public boolean updateBookStock(String bookId, int stockChange) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE Book SET stock = stock + ? WHERE book_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, stockChange);
            stmt.setString(2, bookId);
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, null);
        }
        
        return success;
    }
    
    // 获取图书最近归还时间
    public Date getLatestReturnDate(String bookId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Date returnDate = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT MAX(return_date) FROM Borrow WHERE book_id = ? AND return_date IS NOT NULL";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, bookId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                returnDate = rs.getDate(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, rs);
        }
        
        return returnDate;
    }
}