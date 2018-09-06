package com.example.administrator.jinrong.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.administrator.jinrong.R;
import com.example.administrator.jinrong.activity.UserRegisterActivity;
import com.example.administrator.jinrong.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MoreFragment extends BaseFragment {


    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_top_setting)
    ImageView ivTopSetting;
    @BindView(R.id.ll_more_regist)
    LinearLayout llMoreRegist;
    @BindView(R.id.toggle_more_secret)
    ToggleButton toggleMoreSecret;
    @BindView(R.id.ll_more_reset)
    LinearLayout llMoreReset;
    @BindView(R.id.tv_more_number)
    TextView tvMoreNumber;
    @BindView(R.id.ll_more_contact)
    LinearLayout llMoreContact;
    @BindView(R.id.ll_more_sms)
    LinearLayout llMoreSms;
    @BindView(R.id.ll_more_share)
    LinearLayout llMoreShare;
    @BindView(R.id.ll_more_about)
    LinearLayout llMoreAbout;
    private SharedPreferences sp;

    @Override
    protected String gerUrl() {
        return null;
    }

    @Override
    public void initData(String content) {
        //获取设置手势密码的ToggleButton的状态
        getGestureStatus();
        //设置手势密码的ToggleButton状态改变的监听
        setGestureListener();

        //联系客服
        contactSupportStaff();

        //关于硅谷理财
       // aboutGuiguInvest();
    }


    @Override
    public void initTitle() {
        ivTopBack.setVisibility(View.INVISIBLE);
        tvTopTitle.setText("更多");
        ivTopSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }


    @OnClick({R.id.iv_top_back, R.id.tv_top_title, R.id.iv_top_setting, R.id.ll_more_regist, R.id.ll_more_reset, R.id.tv_more_number, R.id.ll_more_contact, R.id.ll_more_sms, R.id.ll_more_share, R.id.ll_more_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_back:
                break;
            case R.id.tv_top_title:
                break;
            case R.id.iv_top_setting:
                break;
            case R.id.ll_more_regist:
                startActivity(new Intent(this.getActivity(), UserRegisterActivity.class));
                break;
            case R.id.ll_more_reset:
                break;
            case R.id.tv_more_number:
                break;
            case R.id.ll_more_contact:
                break;
            case R.id.ll_more_sms:
                break;
            case R.id.ll_more_share:
               // showShare();
                break;
            case R.id.ll_more_about:
                break;
        }
    }

    //获取设置手势密码的ToggleButton的状态
    private void getGestureStatus() {
        sp = this.getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        //读取当前的toggleButton的状态并显示
        boolean isOpen = sp.getBoolean("isOpen", false);
        toggleMoreSecret.setChecked(isOpen);
    }

    private void setGestureListener() {

    }

    private void contactSupportStaff() {
        llMoreContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MoreFragment.this.getActivity())
                        .setTitle("联系客服")
                        .setMessage("是否联系客服010-56253825")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL);

                                Uri uri = Uri.parse("tel:010-56253825");
                                intent.setData(uri);
                                if (ActivityCompat.checkSelfPermission(MoreFragment.this.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                    return;
                                }
                                MoreFragment.this.getActivity().startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

//    private void showShare(){
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
//        oks.setTitle(getResources().getString(R.string.app_name));
//        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
//        oks.setTitleUrl("http://www.atguigu.com");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("世界上最遥远的距离，是我在if里你在else里，似乎一直相伴又永远分离；\n" +
//                "     世界上最痴心的等待，是我当case你是switch，或许永远都选不上自己；\n" +
//                "     世界上最真情的相依，是你在try我在catch。无论你发神马脾气，我都默默承受，静静处理。到那时，再来期待我们的finally。");
//        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://www.atguigu.com");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("word妈呀，精辟的不要不要的！");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getResources().getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://www.atguigu.com");
//
//        // 启动分享GUI
//        oks.show(this.getActivity());
//    }

}
