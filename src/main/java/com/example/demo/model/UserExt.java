package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "user_ext")
public class UserExt {
    /**
     *外键id
     */
    private Long id;
    /**
     *性别
     */
    private String sex;
    /**
     *生日
     */
    private Date birthdy;
    /**
     *文化程度
     */
    private String degree;
    /**
     *邮编
     */
    private String postCode;
    /**
     *头像
     */
    private String profilePhoto;

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "birthdy")
    public Date getBirthdy() {
        return birthdy;
    }

    public void setBirthdy(Date birthdy) {
        this.birthdy = birthdy;
    }

    @Column(name = "degree")
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Column(name = "post_code")
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Column(name = "profile_photo")
    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
