package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "options")
public class Options {
    /**
     * 主鍵自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 类型
     */
    @Column(name = "type")
    private Integer type;
    /**
     * key
     */
    @Column(name = "key")
    private String key;
    /**
     * value
     */
    @Column(name = "value")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
