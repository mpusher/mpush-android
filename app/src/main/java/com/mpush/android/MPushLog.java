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

import android.util.Log;

import com.mpush.api.Logger;

/**
 * Created by yxx on 2016/2/15.
 *
 * @author ohun@live.cn
 */
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