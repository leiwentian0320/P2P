package com.example.administrator.jinrong;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.jinrong.commom.ActivityManager;
import com.example.administrator.jinrong.fragment.HomeFragment;
import com.example.administrator.jinrong.fragment.InvestFragment;
import com.example.administrator.jinrong.fragment.MeFragment;
import com.example.administrator.jinrong.fragment.MoreFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
public class MainActivity extends AppCompatActivity {


    private static final int MESSAGE_BACK = 1;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.rb_shouye)
    RadioButton rbShouye;
    @BindView(R.id.rb_touzi)
    RadioButton rbTouzi;
    @BindView(R.id.rb_wodezichan)
    RadioButton rbWodezichan;
    @BindView(R.id.rb_gengduo)
    RadioButton rbGengduo;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private HomeFragment homeFragment;
    private InvestFragment investFragment;
    private MeFragment meFragment;
    private MoreFragment moreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.getInstance().add(this);
        ButterKnife.bind(this);
        setListener();//选择改变RadioGroup的监听


    }

    private void setListener() {
        rgMain.check(R.id.rb_shouye);
        setSelect(0);
        //默认选中“首页”
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shouye:
                        setSelect(0);
                        break;
                    case R.id.rb_touzi:
                        setSelect(1);
                        break;
                    case R.id.rb_wodezichan:
                        setSelect(2);
                        break;
                    case R.id.rb_gengduo:
                        setSelect(3);
                        break;
                }
            }
        });


    }

    private void setSelect(int i) {
        fragmentManager = this.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        hideFragment();

        if (i == 0) {//首页
            if (homeFragment == null) {
                homeFragment = new HomeFragment();//创建对象以后，并不会马上执行其生命周期方法，而是在事务执行commit()，才会执行生命周期方法
                transaction.add(R.id.fl_main, homeFragment);

            }
            //显示
            transaction.show(homeFragment);

        } else if (i == 1) {
            if (investFragment == null) {
                investFragment = new InvestFragment();//创建对象以后，并不会马上执行其生命周期方法，而是在事务执行commit()，才会执行生命周期方法
                transaction.add(R.id.fl_main, investFragment);
            }
            //显示
            transaction.show(investFragment);
        } else if (i == 2) {
            if (meFragment == null) {
                meFragment = new MeFragment();//创建对象以后，并不会马上执行其生命周期方法，而是在事务执行commit()，才会执行生命周期方法
                transaction.add(R.id.fl_main, meFragment);
            }
            //显示
            transaction.show(meFragment);

        } else if (i == 3) {//更多
            if (moreFragment == null) {
                moreFragment = new MoreFragment();//创建对象以后，并不会马上执行其生命周期方法，而是在事务执行commit()，才会执行生命周期方法
                transaction.add(R.id.fl_main, moreFragment);
            }
            //显示
            transaction.show(moreFragment);
        }
        //提交
        transaction.commit();


    }

    //隐藏所有的Fragment的显示
    private void hideFragment() {
        if(homeFragment != null){
            transaction.hide(homeFragment);
        }

        if(investFragment != null){
            transaction.hide(investFragment);
        }
        if(meFragment != null){
            transaction.hide(meFragment);
        }
        if(moreFragment != null){
            transaction.hide(moreFragment);
        }
    }

    /**
     * 实现连续点击两次"返回键"，退出当前应用
     * @param keyCode
     * @param event
     * @return
     */
    private boolean isFlag = true;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case MESSAGE_BACK :
                    isFlag = true;//在2时，恢复isFlag的变量值
                    break;
            }
        }
    };


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && isFlag){//如果操作的是“返回键”
            isFlag = false;
            Toast.makeText(MainActivity.this, "再点击一次退出应用", Toast.LENGTH_SHORT).show();
            //发送延迟消息
            handler.sendEmptyMessageDelayed(MESSAGE_BACK,2000);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保证在activity退出前，移除所有未被执行的消息，避免出现内存泄漏
        handler.removeCallbacksAndMessages(null);//移除所有的消息
//        handler.removeMessages(MESSAGE_BACK);//移除指定id的所有消息
    }

}
