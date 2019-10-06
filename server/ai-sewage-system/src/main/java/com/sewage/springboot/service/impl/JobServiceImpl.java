/**
 * 
 */
package com.sewage.springboot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sewage.springboot.entity.constant.JobConstant;
import com.sewage.springboot.entity.po.Job;
import com.sewage.springboot.entity.po.JobProcess;
import com.sewage.springboot.entity.po.JobType;
import com.sewage.springboot.handle.exception.response.BussinessException;
import com.sewage.springboot.mapper.impl.JobMapper;
import com.sewage.springboot.mapper.impl.JobProcessMapper;
import com.sewage.springboot.mapper.impl.JobTypeMapper;
import com.sewage.springboot.service.JobService;
import com.sewage.springboot.util.CommonUtil;
import com.sewage.springboot.util.constants.Constants;
import com.sewage.springboot.util.constants.ErrorEnum;

import tk.mybatis.mapper.entity.Example;

/**
 * 
 * @author sc
 * @date 2019年9月10日
 *  
 */
@Service
@Transactional
public class JobServiceImpl implements JobService {
	

	
	@Autowired JobMapper jobMapper;
	@Autowired JobProcessMapper jobProcessMapper ;
	@Autowired JobTypeMapper jobTypeMapper ;
	

	@Override
	public JSONObject createJob(int userId,JSONObject form) {
		Job job = new Job();
		job.setCreatorUid(userId);
		job.setTitle(form.getString("title"));
		job.setJobTypeId(Integer.parseInt(form.getString("jobTypeId")));
		job.setContent(form.getString("content"));
		job.setEmail(form.getString("email"));
		job.setTelephone(form.getString("telephone"));
		job.setStatus(JobConstant.JOB_STATUS_CREATE);
		job.setCreateTime(new Date()); 
		jobMapper.insertSelective(job);
		
		JobProcess jobProcess= new JobProcess();
		jobProcess.setCreateTime(new Date());
		jobProcess.setUserId(userId);
		jobProcess.setJobId(job.getId());
		jobProcess.setType(JobConstant.JOB_STATUS_CREATE); // 进程类别，工单状态
		jobProcessMapper.insertSelective(jobProcess);
		return CommonUtil.jsonResult(Constants.SUCCESS_CODE, "创建成功");
	}

	@Override
	public JSONObject deleteJob(int jobId) {
		int i = jobMapper.deleteByPrimaryKey(jobId) ;
		return CommonUtil.successJson(i);
	}

	@Override
	public JSONObject updateJob(Job job) {
		int i = jobMapper.updateByPrimaryKeySelective(job);
		return CommonUtil.successJson(i);
 
	}
 
	
	/**
	 * 根据精准匹配分页查询工单列表
	 * @param job 非空字段匹配查询 
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 * @author：sc
	 * @data： 2019年9月24日
	 */
	public JSONObject queryJobs(Job job, Integer pageIndex, Integer pageSize) {
		if(pageIndex==null) pageIndex=PAGE_INDEX;
		if(pageSize==null) pageSize=PAGE_SIZE;
		PageHelper.startPage(pageIndex, pageSize, true);
		List<Job> list = jobMapper.selectJobList(job);
		PageInfo page = new PageInfo(list);
		return CommonUtil.jsonResult(1, "查询完成", page);
	}

	@Override
	public JSONObject queryJobsWaitinghandle(Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_CREATE);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryJobsByMyself(int userId, Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setCreatorUid(userId);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryJobsProcessing(int userId, Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setProcessorUid(userId);
		job.setStatus(JobConstant.JOB_STATUS_PROCESSING);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryJobsProcessed(int userId, Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setProcessorUid(userId);
		job.setStatus(JobConstant.JOB_STATUS_PROCESSED);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryJobsProcessedSuccessful(int userId, Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setProcessorUid(userId);
		job.setStatus(JobConstant.JOB_STATUS_SUCCESS);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryJobsProcessedFailed(int userId, Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setProcessorUid(userId);
		job.setStatus(JobConstant.JOB_STATUS_FAIL);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryAllJobsWaitingForCheck(Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_PROCESSED);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryAllJobsCheckSuccessful(Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_SUCCESS);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryAllJobsCheckFailed(Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_FAIL);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject selectJobList(Integer pageIndex, Integer pageSize) {
		return queryJobs(null, pageIndex, pageSize);
	}

	@Override
	public JSONObject grabJobs(Integer uid,List<Integer> jobsIds) {
		if(jobsIds==null || jobsIds.isEmpty()) 
			throw new BussinessException(-1, "选择工单无效");
		Job job = new Job();
		// 添加工单处理人，更新工单状态
		job.setProcessorUid(uid);
		job.setStatus(JobConstant.JOB_STATUS_PROCESSING);
		Example example = new Example(Job.class);
		example.and().andIn("id", jobsIds);
		int i = jobMapper.updateByExampleSelective(job, example);
		// 添加进程
		List<JobProcess> recordList = new ArrayList<JobProcess>();
		for(Integer id : jobsIds) {
			JobProcess jobProcess = new JobProcess();
			jobProcess.setJobId(id);
			jobProcess.setCreateTime(new Date());
			jobProcess.setType(JobConstant.JOB_STATUS_PROCESSING);
			jobProcess.setUserId(uid);
			jobProcess.setIsValid(true);
			recordList.add(jobProcess);
		}
		int j = jobProcessMapper.insertList(recordList);
		if(i!=j) 
			throw new BussinessException(-1, "操作失败[工单状态与进程不同步]");
		return CommonUtil.jsonResult(1, "领取成功", i);
	}

	@Override
	public JSONObject forwardJobs(Integer ownerUid, List<Integer> jobsIds, Integer receiverUid) {
		if(jobsIds==null ||jobsIds.size()<1 || ownerUid==null || receiverUid==null) throw new BussinessException(-1, "操作失败");
		// 这里需要判断用户是否存在
		// 1. 更新工单当前处理人
		Example example = new Example(Job.class);
		example.and().andIn("id", jobsIds)
		.andEqualTo("processorUid", ownerUid)
		.andEqualTo("status", JobConstant.JOB_STATUS_PROCESSING);
		Job job = new Job();
		job.setProcessorUid(receiverUid);
		int i = jobMapper.updateByExampleSelective(job, example);
		if(i<1 || i!=jobsIds.size()) throw new BussinessException(-1, "操作失败");
		// 2. 添加转发进程 和 新的处理进程
		List<JobProcess> processList = new ArrayList<JobProcess>();
		for(int id:jobsIds) {
			// 转发进程
			JobProcess jobProcess1 = new JobProcess();
			jobProcess1.setIsValid(true);
			jobProcess1.setCreateTime(new Date());
			jobProcess1.setUserId(ownerUid); // 发起转发的用户
			jobProcess1.setJobId(id);
			jobProcess1.setType(JobConstant.JOB_STATUS_TRANSFER); // 进程类别，工单状态
			// 处理进程
			JobProcess jobProcess2 = new JobProcess();
			jobProcess2.setIsValid(true);
			jobProcess2.setCreateTime(new Date());
			jobProcess2.setUserId(receiverUid); // 接收工单的用户
			jobProcess2.setJobId(id);
			jobProcess2.setType(JobConstant.JOB_STATUS_PROCESSING); // 进程类别，工单状态
			processList.add(jobProcess1);
			processList.add(jobProcess2);
		}
		// 插入所有工单进程
		int j = jobProcessMapper.insertList(processList) ;
		if(j!=processList.size())
			throw new BussinessException(-1, "操作失败");
		return CommonUtil.jsonResult(Constants.SUCCESS_CODE, "转发成功");
	}

	@Override
	public JSONObject doneJob(int userId, JSONObject json) {
		int jobId = json.getInteger("jobId");
		String file = json.getString("file");
		String content = json.getString("content");
		/* 1.更新此人(userId)的工单(jobId)状态
		 * 直接通过更新语句匹配工单id和用户id，既可以更新状态，又可以验证工单是否属于此人
		 */
		Example example = new Example(Job.class);
		example.and().andEqualTo("id", jobId)  
		.andEqualTo("processorUid", userId)
		.andEqualTo("status", JobConstant.JOB_STATUS_PROCESSING);
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_PROCESSED);
		int i = jobMapper.updateByExampleSelective(job, example);
		if(i<1) throw new BussinessException(-1, "操作失败");
		/*
		 * 2.添加新的进程
		 */
		JobProcess jobProcess = new JobProcess();
		jobProcess.setCreateTime(new Date());
		jobProcess.setFile(file);
		jobProcess.setIsValid(true);
		jobProcess.setJobId(jobId);
		jobProcess.setUserId(userId);
		jobProcess.setRemark(content);
		jobProcess.setType(JobConstant.JOB_STATUS_PROCESSED);
		int j = jobProcessMapper.insertSelective(jobProcess);
		if(j<1) throw new BussinessException(-1, "操作失败");
		return CommonUtil.jsonResult(Constants.SUCCESS_CODE, "确认成功");
	}

	// 需要验证权限
	@Override
	public JSONObject inspect(int userId, JSONObject json) {
		int jobId = json.getInteger("jobId");
		String file = json.getString("file");
		String content = json.getString("content");
		String type = json.getString("type");
		if(!JobConstant.JOB_STATUS_SUCCESS.equals(type) && !JobConstant.JOB_STATUS_FAIL.equals(type))
			throw new BussinessException(-1, "参数异常");
		/* 1.更新工单状态，添加审核人
		 */
		Example example = new Example(Job.class);
		example.and().andEqualTo("id", jobId)
		.andEqualTo("status",  JobConstant.JOB_STATUS_PROCESSED);
		Job job = new Job();
		job.setStatus(type);
		job.setInspectorUid(userId);
		int i = jobMapper.updateByExampleSelective(job, example);
		if(i<1) throw new BussinessException(-1, "操作失败");
		
		/* 2.添加新的进程
		 */
		JobProcess jobProcess = new JobProcess();
		jobProcess.setCreateTime(new Date());
		jobProcess.setFile(file);
		jobProcess.setIsValid(true);
		jobProcess.setJobId(jobId);
		jobProcess.setUserId(userId);
		jobProcess.setRemark(content);
		jobProcess.setType(type);
		int j = jobProcessMapper.insertSelective(jobProcess);
		if(j<1) throw new BussinessException(-1, "操作失败");
		return CommonUtil.jsonResult(Constants.SUCCESS_CODE, "确认成功");
	}

	@Override
	public JSONObject queryJobAboutMe(int userId, int jobId) {
		Example example = new Example(Job.class);
		example.and().andEqualTo("id", jobId);
		example.and()
		.orEqualTo("creatorUid", userId)
		.orEqualTo("processorUid", userId)
		.orEqualTo("inspectorUid", userId);
		Job job = jobMapper.selectOneByExample(example);
		if(job==null) throw new BussinessException(-1, "查询无果");
		int jobtyte = job.getJobTypeId();
		JobType jobType = jobTypeMapper.selectByPrimaryKey(jobtyte);
		String name = jobType.getJobTypeName();
		job.setJobTypeName(name);
		return new CommonUtil().jsonResult(1, "成功", job);
	}

	@Override
	public JSONObject queryJobsWaitingInspect(Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_PROCESSED);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryJobsAboutMeByCondition(int userId, JSONObject json) {
		// TODO Auto-generated method stub
		return null;
	}
}
