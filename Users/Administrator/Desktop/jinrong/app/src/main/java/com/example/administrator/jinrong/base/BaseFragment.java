package com.example.administrator.jinrong.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.administrator.jinrong.view.LoadingPage;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xushuang on 2016/9/6 0006 13:24.
 * 作用：基类，公共类
 */
public abstract class BaseFragment extends Fragment {
    //上下文
    public  Context mContext;
    private Unbinder unbinder;
    private LoadingPage loadingPage;

    //当BaseFragment被创建的时候被系统调用
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();//getcontext();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       loadingPage = new LoadingPage(container.getContext()) {
            @Override
            public int layoutId() {
                return getLayoutId();
            }

           @Override
           protected void onSuccess(LoadingPage.ResultState resultState, View view_success) {
               unbinder = ButterKnife.bind(BaseFragment.this,view_success);
                       initTitle();
                       initData(resultState.getContent());
           }

           @Override
           public String Url() {
               return gerUrl();
           }
       };
        return loadingPage;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadingPage.show();
    }

    protected abstract String gerUrl();

    public abstract void initData(String content);

    public abstract void initTitle();

    public abstract int getLayoutId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }

}

