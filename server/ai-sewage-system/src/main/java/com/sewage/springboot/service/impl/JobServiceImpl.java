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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sewage.springboot.dao.UserDao;
import com.sewage.springboot.entity.UserSessionInfo;
import com.sewage.springboot.entity.constant.JobConstant;
import com.sewage.springboot.entity.po.File;
import com.sewage.springboot.entity.po.Job;
import com.sewage.springboot.entity.po.JobProcess;
import com.sewage.springboot.handle.exception.response.BussinessException;
import com.sewage.springboot.mapper.impl.FileMapper;
import com.sewage.springboot.mapper.impl.JobMapper;
import com.sewage.springboot.mapper.impl.JobProcessMapper;
import com.sewage.springboot.mapper.impl.JobTypeMapper;
import com.sewage.springboot.service.FileTransferService;
import com.sewage.springboot.service.JobService;
import com.sewage.springboot.util.CommonUtil;
import com.sewage.springboot.util.UserInfoUtils;
import com.sewage.springboot.util.constants.Constants;

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
	@Autowired UserDao userDao;
	@Autowired FileMapper fileMapper;
	@Autowired FileTransferService fileTransferService;

	@Override
	public JSONObject createJob(JSONObject form) {
		if(form==null || form.getString("jobTypeName")==null)  throw new BussinessException(-1, "参数异常！");
		Job job = new Job();
		UserSessionInfo loginUser = UserInfoUtils.getUserInfo();
		job.setContent(form.getString("content"));
		job.setCreator(loginUser.getUsername());
		job.setEmail(loginUser.getMail());
		job.setTelephone(loginUser.getPhone());
		job.setJobTypeName(form.getString("jobTypeName"));
		job.setStatus(JobConstant.JOB_STATUS_CREATE);
		job.setCreateTime(new Date());
		job.setUpdateTime(new Date());
		int i = jobMapper.insertSelective(job);
		
		JobProcess jobProcess= new JobProcess(
				null, 
				job.getId(), 
				loginUser.getUsername(), 
				form.getString("content"), 
				form.getString("fileList"), 
				JobConstant.JOB_STATUS_CREATE, 
				new Date(), 
				new Date());
		int j = jobProcessMapper.insertSelective(jobProcess);
		if(i<=0 || j<=0) throw new BussinessException(0, "创建失败！");
		
		List<Integer> fileList = form.getObject("fileList", ArrayList.class);
		if(fileList!=null && !fileList.isEmpty()) fileTransferService.updateFileStatus(fileList);
		
		return CommonUtil.jsonResult(1, "创建成功！");
	}


	/**
	 * 删除工单，以及其流程（需有权限控制）
	 */
	@Override
	public JSONObject deleteJob(int jobId) {
		int i = jobMapper.deleteByPrimaryKey(jobId) ;
		JobProcess jp = new JobProcess();
		jp.setJobId(jobId);
		int j = jobProcessMapper.delete(jp);
		if(i<=0 || j<=0)  throw new BussinessException(0, "删除失败！");
		return CommonUtil.jsonResult(jobId, "删除成功！");
	}

	@Override
	public JSONObject updateJob(Job job) {
		job.setUpdateTime(new Date());
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
		PageHelper.orderBy("update_time desc");
		List<Job> list = jobMapper.select(job);
		PageInfo<Job> page = new PageInfo<Job>(list);
		return CommonUtil.jsonResult(1, "查询完成！", page);
	}


	@Override
	public JSONObject queryJobsCreatedBySelf(String user, Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setCreator(user);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryJobsProcessing(String user,Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setProcessor(user);
		job.setStatus(JobConstant.JOB_STATUS_PROCESSING);
		return queryJobs(job, pageIndex, pageSize);
	}

	
	@Override
	public JSONObject queryJobsAllProcessed(String username, Integer pageIndex, Integer pageSize) {
		if(pageIndex==null) pageIndex=PAGE_INDEX;
		if(pageSize==null) pageSize=PAGE_SIZE;
		PageHelper.startPage(pageIndex, pageSize, true);
		Example ex = new Example(Job.class);
		ex.and().andEqualTo("processor", username);
		ex.and().orEqualTo("status", JobConstant.JOB_STATUS_PROCESSED)
		.orEqualTo("status", JobConstant.JOB_STATUS_SUCCESS).orEqualTo("status", JobConstant.JOB_STATUS_FAIL);
		ex.orderBy("updateTime").desc();
		List<Job> list = jobMapper.selectByExample(ex);
		PageInfo<Job> page = new PageInfo<Job>(list);
		return CommonUtil.jsonResult(1, "查询完成！", page);
	}
	
	@Override
	public JSONObject queryJobsProcessed(String user, Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setProcessor(user);
		job.setStatus(JobConstant.JOB_STATUS_PROCESSED);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryJobsProcessedSuccessful(String user, Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setProcessor(user);
		job.setStatus(JobConstant.JOB_STATUS_SUCCESS);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryJobsProcessedFailed(String user, Integer pageIndex, Integer pageSize) {
		Job job = new Job();
		job.setProcessor(user);
		job.setStatus(JobConstant.JOB_STATUS_FAIL);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryAllJobsWaitingForHandle(Integer pageIndex, Integer pageSize) {
		if(!UserInfoUtils.getUserInfo().getIdentity().equals("admin")) throw new BussinessException(-1, "权限不足");
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_CREATE);
		return queryJobs(job, pageIndex, pageSize);
	}
	
	@Override
	public JSONObject queryAllJobsWaitingForCheck(Integer pageIndex, Integer pageSize) {
		if(!UserInfoUtils.getUserInfo().getIdentity().equals("admin")) throw new BussinessException(-1, "权限不足");
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_PROCESSED);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryAllJobsCheckSuccessful(Integer pageIndex, Integer pageSize) {
		if(!UserInfoUtils.getUserInfo().getIdentity().equals("admin")) throw new BussinessException(-1, "权限不足");
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_SUCCESS);
		return queryJobs(job, pageIndex, pageSize);
	}

	@Override
	public JSONObject queryAllJobsCheckFailed(Integer pageIndex, Integer pageSize) {
		if(!UserInfoUtils.getUserInfo().getIdentity().equals("admin")) throw new BussinessException(-1, "权限不足");
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_FAIL);
		return queryJobs(job, pageIndex, pageSize);
	}
	
	@Override
	public JSONObject queryAllJobs(Integer pageIndex, Integer pageSize) {
		if(!UserInfoUtils.getUserInfo().getIdentity().equals("admin")) throw new BussinessException(-1, "权限不足");
		return queryJobs(null, pageIndex, pageSize);
	}
	
	@Override
	public JSONObject queryJobsAboutMeByCondition(String keyword, Integer pageIndex, Integer pageSize) {
		if(pageIndex==null) pageIndex=PAGE_INDEX;
		if(pageSize==null) pageSize=PAGE_SIZE;
		String username = UserInfoUtils.getUserInfo().getUsername();
		PageHelper.startPage(pageIndex, pageSize, true);
		Example ex = new Example(Job.class);
		keyword = "%" + keyword + "%";
		if(!UserInfoUtils.getUserInfo().getIdentity().equals("admin"))
			ex.and().orEqualTo("creator", username).orEqualTo("processor", username).orEqualTo("inspector", username);
		ex.and().orLike("jobTypeName", keyword).orLike("content", keyword);
		ex.orderBy("updateTime").desc();
		List<Job> list = jobMapper.selectByExample(ex);
		PageInfo<Job> page = new PageInfo<Job>(list);
		return CommonUtil.jsonResult(1, "查询完成！", page);
	}


	@Override
	public JSONObject selectJobList(Integer pageIndex, Integer pageSize) {
		return queryJobs(null, pageIndex, pageSize);
	}

	@Override
	public JSONObject allocate(JSONObject json) {
		List<Integer> jobsIds = json.getObject("jobsIds", List.class);
		String user = json.getString("username");
		String remark = json.getString("remark");
		if(!UserInfoUtils.getUserInfo().getIdentity().equals("admin"))
			throw new BussinessException(-1, "权限不足");
		if(jobsIds==null || jobsIds.isEmpty())  throw new BussinessException(-1, "选择工单无效！");
		Job job = new Job();
		// 添加工单处理人，更新工单状态
		job.setProcessor(user);
		job.setStatus(JobConstant.JOB_STATUS_PROCESSING);
		job.setUpdateTime(new Date());
		Example example = new Example(Job.class);
		example.and().andIn("id", jobsIds).andEqualTo("status", JobConstant.JOB_STATUS_CREATE);
		int i = jobMapper.updateByExampleSelective(job, example);
		// 添加进程
		List<JobProcess> recordList = new ArrayList<JobProcess>();
		for(Integer jobId : jobsIds) {
			if(jobId==null) continue;
			JobProcess jobProcess = new JobProcess(
					null,
					jobId,
					user,
					remark,
					null,
					JobConstant.JOB_STATUS_PROCESSING,
					new Date(),
					new Date());
			recordList.add(jobProcess);
		}
		int j = jobProcessMapper.insertList(recordList);
		if(i<1 || i!=j || i!=jobsIds.size()) 
			throw new BussinessException(0, "派单失败！");
		return CommonUtil.jsonResult(1, "派单成功！", i);
	}

	@Override
	public JSONObject forwardJobs(String owner,JSONObject json) {
		List<Integer> jobsIds = json.getObject("jobsIds", List.class);
		String receiver = json.getString("receiverUsername");
		String remark = json.getString("remark");
		if(jobsIds==null ||jobsIds.size()<1 || owner==null || receiver==null) throw new BussinessException(-1, "转发失败！");
		if(owner.equals(receiver)) throw new BussinessException(-1, "禁止重复转发给自己！");
		// 查询接收人是否存在
		JSONObject queryUser = userDao.queryUserByName(receiver);
		if(queryUser==null) throw  new BussinessException(-1, "用户["+receiver+"]不存在！"); 
		if(!queryUser.getString("identity").equals("user") || !queryUser.getString("delete_status").equals("0")) throw  new BussinessException(-1, "只可以转发给正常普通用户！"); 
		// 1. 更新工单当前处理人
		Example example = new Example(Job.class);
		example.and().andIn("id", jobsIds)
		.andEqualTo("processor", owner)
		.andEqualTo("status", JobConstant.JOB_STATUS_PROCESSING);
		Job job = new Job();
		job.setProcessor(receiver);
		job.setUpdateTime(new Date());
		int i = jobMapper.updateByExampleSelective(job, example);
		if(i<1 || i!=jobsIds.size()) throw new BussinessException(0, "转发失败");
		// 2. 添加转发进程 和 新的处理进程
		List<JobProcess> processList = new ArrayList<JobProcess>();
		for(Integer jobId:jobsIds) {
			if(jobId==null) continue;
			// 转发进程
			JobProcess jobForwardProcess = new JobProcess(
					null, jobId, owner, remark, null, JobConstant.JOB_STATUS_TRANSFER, new Date(), new Date());
			// 处理进程
			JobProcess jobProcessingProcess = new JobProcess(
					null, jobId, receiver, null, null, JobConstant.JOB_STATUS_PROCESSING, new Date(), new Date());
			processList.add(jobForwardProcess);
			processList.add(jobProcessingProcess);
		}
		// 插入所有工单进程
		int j = jobProcessMapper.insertList(processList) ;
		if(j!=processList.size() || j<1)
			throw new BussinessException(0, "转发失败");
		return CommonUtil.jsonResult(1, "转发成功");
	}

	@Override
	public JSONObject doneJob(String processor, JSONObject json) {
		Integer jobId = json.getInteger("jobId");
		String file = json.getString("fileList");
		String content = json.getString("content");
		if(jobId==null) throw new BussinessException(-1, "参数异常！");
		/* 1.更新此人(userId)的工单(jobId)状态
		 * 直接通过更新语句匹配工单id和用户id，既可以更新状态，又可以验证工单是否属于此人
		 */
		Example example = new Example(Job.class);
		example.and().andEqualTo("id", jobId)  
		.andEqualTo("processor", processor)
		.andEqualTo("status", JobConstant.JOB_STATUS_PROCESSING);
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_PROCESSED);
		job.setUpdateTime(new Date());
		int i = jobMapper.updateByExampleSelective(job, example);
		if(i<1) throw new BussinessException(0, "操作失败！");
		/*
		 * 2.添加新的进程
		 */
		JobProcess jobProcess = new JobProcess(
				null, jobId, processor, content, file, JobConstant.JOB_STATUS_PROCESSED,  new Date(),  new Date());
		int j = jobProcessMapper.insertSelective(jobProcess);
		if(j<1) throw new BussinessException(0, "操作失败！");
		
		/*
		 * 3.更新文件状态
		 */
		List<Integer> fileList = json.getObject("fileList", ArrayList.class);
		if(fileList!=null && !fileList.isEmpty()) fileTransferService.updateFileStatus(fileList);
		return CommonUtil.jsonResult(1, "确认成功！");
	}

	// 需要权限
	@Override
	public JSONObject inspect(String inspector, JSONObject json) {
		if(!UserInfoUtils.getUserInfo().getIdentity().equals("admin"))
			throw new BussinessException(-1, "权限不足");
		Integer jobId = json.getInteger("jobId");
		String file = json.getString("fileList");
		String content = json.getString("content");
		String type = json.getString("type");
		if(jobId==null) throw new BussinessException(-1, "操作失败，原因：工单无效");
		if(!JobConstant.JOB_STATUS_SUCCESS.equals(type) && !JobConstant.JOB_STATUS_FAIL.equals(type))
			throw new BussinessException(-1, "参数异常");
		/* 1.更新工单状态，添加审核人
		 */
		Example example = new Example(Job.class);
		example.and().andEqualTo("id", jobId)
		.andEqualTo("status",  JobConstant.JOB_STATUS_PROCESSED);
		Job job = new Job();
		job.setStatus(type);
		job.setInspector(inspector);
		job.setUpdateTime(new Date());
		int i = jobMapper.updateByExampleSelective(job, example);
		if(i<1) throw new BussinessException(-1, "操作失败");
		
		/* 2.添加新的进程
		 */
		JobProcess jobProcess = new JobProcess(
				null, jobId, inspector, content, file, type, new Date(), new Date());
		int j = jobProcessMapper.insertSelective(jobProcess);
		if(j<1) throw new BussinessException(-1, "操作失败");
		
		/*
		 * 3.更新文件状态
		 */
		List<Integer> fileList = json.getObject("fileList", ArrayList.class);
		if(fileList!=null && !fileList.isEmpty()) fileTransferService.updateFileStatus(fileList);
		return CommonUtil.jsonResult(1, "确认成功");
	}
	
	// 需要权限
	@Override
	public JSONObject reject(String inspector, JSONObject json) {
		if(!UserInfoUtils.getUserInfo().getIdentity().equals("admin"))
			throw new BussinessException(-1, "权限不足");
		Integer jobId = json.getInteger("jobId");
		String file = json.getString("fileList");
		String content = json.getString("content");
		String type = json.getString("type");
		if(jobId==null) throw new BussinessException(-1, "操作失败，原因：工单无效");
		/* 1.更新工单状态，添加审核人
		 */
		Example example = new Example(Job.class);
		example.and().andEqualTo("id", jobId)
		.andEqualTo("status",  JobConstant.JOB_STATUS_PROCESSED);
		Job job = new Job();
		job.setStatus(JobConstant.JOB_STATUS_PROCESSING);
		job.setInspector(inspector);
		job.setUpdateTime(new Date());
		job.setId(jobId); 
		int i = jobMapper.updateByPrimaryKeySelective(job);
		if(i<1) throw new BussinessException(-1, "操作失败");
		
		/* 2.添加新的进程：驳回和处理中
		 */
		JobProcess jobProcess1 = new JobProcess(
				null, jobId, inspector, content, file, JobConstant.JOB_STATUS_REJECT, new Date(), new Date());
		int j = jobProcessMapper.insertSelective(jobProcess1);
		if(j<1) throw new BussinessException(-1, "操作失败");
		
		JobProcess jobProcess2 = new JobProcess(
				null, jobId, jobMapper.selectByPrimaryKey(jobId).getProcessor(), null, null, JobConstant.JOB_STATUS_PROCESSING, new Date(), new Date());
		int k = jobProcessMapper.insertSelective(jobProcess2);
		if(k<1) throw new BussinessException(-1, "操作失败");
		
		/*
		 * 3.更新文件状态
		 */
		List<Integer> fileList = json.getObject("fileList", ArrayList.class);
		if(fileList!=null && !fileList.isEmpty()) fileTransferService.updateFileStatus(fileList);
		return CommonUtil.jsonResult(1, "已驳回");
	}
	
	@Override
	public JSONObject queryById(Integer jobId) {
		Job job = jobMapper.selectByPrimaryKey(jobId);
		return CommonUtil.jsonResult(1, "查询结束", job);
	}
	
	@Override
	public JSONObject queryJobProcessListByJobId(int jobId) {
		Example example = new Example(JobProcess.class);
		example.and().andEqualTo("jobId", jobId);
		example.orderBy("createTime");
		List<JobProcess> list = jobProcessMapper.selectByExample(example);
		return CommonUtil.jsonResult(1, "查询结束", list);
	}

	@Override
	public JSONObject queryUserListAvailableForReceivingJob() {
		List<JSONObject> list = userDao.listUser(null);
		JSONArray userlist = new JSONArray();
		list.forEach(json->{
			if (json.getInteger("delete_status").intValue() == 0  && json.getString("identity").equals("user") )
				userlist.add(json.get("username"));
		});
		return CommonUtil.jsonResult(1, "查询成功", userlist);
	}

	@Override
	public JSONObject queryJobsCount(String username, String type) {
		Example ex = new Example(Job.class);
		if(type.equals("waiting")) { // 所有待领取的工单
			ex.and().andEqualTo("status", JobConstant.JOB_STATUS_CREATE);
			
		}else if(type.equals("create")) { // 我创建的工单
			ex.and().andEqualTo("creator", username);
			
		}else if(type.equals("processing")) { // 我处理中的工单
			ex.and().andEqualTo("status", JobConstant.JOB_STATUS_PROCESSING);
			ex.and().andEqualTo("processor", username);
			
		}else if(type.equals("processed")) { // 我处理完的工单(未确认/待审核)
			ex.and().andEqualTo("status", JobConstant.JOB_STATUS_PROCESSED);
			ex.and().andEqualTo("processor", username);
			
		}else if(type.equals("success")) { // 我处理完成的工单(审核通过)
			ex.and().andEqualTo("status", JobConstant.JOB_STATUS_SUCCESS);
			ex.and().andEqualTo("processor", username);
			
		}else if(type.equals("fail")) { // 我处理完成的工单(审核驳回)
			ex.and().andEqualTo("status", JobConstant.JOB_STATUS_FAIL);
			ex.and().andEqualTo("processor", username);
			
		}else if(type.equals("waitingspect")) { // 所有待审核的工单
			ex.and().andEqualTo("status", JobConstant.JOB_STATUS_PROCESSED);
		}
		
		int i = jobMapper.selectCountByExample(ex);
		return CommonUtil.jsonResult(1, "查询成功", i);
	}


	
	
}
