package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "score")
public class Score {
    /**
     * 主鍵自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 分数
     */
    @Column(name = "theory_score")
    private BigDecimal theoryScore;
    /**
     * 实操
     */
    @Column(name = "operation_score")
    private BigDecimal operationScore;
    /**
     * 综合成绩
     */
    @Column(name = "overall_score")
    private BigDecimal overallScore;
    /**
     * 最终结果
     */
    @Column(name = "final_result")
    private String finalResult;
    /**
     * 证书编号
     */
    @Column(name = "certificate_no")
    private String certificateNo;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date create_time;

    /**
     * 报名表
     */
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = SignUp.class)
    private SignUp signUp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public BigDecimal getTheoryScore() {
        return theoryScore;
    }

    public void setTheoryScore(BigDecimal theoryScore) {
        this.theoryScore = theoryScore;
    }

    public BigDecimal getOperationScore() {
        return operationScore;
    }

    public void setOperationScore(BigDecimal operationScore) {
        this.operationScore = operationScore;
    }

    public BigDecimal getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(BigDecimal overallScore) {
        this.overallScore = overallScore;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }


    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public SignUp getSignUp() {
        return signUp;
    }

    public void setSignUp(SignUp signUp) {
        this.signUp = signUp;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
