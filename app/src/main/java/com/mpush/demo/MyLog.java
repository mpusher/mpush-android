package com.mpush.demo;

import android.app.Activity;
import android.widget.EditText;

import com.mpush.android.MPushLog;
import com.mpush.api.Constants;
import com.mpush.api.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by ohun on 16/9/22.
 */

public class MyLog implements Logger {

    private EditText logView;
    private Activity activity;

    private MPushLog mPushLog;

    public MyLog(Activity activity, EditText logView) {
        this.activity = activity;
        this.logView = logView;
        this.mPushLog = new MPushLog();
    }

    @Override
    public void enable(boolean b) {
        this.mPushLog.enable(true);
    }

    @Override
    public void d(String s, Object... objects) {
        mPushLog.d(s, objects);
        append(null, s, objects);
    }

    @Override
    public void i(String s, Object... objects) {
        mPushLog.i(s, objects);
        append(null, s, objects);
    }

    @Override
    public void w(String s, Object... objects) {
        mPushLog.w(s, objects);
        append(null, s, objects);
    }

    @Override
    public void e(Throwable throwable, String s, Object... objects) {
        mPushLog.e(throwable, s, objects);
        append(throwable, s, objects);
    }

    private void append(final Throwable throwable, final String s, final Object... objects) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logView.getText().append(String.format(s, objects)).append('\n').append('\n');
                if (throwable != null) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    throwable.printStackTrace(new PrintStream(buffer));
                    logView.getText().append(new String(buffer.toByteArray(), Constants.UTF_8));
                }
            }
        });
    }
}
