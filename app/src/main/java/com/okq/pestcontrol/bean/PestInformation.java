package com.okq.pestcontrol.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 虫害信息
 * Created by Administrator on 2015/12/7.
 */
@Table(name = "PestInformation")
public class PestInformation implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private long id;
    /**
     * 设备
     */
    @Column(name = "device")
    private String device;
    /**
     * 害虫种类
     */
    @Column(name = "pestKind")
    private PestKind pestKind;
    /**
     * 关联的种类
     */
    @Column(name = "kindLink")
    public long kindLink;
    /**
     * 害虫数量
     */
    @Column(name = "pestNum")
    private int pestNum;
    /**
     * 设备号
     */
    @Column(name = "deviceNum")
    private String deviceNum;
    /**
     * 发送时间
     */
    @Column(name = "sendTime")
    private long sendTime;

    /**
     * 获取区域
     *
     * @return 区域
     */
    public String getDevice() {
        return device;
    }

    /**
     * 设置区域
     *
     * @param device 区域
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * 获取害虫种类
     *
     * @return 害虫种类
     */
    public PestKind getPestKind() {
        return pestKind;
    }

    /**
     * 设置害虫种类
     *
     * @param pestKind 害虫种类
     */
    public void setPestKind(PestKind pestKind) {
        this.pestKind = pestKind;
    }

    /**
     * 获取害虫数量
     *
     * @return 害虫数量
     */
    public int getPestNum() {
        return pestNum;
    }

    /**
     * 设置害虫数量
     *
     * @param pestNum 害虫数量
     */
    public void setPestNum(int pestNum) {
        this.pestNum = pestNum;
    }

    /**
     * 获取设备号
     *
     * @return 设备号
     */
    public String getDeviceNum() {
        return deviceNum;
    }

    /**
     * 设置设备号
     *
     * @param deviceNum 设备号
     */
    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    /**
     * 获取发送时间
     *
     * @return 发送时间
     */
    public long getSendTime() {
        return sendTime;
    }

    /**
     * 设置发送时间
     *
     * @param sendTime 发送时间
     */
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
