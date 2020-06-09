package com.sewage.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.sewage.springboot.Global;
import com.sewage.springboot.service.DataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author: zzy
 * @desc: 统计数据相关Controller
 */
@CrossOrigin
@RestController
@SpringBootApplication
@RequestMapping(value = "/data")
public class DataRecordController {
    @Autowired
    private DataRecordService recordService;

    @RequestMapping(value = "getServerCreateTime", method = RequestMethod.GET)
    public Date getDayRecord () {
        return Global.createDate;
    }

    /**
     * 用于查询站点某一天的数据情况
     * @param siteID
     * @param date
     * @return Json对象数组
     */
    @RequestMapping(value = "getDayDataRecord", method = RequestMethod.GET)
    public JSONArray getDayRecord (@RequestParam(value = "site_id") String siteID,
                                   @RequestParam(value = "date")
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return recordService.formatDayDataRecord(siteID, date);
    }
}
