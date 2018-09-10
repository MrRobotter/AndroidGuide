package com.joinyon.androidguide.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： JoinYon on 2018/6/25.
 * 邮箱：2816886869@qq.com
 */

public class GSONRequestWithAuth<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Response.Listener<T> mListener;
    private Map<String, String> mHeader = new HashMap<String, String>();
    private String mBody;

    /**
     * http请求编码方式：
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    static {
//        mHeader.put("APP-Key","Key");
//        mHeader.put("APP-Secret","Secret");
    }

    /**
     * 构造方法
     *
     * @param method
     * @param url
     * @param clazz
     * @param mListener
     * @param listener
     */

    public GSONRequestWithAuth(int method, String url, Class<T> clazz, Response.Listener<T> mListener, Map<String, String> appendHeader, String body, Response.ErrorListener listener) {
        super(method, url, listener);
        this.clazz = clazz;
        this.mListener = mListener;


    }

    public GSONRequestWithAuth(String url, Class<T> clazz, Response.Listener<T> mListener, Map<String, String> appendHeader, String body, Response.ErrorListener listener) {
        this(Method.GET, url, clazz, mListener, appendHeader, body, listener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        return null;
    }

    @Override
    protected void deliverResponse(T t) {

    }
}
