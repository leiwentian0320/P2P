package com.example.administrator.jinrong;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.jinrong.bean.UpdateInfo;
import com.example.administrator.jinrong.commom.ActivityManager;
import com.example.administrator.jinrong.commom.AppNetWork;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class WelcomeActivity extends Activity {

    private static final int MESSAGE_MAIN = 1;
    private static final int WHAT_DOWNLOAD_VERSION_SUCCESS = 2;
    private static final int WHAT_DOWNLOAD_FAIL = 3;
    private static final int WHAT_DOWNLOAD_APK_SUCCESS = 4;
    @BindView(R.id.iv_welcome_icon)
    ImageView ivWelcomeIcon;
    @BindView(R.id.tv_welcome_version)
    TextView tvWelcomeVersion;
    @BindView(R.id.rl_welcome)
    RelativeLayout rlWelcome;
    private long startTime;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_MAIN:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                    break;
                case WHAT_DOWNLOAD_VERSION_SUCCESS:
                    //获取了服务器端返回的版本信息
                    String version = getVersion();
                    if (version.equals(updateInfo.version)) {//版本相同
                        Toast.makeText(WelcomeActivity.this, "已经是最新版应用", Toast.LENGTH_SHORT).show();
                        toMain();
                    } else {
                        showDownloadDialog();
                    }
                    break;
                case WHAT_DOWNLOAD_FAIL:
                    Toast.makeText(WelcomeActivity.this, "下载应用文件失败", Toast.LENGTH_SHORT).show();
                    toMain();
                    break;
                case WHAT_DOWNLOAD_APK_SUCCESS:
                    installApk();
                    break;
            }
        }
    };

    private void installApk() {
        Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
        intent.setData(Uri.parse("file:" + apkFile.getAbsolutePath()));
        startActivity(intent);
    }

    private UpdateInfo updateInfo;
    private ProgressDialog dialog;
    private File apkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ActivityManager.getInstance().add(this);
        ButterKnife.bind(this);

        upDateApkFile();


        //启动动画
        startAnimation();

    }

    private void upDateApkFile() {
        //获取系统当前的时间
        startTime = System.currentTimeMillis();
        boolean connected = isConnected();
        if(connected) {//有网络
            //请求网络获取数据看是否有最新版本
            OkHttpUtils.get()
                    .url(AppNetWork.UPDATE)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(WelcomeActivity.this, "联网获取更新数据失败", Toast.LENGTH_SHORT).show();
                            toMain();

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            //使用fastjson解析json数据
                            updateInfo = JSON.parseObject(response, UpdateInfo.class);
                            handler.sendEmptyMessage(WHAT_DOWNLOAD_VERSION_SUCCESS);
                        }
                    });


        }else {

            Toast.makeText(WelcomeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            toMain();
        }

    }

    private void startAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);//0:完全透明 1：完全不透明
        alphaAnimation.setDuration(3000);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());//设置动画的变化率

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                ActivityManager.getInstance().remove(WelcomeActivity.this);

            }
        },3000);

        //启动动画
        rlWelcome.startAnimation(alphaAnimation);
    }


    /**
     * 判断是否可以联网
     *
     * @return
     */
    private boolean isConnected() {
        boolean connected = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            connected = networkInfo.isConnected();
        }
        return connected;

    }

    /**
     * 通过发送延迟消息，进入主界面
     */
    private void toMain() {
        long currentTimeMillis = System.currentTimeMillis();
        long delayTime = 3000 - (currentTimeMillis - startTime);
        if (delayTime < 0) {
            delayTime = 0;
        }

        //发送延迟消息
        handler.sendEmptyMessageDelayed(MESSAGE_MAIN, delayTime);
    }

    /**
     * 当前版本号
     *
     * @return
     */
    private String getVersion() {
        String version = "未知版本";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace(); //如果找不到对应的应用包信息, 就返回"未知版本"
        }
        return version;
    }

    /**
     * 显示是否需要联网下载最新版本apk的Dialog
     */
    private void showDownloadDialog() {
        new AlertDialog.Builder(this)
                .setTitle("下载最新版本")
                .setMessage(updateInfo.desc)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDownLoad();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toMain();
                    }
                })
                .show();
    }

    /**
     * 联网下载指定url地址对应的apk文件
     * 1.提供ProgressDialog
     * 2.提供本地的存储文件
     * 3.联网下载数据
     * 4.安装
     */
    private void showDownLoad() {
        //1.提供ProgressDialog
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平显示
        dialog.setCancelable(false);
        dialog.show();
        //2.提供本地的存储文件:sd卡路径1
        String filePath = this.getExternalFilesDir(null) + "/update_app.apk";
        apkFile = new File(filePath);
        //3.联网下载数据
        new Thread() {
            public void run() {
                try {
                    downloadAPk();
                    handler.sendEmptyMessage(WHAT_DOWNLOAD_APK_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 联网下载数据
     * @throws Exception
     */
    private void downloadAPk() throws Exception {
        //作用在存储apk文件上一个输入流
        FileOutputStream fos = new FileOutputStream(apkFile);
        //获取到网络apk文件的地址url
        String path = updateInfo.apkUrl;
        URL url = new URL(path);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");

        conn.connect();
        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            dialog.setMax(conn.getContentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);

                dialog.incrementProgressBy(len);
                Thread.sleep(2);
            }

            //暂且使用throws的方式处理异常了
            is.close();
            fos.close();
        } else {
            handler.sendEmptyMessage(WHAT_DOWNLOAD_FAIL);
        }

        //关闭连接
        conn.disconnect();
    }

}
