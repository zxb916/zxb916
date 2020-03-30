package com.example.demo.model;


import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "attach")
public class attach {

    /**
     * 主鍵自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "uuid")
    private String uuid;


    @Column(name = "path")
    private String path;


    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;

}
