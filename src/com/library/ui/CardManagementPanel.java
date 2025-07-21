/**
 * 借书证管理面板
 */
package com.library.ui;

import com.library.dao.CardDAO;
import com.library.model.Card;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CardManagementPanel extends JPanel {
    private JTabbedPane tabbedPane;
    
    // 添加借书证组件
    private JTextField txtAddCardId;
    private JTextField txtAddName;
    private JTextField txtAddDepartment;
    private JComboBox<String> cboAddType;
    private JButton btnAdd;
    
    // 删除借书证组件
    private JTextField txtDeleteCardId;
    private JButton btnDelete;
    private JTable tblCards;
    private DefaultTableModel tableModel;
    
    public CardManagementPanel() {
        setLayout(new BorderLayout());
        
        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        
        // 创建添加借书证面板
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridLayout(5, 2, 10, 20));
        addPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        addPanel.add(new JLabel("借书证卡号："));
        txtAddCardId = new JTextField();
        addPanel.add(txtAddCardId);
        
        addPanel.add(new JLabel("姓名："));
        txtAddName = new JTextField();
        addPanel.add(txtAddName);
        
        addPanel.add(new JLabel("单位："));
        txtAddDepartment = new JTextField();
        addPanel.add(txtAddDepartment);
        
        addPanel.add(new JLabel("类别："));
        String[] types = {"学生", "教师", "职工", "其他"};
        cboAddType = new JComboBox<>(types);
        addPanel.add(cboAddType);
        
        addPanel.add(new JLabel(""));
        btnAdd = new JButton("添加借书证");
        addPanel.add(btnAdd);
        
        // 创建删除借书证面板
        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new BorderLayout(10, 10));
        deletePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel deleteTopPanel = new JPanel();
        deleteTopPanel.setLayout(new BorderLayout(10, 0));
        
        deleteTopPanel.add(new JLabel("借书证卡号："), BorderLayout.WEST);
        txtDeleteCardId = new JTextField();
        deleteTopPanel.add(txtDeleteCardId, BorderLayout.CENTER);
        
        btnDelete = new JButton("删除借书证");
        deleteTopPanel.add(btnDelete, BorderLayout.EAST);
        
        // 创建表格
        String[] columnNames = {"卡号", "姓名", "单位", "类别"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置表格不可编辑
            }
        };
        tblCards = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblCards);
        
        deletePanel.add(deleteTopPanel, BorderLayout.NORTH);
        deletePanel.add(scrollPane, BorderLayout.CENTER);
        
        // 添加按钮事件
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCard();
            }
        });
        
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCard();
            }
        });
        
        // 添加选项卡
        tabbedPane.addTab("添加借书证", addPanel);
        tabbedPane.addTab("删除借书证", deletePanel);
        
        // 加载借书证列表
        loadCards();
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    // 添加借书证方法
    private void addCard() {
        try {
            // 获取输入信息
            String cardId = txtAddCardId.getText().trim();
            String name = txtAddName.getText().trim();
            String department = txtAddDepartment.getText().trim();
            String type = (String) cboAddType.getSelectedItem();
            
            // 检查输入有效性
            if (cardId.isEmpty() || name.isEmpty() || department.isEmpty()) {
                JOptionPane.showMessageDialog(this, "所有字段都不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 检查卡号是否已存在
            CardDAO cardDAO = new CardDAO();
            Card existingCard = cardDAO.getCardById(cardId);
            
            if (existingCard != null) {
                JOptionPane.showMessageDialog(this, "该卡号已存在", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 创建借书证对象
            Card card = new Card(cardId, name, department, type);
            
            // 保存到数据库
            boolean success = cardDAO.addCard(card);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "借书证添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                clearAddInputs();
                loadCards(); // 刷新借书证列表
            } else {
                JOptionPane.showMessageDialog(this, "借书证添加失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "添加失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 清空添加借书证输入
    private void clearAddInputs() {
        txtAddCardId.setText("");
        txtAddName.setText("");
        txtAddDepartment.setText("");
        cboAddType.setSelectedIndex(0);
    }
    
    // 删除借书证方法
    private void deleteCard() {
        String cardId = txtDeleteCardId.getText().trim();
        
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
        
        // 确认删除
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除卡号为 " + cardId + " 的借书证吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 删除借书证
            boolean success = cardDAO.deleteCard(cardId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "借书证删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                txtDeleteCardId.setText("");
                loadCards(); // 刷新借书证列表
            } else {
                JOptionPane.showMessageDialog(this, "借书证删除失败，可能存在未归还的图书", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // 加载借书证列表
    private void loadCards() {
        CardDAO cardDAO = new CardDAO();
        List<Card> cards = cardDAO.getAllCards();
        
        // 更新表格数据
        tableModel.setRowCount(0);
        
        for (Card card : cards) {
            Object[] row = {
                card.getCardId(),
                card.getName(),
                card.getDepartment(),
                card.getType()
            };
            tableModel.addRow(row);
        }
    }
}