/**
 * 
 */
package com.sewage.springboot.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.entity.constant.JobConstant;
import com.sewage.springboot.entity.po.Job;
import com.sewage.springboot.entity.po.JobProcess;
import com.sewage.springboot.handle.exception.response.BussinessException;
import com.sewage.springboot.mapper.impl.JobMapper;
import com.sewage.springboot.mapper.impl.JobProcessMapper;
import com.sewage.springboot.service.JobProcessService;
import com.sewage.springboot.util.CommonUtil;

import tk.mybatis.mapper.entity.Example;


/**
 * 
 * @author sc
 * @date 2019年9月10日
 *  
 */
@Service
@Transactional
public class JobProcessServiceImpl implements JobProcessService {

	@Autowired JobMapper jobMapper;
	@Autowired JobProcessMapper jobProcessMapper;
	
	/**
	 * 查询某工单的所有进程
	 */
	@Override
	public JSONObject queryJobProcessByJobId(int jobId) {
		Example example = new Example(JobProcess.class);
		example.and().andEqualTo("jobId", jobId);
		example.orderBy("createTime");
		List<JobProcess> list = jobProcessMapper.selectByExample(example);
		return CommonUtil.jsonResult(1, "查询结束", list);
	}

	public JSONObject grabOneJob(int userId, int jobId, String content) {
		// 1.1更新工单状态
		Example example = new Example(Job.class);
		example.and().andEqualTo("id", jobId)
		.andEqualTo("status", JobConstant.JOB_STATUS_CREATE);
		Job job = new Job();
		job.setProcessorUid(userId);
		job.setStatus(JobConstant.JOB_STATUS_PROCESSING);
		int i = jobMapper.updateByExampleSelective(job, example);
		if(i<1) throw new BussinessException(-1, "操作失败");

		// 2.1创建受理中进程
		JobProcess jobProcess = new JobProcess();
		jobProcess.setUserId(userId);
		jobProcess.setCreateTime(new Date());
		jobProcess.setJobId(jobId);
		jobProcess.setType(JobConstant.JOB_STATUS_PROCESSING);
		jobProcess.setRemark(content);
		i = jobProcessMapper.insertSelective(jobProcess);
		if(i<1) throw new BussinessException(-1, "操作失败");
		return CommonUtil.successJson();
	}
	
	
	 

	@Override
	public JSONObject deleteById(int jobProcessId) {
		int i = jobProcessMapper.deleteByPrimaryKey(jobProcessId);
		if(i>0) return CommonUtil.jsonResult(1, "删除成功", i);
		else    return CommonUtil.jsonResult(0, "删除失败", i);
	}

	@Override
	public JSONObject updateJobProcess(JobProcess jobProcess) {
		int i = jobProcessMapper.updateByPrimaryKeySelective(jobProcess);
		if(i>0) return CommonUtil.jsonResult(1, "更新成功", i);
		else    return CommonUtil.jsonResult(0, "更新失败", i);
	}

	@Override
	public JSONObject transferOrder(int fromUser, int jobId, int toUser, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject transferOrder(int userId, int jobId, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject inspectOrder(int userId, int jobId, String content, boolean isSuccessful) {
		// TODO Auto-generated method stub
		return null;
	}

	 

}
