package com.sewage.springboot.entity.po;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * 工单类型
 * <br><br>
 *
 * @author sc
 * @date 2019年9月22日
 */
public class JobType {
	
	/** 主键ID自增 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id"  )
    private Integer id;
    
    @Column(name = "father_id"  )
    private Integer fatherId;
    
    /** 工单问题名称 */
    @Column(name = "job_type_name" )
    private String jobTypeName;

    @Column(name = "create_time" )
    private Date  createTime;
    
    @Column(name = "update_time" )
    private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFatherId() {
		return fatherId;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
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
