package com.example.administrator.jinrong.activity;

import android.os.Bundle;
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
import com.example.administrator.jinrong.R;
import com.example.administrator.jinrong.base.BaseActivity;
import com.example.administrator.jinrong.commom.AppNetWork;
import com.example.administrator.jinrong.utils.MD5Utils;
import com.example.administrator.jinrong.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class UserRegisterActivity extends BaseActivity {


    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_top_setting)
    ImageView ivTopSetting;
    @BindView(R.id.tv_register_number)
    TextView tvRegisterNumber;
    @BindView(R.id.et_register_number)
    EditText etRegisterNumber;
    @BindView(R.id.rl_register_number)
    RelativeLayout rlRegisterNumber;
    @BindView(R.id.tv_register_name)
    TextView tvRegisterName;
    @BindView(R.id.et_register_name)
    EditText etRegisterName;
    @BindView(R.id.rl_register_name)
    RelativeLayout rlRegisterName;
    @BindView(R.id.tv_register_secret)
    TextView tvRegisterSecret;
    @BindView(R.id.et_register_secret)
    EditText etRegisterSecret;
    @BindView(R.id.tv_register_verify)
    TextView tvRegisterVerify;
    @BindView(R.id.et_register_verify)
    EditText etRegisterVerify;
    @BindView(R.id.btn_register_login)
    Button btnRegisterLogin;

    @Override
    protected void initTitle() {
        ivTopBack.setVisibility(View.VISIBLE);
        tvTopTitle.setText("用户注册");
        ivTopSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_register;
    }


    @OnClick({R.id.iv_top_back, R.id.btn_register_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_back:
                removeCurrentActivity();
                break;
            case R.id.btn_register_login:
                String name = etRegisterName.getText().toString().trim();
                String phone = etRegisterNumber.getText().toString().trim();
                String password = etRegisterSecret.getText().toString().trim();
                String verify = etRegisterVerify.getText().toString().trim();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verify)){
                    Toast.makeText(UserRegisterActivity.this, "输入的信息不能为空", Toast.LENGTH_SHORT).show();

                }else if(!password.equals(verify)){
                    etRegisterSecret.setText("");
                    etRegisterVerify.setText("");
                    Toast.makeText(UserRegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                }else{

                    //联网发送用户信息给服务器
                    String uri = AppNetWork.REGISTER;
                    OkHttpUtils.get()
                            .url(uri)
                            .addParams("name",name)
                            .addParams("phone",phone)
                            .addParams("password",MD5Utils.MD5(password))
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Toast.makeText(UserRegisterActivity.this, "网络异常，注册失败", Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onResponse(String response, int id) {
                              //如果注册的电话号码的用户已经存在，提示：用户已注册
                                    JSONObject jsonObject = JSON.parseObject(response);
                                    boolean isExist = jsonObject.getBoolean("isExist");
                                    if(isExist){
                                        Toast.makeText(UserRegisterActivity.this, "用户已注册", Toast.LENGTH_SHORT).show();

                                    }else {
                                        Toast.makeText(UserRegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        etRegisterName.setText("");
                                        etRegisterNumber.setText("");
                                        etRegisterSecret.setText("");
                                        etRegisterVerify.setText("");

                                        removeCurrentActivity();


                                    }
                                }
                            });
                }
                break;
        }
    }
}
