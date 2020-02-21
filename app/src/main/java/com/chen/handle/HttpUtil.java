package com.chen.handle;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    public static class Arg{
        private String key;
        private String values;
        public Arg(String key,String values){
            this.key=key;
            this.values=values;
        }
    }

    private static final String TAG="httpUtil";
    private static final String SERVER_URL="http://47.100.43.140";//访问url

    /**
     *发送GET请求
     * @param args 请求参数列表
     * @param callback 回调接口
     */
    public static void sendGetHttpRequest(List<Arg> args,Callback callback){
        try {
            StringBuilder builder=new StringBuilder(SERVER_URL);
            String url;
            builder.append("?");
            for(int i=0;i<args.size();i++){
                Arg arg=args.get(i);
                builder.append(arg.key);
                builder.append("=");
                builder.append(URLEncoder.encode(arg.values,"utf-8"));
                if(i!=args.size()-1){
                    builder.append("&");
                }
            }
            url=builder.toString();
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(url).build();
            client.newCall(request).enqueue(callback);
        }catch (UnsupportedEncodingException e){
            Log.e(TAG, "发送GET请求时编码失败" );
        }
    }

    /**
     * 发送POST请求
     * @param args 请求参数列表
     * @param callback 回调接口
     */
    public static void sendPostHttpRequest(List<Arg> args , Callback callback){
        FormBody.Builder builder =new FormBody.Builder();
        for(Arg arg:args){
            builder.addEncoded(arg.key,arg.values);
        }
        RequestBody requestBody=builder.build();
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(SERVER_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
