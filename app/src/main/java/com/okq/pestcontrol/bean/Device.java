package com.okq.pestcontrol.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 设备信息 Created by Administrator on 2015/12/17.
 */
@Table(name = "Device")
public class Device implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    public long id;
    /**
     * 设备编号
     */
    @Column(name = "deviceNum")
    private String deviceNum;
    /**
     * 纬度
     */
    @Column(name = "wd")
    private double wd;
    /**
     * 经度
     */
    @Column(name = "jd")
    private double jd;

    /**
     * 状态
     */
    @Column(name = "status")
    private int status;

    /**
     * 采集间隔
     */
    @Column(name = "cjjg")
    private int cjjg;

    /**
     * 上传间隔
     */
    @Column(name = "upload")
    private int upload;

    /**
     * 报警号码 (,间隔)
     */
    @Column(name = "tels")
    private String tels;

    /**
     * 害虫阈值 (格式: 害虫1=12,害虫2=3,...)
     */
    @Column(name = "pestThreshold")
    private String pestThreshold;

    /**
     * 环境阈值 (格式: 环境1=12,20;环境2=3,8;...)
     */
    @Column(name = "environmentThreshold")
    private String environmentThreshold;

    /**
     * 获取设备编号
     *
     * @return 设备编号
     */
    public String getDeviceNum() {
        return deviceNum;
    }

    /**
     * 设置设备编号
     *
     * @param deviceNum 设备编号
     */
    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    /**
     * 获取纬度
     *
     * @return 纬度
     */
    public double getWd() {
        return wd;
    }

    /**
     * 设置纬度
     *
     * @param wd 纬度
     */
    public void setWd(double wd) {
        this.wd = wd;
    }

    /**
     * 获取经度
     *
     * @return 经度
     */
    public double getJd() {
        return jd;
    }

    /**
     * 设置经度
     *
     * @param jd 经度
     */
    public void setJd(double jd) {
        this.jd = jd;
    }

    /**
     * 获取设备状态
     *
     * @return 设备状态
     */
    public int getStatus() {
        return status;
    }

    /**
     * 设置设备状态
     *
     * @param status 设备状态
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 获取采集间隔
     *
     * @return 采集间隔
     */
    public int getCjjg() {
        return cjjg;
    }

    /**
     * 设置采集间隔
     *
     * @param cjjg 采集间隔
     */
    public void setCjjg(int cjjg) {
        this.cjjg = cjjg;
    }

    /**
     * 获取上传间隔
     *
     * @return 上传间隔
     */
    public int getUpload() {
        return upload;
    }

    /**
     * 设置上传间隔
     *
     * @param upload 上传间隔
     */
    public void setUpload(int upload) {
        this.upload = upload;
    }

    /**
     * 获取报警号码
     *
     * @return 报警号码
     */
    public String getTels() {
        return tels;
    }

    /**
     * 设置报警号码
     *
     * @param tels 报警号码
     */
    public void setTels(String tels) {
        this.tels = tels;
    }

    /**
     * 获取害虫阈值
     *
     * @return 害虫阈值
     */
    public String getPestThreshold() {
        return pestThreshold;
    }

    /**
     * 设置害虫阈值
     *
     * @param pestThreshold 害虫阈值
     */
    public void setPestThreshold(String pestThreshold) {
        this.pestThreshold = pestThreshold;
    }


    /**
     * 获取环境阈值
     *
     * @return 环境阈值
     */
    public String getEnvironmentThreshold() {
        return environmentThreshold;
    }

    /**
     * 设置环境阈值
     *
     * @param environmentThreshold 环境阈值
     */
    public void setEnvironmentThreshold(String environmentThreshold) {
        this.environmentThreshold = environmentThreshold;
    }
}
