package com.sewage.springboot.entity.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * 工单完成进度表
 * <br><br>
 *
 * @author sc
 * @date 2019年9月5日
 */
public class JobProcess {
	
		/** 主键ID自增 */
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id" )
	    private Integer id;
	    
	    /** 所属工单 */
	    @Column(name = "job_id" )
	    private Integer jobId;

	    /** 当前进程创建/处理人 */
	    @Column(name = "username" )
	    private String username;
	    
	    /** 备注，每个进程状态操作时提交的备注信息 */
	    @Column(name = "remark" )
	    private String remark;
	    
	    /** 备注，每个进程状态操作时提交的附件*/
	    @Column(name = "file"  )
	    private String file;
	    
	    /** 工单当前处理进程状态 
	     *  <br><br>
	   * 状态码：     操作 --> 				产生的状态名（状态解释）<br>
	   * 
	     *  1:  创建 --> 				待受理<br> （任意用户创建工单）
	     *  2:  分配/领取 --> 			处理中<br> （客服领取工单）
	     *  3:  转发 --> 				进程1：转发中<br> （转发产生一个转发的进程，记录转发人。然后又产生一个处理中的进程，记录第二个处理人，相当于进入到了状态2）
	     *  * 转发--> 				进程2：处理中<br>  
	     *  4:  客服处理完成 --> 		待检测人员确认问题已解决<br> （客服处理后，提交处理证明，点击完成。然后等待控制中心确认结果）
	     *  5：     检测人员确认问题已解决 --> 已完成 （控制中心确认结果处理OK）
	     *  6：     检测人员确认问题未解决 --> 已驳回|处理失败 （控制中心确认问题未解决）
	     */
	    @Column(name = "type")
	    private String type;
	    
	    /** 创建时间 */
	    @Column(name = "create_time")
	    private Date  createTime;

	    @Column(name = "update_time")
	    private Date  updateTime;

	    public JobProcess() {
	    	super();
	    }
	    
		public JobProcess(Integer id, Integer jobId, String username, String remark, String file, String type,
				Date createTime, Date updateTime) {
			super();
			this.id = id;
			this.jobId = jobId;
			this.username = username;
			this.remark = remark;
			this.file = file;
			this.type = type;
			this.createTime = createTime;
			this.updateTime = updateTime;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getJobId() {
			return jobId;
		}

		public void setJobId(Integer jobId) {
			this.jobId = jobId;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public Date getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}
 
	    
	    

}
