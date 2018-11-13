package com.example.godcode.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2018/7/30.
 */
@Entity
public class User {
    @Id
    private Long id;
    private String area;
    private String sex;
    private String headImageUrl;
    private String userName;
    private int userId;
    private String syNumber;
    private String phoneNumer;
    @Generated(hash = 2131904150)
    public User(Long id, String area, String sex, String headImageUrl,
            String userName, int userId, String syNumber, String phoneNumer) {
        this.id = id;
        this.area = area;
        this.sex = sex;
        this.headImageUrl = headImageUrl;
        this.userName = userName;
        this.userId = userId;
        this.syNumber = syNumber;
        this.phoneNumer = phoneNumer;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getArea() {
        return this.area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getHeadImageUrl() {
        return this.headImageUrl;
    }
    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getSyNumber() {
        return this.syNumber;
    }
    public void setSyNumber(String syNumber) {
        this.syNumber = syNumber;
    }
    public String getPhoneNumer() {
        return this.phoneNumer;
    }
    public void setPhoneNumer(String phoneNumer) {
        this.phoneNumer = phoneNumer;
    }

    






}