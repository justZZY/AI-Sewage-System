package com.sewage.springboot.entity;

/**
 * 用于存储在shiro的session中
 */
public class UserSessionInfo {
    private String username;
    private int delete_status; // 禁用状态
    private String identity; // 用户身份
    private String area; // 用户可查看的地区

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(int delete_status) {
        this.delete_status = delete_status;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
