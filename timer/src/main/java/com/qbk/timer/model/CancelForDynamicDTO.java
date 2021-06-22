package com.qbk.timer.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 取消动态定时任务 dto
 **/
@Data
public class CancelForDynamicDTO {
    /**
     * 任务 tadkId
     */
    @NotBlank
    private String taskId;
}
