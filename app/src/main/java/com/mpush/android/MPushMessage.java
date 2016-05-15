package com.mpush.android;

public interface MPushMessage {

    Integer getNid();

    String getMsgId();

    String getTicker();

    String getTitle();

    String getContent();

    Integer getNumber();

    Byte getFlags();

    String getLargeIcon();
}