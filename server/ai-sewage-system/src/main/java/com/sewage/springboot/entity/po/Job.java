package com.sewage.springboot.entity.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;


/**
 * 工单表
 * <br><br>
 * 映射数据库表 (项目根据此实体生成表)
 *
 * @author sc
 * @date 2019年9月5日
 */
public class Job {
	
		/** 主键ID自增 */
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id")
	    private Integer id;
	    
	    /** 问题标题 */
	    @Column(name = "title")
	    private String title;

	    /** 问题描述 */
	    @Column(name = "content")
	    private String content;
	    
	    /*** 
	     * 问题类别<br>
	     * 0：未知分类 (默认)
	     */ 
	    @Column(name = "job_type_id")
	    private Integer jobTypeId;
	    
	    @Transient
	    private String jobTypeName;

	    /** 联系电话（用于客服与问题产生处的人进行沟通） */
	    @Column(name = "telephone")
	    private String telephone;

	    @Column(name = "email")
	    private String email;
	    
	    /** 工单创建人 */
	    @Column(name = "creator_uid")
	    private Integer creatorUid;
	    @Transient
	    private String creator; // 用户名字
	   
	    /** 工单最终处理人 */
	    @Column(name = "processor_uid")
	    private Integer processorUid;
	    @Transient
	    private String processor; // 用户名字
	    
	    /** 工单审核人 */
	    @Column(name = "inspector_uid")
	    private Integer inspectorUid;
	    @Transient
	    private String inspector; // 用户名字

	    /** 工单状态 等价于 工单当前的进程状态 
	     * @see JobProcess#type
	     * */
	    @Column(name = "status")
	    private String status;

	    @Column(name = "create_time")
	    private Date  createTime;

	    @Column(name = "is_valid")
	    private Boolean  isValid;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Integer getJobTypeId() {
			return jobTypeId;
		}

		public void setJobTypeId(Integer jobTypeId) {
			this.jobTypeId = jobTypeId;
		}

		public String getJobTypeName() {
			return jobTypeName;
		}

		public void setJobTypeName(String jobTypeName) {
			this.jobTypeName = jobTypeName;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Integer getCreatorUid() {
			return creatorUid;
		}

		public void setCreatorUid(Integer creatorUid) {
			this.creatorUid = creatorUid;
		}

		public String getCreator() {
			return creator;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}

		public Integer getProcessorUid() {
			return processorUid;
		}

		public void setProcessorUid(Integer processorUid) {
			this.processorUid = processorUid;
		}

		public String getProcessor() {
			return processor;
		}

		public void setProcessor(String processor) {
			this.processor = processor;
		}

		public Integer getInspectorUid() {
			return inspectorUid;
		}

		public void setInspectorUid(Integer inspectorUid) {
			this.inspectorUid = inspectorUid;
		}

		public String getInspector() {
			return inspector;
		}

		public void setInspector(String inspector) {
			this.inspector = inspector;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public Boolean getIsValid() {
			return isValid;
		}

		public void setIsValid(Boolean isValid) {
			this.isValid = isValid;
		}

	    

}
