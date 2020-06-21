package com.sewage.springboot.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.entity.po.Job;
import com.sewage.springboot.entity.po.JobConfig;
import com.sewage.springboot.handle.exception.response.BussinessException;
import com.sewage.springboot.mapper.impl.JobConfigMapper;
import com.sewage.springboot.service.JobConfigService;
import com.sewage.springboot.util.CommonUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service
@Transactional
public class JobConfigServiceImpl implements JobConfigService {
	
	@Autowired JobConfigMapper jobConfigMapper;
	
	@Override
	public JSONObject queryConfig(String... names) {
		if(names.length<0) throw new BussinessException("-1", "缺少参数！");
		Example ex = new Example(JobConfig.class);
		Criteria c = ex.or();
		for(String name : names) {
			c.orEqualTo("name", name);
		}
		List<JobConfig> list = jobConfigMapper.selectByExample(ex);
		JSONObject data = null;
		if(list!=null && !list.isEmpty()) {
			data = new JSONObject();
			for(JobConfig conf:list) {
				data.put(conf.getName(), conf.getValue());
			}
			return CommonUtil.jsonResult(list.size(), "查询成功！", data);
		}else {
			return CommonUtil.jsonResult(0, "查询不到该配置！", data);
		}
	}

	@Override
	public JSONObject setConfig(String name, String value) {
		if(StringUtils.isEmpty(name)) throw new BussinessException("-1", "配置名称不能为空！");
		JobConfig model = new JobConfig();
		model.setName(name);
		JobConfig record = jobConfigMapper.selectOne(model);
		model.setValue(value);
		int i = 0;
		String msg =null;
		if(record == null) {
			i = jobConfigMapper.insert(model);
			msg = i>0?"添加成功":"添加失败";
		}else {
			model.setId(record.getId());
			i = jobConfigMapper.updateByPrimaryKeySelective(model);
			msg = i>0?"修改成功":"修改失败";
		}
		return CommonUtil.jsonResult(i, msg);
	}
	
	@Override
	public JSONObject setConfigs(JSONObject json) {
		json.forEach((k,v)->{
			setConfig(k, (String) v);
		});
		return CommonUtil.jsonResult(1, "更新成功！");
	}

}
