package com.sewage.springboot.signalr;

public class UserCredential {
    public UserCredential(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /// <summary>
    /// 用户名
    /// </summary>
    public String userName;

    /// <summary>
    /// 密码
    /// </summary>
    public String password;
}
