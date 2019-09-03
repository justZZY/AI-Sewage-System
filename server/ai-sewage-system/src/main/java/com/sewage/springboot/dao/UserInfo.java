package com.sewage.springboot.dao;

public class UserInfo {
    private String Password;
    private String CredentialsSalt;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCredentialsSalt() {
        return CredentialsSalt;
    }

    public void setCredentialsSalt(String credentialsSalt) {
        CredentialsSalt = credentialsSalt;
    }
}
