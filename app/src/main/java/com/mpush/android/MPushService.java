package com.mpush.android;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mpush.android.BuildConfig;
import com.mpush.api.Client;
import com.mpush.api.ClientListener;

/**
 * Created by yxx on 2016/2/13.
 *
 * @author ohun@live.cn
 */
public final class MPushService extends Service implements ClientListener {
    public static final String ACTION_MESSAGE_RECEIVED = "com.mpush.MESSAGE_RECEIVED";
    public static final String ACTION_NOTIFICATION_OPENED = "com.mpush.NOTIFICATION_OPENED";
    public static final String ACTION_KICK_USER = "com.mpush.KICK_USER";
    public static final String EXTRA_PUSH_MESSAGE = "push_message";
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_DEVICE_ID = "device_id";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);
        if (!MPush.I.hasStarted()) {
            MPush.I.checkInit(this).create(this);
        }
        if (MPush.I.hasStarted()) {
            if (MPushReceiver.hasNetwork(this)) {
                MPush.I.client.start();
            }
            return START_REDELIVER_INTENT;
        } else {
            stopSelf();
            return ret;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MPushReceiver.cancelAlarm(this);
        MPush.I.destroy();
    }

    @Override
    public void onReceivePush(Client client, byte[] content) {
        sendBroadcast(new Intent(ACTION_MESSAGE_RECEIVED)
                .addCategory(BuildConfig.APPLICATION_ID)
                .putExtra(EXTRA_PUSH_MESSAGE, content)
        );
    }

    @Override
    public void onKickUser(String deviceId, String userId) {
        MPush.I.unbindAccount();
        sendBroadcast(new Intent(ACTION_KICK_USER)
                .addCategory(BuildConfig.APPLICATION_ID)
                .putExtra(EXTRA_DEVICE_ID, deviceId)
                .putExtra(EXTRA_USER_ID, userId)
        );
    }

    @Override
    public void onConnected(Client client) {

    }

    @Override
    public void onDisConnected(Client client) {
        MPushReceiver.cancelAlarm(this);
    }

    @Override
    public void onHandshakeOk(Client client, int heartbeat) {
        MPushReceiver.startAlarm(this, heartbeat - 1000);
    }
}
