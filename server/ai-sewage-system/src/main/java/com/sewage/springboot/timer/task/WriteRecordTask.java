package com.sewage.springboot.timer.task;

import com.sewage.springboot.timer.WriteRecord;

import java.util.TimerTask;

public class WriteRecordTask extends TimerTask {

    @Override
    public void run() {
        // 做模拟测试用 站点为测试站点
        WriteRecord writeRecord = new WriteRecord();
        writeRecord.write();
    }
}
