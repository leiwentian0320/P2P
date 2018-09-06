package com.example.administrator.jinrong.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.jinrong.R;
import com.example.administrator.jinrong.activity.LoginActivity;
import com.example.administrator.jinrong.activity.UserInfoActivity;
import com.example.administrator.jinrong.base.BaseActivity;
import com.example.administrator.jinrong.base.BaseFragment;
import com.example.administrator.jinrong.bean.User;
import com.example.administrator.jinrong.utils.BitmapUtils;
import com.example.administrator.jinrong.utils.UIUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MeFragment extends BaseFragment {


    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_top_setting)
    ImageView ivTopSetting;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.icon_time)
    RelativeLayout iconTime;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @BindView(R.id.recharge)
    ImageView recharge;
    @BindView(R.id.withdraw)
    ImageView withdraw;
    @BindView(R.id.ll_touzi)
    TextView llTouzi;
    @BindView(R.id.ll_touzi_zhiguan)
    TextView llTouziZhiguan;
    @BindView(R.id.ll_zichang)
    TextView llZichang;

    @Override
    protected String gerUrl() {
        return null;
    }

    @Override
    public void initData(String content) {
        //判断是否需要进行登录的提示
        isLogin();

    }

    private void isLogin() {
        //在本应用对应的sp存储的位置，是否已经保存了用户的登录信息。
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String userName = sp.getString("name", "");
        if (TextUtils.isEmpty(userName)) {//如果没有保存：没有登录过，提示用户登录
            login();
        } else {//如果保存了：读取sp中的用户信息，并显示在页面上
            doUser();
        }
    }

    //得到了本地的登录信息，加载显示
    private void doUser() {
        //读取数据，得到内存中的User对象
        User user = readUser();
        //一方面，显示用户名
        textView11.setText(user.name);

        Picasso.with(getActivity()).load(user.imageUrl).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                //1.压缩处理
                Bitmap zoomBitmp = BitmapUtils.zoom(source, UIUtils.dp2px(62), UIUtils.dp2px(62));
                //2.圆形处理
                Bitmap bitmap = BitmapUtils.circleBitmap(zoomBitmp);
                //回收source
                source.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "";
            }
        }).into(imageView1);


    }

    //未发现登录信息，提示用户登录的Dialog
    private void login() {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("请先登录哦")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        //((BaseActivity) MeFragment.this.getActivity()).goToActivity(LoginActivity.class, null);
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void initTitle() {
        ivTopBack.setVisibility(View.INVISIBLE);
        ivTopSetting.setVisibility(View.VISIBLE);
        tvTopTitle.setText("我的资产");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }


    //读取数据，得到内存中的User对象
    public User readUser() {
        User user = new User();
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user.name = sp.getString("name", "");
        user.imageUrl = sp.getString("imageUrl", "");
        user.isCredit = sp.getBoolean("isCredit", false);
        user.phone = sp.getString("phone", "");

        return user;
    }


    @OnClick({R.id.iv_top_setting, R.id.recharge, R.id.withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_setting:
              startActivity(new Intent(this.getContext(),UserInfoActivity.class));
                break;
            case R.id.recharge:
                break;
            case R.id.withdraw:
                break;
        }
    }
}
