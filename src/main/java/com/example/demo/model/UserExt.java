package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_ext")
public class UserExt {
    /**
     * 外键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 性别
     */
    @Column(name = "sex")
    private String sex;
    /**
     * 生日
     */
    @Column(name = "birthdy")
    private Date birthdy;
    /**
     * 文化程度
     */
    @Column(name = "degree")
    private String degree;
    /**
     * 邮编
     */
    @Column(name = "post_code")
    private String postCode;
    /**
     * 头像
     */
    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "政治面貌")
    private String political;

    @Column(name = "work_time")
    private Integer workTime;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = User.class)
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public Date getBirthdy() {
        return birthdy;
    }

    public void setBirthdy(Date birthdy) {
        this.birthdy = birthdy;
    }


    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }


    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }


    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
    }
}
