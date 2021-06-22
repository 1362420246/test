package com.qbk.timer.service.impl;

import com.qbk.timer.annotation.DynamicSchedule;
import com.qbk.timer.enums.DynamicOperationType;
import com.qbk.timer.model.AddForDynamicDTO;
import com.qbk.timer.model.CancelForDynamicDTO;
import com.qbk.timer.model.UpdateForDynamicDTO;
import com.qbk.timer.service.DynamicScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态定时任务服务类
 **/
@Service
public class DynamicScheduleServiceImpl implements DynamicScheduleService {
    @Override
    @DynamicSchedule(scheduleMethod = "add",dynamicOperationType = DynamicOperationType.ADD)
    public void add(AddForDynamicDTO addForDynamicDto){

    }

    @Override
    @DynamicSchedule(scheduleMethod = "list",dynamicOperationType = DynamicOperationType.LIST)
    public List<String> list(){
        return null;
    }
    @Override
    @DynamicSchedule(scheduleMethod = "update",dynamicOperationType = DynamicOperationType.UPDATE)
    public void update(UpdateForDynamicDTO updateForDynamicDto){

    }
    @Override
    @DynamicSchedule(scheduleMethod = "cancel",dynamicOperationType = DynamicOperationType.CANCEL)
    public void cancel(CancelForDynamicDTO cancelForDynamicDto){

    }
}
