package com.okq.pestcontrol.bean;

import android.text.TextUtils;

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
    private long Id;
    /**
     * 害虫名称
     */
    @Column(name = "pest")
    private String pest;
    /**
     * 设备
     */
    @Column(name = "deviceid")
    private String deviceid;
    /**
     * 害虫数量
     */
    @Column(name = "count")
    private int count;
    /**
     * 发送时间
     */
    @Column(name = "cjtime")
    private String cjtime;
    /**
     * 环境
     */
    @Column(name = "environment")
    private String environments;

    private String wd = "";
    private String sd = "";
    private String ld = "";
    private String gz = "";
    private String zf = "";
    private String fx = "";
    private String fs = "";
    private String gh = "";
    private String pm = "";
    private String yl = "";
    private String tw = "";
    private String ts = "";
    private String qy = "";
    private String co2 = "";
    private String yf = "";
    private String an = "";
    private String ph = "";
    private String zfs = "";
    private String yx = "";
    private String ys = "";
    private String jfs = "";
    private String zwfs = "";
    private String rizhao = "";

    /**
     * 获取设备
     *
     * @return 设备
     */
    public String getDeviceid() {
        return deviceid;
    }

    /**
     * 设置设备
     *
     * @param deviceid 设备
     */
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    /**
     * 获取害虫数量
     *
     * @return 害虫数量
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置害虫数量
     *
     * @param count 害虫数量
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 获取发送时间
     *
     * @return 发送时间
     */
    public String getCjtime() {
        return cjtime;
    }

    /**
     * 设置发送时间
     *
     * @param cjtime 发送时间
     */
    public void setCjtime(String cjtime) {
        this.cjtime = cjtime;
    }

    /**
     * 获取害虫名称
     *
     * @return 害虫名称
     */
    public String getPest() {
        return pest;
    }

    /**
     * 设置害虫名称
     *
     * @param pest 害虫名称
     */
    public void setPest(String pest) {
        this.pest = pest;
    }

    /**
     * 获取环境
     *
     * @return 所有环境
     */
    public String getEnvironments() {

        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(wd)) sb.append("温度=").append(wd).append("℃,");
        if (!TextUtils.isEmpty(sd)) sb.append("湿度=").append(sd).append("%,");
        if (!TextUtils.isEmpty(ld)) sb.append("露点=").append(ld).append("℃,");
        if (!TextUtils.isEmpty(gz)) sb.append("光照度=").append(gz).append("LUX,");
        if (!TextUtils.isEmpty(zf)) sb.append("蒸发量=").append(zf).append("mm,");
        if (!TextUtils.isEmpty(fx)) sb.append("风向=").append(fx).append("°,");
        if (!TextUtils.isEmpty(fs)) sb.append("风速=").append(fs).append("m/s,");
        if (!TextUtils.isEmpty(gh)) sb.append("光合辐射=").append(gh).append("umol,");
        if (!TextUtils.isEmpty(pm)) sb.append("PM2.5=").append(pm).append("ppm,");
        if (!TextUtils.isEmpty(yl)) sb.append("雨量=").append(yl).append("mm/min,");
        if (!TextUtils.isEmpty(tw)) sb.append("土温=").append(tw).append("℃,");
        if (!TextUtils.isEmpty(ts)) sb.append("土湿=").append(ts).append("%,");
        if (!TextUtils.isEmpty(qy)) sb.append("大气压=").append(qy).append("Kpa,");
        if (!TextUtils.isEmpty(co2)) sb.append("二氧化碳=").append(co2).append("ppm,");
        if (!TextUtils.isEmpty(yf)) sb.append("土壤盐分=").append(yf).append("ms/cm,");
        if (!TextUtils.isEmpty(an)) sb.append("氨气=").append(an).append("ppm,");
        if (!TextUtils.isEmpty(ph)) sb.append("PH=").append(ph).append(",");
        if (!TextUtils.isEmpty(zfs)) sb.append("总辐射=").append(zfs).append("w/㎡,");
        if (!TextUtils.isEmpty(yx)) sb.append("雨雪=").append(yx).append(",");
        if (!TextUtils.isEmpty(ys)) sb.append("页面湿度=").append(ys).append("%,");
        if (!TextUtils.isEmpty(jfs)) sb.append("净辐射=").append(jfs).append("w/㎡,");
        if (!TextUtils.isEmpty(zwfs)) sb.append("紫外辐射=").append(zwfs).append("w/㎡,");
        if (!TextUtils.isEmpty(rizhao)) sb.append("日照时数=").append(rizhao).append("H,");
        if (!TextUtils.isEmpty(sb))
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * 设置环境
     *
     * @param environments 环境
     */
    public void setEnvironments(String environments) {
        this.environments = environments;
    }


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getLd() {
        return ld;
    }

    public void setLd(String ld) {
        this.ld = ld;
    }

    public String getGz() {
        return gz;
    }

    public void setGz(String gz) {
        this.gz = gz;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getGh() {
        return gh;
    }

    public void setGh(String gh) {
        this.gh = gh;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getYl() {
        return yl;
    }

    public void setYl(String yl) {
        this.yl = yl;
    }

    public String getTw() {
        return tw;
    }

    public void setTw(String tw) {
        this.tw = tw;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getQy() {
        return qy;
    }

    public void setQy(String qy) {
        this.qy = qy;
    }

    public String getCo2() {
        return co2;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }

    public String getYf() {
        return yf;
    }

    public void setYf(String yf) {
        this.yf = yf;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getZfs() {
        return zfs;
    }

    public void setZfs(String zfs) {
        this.zfs = zfs;
    }

    public String getYx() {
        return yx;
    }

    public void setYx(String yx) {
        this.yx = yx;
    }

    public String getYs() {
        return ys;
    }

    public void setYs(String ys) {
        this.ys = ys;
    }

    public String getJfs() {
        return jfs;
    }

    public void setJfs(String jfs) {
        this.jfs = jfs;
    }

    public String getZwfs() {
        return zwfs;
    }

    public void setZwfs(String zwfs) {
        this.zwfs = zwfs;
    }

    public String getRizhao() {
        return rizhao;
    }

    public void setRizhao(String rizhao) {
        this.rizhao = rizhao;
    }
}
