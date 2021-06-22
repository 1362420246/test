package com.qbk.tkmybatis.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

/**
 * NameStyle 注解可以在类上进行配置，优先级高于对应的 style 全局配置。
 * 注解支持以下几个选项：
 * normal,                     //原值
 * camelhump,                  //驼峰转下划线
 * uppercase,                  //转换为大写
 * lowercase,                  //转换为小写
 * camelhumpAndUppercase,      //驼峰转下划线大写形式
 * camelhumpAndLowercase,      //驼峰转下划线小写形式
 */
@NameStyle(Style.normal)
/**
 * Table 注解可以配置 name,catalog 和 schema 三个属性，
 * 配置 name 属性后，直接使用提供的表名，不再根据实体类名进行转换。
 * 其他两个属性中，同时配置时，catalog 优先级高于 schema，也就是只有 catalog 会生效。
 */
@Table(name = "tb_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TbUser implements Serializable {
    /**
     * Id 注解和映射无关，它是一个特殊的标记，用于标识数据库中的主键字段。
     * 正常情况下，一个实体类中至少需要一个标记 @Id 注解的字段，存在联合主键时可以标记多个。
     * 如果表中没有主键，类中就可以不标记。
     * 当类中没有存在标记 @Id 注解的字段时，你可以理解为类中的所有字段是联合主键。使用所有的 ByPrimaryKey 相关的方法时，有 where 条件的地方，会将所有列作为条件
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    /**
     * @KeySql 注解
     * 主键策略注解，用于配置如何生成主键。
     * 这是通用 Mapper 的自定义注解，改注解的目的就是替换 @GeneratedValue 注解。
     * @GeneratedValue 注解（JPA）
     * 主键策略注解，用于配置如何生成主键。
     * 推荐使用上面的 @KeySql 注解。
     * 例如：
     *  oracle:
     *      * @KeySql(sql = "select SEQ_ID.nextval from dual", order = ORDER.BEFORE)
     *      * @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select uuid()")
     * @Version 是实现乐观锁的一个注解
     *      支持的方法
     *          delete
     *          deleteByPrimaryKey
     *          updateByPrimaryKey
     *          updateByPrimaryKeySelective
     *          updateByExample
     *          updateByExampleSelective
     */

    /**
     * Column 注解支持 name, insertable 和 updateable 三个属性。
     * name 配置映射的列名。
     * insertable 对提供的 insert 方法有效，如果设置 false 就不会出现在 SQL 中。
     * updateable 对提供的 update 方法有效，设置为 false 后不会出现在 SQL 中。
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * Transient 注解来告诉通用 Mapper 这不是表中的字段
     * 非数据库表中字段
     */
    @Transient
    private String otherThings;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "`password`")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "is_lock")
    private Byte isLock;

    @Column(name = "is_del")
    private Byte isDel;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    private static final long serialVersionUID = 1L;
}