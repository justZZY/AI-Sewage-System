package com.sewage.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class SiteDetail {
    @Id
    private String id;
    @Column
    private String date;
    @Column
    private String standard;
    @Column
    private String efficiency;
    @Column
    private String process;
    @Column
    private String address;
    @Column
    private String latitude;
    @Column
    private String longitude;
    @Column
    private String type;
    @Column
    private String operator;
    @Column
    private String can;
    @Column
    private String fan;
    @Column
    private String phone;
    @Column
    private String name;
    @Column
    private String boost;
    @Column
    private String reflux;
    @Column(length = 1000)
    private String monitors;
    @Column
    private String uploadImgIDArray;

    public SiteDetail(String siteID) {
        this.id = siteID;
        this.date = "";
        this.standard = "";
        this.efficiency = "";
        this.process = "";
        this.address = "";
        this.latitude = "";
        this.longitude = "";
        this.type = "";
        this.operator = "";
        this.can = "";
        this.fan = "";
        this.phone = "";
        this.name = "";
        this.boost = "";
        this.reflux = "";
        this.monitors = "";
        this.uploadImgIDArray = "";
    }

    public SiteDetail() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCan() {
        return can;
    }

    public void setCan(String can) {
        this.can = can;
    }

    public String getFan() {
        return fan;
    }

    public void setFan(String fan) {
        this.fan = fan;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoost() {
        return boost;
    }

    public void setBoost(String boost) {
        this.boost = boost;
    }

    public String getReflux() {
        return reflux;
    }

    public void setReflux(String reflux) {
        this.reflux = reflux;
    }

    public String getMonitors() {
        return monitors;
    }

    public void setMonitors(String monitors) {
        this.monitors = monitors;
    }

    public String getUploadImgIDArray() {
        return uploadImgIDArray;
    }

    public void setUploadImgIDArray(String uploadImgIDArray) {
        this.uploadImgIDArray = uploadImgIDArray;
    }
}
