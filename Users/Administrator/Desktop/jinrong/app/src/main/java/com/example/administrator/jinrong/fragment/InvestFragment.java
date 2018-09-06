package com.example.administrator.jinrong.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.jinrong.R;
import com.example.administrator.jinrong.base.BaseFragment;
import com.example.administrator.jinrong.utils.UIUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InvestFragment extends BaseFragment {


    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_top_setting)
    ImageView ivTopSetting;
    @BindView(R.id.tab_indicator)
    TabPageIndicator tabIndicator;
    @BindView(R.id.viewpager_invest)
    ViewPager viewpagerInvest;

    @Override
    protected String gerUrl() {
        return null;
    }

    @Override
    public void initData(String content) {
        initFragments();
        MyAdapter myAdapter = new MyAdapter(getFragmentManager());
        viewpagerInvest.setAdapter(myAdapter);
        //关联TabPagerIndicator 和 ViewPager
        tabIndicator.setViewPager(viewpagerInvest);
    }

    /**
     * 初始化3个Fragment，并保存在集合中
     */
    private List<Fragment> fragmentList = new ArrayList<>();

    private void initFragments() {
        ProductListFragment productListFragment = new ProductListFragment();
        ProductRecommondFragment productRecommondFragment = new ProductRecommondFragment();
        ProductHotFragment productHotFragment = new ProductHotFragment();

        fragmentList.add(productListFragment);
        fragmentList.add(productRecommondFragment);
        fragmentList.add(productHotFragment);
    }

    @Override
    public void initTitle() {
        ivTopBack.setVisibility(View.INVISIBLE);
        ivTopSetting.setVisibility(View.INVISIBLE);
        tvTopTitle.setText("投资");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_inverst;
    }


    /**
     * FragmentStatePagerAdapter: 如果viewPager中加载显示的Fragment较多，则建议继承此父类，因为系统会自动的回收长时间不用的fragment
     * FragmentPagerAdapter:如果viewPager中加载显示的Fragment较少情况。系统不会做回收操作
     */
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        //获取指定位置的fragment
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        //返回集合的个数
        @Override
        public int getCount() {
            return fragmentList.size();
        }

        //提供TabPagerIndicator显示的文本
        @Override
        public CharSequence getPageTitle(int position) {
            //方式一
//            if(position == 0){
//                return "全部理财";
//            }else if(position == 1){
//                return "推荐理财";
//            }else ..
            //方式二
            return UIUtils.getStrArray(R.array.invest_tab)[position];
        }
    }

}
