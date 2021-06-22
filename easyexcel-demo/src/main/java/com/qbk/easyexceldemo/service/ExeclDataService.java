package com.qbk.easyexceldemo.service;

import com.qbk.easyexceldemo.entity.ExcelData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExeclDataService {

    public void saveData(List<ExcelData> list) {
        System.out.println(list);
    }
}
