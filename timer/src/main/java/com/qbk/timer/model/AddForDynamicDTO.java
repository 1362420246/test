package com.qbk.timer.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *  添加动态定时任务 dto
 **/
@Data
public class AddForDynamicDTO {
    /**
     * 任务定时运行时间
     * yyyy-M-d hh:mm:ss
     */
    @NotBlank
    private String runTime;
}
