/**
 * 主窗口界面
 */
package com.library.ui;

import com.library.model.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Admin admin;
    private JTabbedPane tabbedPane;
    
    public MainFrame(Admin admin) {
        this.admin = admin;
        
        // 设置窗口标题和大小
        setTitle("图书馆管理系统 - " + admin.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        
        // 添加各功能模块面板
        tabbedPane.addTab("图书入库", new BookAddPanel(admin));
        tabbedPane.addTab("图书查询", new BookSearchPanel());
        tabbedPane.addTab("借书", new BorrowPanel(admin));
        tabbedPane.addTab("还书", new ReturnPanel(admin));
        tabbedPane.addTab("借书证管理", new CardManagementPanel());
        
        // 添加选项卡面板到窗口
        add(tabbedPane);
        
        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu menuSystem = new JMenu("系统");
        JMenuItem menuItemLogout = new JMenuItem("注销");
        JMenuItem menuItemExit = new JMenuItem("退出");
        
        menuSystem.add(menuItemLogout);
        menuSystem.add(menuItemExit);
        menuBar.add(menuSystem);
        
        // 添加菜单事件
        menuItemLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose(); // 关闭主窗口
            }
        });
        
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // 设置菜单栏
        setJMenuBar(menuBar);
    }
}
