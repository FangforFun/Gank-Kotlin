package com.gkzxhn.gank_kotlin.bean.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 方 on 2017/8/1.
 */
@Entity
public class Remind {

    @Id(autoincrement = true)
    private Long id;
    private Long time;
    private String voice_uri;   //录音文件路径
    private String content_detail;   //内容详情
    @Generated(hash = 2043360579)
    public Remind(Long id, Long time, String voice_uri, String content_detail) {
        this.id = id;
        this.time = time;
        this.voice_uri = voice_uri;
        this.content_detail = content_detail;
    }
    @Generated(hash = 1173539496)
    public Remind() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public String getVoice_uri() {
        return this.voice_uri;
    }
    public void setVoice_uri(String voice_uri) {
        this.voice_uri = voice_uri;
    }
    public String getContent_detail() {
        return this.content_detail;
    }
    public void setContent_detail(String content_detail) {
        this.content_detail = content_detail;
    }
}
