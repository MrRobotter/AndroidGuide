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
    //...
    
    //构造方法
    public MyView(Context context) {
        super(context);
    }
    //构造方法
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    //...
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
    //...
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    //...
}

````
该方法有两个`int`类型参数`widthMeasureSpec`和`heightMeasureSpec`分别是什么意思？
从字面上看起来很像width和height，是的，没错，确实是包含宽度和高度的信息。
它还包含测量模式，一个int整数里面放了`测量模式`和`尺寸大小`。那是怎么放的呢？
咱们知道，当设置宽高时有三个选择：`wrap_content`、`match_parent`以及`指定固定大小`，
而测量模式也有三种：`UNSPECIFIED`,`EXACTLY`,`AT_MOST`这三种模式并不是一一对应。
测量模式无非就这三种情况，采用二进制，只需要使用2个bit就可以做到，因为2bit取值范围是\[0,3]
里面可以放4个数足够我们使用了。那么Google是怎么把一个int同时放测量模式和尺寸信息呢？int类型
数据占用32个bit，而Google的实现是，将int数据的前2个bit用于区分不同的布局模式，
后面的30个bit存放的是尺寸的数据。

那我们如何从int数据中获取`测量模式`和`尺寸大小`呢？
是不是每次都需要做一次移位`<<`和取且`&`操作呢？其实不必这样操作，Android
内置类MeasureSpec已经帮我们写好了，只需要调用以下方法就可以拿到了：
````java
import android.view.View;

/**
 * 作者： JoinYon on 2018/6/13.
 * 邮箱：2816886869@qq.com
 */

public class MyView extends View{
    
    //...
    
   int widthMode=MesureSpec.getMode(widthMeasureSpec); 
   int widthSize=MesureSpec.getSize(widthMeasureSpec);
   
   //...
}

````
于是，我们可以思考一个问题：既然通过widthMeasureSpec拿到宽度大小，那为什么还
要测量模式干嘛？测量模式会不会是多余的？请注意：这里的尺寸大小并不是最终我们的View的尺寸大小，
而是父View提供的参考大小。测量模式的具体用途见下表：

|**测量模式** | **表示意思**|
| :------:|:----:|
| UNSPECIFIED |父容器没有对当前View有任何限制，当前View可以任意取尺寸|
|EXACTLY|当的尺寸就是当前View应该取的尺寸|
|AT_MOST|当前尺寸是当前View能取的最大尺寸|

而上面的测量模式跟我们布局时的 `wrap_content`、`match_parent`以及`固定尺寸`有何对应关系呢？
> `match_parent` --->EXACTLY. `match_parent`就是要利用父View给我们提供
的所有剩余空间，而父View剩余空间是确定的，也就是这个测量模式的整数里面存放的尺寸。

> `wrap_content` --->AT_MOST. 就是我们想要将大小设置为包裹我们的View内容，
那么尺寸大小就是父View给我们作为参考的尺寸，只有不超过这个尺寸就可以了，具体尺寸就是根据我们的需求去设定。

> `固定尺寸` --->EXACTLY. 用户自己指定了尺寸大小，我们就不用再去操作，以指定的大小为准。

## 1.2 重写onMeasure方法
以上讲的是理论，具体要自己操作才能理解onMeasure的使用方法,我们以实例来演示：
将当前的View以正方形的形式显示，即要宽高相等，并且默认的宽高值为100像素。具体代码如下：

````java
import android.view.View;

/**
 * 作者： JoinYon on 2018/6/13.
 * 邮箱：2816886869@qq.com
 */

public class MyView extends View {

   //...

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
    
    //...
    
}

````

在xml布局中使用：

````xml

 <com.joinyon.androidguide.MyView
        android:layout_width="90dp"
        android:layout_height="wrap_content"
        android:background="#ff0000" />
        
````



## 1.3 重写onDraw方法

我们知道了自定义尺寸大小，接下来就可以把我们想要的效果绘制出来了。
我们先绘制一个简单的效果，直接在Canvas对象上绘制就好了：接下来
绘制一个圆形，在上面已经实现了正方形的基础上，继续做：

````java
import android.view.View;

/**
 * 作者： JoinYon on 2018/6/13.
 * 邮箱：2816886869@qq.com
 */

public class MyView extends View {

   //...

     @Override
       protected void onDraw(Canvas canvas) {
           // 调用父类的onDraw()方法,因为View类
           //帮我们实现了一些基本的绘制功能，比如绘制背景色、背景图片等。
           super.onDraw(canvas);
   
           // 第一步:定义半径。宽或者高的一半
           int r = getMeasuredWidth() / 2;//我们已经将宽高设置相等了，所以随便选择一个。
   
           // 第二步:确定圆心坐标
           int centerX = getLeft() + r;//圆心的横坐标为当前View的左边起始位置+半径。
           int centerY = getTop() + r; //圆心的纵坐标为当前View的顶部起始点+半径。
   
           // 第三步:实例化画笔对象，并设置画笔属性
   
           paint.setColor(Color.BLACK); //黑色画笔
   
           // 第四步:开始绘制圆形
           canvas.drawCircle(centerX, centerY, r, paint);
   
       } 
    
       
    //...
    
}

````
**注意：** 画笔化的只是在画板上画，整个背景色还是有的.
 ![]( https://github.com/MrRobotter/AndroidGuide/tree/master/resource/image/自定义View-1.jpg )

这里有坑使用当我们使用`marginTop `时发现不是一个圆形了。



## 1.4 自定义布局属性
 

# 2 自定义ViewGroup

## 2.1 重写onMeasure方法

## 2.2 要实现的功能

## 2.3 子view的摆放规则

## 2.4 摆放子view