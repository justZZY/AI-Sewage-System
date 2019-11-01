package com.sewage.springboot.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.entity.po.Job;

/**
 * 工单业务接口
 * 
 * @author sc
 * @date 2019年9月10日
 *
 */
public interface JobService {
	
	public static final int PAGE_INDEX = 1; 
	public static final int PAGE_SIZE  = 20;
	
	/** 查询已创建，待领取/分配的工单 
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 */
	public JSONObject queryJobsWaitinghandle(Integer pageIndex, Integer pageSize);
	
	/** 查询用户自己创建的工单  
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 */
	public JSONObject queryJobsByMyself(int userId,Integer pageIndex, Integer pageSize );
	
	/** 查询用户处理中的工单 
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 */
	public JSONObject queryJobsProcessing(int userId,Integer pageIndex, Integer pageSize);
	
	/** 查询用户处理完成的订单（待确认） 
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 */
	public JSONObject queryJobsProcessed(int userId,Integer pageIndex, Integer pageSize);
	
	/** 查询用户处理完成的订单（处理成功） 
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 */
	public JSONObject queryJobsProcessedSuccessful(int userId,Integer pageIndex, Integer pageSize);
	
	/** 查询用户处理完成的订单（处理失败） 
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 */
	public JSONObject queryJobsProcessedFailed(int userId,Integer pageIndex, Integer pageSize);
	
	
	/** 查询所有已处理，待审核的工单 
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 */
	public JSONObject queryAllJobsWaitingForCheck(Integer pageIndex, Integer pageSize);
	
	/** 查询所有已审核通过的工单（确认处理完毕） 
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 */
	public JSONObject queryAllJobsCheckSuccessful(Integer pageIndex, Integer pageSize);
	
	/** 查询所有已审核未通过的工单（确认处理失败） 
	 * @param pageIndex 查询当前页，默认为{@link JobService#PAGE_INDEX}
	 * @param pageSize 数据条数，默认{@link JobService#PAGE_SIZE}
	 */
	public JSONObject queryAllJobsCheckFailed(Integer pageIndex, Integer pageSize);
	
	
	/** 创建一个工单 */
	public JSONObject createJob(int userId,JSONObject form);
	
	public JSONObject deleteJob(int jobId);
	
	public JSONObject updateJob(Job job);

	public JSONObject selectJobList(Integer pageIndex, Integer pageSize);
	
	/** 抢单 */
	public JSONObject grabJobs(Integer uid, List<Integer> jobsIds);
	
	/**
	 *  转发工单
	 *  @param ownerUid 工单所属人
	 *  @param receiverUid 工单接收人
	 */
	public JSONObject forwardJobs(Integer ownerUid, List<Integer> jobsIds, Integer receiverUid);

	/**
	 * 完成工单
	 *
	 * @author：sc
	 * @data： 2019年9月25日
	 */
	public JSONObject doneJob(int userId,JSONObject json);

	/**
	 * 审核工单
	 * @param userId 审核人
	 * @param pass 对处理结果验证通过
	 * @author：sc
	 * @data： 2019年9月25日
	 */
	public JSONObject inspect(int userId, JSONObject json);

	/**
	 * 通过ID查询属于我的工单，我必须是创建人/处理人/审核人
	 *
	 * @author：sc
	 * @data： 2019年9月28日
	 */
	public JSONObject queryJobAboutMe(int userId, int jobId);

	public JSONObject queryJobsWaitingInspect(Integer pageIndex, Integer pageSize);

	/** 按条件查询与我相关的工单 */
	public JSONObject queryJobsAboutMeByCondition(int userId, JSONObject json);
}
