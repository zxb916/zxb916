package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 角色 是否是管理员
     */
    @Column(name = "is_admin")
    private RoleType isAdmin;
    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;
    /**
     * 身份证
     */
    @Column(name = "id_card")
    private String idCard;
    /**
     * 士兵证
     */
    @Column(name = "soldier_id")
    private String soldierId;
    /**
     * 军衔
     */
    @Column(name = "armed_rank")
    private ArmedRankType armedRank;
    /**
     * 联系电话
     */
    @Column(name = "mobile")
    private String mobile;
    /**
     * 部号
     */
    @Column(name = "dept_no")
    private String deptno;
    /**
     * 通信地址
     */
    @Column(name = "mailing_address")
    private String mailingAddress;
    /**
     * 密码
     */
    @Column(name = "password")
    private String password;
    /**
     * 入伍月份
     */
    @Column(name = "join_time")
    private Date joinTime;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @CreatedDate
    private Timestamp createTime;
    /**
     * 用户元信息
     */
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = UserExt.class)
    private UserExt userExt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public RoleType getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(RoleType isAdmin) {
        this.isAdmin = isAdmin;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }


    public String getSoldierId() {
        return soldierId;
    }

    public void setSoldierId(String soldierId) {
        this.soldierId = soldierId;
    }


    public ArmedRankType getArmedRank() {
        return armedRank;
    }

    public void setArmedRank(ArmedRankType armedRank) {
        this.armedRank = armedRank;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }


    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public UserExt getUserMeta() {
        return userExt;
    }

    public void setUserMeta(UserExt userMeta) {
        this.userExt = userExt;
    }
}
