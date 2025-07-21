/**
 * 借书记录数据访问对象
 */
package com.library.dao;

import com.library.model.BorrowRecord;
import com.library.model.Book;
import com.library.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {
    // 添加借书记录
    public boolean addBorrowRecord(BorrowRecord record) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO Borrow (book_id, card_id, borrow_date, admin_id) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, record.getBookId());
            stmt.setString(2, record.getCardId());
            stmt.setDate(3, new java.sql.Date(record.getBorrowDate().getTime()));
            stmt.setString(4, record.getAdminId());
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, null);
        }
        
        return success;
    }
    
    // 更新还书记录
    public boolean updateReturnRecord(String bookId, String cardId, Date returnDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE Borrow SET return_date = ? WHERE book_id = ? AND card_id = ? AND return_date IS NULL";
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(returnDate.getTime()));
            stmt.setString(2, bookId);
            stmt.setString(3, cardId);
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, null);
        }
        
        return success;
    }
    
    // 获取某借书证的所有未归还的图书
    public List<Book> getBorrowedBooks(String cardId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT b.* FROM Book b JOIN Borrow br ON b.book_id = br.book_id " +
                         "WHERE br.card_id = ? AND br.return_date IS NULL";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cardId);
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
    
    // 检查图书是否已被借出
    public boolean isBookBorrowed(String bookId, String cardId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isBorrowed = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM Borrow WHERE book_id = ? AND card_id = ? AND return_date IS NULL";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, bookId);
            stmt.setString(2, cardId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                isBorrowed = (rs.getInt(1) > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, rs);
        }
        
        return isBorrowed;
    }
}