package com.qbk.timer.service;

import com.qbk.timer.model.AddForDynamicDTO;
import com.qbk.timer.model.CancelForDynamicDTO;
import com.qbk.timer.model.UpdateForDynamicDTO;

import java.util.List;

/**
 * 动态定时任务接口服务
 **/
public interface DynamicScheduleService {
    /**
     * 添加定时任务服务类
     * @param addForDynamicDto 添加动态定时任务 dto
     */
    void add(AddForDynamicDTO addForDynamicDto);

    /**
     * 定时任务列表
     * @return list
     */
    List<String> list();

    /**
     * 更新定时任务任务
     * @param updateForDynamicDto 更新定时任务数据结构体
     */
    void update(UpdateForDynamicDTO updateForDynamicDto);

    /**
     * 取消定时任务
     * @param cancelForDynamicDto 取消定时任务数据结构体
     */
    void cancel(CancelForDynamicDTO cancelForDynamicDto);

}
