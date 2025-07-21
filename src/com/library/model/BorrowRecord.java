/**
 * 借书记录实体类
 */
package com.library.model;

import java.util.Date;

public class BorrowRecord {
    private int borrowId;
    private String bookId;
    private String cardId;
    private Date borrowDate;
    private Date returnDate;
    private String adminId;
    
    // 额外的属性，用于显示
    private String bookTitle;
    private String borrowerName;

    public BorrowRecord() {
    }

    public BorrowRecord(int borrowId, String bookId, String cardId, Date borrowDate, 
                        Date returnDate, String adminId) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.cardId = cardId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.adminId = adminId;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
}