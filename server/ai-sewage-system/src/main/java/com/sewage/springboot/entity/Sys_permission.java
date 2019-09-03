package com.sewage.springboot.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Sys_permission")
public class Sys_permission implements Serializable {
        @Id
        @GeneratedValue
        private Integer id;//主键.
        @Column(columnDefinition="enum('menu','button')")
        private String menu_code;
        private String menu_name;
        private String permission_code; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
        private String permission_name;
        private Integer required_permission;
        @ManyToMany
        @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
        private List<Sys_role> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenu_code() {
        return menu_code;
    }

    public void setMenu_code(String menu_code) {
        this.menu_code = menu_code;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getPermission_code() {
        return permission_code;
    }

    public void setPermission_code(String permission_code) {
        this.permission_code = permission_code;
    }

    public String getPermission_name() {
        return permission_name;
    }

    public void setPermission_name(String permission_name) {
        this.permission_name = permission_name;
    }

    public Integer getRequired_permission() {
        return required_permission;
    }

    public void setRequired_permission(Integer required_permission) {
        this.required_permission = required_permission;
    }

    public List<Sys_role> getRoles() {
        return roles;
    }

    public void setRoles(List<Sys_role> roles) {
        this.roles = roles;
    }
}
