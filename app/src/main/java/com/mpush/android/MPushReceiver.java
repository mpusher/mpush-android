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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.SystemClock;

import com.mpush.api.Constants;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * Created by yxx on 2016/2/14.
 *
 * @author ohun@live.cn
 */
public final class MPushReceiver extends BroadcastReceiver {
    public static final String ACTION_HEALTH_CHECK = "com.mpush.HEALTH_CHECK";
    public static final String ACTION_NOTIFY_CANCEL = "com.mpush.NOTIFY_CANCEL";
    public static int delay = Constants.DEF_HEARTBEAT;
    public static State STATE = State.UNKNOWN;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_HEALTH_CHECK.equals(action)) {//处理心跳
            if (MPush.I.hasStarted()) {
                if (MPush.I.client.isRunning()) {
                    if (MPush.I.client.healthCheck()) {
                        startAlarm(context, delay);
                    }
                }
            }
        } else if (CONNECTIVITY_ACTION.equals(action)) {//处理网络变化
            if (hasNetwork(context)) {
                if (STATE != State.CONNECTED) {
                    STATE = State.CONNECTED;
                    if (MPush.I.hasStarted()) {
                        MPush.I.onNetStateChange(true);

                        //MPush.I.resumePush();
                    } else {
                        MPush.I.checkInit(context).startPush();
                    }
                }
            } else {
                if (STATE != State.DISCONNECTED) {
                    STATE = State.DISCONNECTED;
                    MPush.I.onNetStateChange(false);

                    //MPush.I.pausePush();
                    //cancelAlarm(context);//防止特殊场景下alarm没被取消
                }
            }
        } else if (ACTION_NOTIFY_CANCEL.equals(action)) {//处理通知取消
            Notifications.I.clean(intent);
        }
    }

    static void startAlarm(Context context, int delay) {
        Intent it = new Intent(MPushReceiver.ACTION_HEALTH_CHECK);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, it, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, pi);
        MPushReceiver.delay = delay;
    }

    static void cancelAlarm(Context context) {
        Intent it = new Intent(MPushReceiver.ACTION_HEALTH_CHECK);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, it, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
}
