package com.joinyon.androidguide.net;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joinyon.androidguide.R;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Volley的的使用步骤：
 * 1.导入Volley
 * 2.实例化请求队列
 * 3.获取请求对象
 * 4.给请求设置标记
 * 5.将请求放到请求队列
 * 6.在onDestroy方法里取消请求
 */

public class VolleyActivity extends AppCompatActivity {
    private TextView tvContent;
    private ImageView imageView;
    // 声明请求队列
    private RequestQueue requestQueue;

    String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        tvContent = findViewById(R.id.tvContent);
        imageView = findViewById(R.id.imageView);

    }

    public void onClick(View view) {
        //stringRequestByGet();
        //setRequestQueueByPost();
        //jsonRequest();

        //imageRequest();

        //imageLoader();
        //networkImageView();
        xmlRequest();
        //gsonRequest();
    }

    /**
     * StringRequest的GET请求
     */
    private void stringRequestByGet() {
        // 实例化请求队列
        requestQueue = Volley.newRequestQueue(VolleyActivity.this);
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
        //给请求设置标记
        stringRequest.setTag("q1");
        //将请求放到请求队列
        requestQueue.add(stringRequest);
    }

    /**
     * StringRequest的POST请求
     */
    private void setRequestQueueByPost() {
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(VolleyActivity.this);
        //创建一个请求
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                tvContent.setText(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tvContent.setText("onError" + volleyError);

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                // map.put("value1","param1");//传入参数
                return map;
            }
        };
        //给请求设置标记
        stringRequest.setTag("q1");
        //将请求放到请求队列
        requestQueue.add(stringRequest);
    }

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

    /**
     * imageLoader
     */
    private void imageLoader() {
        //1. 创建一个RequestQueue对象
        requestQueue = Volley.newRequestQueue(VolleyActivity.this);
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        imageView.setImageResource(R.mipmap.ic_launcher);
        String url = "http://img.my.csdn.net/uploads/201309/01/1378037151_7904.jpg";

        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher_round);

//        imageLoader.get(url, new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                imageView.setImageBitmap(imageContainer.getBitmap());
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                imageView.setImageResource(R.mipmap.ic_launcher);
//            }
//        });

        imageLoader.get(url, imageListener);

    }

    private void networkImageView() {
        //创建一个RequestQueue对象
        requestQueue = Volley.newRequestQueue(VolleyActivity.this);
        //创建一个ImageLoader对象
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        //在布局中加一个NetworkImageView控件并找到控件 设置默认，失败图片
        NetworkImageView networkImageView = findViewById(R.id.networkImageView);
        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        networkImageView.setErrorImageResId(R.mipmap.ic_launcher_round);
        String imageUrl = "https://f11.baidu.com/it/u=1193253070,1409867335&fm=76";
        //设置要加载的图片地址
        networkImageView.setImageUrl(imageUrl, imageLoader);

    }

    /**
     * <china dn="nay">
     * <city quName="é»é¾æ±" pyName="heilongjiang" cityname="åå°æ»¨" state1="1" state2="1" stateDetailed="å¤äº" tem1="14" tem2="26" windState="è¥¿é£å¾®é£çº§"/>
     * <city quName="åæ" pyName="jilin" cityname="é¿æ¥" state1="0" state2="0" stateDetailed="æ´" tem1="16" tem2="29" windState="å¾®é£"/>
     * <city quName="è¾½å®" pyName="liaoning" cityname="æ²é³" state1="0" state2="0" stateDetailed="æ´" tem1="19" tem2="33" windState="ä¸åé£å¾®é£çº§"/>
     * <city quName="æµ·å" pyName="hainan" cityname="æµ·å£" state1="1" state2="4" stateDetailed="å¤äºè½¬é·éµé¨" tem1="27" tem2="34" windState="å¾®é£"/>
     * <city quName="åèå¤" pyName="neimenggu" cityname="å¼åæµ©ç¹" state1="2" state2="7" stateDetailed="é´è½¬å°é¨" tem1="19" tem2="28" windState="è¥¿åé£3-4çº§"/>
     * <city quName="æ°ç" pyName="xinjiang" cityname="ä¹é²æ¨é½" state1="0" state2="0" stateDetailed="æ´" tem1="19" tem2="32" windState="å¾®é£"/>
     * <city quName="è¥¿è" pyName="xizang" cityname="æè¨" state1="1" state2="3" stateDetailed="å¤äºè½¬éµé¨" tem1="14" tem2="28" windState="å¾®é£"/>
     * <city quName="éæµ·" pyName="qinghai" cityname="è¥¿å®" state1="3" state2="4" stateDetailed="éµé¨è½¬é·éµé¨" tem1="15" tem2="26" windState="å¾®é£"/>
     * <city quName="å®å¤" pyName="ningxia" cityname="é¶å·" state1="2" state2="7" stateDetailed="é´è½¬å°é¨" tem1="21" tem2="27" windState="å¾®é£"/>
     * <city quName="çè" pyName="gansu" cityname="å°å·" state1="3" state2="3" stateDetailed="éµé¨" tem1="20" tem2="29" windState="å¾®é£"/>
     * <city quName="æ²³å" pyName="hebei" cityname="ç³å®¶åº" state1="0" state2="1" stateDetailed="æ´è½¬å¤äº" tem1="25" tem2="37" windState="å¾®é£è½¬åé£å¾®é£çº§"/>
     * <city quName="æ²³å" pyName="henan" cityname="éå·" state1="1" state2="1" stateDetailed="å¤äº" tem1="22" tem2="34" windState="åé£å¾®é£çº§"/>
     * <city quName="æ¹å" pyName="hubei" cityname="æ­¦æ±" state1="4" state2="4" stateDetailed="é·éµé¨" tem1="24" tem2="32" windState="ä¸åé£è½¬åé£3-4çº§"/>
     * <city quName="æ¹å" pyName="hunan" cityname="é¿æ²" state1="1" state2="1" stateDetailed="å¤äº" tem1="26" tem2="34" windState="ä¸åé£è½¬åé£å¾®é£çº§"/>
     * <city quName="å±±ä¸" pyName="shandong" cityname="æµå" state1="0" state2="0" stateDetailed="æ´" tem1="26" tem2="35" windState="åé£3-4çº§"/>
     * <city quName="æ±è" pyName="jiangsu" cityname="åäº¬" state1="2" state2="4" stateDetailed="é´è½¬é·éµé¨" tem1="21" tem2="31" windState="ä¸é£è½¬ä¸åé£3-4çº§"/>
     * <city quName="å®å¾½" pyName="anhui" cityname="åè¥" state1="1" state2="7" stateDetailed="å¤äºè½¬å°é¨" tem1="21" tem2="29" windState="ä¸é£å¾®é£çº§è½¬ä¸åé£3-4çº§"/>
     * <city quName="å±±è¥¿" pyName="shanxi" cityname="å¤ªå" state1="0" state2="0" stateDetailed="æ´" tem1="19" tem2="35" windState="åé£è½¬ä¸åé£å¾®é£çº§"/>
     * <city quName="éè¥¿" pyName="sanxi" cityname="è¥¿å®" state1="0" state2="1" stateDetailed="æ´è½¬å¤äº" tem1="21" tem2="36" windState="ä¸åé£3-4çº§è½¬å¾®é£çº§"/>
     * <city quName="åå·" pyName="sichuan" cityname="æé½" state1="8" state2="7" stateDetailed="ä¸­é¨è½¬å°é¨" tem1="22" tem2="29" windState="å¾®é£"/>
     * <city quName="äºå" pyName="yunnan" cityname="ææ" state1="8" state2="8" stateDetailed="ä¸­é¨" tem1="17" tem2="24" windState="å¾®é£"/>
     * <city quName="è´µå·" pyName="guizhou" cityname="è´µé³" state1="2" state2="3" stateDetailed="é´è½¬éµé¨" tem1="20" tem2="25" windState="åé£å¾®é£çº§"/>
     * <city quName="æµæ±" pyName="zhejiang" cityname="æ­å·" state1="3" state2="3" stateDetailed="éµé¨" tem1="21" tem2="28" windState="ä¸é£å¾®é£çº§"/>
     * <city quName="ç¦å»º" pyName="fujian" cityname="ç¦å·" state1="1" state2="4" stateDetailed="å¤äºè½¬é·éµé¨" tem1="25" tem2="32" windState="å¾®é£"/>
     * <city quName="æ±è¥¿" pyName="jiangxi" cityname="åæ" state1="2" state2="2" stateDetailed="é´" tem1="26" tem2="33" windState="åé£è½¬è¥¿åé£å¾®é£çº§"/>
     * <city quName="å¹¿ä¸" pyName="guangdong" cityname="å¹¿å·" state1="9" state2="9" stateDetailed="å¤§é¨" tem1="26" tem2="32" windState="ä¸åé£3-4çº§è½¬å¾®é£"/>
     * <city quName="å¹¿è¥¿" pyName="guangxi" cityname="åå®" state1="3" state2="3" stateDetailed="éµé¨" tem1="26" tem2="32" windState="åé£å¾®é£çº§"/>
     * <city quName="åäº¬" pyName="beijing" cityname="åäº¬" state1="0" state2="1" stateDetailed="æ´è½¬å¤äº" tem1="25" tem2="36" windState="è¥¿åé£å¾®é£çº§è½¬åé£3-4çº§"/>
     * <city quName="å¤©æ´¥" pyName="tianjin" cityname="å¤©æ´¥" state1="0" state2="1" stateDetailed="æ´è½¬å¤äº" tem1="27" tem2="36" windState="è¥¿åé£è½¬åé£3-4çº§"/>
     * <city quName="ä¸æµ·" pyName="shanghai" cityname="ä¸æµ·" state1="1" state2="1" stateDetailed="å¤äº" tem1="21" tem2="28" windState="ä¸åé£å¾®é£çº§è½¬3-4çº§"/>
     * <city quName="éåº" pyName="chongqing" cityname="éåº" state1="7" state2="7" stateDetailed="å°é¨" tem1="24" tem2="32" windState="å¾®é£"/>
     * <city quName="é¦æ¸¯" pyName="xianggang" cityname="é¦æ¸¯" state1="7" state2="1" stateDetailed="å°é¨è½¬å¤äº" tem1="27" tem2="31" windState="å¾®é£"/>
     * <city quName="æ¾³é¨" pyName="aomen" cityname="æ¾³é¨" state1="3" state2="3" stateDetailed="éµé¨" tem1="25" tem2="31" windState="åé£3-4çº§"/>
     * <city quName="å°æ¹¾" pyName="taiwan" cityname="å°å" state1="2" state2="7" stateDetailed="é´è½¬å°é¨" tem1="27" tem2="29" windState="ä¸åé£è½¬è¥¿åé£å¾®é£çº§"/>
     * <city quName="è¥¿æ²" pyName="xisha" cityname="è¥¿æ²" state1="4" state2="4" stateDetailed="é·éµé¨" tem1="25" tem2="29" windState="åé£4-5çº§è½¬3-4çº§"/>
     * <city quName="åæ²" pyName="nanshadao" cityname="åæ²" state1="3" state2="3" stateDetailed="éµé¨" tem1="27" tem2="28" windState="è¥¿åé£3-4çº§è½¬å¾®é£"/>
     * <city quName="éé±¼å²" pyName="diaoyudao" cityname="éé±¼å²" state1="7" state2="7" stateDetailed="å°é¨" tem1="25" tem2="27" windState="åé£5-6çº§è½¬4-5çº§"/>
     * </china>
     * <p>
     * 乱码解决方案。。。已解决，具体见XMLRequest.java 注释
     */

    private void xmlRequest() {
        //创建一个RequestQueue对象
        requestQueue = Volley.newRequestQueue(VolleyActivity.this);
        String xmlUrl = "http://flash.weather.com.cn/wmaps/xml/china.xml";
        //创建一个请求
        XMLRequest xmlRequest = new XMLRequest(xmlUrl, new Response.Listener<XmlPullParser>() {
            @Override
            public void onResponse(XmlPullParser xmlPullParser) {
                try {
                    int eventType = xmlPullParser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                String nodeName = xmlPullParser.getName();
                                if ("city".equals(nodeName)) {
                                    String pName = xmlPullParser.getAttributeValue(0);
                                    String cName = xmlPullParser.getAttributeValue(2);
                                    Log.e("省份：" + pName, "省会:" + cName);
                                }
                                break;
                        }
                        eventType = xmlPullParser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        //设置标记
        xmlRequest.setTag("q1");
        //加入到RequestQueue中
        requestQueue.add(xmlRequest);
    }

    private void gsonRequest() {
        //创建一个RequestQueue对象
        requestQueue = Volley.newRequestQueue(VolleyActivity.this);
        String url = "http://www.weather.com.cn/data/sk/101020100.html";
        //实例化一个Request对象
        GSONRequest<Weather> gsonRequest = new GSONRequest<Weather>(url, Weather.class, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather weather) {
                Log.e("gsonRequest", weather.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        gsonRequest.setTag("q1");

        requestQueue.add(gsonRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消请求
        requestQueue.cancelAll(this);
    }


}
