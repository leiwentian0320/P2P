package com.example.administrator.jinrong.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.administrator.jinrong.commom.ActivityManager;


import butterknife.ButterKnife;

/**
 * 提供通用的Activity的使用的基类
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        //将当前的activity添加到ActivityManager中
        ActivityManager.getInstance().add(this);

        initTitle();
        initData();

    }
    //初始化title
    protected abstract void initTitle();

    //初始化内容数据
    protected abstract void initData();

    //提供加载的布局的方法
    protected abstract int getLayoutId();

    //销毁当前的activity
    public void removeCurrentActivity(){
        ActivityManager.getInstance().removeCurrent();
    }

    //启动新的activity
    public void goToActivity(Class activity,Bundle bundle){
        Intent intent = new Intent(this, activity);
        if(bundle != null && bundle.size() != 0){
            intent.putExtra("data",bundle);
        }

        startActivity(intent);
    }

    //销毁所有的Activity
    public void removeAll(){
        ActivityManager.getInstance().removeAll();
    }



}
