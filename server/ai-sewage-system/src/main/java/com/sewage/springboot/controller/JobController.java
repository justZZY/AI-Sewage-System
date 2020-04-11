/**
 * 
 */
package com.sewage.springboot.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.entity.UserSessionInfo;
import com.sewage.springboot.service.JobService;
import com.sewage.springboot.service.JobTypeService;
import com.sewage.springboot.util.CommonUtil;
import com.sewage.springboot.util.UserInfoUtils;

/**
 * 
 * @author sc
 * @date 2019年9月10日
 *  
 */
@CrossOrigin
@RestController
@RequestMapping("/job")
public class JobController {
		
	@Autowired JobService jobService;
	@Autowired JobTypeService jobTypeService; 
	
	/** 当前登录用户创建工单 */
	@RequestMapping(value = "/add", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	public JSONObject create(@RequestBody JSONObject form) {
		return jobService.createJob(form);
	}
	
	/** 派单 */
	@RequestMapping(value = "/allocate")
	public JSONObject allocateOrder(@RequestBody JSONObject json) {
		return jobService.allocate(json);
	}
	
	
	/** 转发工单 */
	@RequestMapping(value = "/forward")
	public JSONObject forwardJobs(@RequestBody JSONObject json) {
		return jobService.forwardJobs(UserInfoUtils.getUserInfo().getUsername(), json);
	}
	
	/** 确认工单完成 */
	@RequestMapping(value = "/done")
	public JSONObject doneJob(@RequestBody JSONObject json) {
		return jobService.doneJob(UserInfoUtils.getUserInfo().getUsername(), json);
	}
	
	/** 审核工单 */
	@RequestMapping(value = "/inspect")
	public JSONObject inspectJob(@RequestBody JSONObject json) {
		return jobService.inspect(UserInfoUtils.getUserInfo().getUsername(), json);
	}
	
	/** 驳回工单 */
	@RequestMapping(value = "/reject")
	public JSONObject rejectJob(@RequestBody JSONObject json) {
		return jobService.reject(UserInfoUtils.getUserInfo().getUsername(), json);
	}
	
	/** 查询某个工单 */
	@RequestMapping(value = "/query/one")
	public JSONObject inspectJob(@RequestParam Integer jobId) {
		return jobService.queryById(jobId);
	}
	
	/**
	 * 查询所有工单
	 */
	@RequestMapping("/query/all")
	public JSONObject queryAllJobs( @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		return jobService.queryAllJobs(pageIndex,pageSize);
	}
	
	/**
	 * 查询所有待分配（待处理）的工单
	 */
	@RequestMapping("/query/waiting")
	public JSONObject queryJobsWaitingHandle( @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		return jobService.queryAllJobsWaitingForHandle(pageIndex,pageSize);
	}
	
	/**
	 * 查询所有待审核的工单
	 */
	@RequestMapping("/query/waitingspect")
	public JSONObject queryJobsWaitingInspect( @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		return jobService.queryAllJobsWaitingForCheck(pageIndex,pageSize);
	}
	
	/** 查询自己 创建的工单 */
	@RequestMapping("/query/create")
	public JSONObject queryJobsCreatedBySelf(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		return jobService.queryJobsCreatedBySelf(UserInfoUtils.getUserInfo().getUsername(),pageIndex,pageSize);
	}
	
	/** 查询自己未处理的工单 */
	@RequestMapping("/query/processing")
	public JSONObject queryJobsProcessing(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		return jobService.queryJobsProcessing(UserInfoUtils.getUserInfo().getUsername(),pageIndex,pageSize);
	}
	
	/** 查询自己已处理的工单 （未确认）*/
	@RequestMapping("/query/processed")
	public JSONObject queryJobsProcessed(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		return jobService.queryJobsProcessed(UserInfoUtils.getUserInfo().getUsername(),pageIndex,pageSize);
	}
	
	/** 查询自己已处理的工单 （已确认成功）*/
	@RequestMapping("/query/success")
	public JSONObject queryJobsProcessedSuccessful(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		return jobService.queryJobsProcessedSuccessful(UserInfoUtils.getUserInfo().getUsername(),pageIndex,pageSize);
	}
	
	/** 查询自己已处理的工单 （已确认失败）*/
	@RequestMapping("/query/fail")
	public JSONObject queryJobsProcessedFailed(@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		return jobService.queryJobsProcessedFailed(UserInfoUtils.getUserInfo().getUsername(),pageIndex,pageSize);
	}
	
	/** 根据条件查询属于用户的工单 */
	@RequestMapping("/query/search")
	public JSONObject searchJobsAboutMe(@RequestParam String keyword,@RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
		return jobService.queryJobsAboutMeByCondition( keyword, pageIndex,pageSize);
	}
	
	/** 查询工单数量 */
	@RequestMapping("/query/count")
	public JSONObject queryJobsCount(@RequestParam(required=true) String type) {
		return jobService.queryJobsCount(UserInfoUtils.getUserInfo().getUsername(),type);
	}
	
	
	
	/*------------------------------ 工单进程 ------------------------------*/
	
	/**
	 * 查询某工单的所有进程
	 */
	@RequestMapping("/jobprocess/query/list/{jobId}")
	public JSONObject queryJobsProcesseList(@PathVariable("jobId") int jobId) {
		return jobService.queryJobProcessListByJobId(jobId);
	}
	
	/*------------------------------ 工单类型 ------------------------------*/
	
	@RequestMapping(method = RequestMethod.POST, value = "/type/queryall")
	public JSONObject queryAllJobTypes() {
		return jobTypeService.queryList();
	}
	
	
	/*------------------------------ 与用户相关 ------------------------------*/
	
	/**
	 * 查询当前会话用户，用于前端工单某些逻辑判断需要
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/user/curlogin")
	public JSONObject queryLoginUser() {
		UserSessionInfo user =  UserInfoUtils.getUserInfo();
		return CommonUtil.jsonResult(1, "查询成功", user);
	}
	
	/**
	 * 获取可接单用户列表(仅 :user用户)
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/user/list")
	public JSONObject queryUserList() {
		return jobService.queryUserListAvailableForReceivingJob();
	}
	
	
}	
