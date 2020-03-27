package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {
    /**
     * id
     */
    private Long id;
    /**
     * 角色 是否是管理员
     */
    private RoleType isAdmin;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 士兵证
     */
    private String soldierId;
    /**
     * 军衔
     */
    private ArmedRankType armedRank;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 部号
     */
    private String deptno;
    /**
     * 通信地址
     */
    private String mailingAddress;
    /**
     * 密码
     */
    @JsonIgnore
    private String password;
    /**
     * 入伍月份
     */
    private Date joinTime;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 用户元信息
     */
    private UserExt userExt;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "is_admin")
    public RoleType getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(RoleType isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "id_card")
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Column(name = "soldier_id")
    public String getSoldierId() {
        return soldierId;
    }

    public void setSoldierId(String soldierId) {
        this.soldierId = soldierId;
    }

    @Column(name = "armed_rank")
    public ArmedRankType getArmedRank() {
        return armedRank;
    }

    public void setArmedRank(ArmedRankType armedRank) {
        this.armedRank = armedRank;
    }

    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "dept_no")
    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    @Column(name = "mailing_address")
    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "join_time")
    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @OneToOne
    @JoinColumn(name = "userMeta_id", referencedColumnName = "id")
    public UserExt getUserMeta() {
        return userExt;
    }

    public void setUserMeta(UserExt userMeta) {
        this.userExt = userExt;
    }
}
