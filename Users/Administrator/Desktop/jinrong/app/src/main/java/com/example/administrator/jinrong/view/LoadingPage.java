package com.example.administrator.jinrong.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.administrator.jinrong.R;
import com.example.administrator.jinrong.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public abstract class LoadingPage extends FrameLayout {

    //1.提供联网操作的4种状态
    private final int STATE_LOADING = 1;//加载状态
    private final int STATE_ERROR = 2;//联网失败的状态
    private final int STATE_EMPTY = 3;//联网成功，但是返回数据为空的状态
    private final int STATE_SUCCESS = 4;//联网成功，且正确返回数据的状态
    private final Context mContext;


    //2.提供4个不同的页面
    private View view_loading;
    private View view_error;
    private View view_empty;
    private View view_success;

    private int state_current = STATE_LOADING;//表示当前的状态
    public LoadingPage(@NonNull Context context) {
       this(context,null);
    }

    public LoadingPage(@NonNull Context context, @Nullable AttributeSet attrs) {
       this(context, attrs,0);
    }

    public LoadingPage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        //初始化必要的View
        //指明视图显示宽高的参数
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (view_loading == null) {
            view_loading = UIUtils.getView(R.layout.page_loading);
            addView(view_loading, params);
        }
        if (view_error == null) {
            view_error = UIUtils.getView(R.layout.page_error);
            addView(view_error, params);
        }
        if (view_empty == null) {
            view_empty = UIUtils.getView(R.layout.page_empty);
            addView(view_empty, params);
        }

        //根据state_current的值，决定显示哪个具体的View
        showSafePage();
    }

    //根据state_current的值，决定显示哪个具体的View
    private void showSafePage() {
        //更新界面的操作需要在主线程中执行
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //加载显示具体的View
                showPage();
            }
        });
    }

    //主线程中：根据state_current的值，决定显示哪个具体的View
    private void showPage() {
        view_loading.setVisibility(state_current == STATE_LOADING ? View.VISIBLE : View.GONE);
        view_error.setVisibility(state_current == STATE_ERROR ? View.VISIBLE : View.GONE);
        view_empty.setVisibility(state_current == STATE_EMPTY ? View.VISIBLE : View.GONE);

        if (view_success == null) {
//            view_success = UIUtils.getView(layoutId());
            //修改为如下的加载：
            view_success = View.inflate(mContext,layoutId(),null);
            addView(view_success);
        }

        view_success.setVisibility(state_current == STATE_SUCCESS ? View.VISIBLE : View.GONE);
    }

    public abstract int layoutId();


    private ResultState resultState;


    //在show方法中执行联网操作

    public void show() {

        String url = Url();
        if(TextUtils.isEmpty(url)) {
            resultState = ResultState.SUCCESS;
            resultState.setContent("");
            loadImage();
            return;

        }
        OkHttpUtils.get()
                   .url(url)
                   .build()
                   .execute(new StringCallback() {
                       @Override
                       public void onError(Call call, Exception e, int id) {

                           Log.e("TAG", "请求失败");
//                           state_current = STATE_ERROR;
//                           showSafePage();
                           resultState = ResultState.ERROR;
                           resultState.setContent("");
                           loadImage();

                       }

                       @Override
                       public void onResponse(String response, int id) {
                           if(TextUtils.isEmpty(response)) {
                               Log.e("TAG", "数据为空");
                               //成功，数据为空
//                               state_current = STATE_EMPTY;
                               resultState = ResultState.EMPTY;
                               resultState.setContent("");

                           }else {
                               //返回成功，并且有数据
                               Log.e("TAG", "请求成功");
//                               state_current = STATE_SUCCESS;
                               resultState = ResultState.SUCCESS;
                               resultState.setContent(response);

                           }

                           loadImage();

                       }
                   });
    }

    private void loadImage() {
        switch (resultState) {
            case ERROR:
                state_current = STATE_ERROR;
                break;
            case EMPTY:
                state_current = STATE_EMPTY;
                break;
            case SUCCESS:
                state_current = STATE_SUCCESS;
                break;
        }

        showSafePage();

        if (state_current == STATE_SUCCESS) {//如果当前是联网成功的状态
            onSuccess(resultState, view_success);
        }
    }

    protected abstract void onSuccess(ResultState resultState, View view_success);

    /**
     * 公共的联网url地址暴露出去
     * @return
     */
    public abstract String Url();


    //提供一个枚举类:将当前联网以后的状态以及可能返回的数据，封装在枚举类中
    public enum ResultState {

        ERROR(2), EMPTY(3), SUCCESS(4);

        int state;
        private String content;

        ResultState(int state) {
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
