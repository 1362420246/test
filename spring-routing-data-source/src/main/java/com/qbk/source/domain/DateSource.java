package com.qbk.source.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DateSource implements Serializable {
    private static final long serialVersionUID = 1146647233354623824L;
    /**
     * 数据源key
     */
    private String key;
    /**
     * 驱动
     */
    private String driveClass;
    private String url;
    private String username;
    private String password;
    /**
     * 连接等待超时时间 毫秒
     */
    @Builder.Default
    private int maxWait = 3000;
}
