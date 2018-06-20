**前言**
看到大神们写的自定义View很精彩，借此机会温习Android知识点，
决定一步一步从基础学习总结自定义View，来提示对自定义View的理解

# 1 自定义View
为什么要自定义View？主要是Android系统自带的View无法满足我们的需求，
这时我们需要自定义View为我们的项目需求定制View。
通常我们只需要重写两个方法：`onMEasure()`和`onDraw()`。
onMeasure负责对当前View的尺寸进行测量，onDraw负责把当前的View绘制出来。
在这之前，还要至少写两个构造方法：
````java
import android.view.View;

/**
 * 作者： JoinYon on 2018/6/13.
 * 邮箱：2816886869@qq.com
 */

public class MyView extends View{
    //构造方法
    public MyView(Context context) {
        super(context);
    }
    //构造方法
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
````
## 1.1 onMeasure
在学习`onMeasure`之前我们使用系统自带控件如:`TextView`
咱们可以在xml文件中指定宽、高尺寸，那么为什么还要在自定义View中再次测量宽高尺寸呢？

学习Android的时候，我们知道，在xml布局文件中，
使用的`layout_width`和`layout_height`参数可以不用写具体的尺寸，
而可以使用`warp_content`或者`match_parent`。
就是将控件的尺寸设置为**内容包裹**和**填充父容器给我们的所有空间**。
这两个设置并没有指定具体的大小，可是我们绘制到屏幕上的View是必须要有具体的宽和高的，
正是因为这个原因，我们必须自己去处理和设置尺寸。当然,View类给了默认处理，
但是如果View类的默认处理不满足我们的要求，我们就得重写onMeasure方法。
如：我们希望我们的View是个正方形，如果在xml中指定宽高为`wrap_content`，
我们直接使用View类提供的measure处理方式，显然是无法做到的。

**onMeasure方法：**
````java
import android.view.View;

/**
 * 作者： JoinYon on 2018/6/13.
 * 邮箱：2816886869@qq.com
 */

public class MyView extends View{
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

````
该方法有两个`int`类型参数`widthMeasureSpec`和`heightMeasureSpec`分别是什么意思？
从字面上看起来很像width和height，是的，没错，确实是包含宽度和高度的信息。
它还包含测量模式，一个int整数里面放了测量模式和尺寸大小。那是怎么放的呢？
咱们知道，当设置宽高时有三个选择：`wrap_content`、`match_parent`以及`指定固定大小`，
而测量模式也有三种：`UNSPECIFIED`,`EXACTLY`,`AT_MOST`这三种模式并不是一一对应。
测量模式无非就这三种情况，采用二进制，只需要使用2个bit就可以做到，因为2bit取值范围是\[0,3]
里面可以放4个数足够我们使用了。那么Google是怎么把一个int同时放测量模式和尺寸信息呢？int类型
数据占用32个bit，而Google的实现是，将int数据的前2个bit用于区分不同的布局模式，
后面的30个bit存放的是尺寸的数据。

那么

## 1.2 重写onMeasure方法

## 1.3 重写onDraw方法

## 1.4 自定义布局属性
 

# 2 自定义ViewGroup

## 2.1 重写onMeasure方法

## 2.2 要实现的功能

## 2.3 子view的摆放规则

## 2.4 摆放子view