package com.example.administrator.jinrong.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.jinrong.MainActivity;
import com.example.administrator.jinrong.R;
import com.example.administrator.jinrong.base.BaseActivity;
import com.example.administrator.jinrong.bean.User;
import com.example.administrator.jinrong.commom.ActivityManager;
import com.example.administrator.jinrong.commom.AppNetWork;
import com.example.administrator.jinrong.utils.MD5Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_top_setting)
    ImageView ivTopSetting;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.log_ed_mob)
    EditText logEdMob;
    @BindView(R.id.about_com)
    RelativeLayout aboutCom;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.log_ed_pad)
    EditText logEdPad;
    @BindView(R.id.log_log_btn)
    Button logLogBtn;

    @Override
    protected void initTitle() {
        ivTopBack.setVisibility(View.VISIBLE);
        tvTopTitle.setText("用户登录");
        ivTopSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.iv_top_back, R.id.iv_top_setting, R.id.log_log_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_back:
                ActivityManager.getInstance().removeCurrent();
                break;
            case R.id.iv_top_setting:
                break;
            case R.id.log_log_btn:
                if(TextUtils.isEmpty(logEdMob.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(logEdPad.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    
                }else {
                    LoginActivity.this.removeAll();
                    LoginActivity.this.goToActivity(MainActivity.class,null);
//                    String url = AppNetWork.LOGIN;
//                    OkHttpUtils.get()
//                            .url(url)
//                            .addParams("phone",logEdMob.getText().toString())
//                            .addParams("password", MD5Utils.MD5(logEdPad.getText().toString()))
//                            .build()
//                            .execute(new StringCallback() {
//                                @Override
//                                public void onError(Call call, Exception e, int id) {
//                                    Toast.makeText(LoginActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
//
//                                }
//
//                                @Override
//                                public void onResponse(String response, int id) {
//                                    //3.1解析json数据
//                                    JSONObject jsonObject = JSON.parseObject(response);
//                                    boolean isSuccess = jsonObject.getBoolean("success");
//                                    if(!isSuccess) {
//                                        Toast.makeText(LoginActivity.this, "用户名不存在或密码不正确", Toast.LENGTH_SHORT).show();
//
//                                    }else {
//                                        //1
//                                        String data = jsonObject.getString("data");
//                                        User user = JSON.parseObject(data, User.class);
//                                        //2保存得到的用户信息（使用sp存储）
//                                        saveUser(user);
//                                        //3重新加载页面，显示用户的信息在MeFragment中
//                                        LoginActivity.this.removeAll();
//                                        LoginActivity.this.goToActivity(MainActivity.class,null);
//
//                                    }
//                                }
//                            });
                }
                break;
        }


    }
    //保存用户信息的操作:使用sp存储

    private void saveUser(User user) {
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name",user.name);
        editor.putString("imageUrl",user.imageUrl);
        editor.putBoolean("isCredit", user.isCredit);
        editor.putString("phone",user.phone);
        editor.commit();//只有提交以后，才可以创建此文件，并保存数据

    }


}
