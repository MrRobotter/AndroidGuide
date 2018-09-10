package com.joinyon.androidguide.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： JoinYon on 2018/6/21.
 * 邮箱：2816886869@qq.com
 */

public class MyViewGroup extends ViewGroup {


    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        //设置当前的高度
        int currentHeight = t;
        // 将子View摆放到合适的位置。
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            int height = childView.getMeasuredHeight();
            int width = childView.getMeasuredWidth();
            //摆放子View，参数分别是子View矩形区域的左、上、右、下。
            childView.layout(l, currentHeight, l + width, currentHeight + height);
            currentHeight += height;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 将所有的子View进行测量，这会触发每个子View的onMeasure()方法。
        //注意要与measureChild区分，measureChild是对单个view进行测量。
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);

        int childCount = getChildCount();

        if (childCount == 0) {//如果没有子View，当前的ViewGroup没有存在意义，不用占用空间
            setMeasuredDimension(0, 0);
        } else {

            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {// 宽高都是包裹内容
                //高度为所有子View的相加，宽度为子View中宽度最大的。
                int height = getTotalHeight();
                int width = getMaxChildWidth();
                setMeasuredDimension(width, height);

            } else if (heightMode == MeasureSpec.AT_MOST) {// 如果只有高度是包裹内容，宽度是
                //宽度设置为ViewGroup自己的测量宽度，高度为所有子View的总和。
                setMeasuredDimension(widthSize, getTotalHeight());

            } else if (widthMode == MeasureSpec.AT_MOST) {//如果只有宽度是内容包裹，则宽度是子View的最大值
                //高度是ViewGroup的测量值。
                setMeasuredDimension(getMaxChildWidth(), heightSize);
            } else {// 指定了宽度和高度
                // 宽度，高度均为ViewGroup的测量值
                setMeasuredDimension(widthSize, heightSize);
            }
        }
    }

    /**
     * 获取所有子View的高度相加
     *
     * @return
     */
    private int getTotalHeight() {
        int childCount = getChildCount();
        int height = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            height += childView.getMeasuredHeight();
        }
        return height;
    }

    /**
     * 获取子View中宽度最大的值
     *
     * @return
     */
    private int getMaxChildWidth() {
        int childCount = getChildCount();
        int maxWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getMeasuredWidth() > maxWidth) {
                maxWidth = childView.getMeasuredWidth();
            }
        }
        return maxWidth;
    }
}
