package com.okq.pestcontrol.bean;

import com.okq.pestcontrol.util.JsonParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2015/12/14.
 */
@HttpResponse(parser = JsonParser.class)
public class Test {
    private String title;
    private String source;
    private String article_url;
    private String group_id;
    private long behot_time;
    private long create_time;
    private int digg_count;
    private long publish_time;
    private int repin_count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public long getBehot_time() {
        return behot_time;
    }

    public void setBehot_time(long behot_time) {
        this.behot_time = behot_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(int digg_count) {
        this.digg_count = digg_count;
    }

    public long getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(long publish_time) {
        this.publish_time = publish_time;
    }

    public int getRepin_count() {
        return repin_count;
    }

    public void setRepin_count(int repin_count) {
        this.repin_count = repin_count;
    }

    @Override
    public String toString() {
        return "Test{" +
                "title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", article_url='" + article_url + '\'' +
                ", group_id='" + group_id + '\'' +
                ", behot_time=" + behot_time +
                ", create_time=" + create_time +
                ", digg_count=" + digg_count +
                ", publish_time=" + publish_time +
                ", repin_count=" + repin_count +
                '}';
    }
}
