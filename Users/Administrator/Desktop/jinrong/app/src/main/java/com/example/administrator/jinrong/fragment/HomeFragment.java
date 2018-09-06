package com.example.administrator.jinrong.fragment;



import android.os.SystemClock;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.jinrong.R;
import com.example.administrator.jinrong.base.BaseFragment;
import com.example.administrator.jinrong.bean.Index;
import com.example.administrator.jinrong.commom.AppNetWork;
import com.example.administrator.jinrong.view.RoundProgress;
import com.youth.banner.Banner;

import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseFragment {


    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_top_setting)
    ImageView ivTopSetting;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.roundp_home)
    RoundProgress roundpHome;
    @BindView(R.id.tv_home_rate)
    TextView tvHomeRate;

    private int currentProgress;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            roundpHome.setMax(100);
            roundpHome.setProgress(0);

            for (int i = 0; i < currentProgress; i++) {
                roundpHome.setProgress(roundpHome.getProgress() + 1);

                SystemClock.sleep(20);
//                roundpHome.invalidate();必须执行在主线程中
                roundpHome.postInvalidate();//主线程或分线程都可以执行，用于重绘
            }
        }
    };

    @Override
    protected String gerUrl() {
        return AppNetWork.INDEX;
    }


    @Override
    public void initData(String content) {
        //1.使用fastJson解析得到的json数据,并封装数据到java对象中
        JSONObject jsonObject = JSON.parseObject(content);

        String data = jsonObject.getString("data");
        List<Index> Indexs = JSON.parseArray(data, Index.class);



        //3.根据得到的产品的数据，更新界面中的产品展示
        String yearRate = Indexs.get(0).yearRate;
        tvHomeRate.setText(yearRate + "%");

        currentProgress = Integer.parseInt(Indexs.get(0).progress);
        new Thread(runnable).start();
//

    }

    @Override
    public void initTitle() {
        ivTopBack.setVisibility(View.INVISIBLE);
        ivTopSetting.setVisibility(View.INVISIBLE);
        tvTopTitle.setText("首页");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


}
