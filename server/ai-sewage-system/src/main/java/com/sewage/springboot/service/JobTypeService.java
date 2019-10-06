package com.sewage.springboot.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.entity.po.Job;
import com.sewage.springboot.entity.po.JobType;

/**
 * 工单类型业务接口
 * 
 * @author sc
 * @date 2019年9月10日
 *
 */
public interface JobTypeService {
	
	
	 public JSONObject queryList();

	
}
