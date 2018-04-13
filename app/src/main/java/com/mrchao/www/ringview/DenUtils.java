package com.mrchao.www.ringview;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

/**
 * Created by MrChao on 2018/3/8.
 */

public class DenUtils {
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    public static int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }


    public static int getTextWidth(Paint mPaint, String text) {
        //获取文本的高度
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    public static int getTextHeight(Paint mPaint, String text) {
        //获取文本的高度
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }
}
