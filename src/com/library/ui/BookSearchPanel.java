/**
 * 图书查询面板
 */
package com.library.ui;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class BookSearchPanel extends JPanel {
    private JTextField txtCategory;
    private JTextField txtTitle;
    private JTextField txtPublisher;
    private JTextField txtMinYear;
    private JTextField txtMaxYear;
    private JTextField txtAuthor;
    private JTextField txtMinPrice;
    private JTextField txtMaxPrice;
    private JComboBox<String> cboSortBy;
    private JButton btnSearch;
    private JTable tblBooks;
    private DefaultTableModel tableModel;
    
    public BookSearchPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建查询条件面板
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(5, 4, 10, 10));
        
        searchPanel.add(new JLabel("类别："));
        txtCategory = new JTextField();
        searchPanel.add(txtCategory);
        
        searchPanel.add(new JLabel("书名："));
        txtTitle = new JTextField();
        searchPanel.add(txtTitle);
        
        searchPanel.add(new JLabel("出版社："));
        txtPublisher = new JTextField();
        searchPanel.add(txtPublisher);
        
        searchPanel.add(new JLabel("作者："));
        txtAuthor = new JTextField();
        searchPanel.add(txtAuthor);
        
        searchPanel.add(new JLabel("最小年份："));
        txtMinYear = new JTextField();
        searchPanel.add(txtMinYear);
        
        searchPanel.add(new JLabel("最大年份："));
        txtMaxYear = new JTextField();
        searchPanel.add(txtMaxYear);
        
        searchPanel.add(new JLabel("最小价格："));
        txtMinPrice = new JTextField();
        searchPanel.add(txtMinPrice);
        
        searchPanel.add(new JLabel("最大价格："));
        txtMaxPrice = new JTextField();
        searchPanel.add(txtMaxPrice);
        
        searchPanel.add(new JLabel("排序字段："));
        String[] sortOptions = {"书名", "类别", "出版社", "年份", "作者", "价格"};
        cboSortBy = new JComboBox<>(sortOptions);
        searchPanel.add(cboSortBy);
        
        searchPanel.add(new JLabel(""));
        btnSearch = new JButton("查询");
        searchPanel.add(btnSearch);
        
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
        
        // 添加查询按钮事件
        // 添加查询按钮事件
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });
        
        // 添加组件到面板
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // 图书查询方法
    private void searchBooks() {
        try {
            // 获取查询条件
            String category = txtCategory.getText().trim();
            String title = txtTitle.getText().trim();
            String publisher = txtPublisher.getText().trim();
            String author = txtAuthor.getText().trim();
            
            Integer minYear = null;
            if (!txtMinYear.getText().trim().isEmpty()) {
                minYear = Integer.parseInt(txtMinYear.getText().trim());
            }
            
            Integer maxYear = null;
            if (!txtMaxYear.getText().trim().isEmpty()) {
                maxYear = Integer.parseInt(txtMaxYear.getText().trim());
            }
            
            BigDecimal minPrice = null;
            if (!txtMinPrice.getText().trim().isEmpty()) {
                minPrice = new BigDecimal(txtMinPrice.getText().trim());
            }
            
            BigDecimal maxPrice = null;
            if (!txtMaxPrice.getText().trim().isEmpty()) {
                maxPrice = new BigDecimal(txtMaxPrice.getText().trim());
            }
            
            // 获取排序字段
            String sortByDisplay = (String) cboSortBy.getSelectedItem();
            String sortBy = mapSortField(sortByDisplay);
            
            // 执行查询
            BookDAO bookDAO = new BookDAO();
            List<Book> books = bookDAO.searchBooks(category, title, publisher, minYear, maxYear, author, minPrice, maxPrice, sortBy);
            
            // 更新表格数据
            updateTable(books);
            
            // 显示查询结果信息
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(this, "没有找到符合条件的图书", "查询结果", JOptionPane.INFORMATION_MESSAGE);
            } else if (books.size() >= 50) {
                JOptionPane.showMessageDialog(this, "查询结果超过50条，只显示前50条", "查询结果", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "年份和价格必须为数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 更新表格数据
    private void updateTable(List<Book> books) {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 添加数据
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
    }
    
    // 映射排序字段
    private String mapSortField(String displayName) {
        switch (displayName) {
            case "书名": return "title";
            case "类别": return "category";
            case "出版社": return "publisher";
            case "年份": return "publish_year";
            case "作者": return "author";
            case "价格": return "price";
            default: return "title";
        }
    }
}
