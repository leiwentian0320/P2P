package com.example.administrator.jinrong.activity;


import android.app.AlertDialog;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.jinrong.MainActivity;
import com.example.administrator.jinrong.R;
import com.example.administrator.jinrong.base.BaseActivity;
import com.example.administrator.jinrong.utils.BitmapUtils;
import com.example.administrator.jinrong.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.OnClick;
public class UserInfoActivity extends BaseActivity {

    private static final int CAMERA = 100;
    private static final int PICTURE = 200;


    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_top_setting)
    ImageView ivTopSetting;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.loginout)
    Button loginout;

    @Override
    protected void initTitle() {
        ivTopBack.setVisibility(View.VISIBLE);
        tvTopTitle.setText("用户信息");
        ivTopSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }




    @OnClick({R.id.iv_top_back, R.id.textView1, R.id.loginout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_back:
                //销毁当前的activity
                removeCurrentActivity();
                break;
            case R.id.textView1:

                new AlertDialog.Builder(this)
                        .setTitle("选择来源")
                        .setItems(new String[]{"拍照", "图库"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0://拍照
                                        //打开系统拍照程序，选择拍照图片
                                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(camera, CAMERA);
                                        break;
                                    case 1://图库
                                        //打开系统图库程序，选择图片
                                        Intent picture = new Intent("android.intent.action.GET_CONTENT");
                                        picture.setType("image/*");
                                        startActivityForResult(picture, PICTURE);
//                                        if(ContextCompat.checkSelfPermission(UserInfoActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
//                                            ActivityCompat.requestPermissions(UserInfoActivity.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//                                        }else {
//                                            //打开系统图库程序，选择图片
//                                            Intent picture = new Intent("android.intent.action.GET_CONTENT");
//                                            picture.setType("image/*");
//                                            startActivityForResult(picture, PICTURE);
//                                        }

                                        break;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.loginout:

                //1.清空本地存储的用户数据：sp存储中保存的用户信息。本地用户头像
                //1.1sp存储中保存的用户信息的清除
                SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                sp.edit().clear().commit();//清空sp存储的数据。（user_info.xml仍然存在，但是内部没有数据）
                //2.本地用户头像文件的删除
                String filePath = this.getCacheDir() + "/tx.png";
                File file = new File(filePath);
                if(file.exists()){
                    file.delete();
                }
                //2.结束当前Activity的显示
                this.removeAll();
                this.goToActivity(MainActivity.class,null);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null) {
            //拍照
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            //bitmap圆形裁剪
            bitmap = BitmapUtils.zoom(bitmap, UIUtils.dp2px(62), UIUtils.dp2px(62));
            Bitmap circleImage = BitmapUtils.circleBitmap(bitmap);


            //真是项目当中，是需要上传到服务器的..这步我们就不做了。
            imageView1.setImageBitmap(circleImage);
            //将图片保存在本地
            saveImage(circleImage);

        } else if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {
            //判断手机号码的版本号
            if(Build.VERSION.SDK_INT>19) {
                //4.4以上
              handleImageOnKitKat(data);
//                Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
//
//                //图片压缩
//                Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, UIUtils.dp2px(62), UIUtils.dp2px(62));
//                //bitmap圆形裁剪p
//                Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);
//                //真是项目当中，是需要上传到服务器的..这步我们就不做了。
//               imageView1.setImageBitmap(decodeFile);
////                //保存图片到本地
//                saveImage(circleImage);
            }else {
                Log.e("TAG", "版本小于19");
                //4.4以下，老版本
               handleImageBeforeKitKat(data);

//                Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
//                //图片压缩
//                Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, UIUtils.dp2px(62), UIUtils.dp2px(62));
//                //bitmap圆形裁剪p
//                Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);
//                //真是项目当中，是需要上传到服务器的..这步我们就不做了。
//               imageView1.setImageBitmap(decodeFile);
////                //保存图片到本地
//               saveImage(circleImage);

            }

        }
    }

    //4.4以下，老版本
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImags(imagePath);


    }

    private void displayImags(String imagePath) {

        if(imagePath!=null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        }else {
            Toast.makeText(UserInfoActivity.this, "空", Toast.LENGTH_SHORT).show();
        }
    }

    //4.4以上
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri= data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);

            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri =  ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(documentId));
                 imagePath= getImagePath(contentUri, null);
            }


        }else if("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath =  getImagePath(uri,null);

        }else if("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath  = uri.getPath();
        }

        displayImags(imagePath);
    }


    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor query = getContentResolver().query(uri, null, selection, null, null);
         if(query!=null) {
             if(query.moveToNext()) {
                 query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA));
             }
             query.close();
         }
        return path;
    }

    //保存图片的方法
    public void saveImage(Bitmap bitmap) {
        String path = this.getCacheDir() + "/tx.png";
        Log.e("TAG", "path = " + path);
        try {
            FileOutputStream fos = new FileOutputStream(path);
            //bitmap压缩(压缩格式、质量、压缩文件保存的位置)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
