/**
 * 
 */
package com.sewage.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.entity.po.Job;
import com.sewage.springboot.entity.po.JobProcess;
import com.sewage.springboot.mapper.impl.JobTypeMapper;
import com.sewage.springboot.service.JobProcessService;
import com.sewage.springboot.service.JobService;
import com.sewage.springboot.service.impl.JobTypeServiceImpl;

/**
 * 
 * @author sc
 * @date 2019年9月10日
 *  
 */
@RestController
@RequestMapping("/job")
public class JobController {
		
	@Autowired JobService jobService;
	@Autowired JobTypeServiceImpl jobTypeServiceImpl; 
	@Autowired JobProcessService jobProcessService;
	
	/** 创建工单 */
	@RequestMapping(value = "/add", /*method = RequestMethod.POST,*/ produces = "application/json;charset=UTF-8")
	public JSONObject create(@RequestBody JSONObject form) {
		// 1.查询当前登录用户
		int userId = 1;
		form = form.getJSONObject("form");
		return jobService.createJob(userId, form);
	}
	
	/** 领取工单 */
	@RequestMapping(value = "/grab")
	public JSONObject grabOrder(@RequestBody(required = false) JSONObject json) {
		// 1.查询当前登录用户
		List list = json.getObject("jobsIds", List.class);
		int userId = 1;
		return jobService.grabJobs(userId, list);
	}
	
	
	/** 转发工单 */
	@RequestMapping(value = "/forward")
	public JSONObject forwardJobs(@RequestBody(required = false) JSONObject json) {
		// 1.查询当前登录用户
		int userId = 1;
		List list = json.getObject("jobsIds", List.class);
		int receiverUid = json.getInteger("receiverUid");
		return jobService.forwardJobs(userId, list, receiverUid);
	}
	
	/** 确认工单完成 */
	@RequestMapping(value = "/done")
	public JSONObject doneJob(@RequestBody(required = false) JSONObject json) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobService.doneJob(userId, json);
	}
	
	/** 审核工单 */
	@RequestMapping(value = "/inspect")
	public JSONObject inspectJob(@RequestBody(required = false) JSONObject json) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobService.inspect(userId, json);
	}
	
	/**
	 * 查询所有待领取（待处理）的工单
	 */
	@RequestMapping("/query/waiting")
	public JSONObject queryJobsWaitingHandle( @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		int userId = 1;
		return jobService.queryJobsWaitinghandle(pageIndex,pageSize);
	}
	
	/**
	 * 查询所有待审核的工单
	 */
	@RequestMapping("/query/waitinspect")
	public JSONObject queryJobsWaitingInspect( @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		int userId = 1;
		return jobService.queryJobsWaitingInspect(pageIndex,pageSize);
	}
	
	/** 查询自己 创建的工单 */
	@RequestMapping("/query/create")
	public JSONObject queryJobsCreatedBySelf(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobService.queryJobsByMyself(userId,pageIndex,pageSize);
	}
	
	/** 查询自己未处理的工单 */
	@RequestMapping("/query/processing")
	public JSONObject queryJobsProcessing(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobService.queryJobsProcessing(userId,pageIndex,pageSize);
	}
	
	/** 查询自己已处理的工单 （未确认）*/
	@RequestMapping("/query/processed")
	public JSONObject queryJobsProcessed(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobService.queryJobsProcessed(userId,pageIndex,pageSize);
	}
	
	/** 查询自己已处理的工单 （已确认成功）*/
	@RequestMapping("/query/success")
	public JSONObject queryJobsProcessedSuccessful(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobService.queryJobsProcessedSuccessful(userId,pageIndex,pageSize);
	}
	
	/** 查询自己已处理的工单 （已确认失败）*/
	@RequestMapping("/query/fail")
	public JSONObject queryJobsProcessedFailed(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobService.queryJobsProcessedFailed(userId,pageIndex,pageSize);
	}
	
	@RequestMapping("/query/one")
	public JSONObject queryOneJobAbountMeById(@RequestParam int jobId) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobService.queryJobAboutMe(userId, jobId);
	}
	
	/** 根据条件查询属于用户的工单 */
	@RequestMapping("/query/search")
	public JSONObject searchJobsAboutMe(@RequestBody(required = false) JSONObject json) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobService.queryJobsAboutMeByCondition(userId, json);
	}
	
	/*------------------------------ 工单进程 ------------------------------*/
	/**
	 * 查询某工单的所有进程
	 */
	@RequestMapping("/jobprocess/query/list/{jobId}")
	public JSONObject queryJobsProcesseList(@PathVariable("jobId") int jobId) {
		// 1.查询当前登录用户
		int userId = 1;
		return jobProcessService.queryJobProcessByJobId(jobId);
	}
	
	/*------------------------------ 工单类型 ------------------------------*/
	
	@RequestMapping(method = RequestMethod.POST, value = "/type/queryall" ,produces = "application/json;charset=UTF-8")
	public JSONObject queryAllJobTypes() {
		return jobTypeServiceImpl.queryList();
	}
}	
