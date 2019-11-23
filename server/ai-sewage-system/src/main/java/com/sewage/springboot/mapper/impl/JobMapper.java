package com.sewage.springboot.mapper.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sewage.springboot.entity.po.Job;
import com.sewage.springboot.mapper.IBaseMapper;


public interface JobMapper extends IBaseMapper<Job> {
	
	
	/**
	 * 根据工单所属人以及工单状态查询列表（注：设置其他属性无效）
	 * 
	 * @author：sc
	 * @data： 2019年9月25日
	 */
	public List <Job> selectJobList(Job job);
	
}