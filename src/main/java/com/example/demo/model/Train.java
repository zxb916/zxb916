package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "train")
public class Train implements Comparable<Train> {
    /**
     * 主键id
     */
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private String startTime;
    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private String endTime;
    /**
     * 所在单位
     */
    @Column(name = "unit")
    private String unit;
    /**
     * 培训专业名称
     */
    @Column(name = "major_name")
    private String majorName;
    /**
     * 标准学时数
     */
    @Column(name = "count")
    private String count;

    @Column(name = "sub")
    private Integer sub;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Integer getSub() {
        return sub;
    }

    public void setSub(Integer sub) {
        this.sub = sub;
    }

    @Override
    public int compareTo(Train o) {
        if (this.sub < o.getSub()) {
            return 1;
        } else if (this.sub > o.getSub()) {
            return -1;
        } else {
            return this.startTime.compareTo(o.getStartTime());  // 调用String中的compareTo()方法
        }
    }
}
