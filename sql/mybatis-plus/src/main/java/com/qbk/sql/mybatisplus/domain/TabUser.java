package com.qbk.sql.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Created by Mybatis Generator 2020/04/25
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tab_user")
public class TabUser implements Serializable {
     @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @TableField(value = "user_name")
    private String userName;

    private static final long serialVersionUID = 1L;

    public static final String COL_USER_NAME = "user_name";
}