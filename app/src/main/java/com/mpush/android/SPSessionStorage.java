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
