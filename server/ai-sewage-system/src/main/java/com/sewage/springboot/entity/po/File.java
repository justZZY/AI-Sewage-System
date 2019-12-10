package com.sewage.springboot.entity.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class File {
	/** 主键ID自增 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id"  )
    private Integer id;
    
    @Column(name = "original_filename"  )
    private String originalFilename;
    
    /**文件存储相对路径,相对于根目录*/
    @Column(name = "filepath"  )
    private String filepath;
    
    @Column(name = "status"  )
    private String status;
    
    /** 上传者username */
    @Column(name = "uploader"  )
    private String uploader;

    @Column(name = "create_time" )
    private Date  createTime;
    
    @Column(name = "update_time" )
    private Date updateTime;

    
    public File() {
    	super();
    }
	public File(Integer id, String originalFilename, String filepath, String status, String uploader, Date createTime, Date updateTime) {
		super();
		this.id = id;
		this.originalFilename = originalFilename;
		this.filepath = filepath;
		this.status = status;
		this.uploader = uploader;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
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
