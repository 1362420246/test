package com.qbk.enumdemo.paramconver;

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
    private VipEnum vip;
}
