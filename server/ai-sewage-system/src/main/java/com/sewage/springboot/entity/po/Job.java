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
	    
		@Column(name = "job_type_name")
	    private String jobTypeName;
		 
	    @Column(name = "content")
	    private String content;
	    
	    @Column(name = "telephone")
	    private String telephone;
	    
	    @Deprecated
	    @Column(name = "email")
	    private String email;
	    
	    @Column(name = "creator")
	    private String creator;

	    @Column(name = "processor")
	    private String processor;

	    @Column(name = "inspector")
	    private String inspector;

	    @Column(name = "status")
	    private String status;

	    @Column(name = "create_time")
	    private Date createTime;

	    @Column(name = "update_time")
	    private Date updateTime;
	    
	    
	    // 2020/06/15添加
	    @Column(name = "site")
	    private String site;
	    
	    @Column(name = "site_addr")
	    private String siteAddr;
	    
	    @Column(name = "expected_time")
	    private Integer expectedTime;
	    
	    @Column(name = "severity")
	    private String severity;
	    
	    @Column(name = "priority")
	    private String priority;
	    
	    public Job() {
	    	super();
	    }
		public Job(Integer id, String jobTypeName, String content, String telephone, String email, String creator,
				String processor, String inspector, String status, Date createTime, Date updateTime) {
			super();
			this.id = id;
			this.jobTypeName = jobTypeName;
			this.content = content;
			this.telephone = telephone;
			this.email = email;
			this.creator = creator;
			this.processor = processor;
			this.inspector = inspector;
			this.status = status;
			this.createTime = createTime;
			this.updateTime = updateTime;
		}
		
		public Job(Integer id, String jobTypeName, String content, String telephone, String email, String creator,
				String processor, String inspector, String status, Date createTime, Date updateTime, String site,String siteAddr,
				Integer expectedTime, String severity,String priority) {
			super();
			this.id = id;
			this.jobTypeName = jobTypeName;
			this.content = content;
			this.telephone = telephone;
			this.email = email;
			this.creator = creator;
			this.processor = processor;
			this.inspector = inspector;
			this.status = status;
			this.createTime = createTime;
			this.updateTime = updateTime;
			
			this.site = site;
			this.siteAddr = siteAddr;
			this.expectedTime = expectedTime;
			this.severity = severity;
			this.priority = priority;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getJobTypeName() {
			return jobTypeName;
		}

		public void setJobTypeName(String jobTypeName) {
			this.jobTypeName = jobTypeName;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
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

		public String getCreator() {
			return creator;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}

		public String getProcessor() {
			return processor;
		}

		public void setProcessor(String processor) {
			this.processor = processor;
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

		public Date getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}
		public String getSite() {
			return site;
		}
		public void setSite(String site) {
			this.site = site;
		}
		public String getSiteAddr() {
			return siteAddr;
		}
		public void setSiteAddr(String siteAddr) {
			this.siteAddr = siteAddr;
		}
		public Integer getExpectedTime() {
			return expectedTime;
		}
		public void setExpectedTime(Integer expectedTime) {
			this.expectedTime = expectedTime;
		}
		public String getSeverity() {
			return severity;
		}
		public void setSeverity(String severity) {
			this.severity = severity;
		}
		public String getPriority() {
			return priority;
		}
		public void setPriority(String priority) {
			this.priority = priority;
		}
	    
		
}
