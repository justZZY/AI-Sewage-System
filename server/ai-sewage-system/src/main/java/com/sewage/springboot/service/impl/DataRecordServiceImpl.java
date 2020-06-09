package com.sewage.springboot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.dao.DataRecordDao;
import com.sewage.springboot.entity.DataRecord;
import com.sewage.springboot.logger.ConsoleLoggerFactory;
import com.sewage.springboot.logger.Logger;
import com.sewage.springboot.service.DataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DataRecordServiceImpl implements DataRecordService {
    @Autowired
    private DataRecordDao dataRecordDao;
    private final Logger logger = new ConsoleLoggerFactory().createLogger("DataRecordService");

    @Override
    public void insertDataRecord(DataRecord dataRecord) {
        try {
            dataRecordDao.insertDataRecord(dataRecord);
        }catch (Exception e) {
            logger.logError(e.toString());
        }
    }

    @Override
    public JSONArray formatDayDataRecord(String siteID, Date date) {
        JSONArray dataArray = new JSONArray();
        try {
            List<DataRecord> dataList = dataRecordDao.getDayDataRecord(siteID, date);
            dataList.forEach(item -> {
                // 传入每个时段的各项参数, 需要把时间解析成小时
                JSONObject data = new JSONObject();
                assignData(data, item, "hour");
                dataArray.add(data);
            });
        }catch (Exception e) {
            logger.logError(e.toString());
        }
        // 做sql时已经对时间进行过排序
        // 所以返回的数组应该是时间升序的
        return dataArray;
    }

    @Override
    public JSONArray formatMonthDataRecord(String siteID, Integer year, Integer month) {
        return null;
    }

    @Override
    public JSONArray formatQuarterDataRecord(String siteID, Integer year, Integer quarter) {
        return null;
    }

    @Override
    public JSONArray formatMonthYearDataRecord(String siteID, Integer year) {
        return null;
    }

    @Override
    public JSONArray formatQuarterYearDataRecord(String siteID, Integer year) {
        return null;
    }

    private JSONObject assignData(JSONObject data, DataRecord item, String flag) {
        if (flag.equals("hour")) {
            data.put("site_id", item.getSiteId());
            data.put("in_ph", item.getInPh());
            data.put("in_an", item.getInAn());
            data.put("in_flow", item.getInFlow());
            data.put("in_accflow", item.getInAccflow());
            data.put("out_ph", item.getOutPh());
            data.put("out_an", item.getOutAn());
            data.put("out_flow", item.getOutFlow());
            data.put("out_accflow", item.getOutAccflow());
            data.put("clean_num", item.getCleanNum());
            data.put("output_num", item.getOutputNum());
            data.put("date", item.getDate());
        }
        return data;
    }
}
