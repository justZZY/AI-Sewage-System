package com.sewage.springboot.entity.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;


/**
 * 工单配置
 * <br><br>
 *
 * @author sc
 * @date 2020年6月15日
 */
public class JobConfig {
	
		/** 主键ID自增 */
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id")
	    private Integer id;
	    
		@Column(name = "name")
	    private String name;
		 
	    @Column(name = "value")
	    private String value;

	    @Column(name = "remark")
	    private String remark;
	    
	    public JobConfig() {
	    	super();
	    }

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
		
	    
}
