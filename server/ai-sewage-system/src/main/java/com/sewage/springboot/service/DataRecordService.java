package com.sewage.springboot.service;

import com.alibaba.fastjson.JSONArray;
import com.sewage.springboot.entity.DataRecord;

import java.util.Date;

public interface DataRecordService {
    /**
     * @desc 插入数值数据
     * @param dataRecord
     * @return 是否插入成功
     */
    void insertDataRecord(DataRecord dataRecord);
    /**
     * @desc 获取日报表数据
     * 24小时每个小时数据 每小时隔十分钟采样
     */
    JSONArray formatDayDataRecord(String siteID, Date date);
    /**
     * @desc 获取月度报表数据
     * 根据日报表数据计算月报表
     */
    JSONArray formatMonthDataRecord(String siteID, Integer year, Integer month);
    /**
     * @desc 获取一个季度的报表
     * 三个月的报表为一个季度
     */
    JSONArray formatQuarterDataRecord(String siteID, Integer year, Integer quarter);
    /**
     * @desc 获取12个月的年报表
     */
    JSONArray formatMonthYearDataRecord(String siteID, Integer year);
    /**
     * @desc 获取4个季度的年报表
     */
    JSONArray formatQuarterYearDataRecord(String siteID, Integer year);
}
