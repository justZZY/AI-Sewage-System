package com.sewage.springboot.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @desc 用于记录污水处理数据
 */

@Entity
@Table(name = "dataRecordTest")
public class DataRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 自增id 无实际意义
    @Column(nullable = false)
    private String siteId; // 站点id
    @Column(nullable = false)
    private String inPh; // 进水ph
    @Column(nullable = false)
    private String inAn; // 进水氨氮值 mg/L
    @Column(nullable = false)
    private String inFlow; // 进水流量 L/S
    @Column(nullable = false)
    private String inAccflow; // 进水累计流量 m³
    @Column(nullable = false)
    private String outPh; // 出水ph
    @Column(nullable = false)
    private String outAn; // 出水氨氮值 mg/L
    @Column(nullable = false)
    private String outFlow; // 出水流量 L/s
    @Column(nullable = false)
    private String outAccflow; // 出水累计流量
    @Column(nullable = false)
    private String cleanNum; // 消解量 kg
    @Column(nullable = false)
    private String outputNum; // 排放量
    @Column(nullable = false)
    private Date date; //输入写入时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getInPh() {
        return inPh;
    }

    public void setInPh(String inPh) {
        this.inPh = inPh;
    }

    public String getInAn() {
        return inAn;
    }

    public void setInAn(String inAn) {
        this.inAn = inAn;
    }

    public String getInFlow() {
        return inFlow;
    }

    public void setInFlow(String inFlow) {
        this.inFlow = inFlow;
    }

    public String getInAccflow() {
        return inAccflow;
    }

    public void setInAccflow(String inAccflow) {
        this.inAccflow = inAccflow;
    }

    public String getOutPh() {
        return outPh;
    }

    public void setOutPh(String outPh) {
        this.outPh = outPh;
    }

    public String getOutAn() {
        return outAn;
    }

    public void setOutAn(String outAn) {
        this.outAn = outAn;
    }

    public String getOutFlow() {
        return outFlow;
    }

    public void setOutFlow(String outFlow) {
        this.outFlow = outFlow;
    }

    public String getOutAccflow() {
        return outAccflow;
    }

    public void setOutAccflow(String outAccflow) {
        this.outAccflow = outAccflow;
    }

    public String getCleanNum() {
        return cleanNum;
    }

    public void setCleanNum(String cleanNum) {
        this.cleanNum = cleanNum;
    }

    public String getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(String outputNum) {
        this.outputNum = outputNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
