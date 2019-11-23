/**
 * 
 */
package com.sewage.springboot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.sewage.springboot.entity.po.JobType;
import com.sewage.springboot.mapper.impl.JobTypeMapper;
import com.sewage.springboot.service.JobTypeService;
import com.sewage.springboot.util.CommonUtil;

/**
 *
 * @author：sc
 * @data： 2019年9月22日
 */
@Service
public class JobTypeServiceImpl implements JobTypeService {

	@Autowired JobTypeMapper jobTypeMapper;
	
	@Override
	public JSONObject queryList() {
		List<JobType> list = jobTypeMapper.selectAll();
		return CommonUtil.jsonResult(1, "查询完成", list);
	}

}
