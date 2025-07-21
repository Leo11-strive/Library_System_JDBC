/**
 * 借书面板
 */
package com.library.ui;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.dao.CardDAO;
import com.library.model.Admin;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Card;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BorrowPanel extends JPanel {
    private Admin admin;
    private JTextField txtCardId;
    private JButton btnCheckCard;
    private JTextField txtBookId;
    private JButton btnBorrow;
    private JTable tblBooks;
    private DefaultTableModel tableModel;
    
    public BorrowPanel(Admin admin) {
        this.admin = admin;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建借书证面板
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 0));
        
        cardPanel.add(new JLabel("借书证卡号："), BorderLayout.WEST);
        txtCardId = new JTextField();
        cardPanel.add(txtCardId, BorderLayout.CENTER);
        
        btnCheckCard = new JButton("查看已借图书");
        cardPanel.add(btnCheckCard, BorderLayout.EAST);
        
        // 创建借书面板
        JPanel borrowPanel = new JPanel();
        borrowPanel.setLayout(new BorderLayout(10, 0));
        
        borrowPanel.add(new JLabel("图书编号："), BorderLayout.WEST);
        txtBookId = new JTextField();
        borrowPanel.add(txtBookId, BorderLayout.CENTER);
        
        btnBorrow = new JButton("借书");
        borrowPanel.add(btnBorrow, BorderLayout.EAST);
        
        // 创建顶部面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1, 0, 10));
        topPanel.add(cardPanel);
        topPanel.add(borrowPanel);
        
        // 创建表格
        String[] columnNames = {"书号", "类别", "书名", "出版社", "年份", "作者", "价格", "总藏书量", "库存"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置表格不可编辑
            }
        };
        tblBooks = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblBooks);
        
        // 添加查看已借图书按钮事件
        btnCheckCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBorrowedBooks();
            }
        });
        
        // 添加借书按钮事件
        btnBorrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowBook();
            }
        });
        
        // 添加组件到面板
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // 查看已借图书方法
    private void checkBorrowedBooks() {
        String cardId = txtCardId.getText().trim();
        
        if (cardId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入借书证卡号", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 检查借书证是否存在
        CardDAO cardDAO = new CardDAO();
        Card card = cardDAO.getCardById(cardId);
        
        if (card == null) {
            JOptionPane.showMessageDialog(this, "借书证不存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 获取已借图书
        BorrowDAO borrowDAO = new BorrowDAO();
        List<Book> books = borrowDAO.getBorrowedBooks(cardId);
        
        // 更新表格数据
        tableModel.setRowCount(0);
        
        for (Book book : books) {
            Object[] row = {
                book.getBookId(),
                book.getCategory(),
                book.getTitle(),
                book.getPublisher(),
                book.getPublishYear(),
                book.getAuthor(),
                book.getPrice(),
                book.getTotal(),
                book.getStock()
            };
            tableModel.addRow(row);
        }
        
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "该借书证没有借阅任何图书", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // 借书方法
    private void borrowBook() {
        String cardId = txtCardId.getText().trim();
        String bookId = txtBookId.getText().trim();
        
        if (cardId.isEmpty() || bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入借书证卡号和图书编号", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 检查借书证是否存在
        CardDAO cardDAO = new CardDAO();
        Card card = cardDAO.getCardById(cardId);
        
        if (card == null) {
            JOptionPane.showMessageDialog(this, "借书证不存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 检查图书是否存在
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.getBookById(bookId);
        
        if (book == null) {
            JOptionPane.showMessageDialog(this, "图书不存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 检查图书是否已被该借书证借出
        BorrowDAO borrowDAO = new BorrowDAO();
        if (borrowDAO.isBookBorrowed(bookId, cardId)) {
            JOptionPane.showMessageDialog(this, "该图书已被您借出", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 检查库存
        if (book.getStock() <= 0) {
            // 查询最近归还时间
            Date latestReturnDate = bookDAO.getLatestReturnDate(bookId);
            String returnDateStr = "暂无归还记录";
            
            if (latestReturnDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                returnDateStr = sdf.format(latestReturnDate);
            }
            
            JOptionPane.showMessageDialog(this, "该图书暂无库存，最近归还时间：" + returnDateStr, "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 创建借书记录
        BorrowRecord record = new BorrowRecord();
        record.setBookId(bookId);
        record.setCardId(cardId);
        record.setBorrowDate(new Date());
        record.setAdminId(admin.getAdminId());
        
        // 保存借书记录
        boolean success1 = borrowDAO.addBorrowRecord(record);
        
        // 更新库存
        boolean success2 = bookDAO.updateBookStock(bookId, -1);
        
        if (success1 && success2) {
            JOptionPane.showMessageDialog(this, "借书成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            txtBookId.setText("");
            checkBorrowedBooks(); // 刷新已借图书列表
        } else {
            JOptionPane.showMessageDialog(this, "借书失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
