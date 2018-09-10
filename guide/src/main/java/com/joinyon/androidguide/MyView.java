package com.joinyon.androidguide;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者： JoinYon on 2018/6/13.
 * 邮箱：2816886869@qq.com
 */

public class MyView extends View {
    Paint paint = new Paint();
    private int defaultSize;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //第二个参数是我们在styles.xml文件中的<declare-styleable>标签
        //即属性集合的标签，在R文件中名称为R.styleable+name

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);

        //第一个参数为属性集合里面的属性，R文件名称：R.styleable_属性集合名+下划线+属性名称
        defaultSize = typedArray.getDimensionPixelSize(R.styleable.MyView_default_size, 100);

        typedArray.recycle();//最后记得将TypedArray对象释放掉；
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100, widthMeasureSpec);
        int height = getMySize(100, heightMeasureSpec);

        if (width < height) {
            height = width;
        } else {
            width = height;
        }
        setMeasuredDimension(width, height);
    }

    /**
     * @param defaultSize 默认大小
     * @param measureSpec MeasureSpec
     * @return
     */
    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED://没有指定大小，设置为默认大小
                mySize = defaultSize;
                break;

            case MeasureSpec.AT_MOST:// 最大取值是size
                mySize = size;//大小取最大值，我们也可以取其他值
                break;
            case MeasureSpec.EXACTLY://指定固定大小，就不需要改变
                mySize = size;
                break;
        }

        return mySize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 调用父类的onDraw()方法,因为View类
        //帮我们实现了一些基本的绘制功能，比如绘制背景色、背景图片等。
        super.onDraw(canvas);

        // 第一步:定义半径。宽或者高的一半
        int r = getMeasuredWidth() / 2;//我们已经将宽高设置相等了，所以随便选择一个。

        // 第二步:确定圆心坐标
//        int centerX = getLeft() + r;//圆心的横坐标为当前View的左边起始位置+半径。
//        int centerY = getTop() + r; //圆心的纵坐标为当前View的顶部起始点+半径。
        int centerX = r;
        int centerY = r;

        // 第三步:实例化画笔对象，并设置画笔属性

        paint.setColor(Color.GRAY); //灰色画笔

        // 第四步:开始绘制圆形
        canvas.drawCircle(centerX, centerY, r, paint);

    }


}
