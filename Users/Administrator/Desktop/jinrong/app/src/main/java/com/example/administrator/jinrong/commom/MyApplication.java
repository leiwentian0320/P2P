package com.example.administrator.jinrong.commom;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by shkstart on 2016/11/12 0012.
 */
public class MyApplication extends Application {

    public static Context context;//应用中使用到的上下文
    public static Handler handler;//应用中使用到的handler对象声明
    public static Thread mainThread;//获取主线程
    public static int mainThreadId;//获取主线程的id

    @Override
    public void onCreate() {
        super.onCreate();

        context = this.getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();//当前用于初始化Application的线程，即为主线程
        mainThreadId = android.os.Process.myTid();//获取当前主线程的id

       // 设置出现未捕获异常时的处理类
      //  CrashHandler.getInstance().init();
        //初始化分享模块的操作
//        ShareSDK.initSDK(this);

        //设置okhttputils连接超时
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
