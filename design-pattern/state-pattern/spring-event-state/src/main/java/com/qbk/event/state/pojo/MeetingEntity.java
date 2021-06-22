package com.qbk.event.state.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会议
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingEntity {
    /**
     * 会议id
     */
    private Integer id;
    /**
     * 会议名称
     */
    private String name;
    /**
     * 会议类型
     */
    private Integer type;
    /**
     * 会议状态 0 预约 1 进行中 2 结束 3 取消
     */
    private Integer status;
}
