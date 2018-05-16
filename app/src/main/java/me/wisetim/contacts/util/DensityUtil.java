package me.wisetim.contacts.util;

import android.content.Context;

public class DensityUtil {
    private float mScale;

    public DensityUtil(Context context) {
        mScale = context.getResources().getDisplayMetrics().density;
    }

    public int dp2px(float dpValue) {
        return (int) (dpValue * mScale + 0.5f);
    }

    public int px2dp(float pxValue) {
        return (int) (pxValue / mScale + 0.5f);
    }

}
