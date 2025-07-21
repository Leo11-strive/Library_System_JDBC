/**
 * 图书实体类
 */
package com.library.model;

import java.math.BigDecimal;

public class Book {
    private String bookId;
    private String category;
    private String title;
    private String publisher;
    private int publishYear;
    private String author;
    private BigDecimal price;
    private int total;
    private int stock;

    public Book() {
    }

    public Book(String bookId, String category, String title, String publisher, 
                int publishYear, String author, BigDecimal price, int total, int stock) {
        this.bookId = bookId;
        this.category = category;
        this.title = title;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.author = author;
        this.price = price;
        this.total = total;
        this.stock = stock;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}