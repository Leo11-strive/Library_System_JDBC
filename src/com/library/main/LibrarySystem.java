/**
 * 图书馆管理系统 - 主类
 * 系统入口点
 */
package com.library.main;

import com.library.ui.LoginFrame;

public class LibrarySystem {
    public static void main(String[] args) {
        // 创建并显示登录窗口
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
