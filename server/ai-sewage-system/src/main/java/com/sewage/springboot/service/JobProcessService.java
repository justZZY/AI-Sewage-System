package com.sewage.springboot.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.entity.po.Job;
import com.sewage.springboot.entity.po.JobProcess;

/**
 * 工单处理进程业务接口
 * 
 * @author sc
 * @date 2019年9月10日
 *
 */
public interface JobProcessService {
	
	/**
	 * 查询工单的所有处理进程
	 * 
	 * @author sc
	 * @date 2019年9月10日
	 *
	 */
	public JSONObject queryJobProcessByJobId(int jobId);
	
	 
	
	public JSONObject deleteById(int jobProcessId);
	
	public JSONObject updateJobProcess(JobProcess jobProcess);
	
	/** 领取工单/分配工单
	 * <br><br>
	 * userId 接单人   <br>
	 * content 接单人提交的备注说明    
	 * 
	 */
	public JSONObject grabOneJob(int userId, int jobId, String content);
	
	/**
	 * 转发工单
	 * 
	 * @author sc
	 * @date 2019年9月10日
	 *
	 */
	public JSONObject transferOrder(int fromUser, int jobId, int toUser, String content);
	
	/**
	 * 接单人完成工单
	 * 
	 * @author sc
	 * @date 2019年9月10日
	 *
	 */
	public JSONObject transferOrder(int userId, int jobId, String content);
	
	/**
	 * 监控中心审核，确认工单处理结果
	 */
	public JSONObject inspectOrder(int userId, int jobId, String content, boolean isSuccessful);
	
}
