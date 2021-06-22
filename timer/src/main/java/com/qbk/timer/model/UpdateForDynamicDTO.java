package com.qbk.timer.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *  更新动态定时任务 dto
 **/
@Data
public class UpdateForDynamicDTO {

    /**
     * 任务定时运行时间
     * yyyy-M-d hh:mm:ss
     */
    @NotBlank
    private String runTime;

    /**
     * 任务 tadkId
     */
    @NotBlank
    private String taskId;
}
