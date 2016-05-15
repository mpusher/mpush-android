package com.mpush.demo;

import com.mpush.android.MPushMessage;

import org.json.JSONObject;

/**
 * Created by yxx on 2016/2/19.
 *
 * @author ohun@live.cn
 */
public final class NotificationDO implements MPushMessage {
    private String msgId;
    private String title;
    private String content;
    private Integer nid; //主要用于聚合通知，非必填
    private Byte flags; //特性字段。 0x01:声音   0x02:震动 0x03:闪灯
    private String largeIcon; // 大图标
    private String ticker; //和title一样
    private Integer number;
    private JSONObject extras;

    @Override
    public Integer getNid() {
        return nid;
    }

    @Override
    public String getMsgId() {
        return msgId;
    }

    @Override
    public String getTicker() {
        return ticker;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Integer getNumber() {
        return number;
    }

    @Override
    public Byte getFlags() {
        return flags;
    }

    @Override
    public String getLargeIcon() {
        return largeIcon;
    }

    public NotificationDO setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public NotificationDO setTitle(String title) {
        this.title = title;
        return this;
    }

    public NotificationDO setContent(String content) {
        this.content = content;
        return this;
    }

    public NotificationDO setNid(Integer nid) {
        this.nid = nid;
        return this;
    }

    public NotificationDO setFlags(Byte flags) {
        this.flags = flags;
        return this;
    }

    public NotificationDO setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
        return this;
    }

    public NotificationDO setTicker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    public NotificationDO setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public NotificationDO setExtras(JSONObject extras) {
        this.extras = extras;
        return this;
    }
}
