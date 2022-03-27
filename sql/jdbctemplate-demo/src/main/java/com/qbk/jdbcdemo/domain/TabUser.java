package com.qbk.jdbcdemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TabUser implements Serializable {

    private Integer userId;

    private String userName;

    private static final long serialVersionUID = 1L;
}