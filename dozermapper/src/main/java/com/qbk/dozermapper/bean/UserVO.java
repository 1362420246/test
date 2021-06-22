package com.qbk.dozermapper.bean;

import lombok.Data;

import java.util.List;

@Data
public class UserVO {
    private Integer id;
    private String username ;
    private List<String> roleList;
}
