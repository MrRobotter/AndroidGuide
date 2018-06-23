# Volley的使用及源码分析
开发Android应用的时候大多数都会涉及到网络操作，Android SDK中提供了
[HttpClient]()和[HttpUrlConnection]()两种方式来处理网络操作，但当应用
比较复杂的时候需要我们编写大量的代码处理很多东西：图像缓存，请求的调度等；而
Volley框架就是为了解决这些而生的，它于2013年Google I/O大会上被提出：使得
Android应用网络操作更方便更快捷；抽象了底层HttpClient等实现的细节，让开发者
更专注于产生的Restful Request，另外，Volley在不同的线程上异步执行所有请求
而避免了阻塞主线程。

## Volley的应用场景
适用于数据量不大通讯频繁的场景，并发，快速。
## Volley的原理
将数据添加到网络请求队列->从缓存线程取请求，如果有 在缓存线程返回到主线程，
如果没有 取网络请求线程下载数据，在进行解析，写入缓存 返回主线程,如下图所示：
![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/Volley原理图.jpg )
## Volley的特点
* 自动调度网络请求
* 多个并发的网络连接
* 通过使用标准的HTTP缓存机制保持磁盘和内存响应的一致
* 支持请求优先级
* 支持取消请求的强大API，可以取消单个或多个请求
* 易于定制
* 健壮性：便于正确的更新UI和获取数据
* 包含调试和追踪工具

## Volley的使用
**Volley中的RequestQueue和Request**
* RequestQueue用来执行请求的请求队列
* Request用来构造一个请求对象
* Request对象分以下几种类型：
    * StringRequest响应的主体为字符串
    * JsonArrayRequest 发送和接收JSON数组
    * JsonObjectRequest 发送和接收JSON对象
    * ImageRequest 发送和接收图片
### 首先导入jar包
下载一个volley.jar包导入libs下，[点这里下载](),申请网络权限
````xml
    <uses-permission android:name="android.permission.INTERNET" />
````
### HTTP请求与响应
一个最基本HTTP请求与响应主要就是分以下三大步操作：
1. 创建一个RequestQueue对象
2. 创建一个Request对象
3. 将Request对象添加到RequestQueue里

下面根据具体情况分析：

#### StringRequest
* 初始化请求队列对象： `RequestQueue`
 ````java
    public class VolleyActivity extends AppCompatActivity {
    
     //...
    
     RequestQueue requestQueue=Volley.newRequestQueue(this);
     
     //...
        }
 ````
 _RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，
 然后按照一定的算法并发地发出这些请求。RequestQueue内部的设计就是非常适合高并发的，
 因此我们不必为每一次HTTP请求都创建一个RequestQueue对象，这是非常浪费资源的。所以
 这里建议用单例模式定义这个对象。当然我们也可以选择在一个Activity中定义一个
 RequestQueue对象，但是这样可能会比较麻烦，而且还可能出现请求队列包含activity
 强引用的问题。_
 * 使用StringRequest接收String类型的响应
 Volley框架中有多个响应对象，StringRequest接收到的响应是String类型的，
 JSONRequest接收到的响应就是Json类型对象，其实他们都是继承自  `Request<T>`任何根据
 数据的不同来进行特殊处理
 ````java
     public class VolleyActivity extends AppCompatActivity {
     
      //...
    

     
     // 获取请求对象
             //参数1：请求方式(默认为GET请求,不写是GET)；参数2：请求的地址；参数3：请求成功后的回调；参数4：请求失败的回调；
             //
             StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String s) {
                     Log.d("VolleyActivity", s);
                     tvContent.setText(s);
                 }
             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError volleyError) {
                     Log.d("VolleyActivity", volleyError.toString());
                     tvContent.setText("onError" + volleyError);
                 }
             });
           
          
      //...
         }
  ````
  * 给Request设置标记
  * 将Request添加到RequestQueue中
  * 在onDestroy方法中取消已经在请求队列的请求
  
  #### 使用JsonObjectRequest接收Json类的请求
  套路一样，看代码：
   ````java
      public class VolleyActivity extends AppCompatActivity {
      
       //...
      
       /**
           * json请求
           */
          private void jsonRequest() {
              //创建一个请求队列
              requestQueue = Volley.newRequestQueue(VolleyActivity.this);
              //创建一个请求
              JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject jsonObject) {
                      Log.e("jsonRequest", jsonObject.toString());
                  }
              }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError volleyError) {
                      Log.e("jsonRequest", "onError" + volleyError);
                  }
              });
              //给请求设置标记
              jsonObjectRequest.setTag("q1");
              //将请求放到请求队列
              requestQueue.add(jsonObjectRequest);
      
          }
       
       //...
          }
   ```````
   #### 使用ImageRequest请求图片
   
   首先看一下ImageRequest的构造方法
   ````
   public ImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, Config decodeConfig, Response.ErrorListener errorListener) {
   super(Method.GET, url, errorListener);
   setRetryPolicy(new DefaultRetryPolicy(IMAGE_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
   mListener = listener;
   mDecodeConfig = decodeConfig;
   mMaxWidth = maxWidth;
   mMaxHeight = maxHeight;
   }
   
   ````
默认的请求是GET，初始化需要传入的参数分别是：图片的URL，一个响应结果监听器，
图片的最大宽度，图片的最大高度，图片的颜色属性，出错响应的监听器。

第三第四个参数分别用于指定允许图片最大的宽度和高度，
如果指定的网络图片的宽度或高度大于这里的最大值，则会对图片进行 "等比例"进行压缩，
指定成0的话就表示不管图片有多大，都不会进行压缩。第五个参数用于指定图片的颜色属性，
Bitmap.Config下的几个常量都可以在这里使用，其中ARGB_8888可以展示最好的颜色
属性，每个图片像素占据4个字节大小，而RGB_565则表示每个图片像素占据2个字节大小。

走起：
 ````java
      public class VolleyActivity extends AppCompatActivity {
      
       //...
      
        private void imageRequest() {
               requestQueue = Volley.newRequestQueue(VolleyActivity.this);
               String imgUrl = "http://img5.mtime.cn/mg/2016/12/26/164311.99230575.jpg";
       
               ImageRequest imageRequest = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
                   @Override
                   public void onResponse(Bitmap bitmap) {
                       imageView.setImageBitmap(bitmap);
                   }
               }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError volleyError) {
                       imageView.setImageResource(R.mipmap.ic_launcher);
                   }
               });
       
               imageRequest.setTag("q1");
       
               requestQueue.add(imageRequest);
           }

       
       //...
          }
   ```````
   #### 加载图片神器：ImageLoader&NetWorkImageView
   这两个是Volley的内置对象，专门来处理图片加载的，使用它们可以更方便有效地获取图片
   它们内部都是使用了ImageRequest进行操作的。
   
   * **ImageLoader加载图片**
   ImageLoader也可以用于加载网络上的图片，不过ImageLoader明细要比ImageRequest
   更加高效，因为它不仅仅帮助我们对图片进行缓存，还可以过滤掉重复的链接，避免重复发送
   请求，由于ImageLoader已经不是继承自Request的了，所以用法有所不同，可以分为四步：
          
         1. 创建一个RequestQueue对象
         2. 创建一个ImageLoader对象
         3. 获取一个ImageListener对象
         4. 调用ImageLoader的get()方法加载网络上的图片。


具体代码示例：

````java
      public class VolleyActivity extends AppCompatActivity {
      
       //...
      
        /**
            * imageLoader
            */
           private void imageLoader() {
                //1. 创建一个RequestQueue对象
               requestQueue = Volley.newRequestQueue(VolleyActivity.this);
                // 2.创建一个ImageLoader对象
               ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
         
               String url = "http://img.my.csdn.net/uploads/201309/01/1378037151_7904.jpg";
                //3.获取一个ImageListener对象
               ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher_round);
                //4.调用ImageLoader的get()方法加载网络上的图片。
               imageLoader.get(url, imageListener);
       
           }
       
       //...
       }
       
      
       
 ````  
 可以看到，ImageLoader的构造方法接收两个参数，第一个是RequestQueue对象，第二个参数是ImageCache对象
 (不能为null)，这里的ImageCache就是为我们做内存缓存用的，我们可以定制自己的实现方式，
 现在主流的实现是[LruCache]()以下是我的实现方式：
 ```java
import com.android.volley.toolbox.ImageLoader;

/**
 * 作者： JoinYon on 2018/6/23.
 * 邮箱：2816886869@qq.com
 */

public class BitmapCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mCache;

    public BitmapCache() {
        int maxSize = 10 * 1024 * 1024;//10MB
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {

                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {


        return mCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        mCache.put(s, bitmap);
    }
}
```

 * **NetworkImageView控件加载图片**
 NetworkImageView继承自ImageView，我们可以认为它是一个可以实现加载网络图片的
 ImageView，十分简单好用，这个控件在被从父控件分离的时候，会自动取消网络请求的，
 即完全不用我们担心相关网络请求的生命周期问题。
 以下为实现过程，十分简单：
 
 ````java
       public class VolleyActivity extends AppCompatActivity {
       
        //...
       
         private void networkImageView() {
                //创建一个RequestQueue对象
                requestQueue = Volley.newRequestQueue(VolleyActivity.this);
                //创建一个ImageLoader对象
                ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
                //在布局中加一个NetworkImageView控件并找到控件 设置默认，失败图片
                NetworkImageView networkImageView = findViewById(R.id.networkImageView);
                networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                networkImageView.setErrorImageResId(R.mipmap.ic_launcher_round);
                String imageUrl="https://f11.baidu.com/it/u=1193253070,1409867335&fm=76";
                //设置要加载的图片地址
                networkImageView.setImageUrl(imageUrl, imageLoader);
        
            }
        
        //...
        }
        
       
        
  ````  
  NetworkImageView没有提供任何设置图片宽高的方法，这是由于它是一个控件，在加载图片的时候
  会自动获取自身的宽高，然后对比与网络图片的宽度，再决定是否要对图片进行压缩，
  压缩过程是在内部自动化完成的，并不需要我们关心。它最终会始终呈现给我们一张大小比控件尺寸略大的
  网络图片，会根据控件宽高来等比缩放原始图片，不会占用任何一点内存。如果不想对图片进行压缩的话，
  只需要在布局文件中 `layout_width`和 `layout_height`都设为 `wrap_conent`就可以了，
  这样会按图片原始大小展示，不进行任何压缩。
  
 #### 自定义Request
 Volley中提供了几个常用的Request(StringRequest、JsonObjectRequest、JsonArrayRequest、ImageRequest),
 如果我们有自己的需求，其实完全可以自定义自己的Request。
 
 首先我们先看一下StringRequest的源码实现：
 ```package com.android.volley.toolbox;
    public class StringRequest extends Request<String> {
    // 建立监听器来获得响应成功时返回的结果
    private final Listener<String> mListener; 
    // 传入请求方法，url，成功时的监听器，失败时的监听器
    public StringRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
    super(method, url, errorListener);
    // 初始化成功时的监听器
    mListener = listener;
    }
    /**
    * Creates a new GET request.
    * 建立一个默认的GET请求，调用了上面的构造函数
    */
    public StringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
    this(Method.GET, url, listener, errorListener);
    }
    @Override
    protected void deliverResponse(String response) {
    // 用监听器的方法来传递下响应的结果
    mListener.onResponse(response);
    }
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
    String parsed;
    try {
    // 调用了new String(byte[] data, String charsetName) 这个构造函数来构建String对象，将byte数组按照特定的编码方式转换为String对象，主要部分是data
    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
    } catch (UnsupportedEncodingException e) {
    parsed = new String(response.data);
    }
    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
    }
```
* 首先StringRequest是继承于Request类的，Request可以指定一个泛型类，这里指定的
是String。
* 接下来StringRequest中提供了两个有参的构造方法，参数包括请求类型，请求地址，以及
响应回调等。但需要注意的是在构造方法中一定要调用super()方法将这几个参数传给父类，因为
HTTP的请求和响应都是在父类中自动处理的。
* 另外，由于Request类中的`deliverResponse()`和`parseNetworkResponse()`是两个抽象
方法，因此StringRequest中需要对这两个方法进行实现。`deliverResponse()`方法中的实现
很简单，仅仅是调用了mListener中的`onResponse()`方法，并将response内容传入即可，这样
就可以将服务器响应的数据进行回调了。`parseNetworkResponse()`方法中则是对服务器响应的
数据进行解析，其中数据是以字节的形式存放在NetworkResponse的data变量的，这里将数据取出
然后组装成一个String,并传入Response的`success()`方法中即可。

 
 
 



