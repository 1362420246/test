package com.qbk.enumdemo.params;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户参数
 */
@Data
public class UserDTO implements Serializable{
    private static final long serialVersionUID = -5337631111741913417L;
    private Integer id;
    private String name;
    private SexEnum sex;
}
