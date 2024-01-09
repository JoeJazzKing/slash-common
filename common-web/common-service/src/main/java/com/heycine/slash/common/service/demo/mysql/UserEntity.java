package com.heycine.slash.common.service.demo.mysql;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2022-08-30 12:54:31
 */
@TableName("user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 780376532491423825L;
    /**
    * 用户id
    */
    @TableId
    private String id;
    /**
    * 用户名称
    */
    private String name;
    /**
    * 用户密码
    */
    private String password;
    /**
    * 用户邮箱
    */
    private String email;
    /**
    * 用户类型
    */
    private Integer type;
    /**
    * 手机号码
    */
    private Long phone;
    /**
    * 状态
    */
    private String state;
    
    private String avatar;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}