package com.xiaomai.geek.db;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by wangce on 2018/2/23.
 */

@Entity
public class Task implements Serializable {

    private static final long serialVersionUID = 536871008L;

    @Id(autoincrement = true)
    private Long id;
    // 标题
    private String title;
    // 内容
    private String content;
    // 优先级
    private int priority;
    // 是否完成
    private boolean complete;
    // 创建时间
    private long createTime;
    @Transient
    private boolean checked;

    @Generated(hash = 930472377)
    public Task(Long id, String title, String content, int priority,
            boolean complete, long createTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.complete = complete;
        this.createTime = createTime;
    }

    @Generated(hash = 733837707)
    public Task() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getComplete() {
        return this.complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(content)
                && TextUtils.isEmpty(title);
    }
}
