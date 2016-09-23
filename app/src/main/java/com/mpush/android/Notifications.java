/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     ohun@live.cn (夜色)
 */


package com.mpush.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;

import java.util.HashMap;
import java.util.Map;

public final class Notifications {
    public static final String EXTRA_MESSAGE_ID = "msg_id";
    public static final Notifications I = new Notifications();
    private int nIdSeq = 1;
    private final Map<Integer, Integer> nIds = new HashMap<>();
    private Context context;
    private NotificationManager nm;
    private int smallIcon;
    private Bitmap largeIcon;
    private int defaults;

    public void init(Context context) {
        this.context = context;
        this.nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int defaults = Notification.DEFAULT_ALL
                | Notification.FLAG_AUTO_CANCEL;
        this.defaults = defaults;
    }

    public boolean hasInit() {
        return context != null;
    }

    public Notifications setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
        return this;
    }

    public Notifications setLargeIcon(Bitmap largeIcon) {
        this.largeIcon = largeIcon;
        return this;
    }

    public Notifications setDefaults(int defaults) {
        this.defaults = defaults;
        return this;
    }

    public int notify(MPushMessage message, Intent clickIT) {
        if (message == null || clickIT == null) return -1;
        Integer nid = message.getNid();
        //1.如果NID不存在则新生成一个，且新生成的Id在nIds是不存在的
        if (nid == null || nid <= 0) {
            do {
                nid = nIdSeq++;
            } while (nIds.containsKey(nid));
        }

        //处理总数
        Integer count = nIds.get(nid);
        if (count == null) {
            count = 0;
        }
        nIds.put(nid, ++count);
        Intent cancelIT = new Intent(MPushReceiver.ACTION_NOTIFY_CANCEL);
        cancelIT.putExtra(EXTRA_MESSAGE_ID, nid);
        clickIT.putExtra(EXTRA_MESSAGE_ID, nid);
        PendingIntent clickPI = PendingIntent.getBroadcast(context, 0, clickIT, 0);//处理点击
        PendingIntent cancelPI = PendingIntent.getBroadcast(context, 0, cancelIT, 0);//处理滑动取消
        nm.notify(nid, build(clickPI, cancelPI,
                message.getTitle(),
                message.getTitle(),
                message.getContent(),
                count));
        return nid;
    }

    public void clean(Integer nId) {
        Integer count = nIds.remove(nId);
        if (count != null) nm.cancel(nId);
    }

    public void clean(Intent intent) {
        int nId = intent.getIntExtra(Notifications.EXTRA_MESSAGE_ID, 0);
        if (nId > 0) clean(nId);
    }

    public void cleanAll() {
        nIds.clear();
        nm.cancelAll();
    }

    private Notification build(PendingIntent clickIntent, PendingIntent cancelIntent,
                               String ticker, String title, String content, int number) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(smallIcon)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker)
                .setContentIntent(clickIntent)
                .setDeleteIntent(cancelIntent)
                .setNumber(number)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                        //.setLights(0xff00ff00, 5000, 5000)
                .setDefaults(defaults)
                .build();
    }
}