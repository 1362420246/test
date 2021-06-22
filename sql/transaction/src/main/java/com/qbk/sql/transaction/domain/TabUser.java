package com.qbk.sql.transaction.domain;

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
public class TabUser implements Serializable {

    private Integer userId;

    private String userName;

    private static final long serialVersionUID = 1L;
}