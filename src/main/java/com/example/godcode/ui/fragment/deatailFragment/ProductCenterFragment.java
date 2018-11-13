package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.bean.ProductCategory;
import com.example.godcode.bean.ProductList;
import com.example.godcode.databinding.FragmentProductcenterBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.adapter.ProductListAdaPter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.view.BottomDialog;
import com.example.godcode.ui.view.MyListView;
import com.example.godcode.utils.LogUtil;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductCenterFragment extends BaseFragment implements BottomDialog.TypeSelect, MyListView.RefreshData {

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
            binding.lvProductCenter.setRefreshData(this);
            view = binding.getRoot();
            iniView();
            typeList = new ArrayList<>();
            dataMap.put("全部", 0);
            typeList.add("全部");
            initListener();
        }
        data.clear();
        binding.lvProductCenter.setPage(1);
        initData();
        return view;
    }

    private void iniView() {
        binding.productCenterToolbar.title.setText("产品中心");
        binding.productCenterToolbar.tvOption.setText("筛选");
        data = new ArrayList<>();
        productListAdaPter = new ProductListAdaPter(data, activity);
        binding.setAdapter(productListAdaPter);
    }

    private int selectPosition;

    private void initListener() {
        binding.productCenterToolbar.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog = new BottomDialog(activity, typeList, selectPosition, "请选择产品类型");
                bottomDialog.setTypeSelect(ProductCenterFragment.this);
                bottomDialog.show();
            }
        });
    }

    private ArrayList<String> typeList;
    private HashMap<String, Integer> dataMap = new HashMap<>();

    private void initData() {
        HttpUtil.getInstance().getAllProductCategorys().subscribe(
                productCategory -> {
                    ProductCategory category = new Gson().fromJson(productCategory, ProductCategory.class);
                    categoryList = category.getResult().getItems();
                    for (int i = 0; i < categoryList.size(); i++) {
                        ProductCategory.ResultBean.ItemsBean itemsBean = categoryList.get(i);
                        String productType = itemsBean.getProductType();
                        int id = itemsBean.getId();
                        dataMap.put(productType, id);
                        typeList.add(productType);
                    }
                    productCategoryID = dataMap.get("全部");
                    binding.productType.setText("全部");
                    getProductList();
                }
        );

    }

    private int productCategoryID;

    private void getProductList() {
        HashMap<String, String> urlMap = new HashMap<>();
        urlMap.put("productType", String.valueOf(productCategoryID));
        urlMap.put("page", binding.lvProductCenter.getPage() + "");
        urlMap.put("limit", "10");
        HttpUtil.getInstance().getPagedProductList(urlMap).subscribe(
                productListStr -> {
                    productList = new Gson().fromJson(productListStr, ProductList.class);
                    List<ProductList.DataBean> list = productList.getData();
                    this.data.addAll(list);
                    if (list.size() > 0) {
                        binding.lvProductCenter.setLoading(true);
                    } else {
                        binding.lvProductCenter.setLoading(false);
                    }
                    productListAdaPter.notifyDataSetChanged();
                }
        );
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void selectType(int selectPosition) {
        this.selectPosition = selectPosition;
        String productType = typeList.get(selectPosition);
        productCategoryID = dataMap.get(productType);
        binding.productType.setText(productType);
        binding.lvProductCenter.setPage(1);
        productListAdaPter.clearView();
        data.clear();
        getProductList();
    }

    @Override
    public void refreshData(int page) {
        getProductList();
    }


}
