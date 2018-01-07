package com.douyu.bean;

import java.io.Serializable;
import java.util.Date;

import com.douyu.until.MyLogic;

/**
 * zjy 2018年1月7日15:59:30
 */
public class DanMu implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int uid;//用户id
    private String snick;//昵称
    private String content;//内容
    private String date;//发布时间
    private int rid;//房间号

    public DanMu(int uid, String snick, String content, int rid) {
        this.uid = uid;
        this.snick = snick;
        this.content = content;
        this.date = MyLogic.getDate();
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "Danmaku{" +
                "uid=" + uid +
                ", snick='" + snick + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", rid=" + rid +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSnick() {
        return snick;
    }

    public void setSnick(String snick) {
        this.snick = snick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }
}
