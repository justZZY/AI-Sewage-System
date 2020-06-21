package com.sewage.springboot.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 工单配置接口
 * @author sc
 *
 */
public interface JobConfigService {
	/**
	 * 根据配置名查询配置值
	 * @param names
	 * @return {name:value}
	 */
	public JSONObject queryConfig(String... names);
	
	/**
	 * 新增配置(已存在则更新配置)
	 * @param names
	 * @return
	 */
	public JSONObject setConfig(String name,String value);

	public JSONObject setConfigs(JSONObject json);
}
