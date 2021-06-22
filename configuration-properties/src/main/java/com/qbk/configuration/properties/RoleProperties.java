package com.qbk.configuration.properties;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RoleProperties {
    private List<String> roleNames;
    private Map<String,String> roleMap;
}
