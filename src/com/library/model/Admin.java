/**
 * 管理员实体类
 */
package com.library.model;

public class Admin {
    private String adminId;
    private String password;
    private String name;
    private String contact;

    public Admin() {
    }

    public Admin(String adminId, String password, String name, String contact) {
        this.adminId = adminId;
        this.password = password;
        this.name = name;
        this.contact = contact;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}