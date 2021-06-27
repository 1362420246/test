package com.qbk.jpa.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
/**
 * 指定表名，和唯一索引字段
 */
@Entity
@Table(name = "tab_user" ,uniqueConstraints = {@UniqueConstraint(columnNames="userName")})
public class User {

    /**
     *  IDENTITY 为自增主键策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * length 指定字段长度
     */
    @Column(length = 64)
    private String userName;

    /**
     * 列名和实体名不同
     */
    @Column(name = "user_age")
    private Integer age;

    /**
     * Enumerated 枚举字段
     * ORDINA 持久枚举类型字段为整数，元素一般从0开始索引.
     * STRING 持久枚举类型为字符串.
     */
    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    /**
     * nullable 表示字段不能为空
     *
     * TIMESTAMP 表示时间格式为 datetime
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;

    /**
     * 表示非数据库映射字段
     */
    @Transient
    private String extend;

}
