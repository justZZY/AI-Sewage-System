package com.sewage.springboot.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "User")
public class User implements Serializable {
        @Id
        @GeneratedValue
        private Integer uid;
        @Column(unique =true)
        private String username;//帐号
        private String password; //密码;
        private String CredentialsSalt;//加密密码的盐
        private String scope;
        private String client_id;
        private String client_secret;
        private String grant_type;
        private Integer delete_status;
        private Integer role_id;
        private String create_time;
        private String update_timel;
        @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
        @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
        private List<Sys_role> roleList;// 一个用户具有多个角色

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCredentialsSalt() {
        return CredentialsSalt;
    }

    public void setCredentialsSalt(String credentialsSalt) {
        CredentialsSalt = credentialsSalt;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public Integer getDelete_status() {
        return delete_status;
    }

    public void setState(Integer delete_status) {
        this.delete_status = delete_status;
    }

    public List<Sys_role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Sys_role> roleList) {
        this.roleList = roleList;
    }
}


