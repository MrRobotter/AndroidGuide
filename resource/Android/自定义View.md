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
           int centerY = getTop() + r; //圆心的纵坐标为当前View的顶部起始点+半径。? 待考究
   
           // 第三步:实例化画笔对象，并设置画笔属性
   
           paint.setColor(Color.BLACK); //黑色画笔
   
           // 第四步:开始绘制圆形
           canvas.drawCircle(centerX, centerY, r, paint);
   
       } 
    
       
    //...
    
}

````
**注意：** 画笔化的只是在画板上画，整个背景色还是有的.
 ![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/自定义View-1.jpg )

这里有坑使用当我们使用`marginTop `时发现不是一个圆形了。
当View在xml中设置margin属性时就会出现不完整。
````xml
<com.joinyon.androidguide.MyView
        android:layout_marginTop="30dp"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:background="@color/colorAccent" />
````

效果如下：
 ![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/自定义View-2.jpg )


## 1.4 自定义布局属性
 我们使用的控件属性可以在xml中指定，比如颜色、大小等，所以我们还需要自定义属性让用户指定控件的一些属性。如果自定义呢？
 * 1. 首先我们需要在 `res/values/styles.xml`文件（没有的话自己新建）里声明一个我们自定义属性：
 ````xml
 <resources>
 
     <!--name为声明的"属性集合名"，可以随便取，建议设置为跟我们自定义View的名称一样-->
     <declare-styleable name="MyView">
         <!--声明我们的属性，名称为default_size，取值类型为尺寸类型（dp，px等）-->
         <attr name="default_size" format="dimension" />
     </declare-styleable>
 
 </resources>
````
* 2. 接下来就是在布局文件上引用我们的自定义属性了：
````xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:hc="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.joinyon.androidguide.android.ViewActivity">

    <com.joinyon.androidguide.MyView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_marginLeft="30dp"
        android:layout_marginTop="100dp"
        android:background="@color/colorAccent"
        hc:default_size="200dp" />
</LinearLayout>
````
 **注意:** 需要在跟标签 `LinearLayout`里面设置命名空间，命名空间名称可以随便取，
比如 `hc`，命名空间后面的取值是固定的：` "http://schemas.android.com/apk/res-auto" `
* 3. 最后就是在我们的自定义的View里面把我们自定义属性的值取出来，
在构造方法中一个AttributeSet对象，就是靠这个对象把布局里面的属性取出来的：


````java
import android.view.View;

/**
 * 作者： JoinYon on 2018/6/13.
 * 邮箱：2816886869@qq.com
 */

public class MyView extends View {
    

   //...

     
        public MyView(Context context, @Nullable AttributeSet attrs) {
               super(context, attrs);
               //第二个参数是我们在styles.xml文件中的<declare-styleable>标签
               //即属性集合的标签，在R文件中名称为R.styleable+name
       
               TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);
       
               //第一个参数为属性集合里面的属性，R文件名称：R.styleable_属性集合名+下划线+属性名称
               defaultSize = typedArray.getDimensionPixelSize(R.styleable.MyView_default_size, 100);
       
               typedArray.recycle();//最后记得将TypedArray对象释放掉；
           } 
    
       
    //...
    
}

````
最后完整代码详见[MyView.java](https://github.com/MrRobotter/AndroidGuide/blob/master/app/src/main/java/com/joinyon/androidguide/MyView.java)文件

# 2 自定义ViewGroup
相比较自定义View，自定义ViewGroup要复杂的多，因为它不仅要管好自己的属性，
还要兼顾他的子View，如果把自定义View比作员工的话，那么自定义ViewGroup就
相当于管理者，不仅仅要管理好自己的工作，还要起到统筹协调手下员工的功能。ViewGroup
是个View的容器，它盛放child view并负责把child view 放入指定的位置。
所以有以下设计思路：
* 首先，需要知道各个子View的大小，只有知道了子View的大小，我们才知道
当前的ViewGroup该设置多大才能装下所有的子View。

* 根据子View的大小，已经我们的ViewGroup要实现的功能，决定出ViewGroup的大小。

* ViewGroup和子View的大小算出来了之后，接下来就是去摆放了，具体怎么摆放，
这得根据定制需求去摆放了，比如：让子View按照垂直顺序一个一个摆放，或者按照先后顺序一个一个叠放上去，看具体需求决定。

* 已经知道怎么摆放也不行，决定怎么摆放就是相当于把已有的空间 "分割"成大大小小的空间，每一个空间对应一个子View，
我们接下来把子View对号入座就行了，把子View摆到他们该放的地方。

有了这样的思路，我们可以开始一个简单的ViewGroup的设计了，接下来实现一个具体的案例：
将子View按从上到下垂直顺序一个挨着一个摆放，即模仿实现LinearLayout的垂直布局。

## 2.1 重写onMeasure方法
按照我们的设计思路首先要重写onMeasure()方法，实现测量子View大小以及设定ViewGroup的大小，由于我们实现的是模仿
LinearLayout的垂直布局，所以有以下两种情况：
* 没有子View的情况，则ViewGroup没有存在的意义，所以宽和高都为0；
* 如果存在子View，则有以下四种情况：
    * 设置ViewGroup宽高都是包裹内容：(ViewGroup的高度为所有子View的相加，ViewGroup的宽度为子View中宽度最大的。)
    * 只设置了ViewGroup高度是包裹内容，指定了宽度：（宽度设置为ViewGroup自己的测量宽度，高度为所有子View的总和。）
    * 只设置了ViewGroup宽度是包裹内容，指定了高度：（宽度是子View的最大值，高度是ViewGroup的测量值。）
    * 指定了宽度和高度：（宽度，高度均为ViewGroup的测量值）
    
**获取子View中宽度最大值的方法：**  
  ````java
  import android.view.ViewGroup;
  
  /**
   * 作者： JoinYon on 2018/6/21.
   * 邮箱：2816886869@qq.com
   */
  
  public class MyViewGroup extends ViewGroup {
      
      //...
  
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
      
      //...
      
  }

  ````
   **获取所有子View的高度相加方法：** 
    
````java
     import android.view.ViewGroup;
     
     /**
      * 作者： JoinYon on 2018/6/21.
      * 邮箱：2816886869@qq.com
      */
     
     public class MyViewGroup extends ViewGroup {
         
         //...
   
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
         
         //...
         
     }
  
````
**重写onMeasure方法：**
````java
     import android.view.ViewGroup;
     
     /**
      * 作者： JoinYon on 2018/6/21.
      * 邮箱：2816886869@qq.com
      */
     
     public class MyViewGroup extends ViewGroup {
         
         //...
   
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

         
         //...
         
     }
  
````
以上就是ViewGroup将子View测量好了，也把自己的尺寸测量好了，接下来就是考虑如何摆放了。
## 2.2 要实现的功能
要实现的功能就是从上到下垂直摆放
## 2.3 子view的摆放规则
具体的操作

## 2.4 摆放子view

重写onLayout方法：
````java
     import android.view.ViewGroup;
     
     /**
      * 作者： JoinYon on 2018/6/21.
      * 邮箱：2816886869@qq.com
      */
     
     public class MyViewGroup extends ViewGroup {
         
         //...
   
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
         
         //...
         
     }
  
````

接下来我们可以测试了

````xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:hc="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.joinyon.androidguide.android.ViewActivity">
    <com.joinyon.androidguide.android.MyViewGroup
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#00ff00">


        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="按钮"
            android:textColor="@color/colorPrimary" />

        <TextView
            android:layout_width="200dp"
            android:layout_height="wrap_content"
            android:background="@android:color/darker_gray"
            android:text="sssss"
            android:textColor="@color/colorAccent"
            android:textSize="20sp" />

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher_round" />

        <Button
            android:layout_width="98dp"
            android:layout_height="wrap_content" />
    </com.joinyon.androidguide.android.MyViewGroup>
</LinearLayout>

````

效果如下图：
![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/自定义ViewGroup-1.jpg )