package com.sewage.springboot.timer;

import com.sewage.springboot.entity.DataRecord;
import com.sewage.springboot.service.DataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author zzy
 * @desc 用于获取数据定时写入
 */
@Component
public class WriteRecord {
    public static WriteRecord writeRecord;
    private DataRecord dataRecord;
    @Autowired
    private DataRecordService recordService;

    // 设备无法配置 使用随机数模拟
    @PostConstruct
    public void init() {
        writeRecord = this;
        writeRecord.recordService = this.recordService;
    }

    public void write() {
        writeRecord.dataRecord = new DataRecord();
        writeRecord.dataRecord.setSiteId("300219050084"); // 暂时写死测试站点
        assignData(writeRecord.dataRecord);
        writeRecord.recordService.insertDataRecord(writeRecord.dataRecord);
    }

    // 模拟数据
    private void assignData(DataRecord dataRecord) {
        Random random = new Random();
        DecimalFormat df = new DecimalFormat( "0.00" );
        dataRecord.setInPh(df.format(random.nextDouble() * 10));
        dataRecord.setInAn(df.format(random.nextDouble() * 10));
        dataRecord.setInFlow(df.format(random.nextDouble() * 10));
        dataRecord.setInAccflow(df.format(random.nextDouble() * 300));
        dataRecord.setOutPh(df.format(random.nextDouble() * 10));
        dataRecord.setOutAn(df.format(random.nextDouble() * 10));
        dataRecord.setOutFlow(df.format(random.nextDouble() * 10));
        dataRecord.setOutAccflow(df.format(random.nextDouble() * 300));
        dataRecord.setCleanNum(df.format(random.nextDouble() * 100));
        dataRecord.setOutputNum(df.format(random.nextDouble() * 400));
        dataRecord.setDate(new Date(System.currentTimeMillis()));
    }

}
