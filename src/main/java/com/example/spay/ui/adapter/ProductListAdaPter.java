package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.bean.ProductList;
import com.example.spay.databinding.ItemLvProductcenterBinding;
import com.example.spay.constant.Constant;

import java.util.List;

public class ProductListAdaPter extends BaseListAdapter {

    public ProductListAdaPter(Context context, List<ProductList.DataBean> datas, int res) {
        super(context, datas, res);
    }


    @Override
    View initView(LayoutInflater layoutInflater, int res, List datas, int position, ViewGroup parent) {
        ItemLvProductcenterBinding binding = DataBindingUtil.inflate(layoutInflater, res, parent, false);
        ProductList.DataBean dataBean = (ProductList.DataBean) datas.get(position);
        binding.setBean(dataBean);
        String url = dataBean.getThumbnailImgPath();
        RxImageLoader.with(context).load(Constant.baseUrl + url).into(binding.ivProduct);
        return binding.getRoot();
    }


}
