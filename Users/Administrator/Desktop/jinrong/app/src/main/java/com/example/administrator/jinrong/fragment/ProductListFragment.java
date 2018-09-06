package com.example.administrator.jinrong.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.jinrong.R;
import com.example.administrator.jinrong.adapter.ProductAdapter;
import com.example.administrator.jinrong.base.BaseFragment;
import com.example.administrator.jinrong.bean.Product;
import com.example.administrator.jinrong.commom.AppNetWork;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProductListFragment extends BaseFragment {
    @BindView(R.id.lv_product_list)
    ListView lvProductList;
    private List<Product> products;

    @Override
    protected String gerUrl() {
        return AppNetWork.PRODUCT;
    }

    @Override
    public void initData(String content) {
        //解析json数据
        JSONObject jsonObject = JSON.parseObject(content);
        boolean isSuccess = jsonObject.getBoolean("success");
        if (isSuccess) {
            String data = jsonObject.getString("data");
            //解析得到集合数据
            products = JSON.parseArray(data, Product.class);
        }

        ProductAdapter productAdapter = new ProductAdapter(products);
        lvProductList.setAdapter(productAdapter);



    }

    @Override
    public void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_product_list;
    }

}
