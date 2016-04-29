package com.shinemo.mpush.demo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shinemo.mpush.android.BuildConfig;
import com.shinemo.mpush.android.MPush;
import com.shinemo.mpush.android.MPushLog;
import com.shinemo.mpush.android.Notifications;
import com.shinemo.mpush.android.R;
import com.shinemo.mpush.client.ClientConfig;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPush();
    }

    private void initPush() {
        Notifications.I.init(this.getApplicationContext());
        Notifications.I.setSmallIcon(R.mipmap.ic_notification);
        Notifications.I.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCghPCWCobG8nTD24juwSVataW7iViRxcTkey/B792VZEhuHjQvA3cAJgx2Lv8GnX8NIoShZtoCg3Cx6ecs+VEPD2fBcg2L4JK7xldGpOJ3ONEAyVsLOttXZtNXvyDZRijiErQALMTorcgi79M5uVX9/jMv2Ggb2XAeZhlLD28fHwIDAQAB";
        String allocServer = "http://allot.mpush.com";

        ClientConfig cc = ClientConfig.build()
                .setPublicKey(publicKey)
                .setAllotServer(allocServer)
                .setDeviceId(getDeviceId())
                .setClientVersion(BuildConfig.VERSION_NAME)
                .setLogger(new MPushLog())
                .setLogEnabled(BuildConfig.DEBUG)
                .setUserId("test1");
        MPush.I.init(getApplicationContext());
        MPush.I.setClientConfig(cc);
        MPush.I.startPush();
    }

    private String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Activity.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            String time = Long.toString((System.currentTimeMillis() / (1000 * 60 * 60)));
            deviceId = time + time;
        }
        return deviceId;
    }

    public void bindUser(View btn) {
        EditText et = (EditText) findViewById(R.id.userId);
        String userId = et.getText().toString().trim();
        if (!TextUtils.isEmpty(userId)) {
            MPush.I.bindAccount(userId);
        }
    }

    public void startPush(View btn) {
        MPush.I.startPush();
        Toast.makeText(this, "start push", Toast.LENGTH_SHORT).show();
    }

    public void stopPush(View btn) {
        MPush.I.stopPush();
        Toast.makeText(this, "stop push", Toast.LENGTH_SHORT).show();
    }

    public void pausePush(View btn) {
        MPush.I.pausePush();
        Toast.makeText(this, "pause push", Toast.LENGTH_SHORT).show();
    }

    public void resumePush(View btn) {
        MPush.I.resumePush();
        Toast.makeText(this, "resume push", Toast.LENGTH_SHORT).show();
    }

    public void unbindUser(View btn) {
        MPush.I.unbindAccount();
        Toast.makeText(this, "unbind user", Toast.LENGTH_SHORT).show();
    }
}
