package com.example.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "signup")
public class SignUp {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;
    /**
     * 准考证号
     */
    @Column(name = "pass_card")
    private String passCard;
    /**
     * 科目
     */
    @Column(name = "name")
    private String name;
    /**
     * 审核 (0待审核 1审核通过 2 审核不通过)
     */
    @Column(name = "review")
    private Integer review;
    /**
     * 审核意见
     */
    @Column(name = "review_option")
    private String reviewoption;
    /**
     * 工种
     */
    @Column(name = "already_work_type")
    private String alreadyWorkType;
    /**
     * 技能等级
     */
    @Column(name = "already_skill_rank")
    private String alreadySkillRank;
    /**
     * 证书编号
     */
    @Column(name = "already_certificate_no")
    private String alreadyCertificateNo;
    /**
     * 发证时间
     */
    @Column(name = "already_issue_date")
    private Date alreadyIssueDate;
    /**
     * 申请工种
     */
    @Column(name = "apply_work_type")
    private String applyWorkType;
    /**
     * 申请技能等级
     */
    @Column(name = "apply_skill_rank")
    private String applySkillRank;
    /**
     * 业务主管审核
     */
    @Column(name = "business_check")
    private Integer businessCheck;
    /**
     * 兵员和文职人员审核通过
     */
    @Column(name = "soldiers_check")
    private Integer soldiersCheck;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime;
    /**
     * 团级以上单位资格审查意见
     */
    @Column(name = "audit_opinion")
    private String auditOpinion;
    /**
     * 模块选择 （0 理论知识 1 操作技能 2 综合评审）
     */
    @Column(name = "choice")
    private Integer choice;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getPassCard() {
        return passCard;
    }

    public void setPassCard(String passCard) {
        this.passCard = passCard;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }


    public String getReviewoption() {
        return reviewoption;
    }

    public void setReviewoption(String reviewoption) {
        this.reviewoption = reviewoption;
    }


    public String getAlreadyWorkType() {
        return alreadyWorkType;
    }

    public void setAlreadyWorkType(String alreadyWorkType) {
        this.alreadyWorkType = alreadyWorkType;
    }


    public String getAlreadySkillRank() {
        return alreadySkillRank;
    }

    public void setAlreadySkillRank(String alreadySkillRank) {
        this.alreadySkillRank = alreadySkillRank;
    }


    public String getAlreadyCertificateNo() {
        return alreadyCertificateNo;
    }

    public void setAlreadyCertificateNo(String alreadyCertificateNo) {
        this.alreadyCertificateNo = alreadyCertificateNo;
    }


    public Date getAlreadyIssueDate() {
        return alreadyIssueDate;
    }

    public void setAlreadyIssueDate(Date alreadyIssueDate) {
        this.alreadyIssueDate = alreadyIssueDate;
    }


    public String getApplyWorkType() {
        return applyWorkType;
    }

    public void setApplyWorkType(String applyWorkType) {
        this.applyWorkType = applyWorkType;
    }


    public String getApplySkillRank() {
        return applySkillRank;
    }

    public void setApplySkillRank(String applySkillRank) {
        this.applySkillRank = applySkillRank;
    }


    public Integer getBusinessCheck() {
        return businessCheck;
    }

    public void setBusinessCheck(Integer businessCheck) {
        this.businessCheck = businessCheck;
    }


    public Integer getSoldiersCheck() {
        return soldiersCheck;
    }

    public void setSoldiersCheck(Integer soldiersCheck) {
        this.soldiersCheck = soldiersCheck;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }


    public Integer getChoice() {
        return choice;
    }

    public void setChoice(Integer choice) {
        this.choice = choice;
    }
}
