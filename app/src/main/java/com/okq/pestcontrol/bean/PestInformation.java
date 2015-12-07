package com.okq.pestcontrol.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/7.
 */
@Table(name = "PestInformation")
public class PestInformation implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private long id;
    /**
     * 区域
     */
    @Column(name = "area")
    private String area;
    /**
     * 开始时间
     */
    @Column(name = "startTime")
    private long startTime;
    /**
     * 截止时间
     */
    @Column(name = "endTime")
    private long endTime;
    /**
     * 害虫种类
     */
    @Column(name = "pestKind")
    private PestKind pestKind;
    /**
     * 温度
     */
    @Column(name = "temperature")
    private int temperature;
    /**
     * 湿度
     */
    @Column(name = "humidity")
    private int humidity;
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
    public String getArea() {
        return area;
    }

    /**
     * 设置区域
     *
     * @param area 区域
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取开始时间
     *
     * @return 开始时间
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取截止时间
     *
     * @return 截止时间
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * 设置截止时间
     *
     * @param endTime 截止时间
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
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
     * 获取温度
     *
     * @return 温度
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * 设置温度
     *
     * @param temperature 温度
     */
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    /**
     * 获取湿度
     *
     * @return 湿度
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * 设置湿度
     *
     * @param humidity 湿度
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
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
