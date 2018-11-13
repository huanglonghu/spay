package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.BindProductBody;
import com.example.godcode.bean.ProductCategory;
import com.example.godcode.databinding.FragmentBindproductBinding;
import com.example.godcode.greendao.entity.ProductCategoryInfo;
import com.example.godcode.greendao.gen.ProductCategoryInfoDao;
import com.example.godcode.greendao.option.ProductCategoryOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.base.GodCodeApplication;
import com.example.godcode.ui.view.BankTypeSelect;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class BindingProductFragment extends BaseFragment implements BankTypeSelect.BankSelect {
    private FragmentBindproductBinding binding;
    private View view;
    private BindProductBody bindProductBody;
    private String[] productTypeArray;
    private HashMap<String,Integer> categryMap=new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bindproduct, container, false);
            binding.setPresenter(presenter);
            binding.setFrgament(this);
            bindProductBody = new BindProductBody();
            binding.setBody(bindProductBody);
            binding.bindproductToolbar.title.setText("绑定商品");
            view = binding.getRoot();
            initListener();
        }
        getProductCategorys();
        if (getArguments() != null) {
            String productNumber = getArguments().getString("productNumber");
            bindProductBody.setProductNumber(productNumber);
        }
        return view;
    }


    private void initListener() {
        binding.productType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productTypeArray != null && productTypeArray.length > 0) {
                    BankTypeSelect bankTypeSelect = new BankTypeSelect(activity, productTypeArray);
                    bankTypeSelect.setBankSelect(BindingProductFragment.this);
                    bankTypeSelect.show();
                }
            }
        });
    }


    private void getProductCategorys() {
        HttpUtil.getInstance().getAllProductCategorys().subscribe(
                productCategory -> {
                    ProductCategory category = new Gson().fromJson(productCategory, ProductCategory.class);
                    List<ProductCategory.ResultBean.ItemsBean> items = category.getResult().getItems();
                    productTypeArray = new String[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        ProductCategory.ResultBean.ItemsBean itemsBean = items.get(i);
                        String productType = itemsBean.getProductType();
                        int id = itemsBean.getId();
                        productTypeArray[i] = productType;
                        categryMap.put(productType,id);
                    }
                    initView();
                }
        );
    }


    public void initView() {
    }

    public void bind() {
        if (checkData(bindProductBody)) {
            bindProductBody.setFK_UserID(Constant.userId);
            bindProductBody.setIsBind(true);
            String productType = (String) binding.productType.getText().toString();
            int productId = categryMap.get(productType);
            bindProductBody.setFK_ProductCategoryID(productId);
            HttpUtil.getInstance().bindProduct(bindProductBody).subscribe(
                    bindProductStr -> {
                        if (bindProductStr.contains("\"success\":true")) {
                            Toast.makeText(activity, "绑定成功", Toast.LENGTH_SHORT).show();
                            bindProductBody = new BindProductBody();
                            binding.setBody(bindProductBody);
                        } else {
                            Toast.makeText(activity, "绑定失败", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    private boolean checkData(BindProductBody body) {

        if (TextUtils.isEmpty(body.getProductNumber())) {
            Toast.makeText(activity, "请输入产品编号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(body.getMachineAddress())) {
            Toast.makeText(activity, "请输入产品地址", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(body.getProductName())) {
            Toast.makeText(activity, "请输入产品名称", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(binding.productType.getText().toString())) {
            Toast.makeText(activity, "请选择产品类型", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;

    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {

    }

    public void handlerScanStr(String productNumber) {
        binding.etNumber.setText(productNumber);
    }


    @Override
    public void selectBankType(String type) {
        binding.productType.setText(type);
    }
}
