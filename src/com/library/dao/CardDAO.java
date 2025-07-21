/**
 * 借书证数据访问对象
 */
package com.library.dao;

import com.library.model.Card;
import com.library.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    // 添加借书证
    public boolean addCard(Card card) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO Card (card_id, name, department, type) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, card.getCardId());
            stmt.setString(2, card.getName());
            stmt.setString(3, card.getDepartment());
            stmt.setString(4, card.getType());
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, null);
        }
        
        return success;
    }
    
    // 删除借书证
    public boolean deleteCard(String cardId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            // 先检查是否有未归还的书
            String checkSql = "SELECT COUNT(*) FROM Borrow WHERE card_id = ? AND return_date IS NULL";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, cardId);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int borrowCount = rs.getInt(1);
            rs.close();
            checkStmt.close();
            
            if (borrowCount > 0) {
                // 还有未归还的书，不能删除
                return false;
            }
            
            String sql = "DELETE FROM Card WHERE card_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cardId);
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, null);
        }
        
        return success;
    }
    
    // 根据卡号查询借书证
    public Card getCardById(String cardId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Card card = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Card WHERE card_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cardId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                card = new Card();
                card.setCardId(rs.getString("card_id"));
                card.setName(rs.getString("name"));
                card.setDepartment(rs.getString("department"));
                card.setType(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, rs);
        }
        
        return card;
    }
    
    // 获取所有借书证
    public List<Card> getAllCards() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Card> cards = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Card";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Card card = new Card();
                card.setCardId(rs.getString("card_id"));
                card.setName(rs.getString("name"));
                card.setDepartment(rs.getString("department"));
                card.setType(rs.getString("type"));
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResources(conn, stmt, rs);
        }
        
        return cards;
    }
}
