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
     * 设备型号
     */
    @Column(name = "deviceModel")
    private String deviceModel;
//    /**
//     * 购买时间
//     */
//    @Column(name = "buyTime")
//    private long buyTime;
//    /**
//     * 安装时间
//     */
//    @Column(name = "installTime")
//    private long installTime;
//    /**
//     * 拆除时间
//     */
//    @Column(name = "removeTime")
//    private long removeTime;
//    /**
//     * 区域
//     */
//    @Column(name = "area")
//    private String area;
//    /**
//     * 具体地点
//     */
//    @Column(name = "place")
//    private String place;
    /**
     * 纬度
     */
    @Column(name = "lat")
    private double lat;
    /**
     * 经度
     */
    @Column(name = "lon")
    private double lon;

    /**
     * 状态
     */
    @Column(name = "status")
    private int status;

    /**
     * 采集间隔
     */
    @Column(name = "collectInterval")
    private int collectInterval;

    /**
     * 上传间隔
     */
    @Column(name = "uploadInterval")
    private int uploadInterval;

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
     * 获取设备型号
     *
     * @return 设备型号
     */
    public String getDeviceModel() {
        return deviceModel;
    }

    /**
     * 设置设备型号
     *
     * @param deviceModel 设备型号
     */
    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

//    /**
//     * 获取购买时间
//     *
//     * @return 购买时间
//     */
//    public long getBuyTime() {
//        return buyTime;
//    }
//
//    /**
//     * 设置购买时间
//     *
//     * @param buyTime 购买时间
//     */
//    public void setBuyTime(long buyTime) {
//        this.buyTime = buyTime;
//    }
//
//    /**
//     * 获取安装时间
//     *
//     * @return 安装时间
//     */
//    public long getInstallTime() {
//        return installTime;
//    }
//
//    /**
//     * 设置安装时间
//     *
//     * @param installTime 安装时间
//     */
//    public void setInstallTime(long installTime) {
//        this.installTime = installTime;
//    }
//
//    /**
//     * 获取拆除时间
//     *
//     * @return 拆除时间
//     */
//    public long getRemoveTime() {
//        return removeTime;
//    }
//
//    /**
//     * 设置拆除时间
//     *
//     * @param removeTime 拆除时间
//     */
//    public void setRemoveTime(long removeTime) {
//        this.removeTime = removeTime;
//    }
//
//    /**
//     * 获取区域
//     *
//     * @return 区域
//     */
//    public String getArea() {
//        return area;
//    }
//
//    /**
//     * 设置区域
//     *
//     * @param area 区域
//     */
//    public void setArea(String area) {
//        this.area = area;
//    }
//
//    /**
//     * 获取具体地点
//     *
//     * @return 具体地点
//     */
//    public String getPlace() {
//        return place;
//    }
//
//    /**
//     * 设置具体地点
//     *
//     * @param place 具体地点
//     */
//    public void setPlace(String place) {
//        this.place = place;
//    }

    /**
     * 获取纬度
     *
     * @return 纬度
     */
    public double getLat() {
        return lat;
    }

    /**
     * 设置纬度
     *
     * @param lat 纬度
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * 获取经度
     *
     * @return 经度
     */
    public double getLon() {
        return lon;
    }

    /**
     * 设置经度
     *
     * @param lon 经度
     */
    public void setLon(double lon) {
        this.lon = lon;
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
    public int getCollectInterval() {
        return collectInterval;
    }

    /**
     * 设置采集间隔
     *
     * @param collectInterval 采集间隔
     */
    public void setCollectInterval(int collectInterval) {
        this.collectInterval = collectInterval;
    }

    /**
     * 获取上传间隔
     *
     * @return 上传间隔
     */
    public int getUploadInterval() {
        return uploadInterval;
    }

    /**
     * 设置上传间隔
     *
     * @param uploadInterval 上传间隔
     */
    public void setUploadInterval(int uploadInterval) {
        this.uploadInterval = uploadInterval;
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
}
