/**
 * 登录窗口界面
 */
package com.library.ui;

import com.library.dao.AdminDAO;
import com.library.model.Admin;
import com.library.util.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtAdminId;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        // 初始化数据库
        DBUtil.initDatabase();
        
        // 设置窗口标题和大小
        setTitle("图书馆管理系统 - 登录");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建登录面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // 添加组件
        panel.add(new JLabel("管理员ID："));
        txtAdminId = new JTextField(20);
        panel.add(txtAdminId);
        
        panel.add(new JLabel("密码："));
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword);
        
        panel.add(new JLabel(""));
        btnLogin = new JButton("登录");
        panel.add(btnLogin);
        
        // 添加登录按钮事件
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adminId = txtAdminId.getText();
                String password = new String(txtPassword.getPassword());
                
                if (adminId.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "请输入管理员ID和密码", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                AdminDAO adminDAO = new AdminDAO();
                Admin admin = adminDAO.login(adminId, password);
                
                if (admin != null) {
                    // 登录成功，打开主界面
                    MainFrame mainFrame = new MainFrame(admin);
                    mainFrame.setVisible(true);
                    dispose(); // 关闭登录窗口
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "管理员ID或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 添加面板到窗口
        add(panel);
    }
}