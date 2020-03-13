package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spay.R;
import com.example.spay.bean.ProductCategory;
import com.example.spay.bean.ProductList;
import com.example.spay.databinding.FragmentProductcenterBinding;
import com.example.spay.ui.adapter.ProductListAdaPter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.widget.BottomDialog;
import java.util.ArrayList;
import java.util.List;

public class ProductCenterFragment extends BaseFragment  {

    private FragmentProductcenterBinding binding;
    private View view;
    private List<ProductCategory.ResultBean.ItemsBean> categoryList;
    private ProductList productList;
    List<ProductList.DataBean> data;
    private ProductListAdaPter productListAdaPter;
    private BottomDialog bottomDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_productcenter, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            iniView();
        }
        return view;
    }

    private void iniView() {
        binding.productCenterToolbar.title.setText("产品中心");
        binding.productCenterToolbar.tvOption.setText("筛选");
        data = new ArrayList<>();
        productListAdaPter = new ProductListAdaPter(activity,data,R.layout.item_lv_productcenter);
        binding.setAdapter(productListAdaPter);
    }





    @Override
    protected void lazyLoad() {
    }





}
