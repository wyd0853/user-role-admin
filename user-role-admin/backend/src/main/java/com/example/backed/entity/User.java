package com.example.backed.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String trueName;
    private String password;
    private String email;
    private Integer gender;
    private String address;
    private String introduction;
    private String phone;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] avatarUrl;
    //private String roleIds;
    private LocalDateTime createTime;
    @Column(nullable = false)
    private Integer status = 0; // 默认值为0

    // 一对多表与外键
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAvatarUrl() {
        if (avatarUrl != null) {
            // 根据实际图片类型返回适当的 MIME 类型
            String mimeType = "image/jpeg"; // 默认设置为JPEG
            if (isPngImage(avatarUrl)) {
                mimeType = "image/png";
            }
            return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(avatarUrl);
        }
        return null;
    }

    public void setAvatarUrl(String avatarUrlBase64) {
        if (avatarUrlBase64 != null && avatarUrlBase64.startsWith("data:image")) {
            // 去掉 Base64 前缀（例如 "data:image/jpeg;base64,"）
            String base64Data = avatarUrlBase64.split(",")[1];
            this.avatarUrl = Base64.getDecoder().decode(base64Data);
        } else {
            this.avatarUrl = null;
        }
    }

    // Helper method to check if the image is PNG
    private boolean isPngImage(byte[] imageData) {
        if (imageData != null && imageData.length > 8) {
            return (imageData[0] == (byte) 0x89 &&
                    imageData[1] == (byte) 0x50 &&
                    imageData[2] == (byte) 0x4E &&
                    imageData[3] == (byte) 0x47 &&
                    imageData[4] == (byte) 0x0D &&
                    imageData[5] == (byte) 0x0A &&
                    imageData[6] == (byte) 0x1A &&
                    imageData[7] == (byte) 0x0A);
        }
        return false;
    }

    //public String getRoleIds() {
        //return roleIds;
    //}

    //public void setRoleIds(String roleIds) {
        //this.roleIds = roleIds;
    //}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}