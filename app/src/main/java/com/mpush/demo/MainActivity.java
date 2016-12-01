package com.mpush.demo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mpush.android.BuildConfig;
import com.mpush.android.MPush;
import com.mpush.android.MPushLog;
import com.mpush.android.Notifications;
import com.mpush.android.R;
import com.mpush.api.Constants;
import com.mpush.api.http.HttpCallback;
import com.mpush.api.http.HttpMethod;
import com.mpush.api.http.HttpRequest;
import com.mpush.api.http.HttpResponse;
import com.mpush.client.ClientConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Notifications.I.init(this.getApplicationContext());
        Notifications.I.setSmallIcon(R.mipmap.ic_notification);
        Notifications.I.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        SharedPreferences sp = this.getSharedPreferences("mpush.cfg", Context.MODE_PRIVATE);
        String alloc = sp.getString("allotServer", null);
        if (alloc != null) {
            EditText et = (EditText) findViewById(R.id.alloc);
            et.setText(alloc);
        }
    }

    private void initPush(String allocServer, String userId) {
        //公钥有服务端提供和私钥对应
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCghPCWCobG8nTD24juwSVataW7iViRxcTkey/B792VZEhuHjQvA3cAJgx2Lv8GnX8NIoShZtoCg3Cx6ecs+VEPD2fBcg2L4JK7xldGpOJ3ONEAyVsLOttXZtNXvyDZRijiErQALMTorcgi79M5uVX9/jMv2Ggb2XAeZhlLD28fHwIDAQAB";

        ClientConfig cc = ClientConfig.build()
                .setPublicKey(publicKey)
                .setAllotServer(allocServer)
                .setDeviceId(getDeviceId())
                .setClientVersion(BuildConfig.VERSION_NAME)
                .setLogger(new MyLog(this, (EditText) findViewById(R.id.log)))
                .setLogEnabled(BuildConfig.DEBUG)
                .setEnableHttpProxy(true)
                .setUserId(userId);
        MPush.I.checkInit(getApplicationContext()).setClientConfig(cc);
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
        EditText et = (EditText) findViewById(R.id.from);
        String userId = et.getText().toString().trim();
        if (!TextUtils.isEmpty(userId)) {
            MPush.I.bindAccount(userId, "mpush:" + (int) (Math.random() * 10));
        }
    }

    public void startPush(View btn) {
        EditText et = (EditText) findViewById(R.id.alloc);
        String allocServer = et.getText().toString().trim();

        if (TextUtils.isEmpty(allocServer)) {
            Toast.makeText(this, "请填写正确的alloc地址", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!allocServer.startsWith("http://")) {
            allocServer = "http://" + allocServer;
        }


        EditText etUser = (EditText) findViewById(R.id.from);
        String userId = etUser.getText().toString().trim();

        initPush(allocServer, userId);

        MPush.I.checkInit(this.getApplication()).startPush();
        Toast.makeText(this, "start push", Toast.LENGTH_SHORT).show();
    }

    public void sendPush(View btn) throws Exception {
        EditText et1 = (EditText) findViewById(R.id.alloc);
        String allocServer = et1.getText().toString().trim();

        if (TextUtils.isEmpty(allocServer)) {
            Toast.makeText(this, "请填写正确的alloc地址", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!allocServer.startsWith("http://")) {
            allocServer = "http://" + allocServer;
        }

        EditText toET = (EditText) findViewById(R.id.to);
        String to = toET.getText().toString().trim();

        EditText fromET = (EditText) findViewById(R.id.from);
        String from = fromET.getText().toString().trim();

        EditText helloET = (EditText) findViewById(R.id.httpProxy);
        String hello = helloET.getText().toString().trim();

        if (TextUtils.isEmpty(hello)) hello = "hello";

        JSONObject params = new JSONObject();
        params.put("userId", to);
        params.put("hello", from + " say:" + hello);

        final Context context = this.getApplicationContext();
        HttpRequest request = new HttpRequest(HttpMethod.POST, allocServer + "/push");
        byte[] body = params.toString().getBytes(Constants.UTF_8);
        request.setBody(body, "application/json; charset=utf-8");
        request.setTimeout((int) TimeUnit.SECONDS.toMillis(10));
        request.setCallback(new HttpCallback() {
            @Override
            public void onResponse(final HttpResponse httpResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (httpResponse.statusCode == 200) {
                            Toast.makeText(context, new String(httpResponse.body, Constants.UTF_8), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, httpResponse.reasonPhrase, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled() {

            }
        });
        MPush.I.sendHttpProxy(request);
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
