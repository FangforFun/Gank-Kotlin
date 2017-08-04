package com.gkzxhn.gank_kotlin.utils;

import com.gkzxhn.gank_kotlin.api.App;

/**
 * Created by 方 on 2017/8/4.
 */

public class UIUtil {
    /**
     * dip转pix
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {
        final float scale = App.instance.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
