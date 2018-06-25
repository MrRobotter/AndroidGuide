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

##### 1.自定义XMLRequest
了解了StringRequest的实现原理，我们可以自己动手尝试一下实现XMLRequest了，代码如下：
```java
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * 作者： JoinYon on 2018/6/23.
 * 邮箱：2816886869@qq.com
 * 自定义Request，接收xml数据
 *
 */

public class XMLRequest extends Request<XmlPullParser> {

    private final Response.Listener<XmlPullParser> mListener;

    public XMLRequest(int method, String url, Response.Listener<XmlPullParser> mListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = mListener;
    }

    /**
     *
     * 默认为GET请求，没写请求方式则为GET
     *
     * @param url
     * @param mListener
     * @param errorListener
     *
     */
    public XMLRequest(String url, Response.Listener<XmlPullParser> mListener, Response.ErrorListener errorListener) {
        this(Method.GET, url, mListener, errorListener);
    }

    @Override
    protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String xmlString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlString));
            return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(XmlPullParser xmlPullParser) {
        mListener.onResponse(xmlPullParser);

    }
}

```
可以看到，其实并没有什么太多的逻辑，基本都是仿照StringRequest写下来的，XMLRequest也是继承自Request类的，
只不过这里指定的泛型是XmlPullParser，说明我们准备使用Pull解析的方式来解析XML。在parseNetworkResponse()
方法中，先是将服务器响应的数据解析成一个字符串，然后设置XmlPullParse对象中，在deliverResponse()方法中则
是将XmlPullParser对象进行回调。

##### 2. 自定义GSONRequest
JsonRequest的数据解析是利用Android本身自带的JSONObject和JSONArray来实现的，配合JSONObject和JSONArray
就可以解析出任意格式的JSON数据。不过也许你会觉得使用JSONObject还是太麻烦了，还有很多方法可以让JSON数据解析变得更加
简单，比如GSON对象。遗憾的是，Volley中默认并不支持使用自家的GSON来解析数据，不过没有关系，通过上面的学习，我们已经
知道自定义Request，接下来自定义一个GSONRequest。
* 首先我们需要把GSON的jar包导入到项目当中，接着定义一个GSONRequest继承自Request，代码如下所示：
```java
package com.joinyon.androidguide.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * 作者： JoinYon on 2018/6/25.
 * 邮箱：2816886869@qq.com
 */

public class GSONRequest<T> extends Request<T> {
    private final Response.Listener<T> myListener;
    private Class<T> mClazz;
    private Gson mGson;

    public GSONRequest(int method, String url, Class<T> clazz, Response.Listener<T> myListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.myListener = myListener;
        this.mClazz = clazz;
        this.mGson = new Gson();

    }

    public GSONRequest(String url, Class<T> clazz, Response.Listener<T> myListener, Response.ErrorListener listener) {
        this(Method.GET, url, clazz, myListener, listener);

    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(mGson.fromJson(jsonString, mClazz), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T t) {
        myListener.onResponse(t);
    }
}

```
GSONRequest是Request的子类，提供了两个构造方法，在parseNetworkResponse()方法中，
先是将服务器响应的数据解析出来，然后通过调用Gson的formJson()方法将数据组装成对象，
在 deliverResponse()方法中仍然是将最终的数据进行回调。
具体用法详见项目对应代码。
##### 3. 自定义GSONRequestWithAuth
以上自定义的Request并没有携带参数，如果我们访问服务器时需要传参呢？如我们通过客户端访问服务器，服务器对客户端进行身份校验后，返回用户信息，客户端直接拿到对象。

## Volley架构解析
### 总体设计图
![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/Volley架构图.png )

上面是Volley的总体设计图，主要是通过两种 Diapatch Thread不断从Request()中取出请求，根据是否已缓存调用Cache或者
Network这两类获取接口之一，从内存缓存或者是服务器取得请求的数据，然后交由RequestDelivery去做结果回调处理。

### Volley中的概念
简单介绍一些概念，在详细设计中会仔细介绍。

Volley的调用比较简单，通过newRequestQueue(...)方法新建并启动一个请求队列RequestQueue后，只需要往这个
RequestQueue不断add Request即可。<br/>
* `Volley:` Volley对外暴露的API，通过newRequestQueue(...)方法新建并启动一个请求队列
RequestQueue。<br/>
* `Request:`表示一个请求的抽象类。StringRequest、JsonRequest、ImageRequest都是他
的子类，表示某种类型的请求。<br/>
* `RequestQueue:`表示请求队列，里面包含一个CacheDispatch(用于处理走缓存的调度线程)、NetworkDispatch数组(用于处理走网络请求的调度线程),
一个ResponseDelivery(返回结果分发接口)，通过start()方法启动时会启动CacheDispatcher和NetworkDispatchers。
* `CacheDispatcher：`一个线程，用于调度处理走缓存的请求。启动后会不断从缓存请求队列中取请求处理，队列为空则等待，请求处理结束则将结果传递给
ResponseDelivery去执行后续处理。当结果未缓存过、缓存失效或缓存需要刷新的情况下，该请求都需要重新进入NetworkDispatcher去调度处理。
* `NetworkDispatche：r`一个线程，用于调度处理走网络的请求。启动后会不断从网络请求队列中取请求处理，队列为空则等待，请求处理结束则将结果
传递给ResponseDelivery去执行后续处理，并判断结果是否要信息缓存。
* `ResponseDelivery:`返回结果分发接口,目前只有基于ExecutorDelivery的在入参handler对应线程内进行分发。
* `HttpStack：`处理Http请求，返回请求结果。目前Volley中有基于HttpURLConnection的HurlStack和基于Apache HttpClient的HttpClient的
HttpClientStack。
* `Network：`用于HttpStack处理请求，并将结果转换为被ResponseDelivery处理的workResponse。
* `Cache：`缓存请求结果，Volley默认使用的是基于sdcard的DiskBasedCache。NetworkDispatcher得到请求结果后判断是否需要存储在Cache、
CacheDispatcher会从Cache中取缓存结果。

### 流程图

Volley流程图.png
![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/Volley流程图.png )
其中蓝色部分代表主线程，绿色部分代表缓存线程，橙色部分代表网格线程。当我们在主线程中调用RequestQueue的add()方法来添加一条网络请求，这条请求就会先被
加入到缓存队列当中，如果发现可以找到相应的缓存结果就直接读取缓存数据并解析，然后回调给主线程。如果在缓存中没有找到结果，则将这条请求加入到网络请求队列中，
然后处理发送HTTP请求，解析响应结果，写入缓存，并回调主线程。
### 源码分析

使用Volley的第一步，首先要调用`Volley.newRequestQueue(context)`方法来获取一个RequestQueue对象，那么我们自然要从这个方法开始着手了，代码如下所示：
````
public static RequestQueue newRequestQueue(Context context) { 
return newRequestQueue(context, null); 
} 
public static RequestQueue newRequestQueue(Context context, HttpStack stack) { 
File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR); 
String userAgent = "volley/0"; 
try { 
String packageName = context.getPackageName(); 
PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0); 
userAgent = packageName + "/" + info.versionCode; 
} catch (NameNotFoundException e) { 
} 
//如果stack是等于null的，则去创建一个HttpStack对象,手机系统版本号是大于9的，则创建一个HurlStack的实例，否则就创建一个HttpClientStack的实例,HurlStack的内部就是使用HttpURLConnection进行网络通讯的，而HttpClientStack的内部则是使用HttpClient进行网络通讯的
if (stack == null) { 
if (Build.VERSION.SDK_INT >= 9) { 
stack = new HurlStack(); 
} else { 
stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent)); 
} 
} 
//创建了一个Network对象，它是用于根据传入的HttpStack对象来处理网络请求的
Network network = new BasicNetwork(stack); 
RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network); 
queue.start(); 
return queue; 
}
````
最终会走到RequestQueue的start()方法，然后将RequestQueue返回。去看看RequestQueue的start()方法内部到底执行了什么？

````
public void start() { 
stop(); // Make sure any currently running dispatchers are stopped. 
//先是创建了一个CacheDispatcher的实例，然后调用了它的start()方法
mCacheDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue, mCache, mDelivery); 
mCacheDispatcher.start(); 
//for循环创建NetworkDispatcher的实例，并分别调用它们的start()方法 
for (int i = 0; i < mDispatchers.length; i++) { 
NetworkDispatcher networkDispatcher = new NetworkDispatcher(mNetworkQueue, mNetwork, mCache, mDelivery); 
mDispatchers[i] = networkDispatcher; 
networkDispatcher.start(); 
} 
}
````
CacheDispatcher和NetworkDispatcher都是继承自Thread的，而默认情况下for循环会执行四次，也就是说当调用了Volley.newRequestQueue(context)
之后，就会有五个线程一直在后台运行，不断等待网络请求的到来，其中CacheDispatcher是缓存线程，NetworkDispatcher是网络请求线程。
得到了RequestQueue之后，我们只需要构建出相应的Request，然后调用RequestQueue的add()方法将Request传入就可以完成网络请求操作了，来看看add()方法吧：
````
public <T> Request<T> add(Request<T> request) { 
// Tag the request as belonging to this queue and add it to the set of current requests. 
request.setRequestQueue(this); 
synchronized (mCurrentRequests) { 
mCurrentRequests.add(request); 
} 
// Process requests in the order they are added. 
request.setSequence(getSequenceNumber()); 
request.addMarker("add-to-queue"); 
//判断当前的请求是否可以缓存，如果不能缓存则直接将这条请求加入网络请求队列
if (!request.shouldCache()) { 
mNetworkQueue.add(request); 
return request; 
} 
// Insert request into stage if there's already a request with the same cache key in flight. 
synchronized (mWaitingRequests) { 
String cacheKey = request.getCacheKey(); 
if (mWaitingRequests.containsKey(cacheKey)) { 
// There is already a request in flight. Queue up. 
Queue<Request<?>> stagedRequests = mWaitingRequests.get(cacheKey); 
if (stagedRequests == null) { 
stagedRequests = new LinkedList<Request<?>>(); 
} 
stagedRequests.add(request); 
mWaitingRequests.put(cacheKey, stagedRequests); 
if (VolleyLog.DEBUG) { 
VolleyLog.v("Request for cacheKey=%s is in flight, putting on hold.", cacheKey); 
} 
} else { 
//当前的请求可以缓存的话则将这条请求加入缓存队列
mWaitingRequests.put(cacheKey, null); 
mCacheQueue.add(request); 
} 
return request; 
} 
}
````
默认情况下，每条请求都可以缓存的，当然我们也可以调用Request的ShouldCache(false)方法来改变这
一默认行为。既然默认每条请求都是可以缓存的，自然就被添加到了缓存队列中，于是一直在后台等待的缓存线程
就要开始运行起来了，我们看预习CacheDispatcher中的run()方法。
```java
public class CacheDispatcher extends Thread { 
    //…… 
@Override
public void run() { 
if (DEBUG) VolleyLog.v("start new dispatcher"); 
Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND); 
// Make a blocking call to initialize the cache. 
mCache.initialize(); 
while (true) { 
try { 
// Get a request from the cache triage queue, blocking until 
// at least one is available. 
final Request<?> request = mCacheQueue.take(); 
request.addMarker("cache-queue-take"); 
// If the request has been canceled, don't bother dispatching it. 
if (request.isCanceled()) { 
request.finish("cache-discard-canceled"); 
continue; 
} 
//尝试从缓存当中取出响应结果 
Cache.Entry entry = mCache.get(request.getCacheKey()); 
if (entry == null) { 
request.addMarker("cache-miss"); 
// 如何为空的话则把这条请求加入到网络请求队列中
mNetworkQueue.put(request); 
continue; 
} 
// 如果不为空的话再判断该缓存是否已过期，如果已经过期了则同样把这条请求加入到网络请求队列中
if (entry.isExpired()) { 
request.addMarker("cache-hit-expired"); 
request.setCacheEntry(entry); 
mNetworkQueue.put(request); 
continue; 
} 
//没有过期就认为不需要重发网络请求，直接使用缓存中的数据即可 
request.addMarker("cache-hit"); 
//对数据进行解析 
Response<?> response = request.parseNetworkResponse( 
new NetworkResponse(entry.data, entry.responseHeaders)); 
request.addMarker("cache-hit-parsed"); 
if (!entry.refreshNeeded()) { 
// Completely unexpired cache hit. Just deliver the response. 
mDelivery.postResponse(request, response); 
} else { 
// Soft-expired cache hit. We can deliver the cached response, 
// but we need to also send the request to the network for 
// refreshing. 
request.addMarker("cache-hit-refresh-needed"); 
request.setCacheEntry(entry); 
// Mark the response as intermediate. 
response.intermediate = true; 
// Post the intermediate response back to the user and have 
// the delivery then forward the request along to the network. 
mDelivery.postResponse(request, response, new Runnable() { 
@Override
public void run() { 
try { 
mNetworkQueue.put(request); 
} catch (InterruptedException e) { 
// Not much we can do about this. 
} 
} 
}); 
} 
} catch (InterruptedException e) { 
// We may have been interrupted because it was time to quit. 
if (mQuit) { 
return; 
} 
continue; 
} 
} 
} 
}

```
看一下NetworkDispatcher是怎么处理网络请求队列的：
````java
public class NetworkDispatcher extends Thread { 
    //...
@Override
public void run() { 
Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND); 
Request<?> request; 
while (true) { 
try { 
// Take a request from the queue. 
request = mQueue.take(); 
} catch (InterruptedException e) { 
// We may have been interrupted because it was time to quit. 
if (mQuit) { 
return; 
} 
continue; 
} 
try { 
request.addMarker("network-queue-take"); 
// If the request was cancelled already, do not perform the 
// network request. 
if (request.isCanceled()) { 
request.finish("network-discard-cancelled"); 
continue; 
} 
addTrafficStatsTag(request); 
//调用Network的performRequest()方法来去发送网络请求 
NetworkResponse networkResponse = mNetwork.performRequest(request); 
request.addMarker("network-http-complete"); 
// If the server returned 304 AND we delivered a response already, 
// we're done -- don't deliver a second identical response. 
if (networkResponse.notModified && request.hasHadResponseDelivered()) { 
request.finish("not-modified"); 
continue; 
} 
// Parse the response here on the worker thread. 
Response<?> response = request.parseNetworkResponse(networkResponse); 
request.addMarker("network-parse-complete"); 
// Write to cache if applicable. 
// TODO: Only update cache metadata instead of entire record for 304s. 
if (request.shouldCache() && response.cacheEntry != null) { 
mCache.put(request.getCacheKey(), response.cacheEntry); 
request.addMarker("network-cache-written"); 
} 
// Post the response back. 
request.markDelivered(); 
mDelivery.postResponse(request, response); 
} catch (VolleyError volleyError) { 
parseAndDeliverNetworkError(request, volleyError); 
} catch (Exception e) { 
VolleyLog.e(e, "Unhandled exception %s", e.toString()); 
mDelivery.postError(request, new VolleyError(e)); 
} 
} 
} 
}

````
调用Network的performRequest()方法去发送网络请求，而Network是一个接口，
这里具体的实现是BasicNetwork,我们来看下它的performRequest()方法。
```java
public class BasicNetwork implements Network { 
//…… 
@Override
public NetworkResponse performRequest(Request<?> request) throws VolleyError { 
long requestStart = SystemClock.elapsedRealtime(); 
while (true) { 
HttpResponse httpResponse = null; 
byte[] responseContents = null; 
Map<String, String> responseHeaders = new HashMap<String, String>(); 
try { 
// Gather headers. 
Map<String, String> headers = new HashMap<String, String>(); 
addCacheHeaders(headers, request.getCacheEntry()); 
//调用了HttpStack的performRequest()方法，这里的HttpStack就是在一开始调用newRequestQueue()方法是创建的实例，默认情况下如果系统版本号大于9就创建的HurlStack对象，否则创建HttpClientStack对象 
httpResponse = mHttpStack.performRequest(request, headers); 
StatusLine statusLine = httpResponse.getStatusLine(); 
int statusCode = statusLine.getStatusCode(); 
responseHeaders = convertHeaders(httpResponse.getAllHeaders()); 
// Handle cache validation. 
if (statusCode == HttpStatus.SC_NOT_MODIFIED) { 
//将服务器返回的数据组装成一个NetworkResponse对象进行返回
return new NetworkResponse(HttpStatus.SC_NOT_MODIFIED, 
request.getCacheEntry() == null ? null : request.getCacheEntry().data, 
responseHeaders, true); 
} 
// Some responses such as 204s do not have content. We must check. 
if (httpResponse.getEntity() != null) { 
responseContents = entityToBytes(httpResponse.getEntity()); 
} else { 
// Add 0 byte response as a way of honestly representing a 
// no-content request. 
responseContents = new byte[0]; 
} 
// if the request is slow, log it. 
long requestLifetime = SystemClock.elapsedRealtime() - requestStart; 
logSlowRequests(requestLifetime, request, responseContents, statusLine); 
if (statusCode < 200 || statusCode > 299) { 
throw new IOException(); 
} 
return new NetworkResponse(statusCode, responseContents, responseHeaders, false); 
} catch (Exception e) { 
//…… 
} 
} 
} 
}
```
在NetworkDispatcher中接收到了NetworkResponse这个返回值后又会调用Request的parseNetworkResponse()
方法来解析NetworkResponse中的数据，以及将数据写入到缓存，这个方法的实现是交给Request的子类来完成的，
因为不同种类的Request解析的方式也肯定不同。我们自定义Request的方式中parseNetworkResponse()这个方法就是
必须要重写的。
在解析完了NetworkResponse中的数据之后，又会调用ExecutorDelivery的postResponse()方法来回调
解析出的数据。
````
public void postResponse(Request<?> request, Response<?> response, Runnable runnable) { 
request.markDelivered(); 
request.addMarker("post-response"); 
mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, runnable)); 
}
````

在mResponsePoster的execute()方法中传入了一个ResponseDeliveryRunnable对象，就可以保证该对象的run()
方法就是在主线程当中运行的了，我们看下run()方法中的代码是怎么样的：
```java
private class ResponseDeliveryRunnable implements Runnable { 
private final Request mRequest; 
private final Response mResponse; 
private final Runnable mRunnable; 
public ResponseDeliveryRunnable(Request request, Response response, Runnable runnable) { 
mRequest = request; 
mResponse = response; 
mRunnable = runnable; 
} 
@SuppressWarnings("unchecked") 
@Override
public void run() { 
// If this request has canceled, finish it and don't deliver. 
if (mRequest.isCanceled()) { 
mRequest.finish("canceled-at-delivery"); 
return; 
} 
// Deliver a normal response or error, depending. 
if (mResponse.isSuccess()) { 
mRequest.deliverResponse(mResponse.result); 
} else { 
mRequest.deliverError(mResponse.error); 
} 
// If this is an intermediate response, add a marker, otherwise we're done 
// and the request can be finished. 
if (mResponse.intermediate) { 
mRequest.addMarker("intermediate-response"); 
} else { 
mRequest.finish("done"); 
} 
// If we have been provided a post-delivery runnable, run it. 
if (mRunnable != null) { 
mRunnable.run(); 
} 
} 
}
```
其中在第22行调用了Request的deliverResponse()方法，
有没有感觉很熟悉，没错，这个就是我们在自定义Request时需要重新的一个方法，
每一条网络请求的响应都是回调这个方法中，
最后我们再在这个方法中将响应的数据回调到Response.Listener的onResponse()方法中既可。



# 参考文章
https://www.jb51.net/article/93177.htm
https://blog.csdn.net/qq_14902389/article/details/53379465
https://blog.csdn.net/u011324501/article/details/53933212
https://www.jianshu.com/p/c1a3a881a144




 
 
 



