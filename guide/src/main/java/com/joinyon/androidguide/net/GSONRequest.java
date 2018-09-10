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
            String jsonString = new String(networkResponse.data, "utf-8");//此处第二个参数是编码方式，
            // 设置为HttpHeaderParser.parseCharset(networkResponse.headers)是使用服务器编码默认，此处强制 utf-8 解决乱码问题
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
