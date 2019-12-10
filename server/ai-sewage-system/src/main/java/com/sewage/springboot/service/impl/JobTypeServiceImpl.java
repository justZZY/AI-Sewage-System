/**
 * 
 */
package com.sewage.springboot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.sewage.springboot.entity.po.JobType;
import com.sewage.springboot.mapper.impl.JobTypeMapper;
import com.sewage.springboot.service.JobTypeService;
import com.sewage.springboot.util.CommonUtil;

import tk.mybatis.mapper.entity.Example;

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
		JobType jobType = new JobType();
		jobType.setFatherId(0); // 查询顶级分类
		List<JobType> list = jobTypeMapper.select(jobType);
		JSONObject json = new JSONObject();
		if(list!=null) {
			for(JobType jt:list) {
				String jobTypeName = jt.getJobTypeName();
				JobType query = new JobType();
				query.setFatherId(jt.getId()); // 二级类别
				List<JobType> ls = jobTypeMapper.select(query);
				json.put(jobTypeName, ls);
			}
		}
		return CommonUtil.jsonResult(1, "查询完成", json);
	}

}
