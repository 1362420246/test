package com.qbk.timer.controller;


import com.qbk.timer.model.AddForDynamicDTO;
import com.qbk.timer.model.CancelForDynamicDTO;
import com.qbk.timer.model.UpdateForDynamicDTO;
import com.qbk.timer.service.DynamicScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *  动态定时任务控制器
 **/
@RestController
@RequestMapping("/dynamic/schedule")
public class DynamicScheduleController {

    @Autowired
    private DynamicScheduleService dynamicScheduleService;

    /**
     * 添加任务
     */
    @PostMapping(value = "/add")
    public String add(@RequestBody @Valid AddForDynamicDTO addForDynamicDTO){
        dynamicScheduleService.add(addForDynamicDTO);
        return "success";
    }

    @GetMapping(value = "/list")
    public List<?> list(){
        return dynamicScheduleService.list();
    }

    @PostMapping(value = "/update")
    public String update(@RequestBody @Valid UpdateForDynamicDTO updateForDynamicDTO){
        dynamicScheduleService.update(updateForDynamicDTO);
        return "success";
    }

    @PostMapping(value = "/cancel")
    public String cancel(@RequestBody @Valid CancelForDynamicDTO cancelForDynamicDTO){
        dynamicScheduleService.cancel(cancelForDynamicDTO);
        return "success";
    }
}
