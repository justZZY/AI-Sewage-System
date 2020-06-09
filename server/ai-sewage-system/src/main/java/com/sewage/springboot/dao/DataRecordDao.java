package com.sewage.springboot.dao;

import com.sewage.springboot.entity.DataRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @desc 获取数据库种表单数据
 *
 */
public interface DataRecordDao {
    void insertDataRecord(DataRecord dataRecord);

    List<DataRecord> getDayDataRecord(@Param("site_id") String site_id, @Param("date") Date date);

    List<DataRecord>  getMonthDataRecord(@Param("site_id") String site_id, @Param("year") Integer year, @Param("month") Integer month);

    List<DataRecord>  getQuarterDataRecord(@Param("site_id") String site_id, @Param("year") Integer year, @Param("quarter") Integer quarter);

    List<DataRecord>  getMonthYearDataRecord(@Param("site_id") String site_id, @Param("year") Integer year);
}
