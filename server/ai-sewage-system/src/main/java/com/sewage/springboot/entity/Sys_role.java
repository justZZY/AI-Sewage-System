package com.sewage.springboot.entity;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Sys_role")
public class Sys_role {
        @Id
        @GeneratedValue
        private Integer id; // 编号
        @Column
        private String roleName; // 角色标识程序中判断使用,如"admin",这个是唯一的:
        private Integer delete_status;

        //角色 -- 权限关系：多对多关系;
        @ManyToMany(fetch= FetchType.EAGER)
        @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="permissionId")})
        private List<Sys_permission> permissions;

        // 用户 - 角色关系定义;
        @ManyToMany
        @JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="uid")})
        private List<User> userInfos;// 一个角色对应多个用户

        public Integer getId() {
            return id;
        }

        public String getRoleName() {
            return roleName;
        }

        public Integer getDelete_status(){return delete_status;}

        public List<Sys_permission> getPermissions() {
            return permissions;
        }

        public List<User> getUserInfos() {
            return userInfos;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setRole(String role) { this.roleName = roleName;}

        public void setDelete_status(Integer Delete_status) {this.delete_status = delete_status;}

        public void setPermissions(List<Sys_permission> permissions) {
            this.permissions = permissions;
        }

        public void setUserInfos(List<User> userInfos) {
            this.userInfos = userInfos;
        }
}

