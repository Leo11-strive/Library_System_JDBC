/**
 * 图书入库面板
 */
package com.library.ui;

import com.library.dao.BookDAO;
import com.library.model.Admin;
import com.library.model.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookAddPanel extends JPanel {
    private Admin admin;
    private JTabbedPane tabbedPane;
    
    // 单本入库组件
    private JTextField txtBookId;
    private JTextField txtCategory;
    private JTextField txtTitle;
    private JTextField txtPublisher;
    private JTextField txtYear;
    private JTextField txtAuthor;
    private JTextField txtPrice;
    private JTextField txtQuantity;
    private JButton btnAddSingle;
    
    // 批量入库组件
    private JTextField txtFilePath;
    private JButton btnBrowse;
    private JButton btnAddBatch;
    private JTextArea txtResult;
    
    public BookAddPanel(Admin admin) {
        this.admin = admin;
        setLayout(new BorderLayout());
        
        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        
        // 创建单本入库面板
        JPanel singlePanel = new JPanel();
        singlePanel.setLayout(new GridLayout(9, 2, 10, 10));
        singlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        singlePanel.add(new JLabel("书号："));
        txtBookId = new JTextField();
        singlePanel.add(txtBookId);
        
        singlePanel.add(new JLabel("类别："));
        txtCategory = new JTextField();
        singlePanel.add(txtCategory);
        
        singlePanel.add(new JLabel("书名："));
        txtTitle = new JTextField();
        singlePanel.add(txtTitle);
        
        singlePanel.add(new JLabel("出版社："));
        txtPublisher = new JTextField();
        singlePanel.add(txtPublisher);
        
        singlePanel.add(new JLabel("年份："));
        txtYear = new JTextField();
        singlePanel.add(txtYear);
        
        singlePanel.add(new JLabel("作者："));
        txtAuthor = new JTextField();
        singlePanel.add(txtAuthor);
        
        singlePanel.add(new JLabel("价格："));
        txtPrice = new JTextField();
        singlePanel.add(txtPrice);
        
        singlePanel.add(new JLabel("数量："));
        txtQuantity = new JTextField();
        singlePanel.add(txtQuantity);
        
        singlePanel.add(new JLabel(""));
        btnAddSingle = new JButton("添加图书");
        singlePanel.add(btnAddSingle);
        
        // 添加单本入库按钮事件
        btnAddSingle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSingleBook();
            }
        });
        
        // 创建批量入库面板
        JPanel batchPanel = new JPanel();
        batchPanel.setLayout(new BorderLayout(10, 10));
        batchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BorderLayout(10, 0));
        
        filePanel.add(new JLabel("文件路径："), BorderLayout.WEST);
        txtFilePath = new JTextField();
        filePanel.add(txtFilePath, BorderLayout.CENTER);
        
        btnBrowse = new JButton("浏览");
        filePanel.add(btnBrowse, BorderLayout.EAST);
        
        batchPanel.add(filePanel, BorderLayout.NORTH);
        
        btnAddBatch = new JButton("批量导入");
        batchPanel.add(btnAddBatch, BorderLayout.CENTER);
        
        txtResult = new JTextArea();
        txtResult.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResult);
        batchPanel.add(scrollPane, BorderLayout.SOUTH);
        
        // 添加批量入库按钮事件
        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseFile();
            }
        });
        
        btnAddBatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBatchBooks();
            }
        });
        
        // 添加选项卡
        tabbedPane.addTab("单本入库", singlePanel);
        tabbedPane.addTab("批量入库", batchPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    // 单本入库方法
    private void addSingleBook() {
        try {
            // 获取输入信息
            String bookId = txtBookId.getText().trim();
            String category = txtCategory.getText().trim();
            String title = txtTitle.getText().trim();
            String publisher = txtPublisher.getText().trim();
            int year = Integer.parseInt(txtYear.getText().trim());
            String author = txtAuthor.getText().trim();
            BigDecimal price = new BigDecimal(txtPrice.getText().trim());
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            
            // 检查输入有效性
            if (bookId.isEmpty() || category.isEmpty() || title.isEmpty() || 
                publisher.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "所有字段都不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (year <= 0 || price.compareTo(BigDecimal.ZERO) <= 0 || quantity <= 0) {
                JOptionPane.showMessageDialog(this, "年份、价格和数量必须为正数", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 创建图书对象
            Book book = new Book(bookId, category, title, publisher, year, author, price, quantity, quantity);
            
            // 保存到数据库
            BookDAO bookDAO = new BookDAO();
            boolean success = bookDAO.addBook(book);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "图书入库成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                clearSingleInputs();
            } else {
                JOptionPane.showMessageDialog(this, "图书入库失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "年份、价格和数量必须为数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "入库失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 清空单本入库输入
    private void clearSingleInputs() {
        txtBookId.setText("");
        txtCategory.setText("");
        txtTitle.setText("");
        txtPublisher.setText("");
        txtYear.setText("");
        txtAuthor.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
    }
    
    // 浏览文件方法
    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtFilePath.setText(selectedFile.getAbsolutePath());
        }
    }
    
    // 批量入库方法
    private void addBatchBooks() {
        String filePath = txtFilePath.getText().trim();
        
        if (filePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择文件", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<Book> books = new ArrayList<>();
            String line;
            int lineCount = 0;
            
            txtResult.setText("开始导入图书...\n");
            
            while ((line = reader.readLine()) != null) {
                lineCount++;
                try {
                    // 解析行数据格式：(book_id, category, title, publisher, year, author, price, quantity)
                    line = line.trim();
                    
                    // 检查是否是空行
                    if (line.isEmpty()) {
                        continue;
                    }
                    
                    // 移除开头的"("和结尾的")"
                    if (line.startsWith("(") && line.endsWith(")")) {
                        line = line.substring(1, line.length() - 1);
                    }
                    
                    // 分割字段
                    String[] fields = line.split(",");
                    
                    if (fields.length != 8) {
                        txtResult.append("第" + lineCount + "行格式错误，应包含8个字段\n");
                        continue;
                    }
                    
                    // 提取和转换字段
                    String bookId = fields[0].trim();
                    String category = fields[1].trim();
                    String title = fields[2].trim();
                    String publisher = fields[3].trim();
                    int year = Integer.parseInt(fields[4].trim());
                    String author = fields[5].trim();
                    BigDecimal price = new BigDecimal(fields[6].trim());
                    int quantity = Integer.parseInt(fields[7].trim());
                    
                    // 创建图书对象
                    Book book = new Book(bookId, category, title, publisher, year, author, price, quantity, quantity);
                    books.add(book);
                    
                    txtResult.append("解析第" + lineCount + "行：" + title + "\n");
                } catch (Exception e) {
                    txtResult.append("解析第" + lineCount + "行出错：" + e.getMessage() + "\n");
                }
            }
            
            reader.close();
            
            if (books.isEmpty()) {
                txtResult.append("没有有效的图书数据\n");
                return;
            }
            
            // 保存到数据库
            BookDAO bookDAO = new BookDAO();
            int successCount = bookDAO.addBooks(books);
            
            txtResult.append("批量导入完成：共" + books.size() + "条记录，成功" + successCount + "条\n");
        } catch (Exception e) {
            txtResult.append("批量导入失败：" + e.getMessage() + "\n");
        }
    }
}
