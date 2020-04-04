package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

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
    @NotEmpty(message = "用户名不允许为空")
    @Column(name = "user_name")
    private String userName;
    /**
     * 身份证
     */
    @NotEmpty(message = "身份证号不允许为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")
    @Column(name = "id_card")
    private String idCard;

    /**
     * 士兵证
     */
    @NotEmpty(message = "士兵类型不允许为空")
    @Column(name = "soldier_type")
    private String soldierType;

    /**
     * 士兵证
     */
    @NotEmpty(message = "士兵证号不允许为空")
    @Column(name = "soldier_id")
    private String soldierId;
    /**
     * 军衔
     */
    @Column(name = "armed_rank")
    private String armedRank;
    /**
     * 联系电话
     */
    @NotEmpty(message = "联系电话不允许为空")
    @Column(name = "mobile")
    private String mobile;
    /**
     * 部号
     */
    @NotEmpty(message = "部别不允许为空")
    @Column(name = "dept_no")
    private String deptNo;
    /**
     * 通信地址
     */
    @NotEmpty(message = "通信地址不允许为空")
    @Column(name = "mailing_address")
    private String mailingAddress;
    /**
     * 加密后的密码
     */
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Column(name = "old_password")
    private String oldPassword;

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

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = Resume.class)
    private Set<Resume> resumes;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = Train.class)
    private Set<Train> trains;


    public Set<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(Set<Resume> resumes) {
        this.resumes = resumes;
    }

    public Set<Train> getTrains() {
        return trains;
    }

    public void setTrains(Set<Train> trains) {
        this.trains = trains;
    }

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


    public String getSoldierType() {
        return soldierType;
    }

    public void setSoldierType(String soldierType) {
        this.soldierType = soldierType;
    }

    public String getArmedRank() {
        return armedRank;
    }

    public void setArmedRank(String armedRank) {
        this.armedRank = armedRank;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
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


    public UserExt getUserExt() {
        return userExt;
    }

    public void setUserExt(UserExt userExt) {
        this.userExt = userExt;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
