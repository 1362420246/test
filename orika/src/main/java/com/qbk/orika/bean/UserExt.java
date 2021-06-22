package com.qbk.orika.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExt {
    private Integer id;
    private String name ;
    private List<String> roles;
    private String ext;
}
