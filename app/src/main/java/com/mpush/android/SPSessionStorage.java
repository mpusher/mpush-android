package com.mpush.android;

import android.content.SharedPreferences;

import com.mpush.api.connection.SessionStorage;

/**
 * Created by yxx on 2016/2/15.
 *
 * @author ohun@live.cn
 */
public final class SPSessionStorage implements SessionStorage {
    private final SharedPreferences sp;

    public SPSessionStorage(SharedPreferences sp) {
        this.sp = sp;
    }

    @Override
    public void saveSession(String sessionContext) {
        sp.edit().putString("session", sessionContext).apply();
    }

    @Override
    public String getSession() {
        return sp.getString("session", null);
    }

    @Override
    public void clearSession() {
        sp.edit().remove("session").apply();
    }
}
