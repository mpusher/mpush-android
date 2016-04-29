package com.shinemo.mpush.android;

import android.util.Log;

import com.shinemo.mpush.api.Logger;

public final class MPushLog implements Logger {
    public static final String sTag = "MPUSH";

    private boolean enable = false;

    @Override
    public void enable(boolean enabled) {
        this.enable = enabled;
    }

    @Override
    public void d(String s, Object... args) {
        if (enable) Log.d(sTag, String.format(s, args));
    }

    @Override
    public void i(String s, Object... args) {
        if (enable) Log.i(sTag, String.format(s, args));
    }

    @Override
    public void w(String s, Object... args) {
        if (enable) Log.w(sTag, String.format(s, args));
    }

    @Override
    public void e(Throwable e, String s, Object... args) {
        if (enable) Log.e(sTag, String.format(s, args), e);
    }
}