package com.example.spay.ui.fragment.bindproduct;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.bean.BindProduct;
import com.example.spay.bean.ProductCategory;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.FragmentBindproduct2Binding;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.adapter.VemConfigAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.customview.AmountView;
import com.example.spay.ui.view.TypeSelect;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BindProduct2Fragment extends BaseFragment implements TypeSelect.SelectResponse {
    private FragmentBindproduct2Binding binding;
    private View view;
    private VemConfigAdapter vemConfigAdapter;
    private List<ProductCategory.ResultBean.ItemsBean> items;
    private String[] productTypeArray;
    private BindProduct bindProductBody;
    private BindProductFragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bindproduct2, container, false);
        view = binding.getRoot();
        binding.amountView.setAmount("20");
        parentFragment = (BindProductFragment) getParentFragment();
        vemConfigAdapter = new VemConfigAdapter(activity, parentFragment.getProductNumber());
        binding.lvVemConfig.setAdapter(vemConfigAdapter);
        initListen();
        initData();
        bindProductBody = new BindProduct();
        bindProductBody.setProductNumber(parentFragment.getProductNumber());
        binding.setBody(bindProductBody);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        getProductCategorys();
    }

    private void getProductCategorys() {
        HttpUtil.getInstance().getAllProductCategorys().subscribe(
                productCategory -> {
                    ProductCategory category = GsonUtil.fromJson(productCategory, ProductCategory.class);
                    items = category.getResult().getItems();
                    if (items != null && items.size() > 0) {
                        productTypeArray = new String[items.size()];
                        for (int i = 0; i < items.size(); i++) {
                            ProductCategory.ResultBean.ItemsBean itemsBean = items.get(i);
                            productTypeArray[i] = itemsBean.getProductType();
                        }
                    }
                }
        );
    }

    private void initListen() {
        binding.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                vemConfigAdapter.refreshCount(amount);
            }
        });

        binding.btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = binding.productType.getText().toString();
                if (TextUtils.isEmpty(bindProductBody.getProductName())) {
                    Toast.makeText(activity, "请输入产品名称", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(bindProductBody.getMachineAddress())) {
                    Toast.makeText(activity, "请输入产品地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(type)) {
                    Toast.makeText(activity, "请选择产品", Toast.LENGTH_SHORT).show();
                    return;
                }
                ProductCategory.ResultBean.ItemsBean itemsBean = items.get(selectPostion);
                int productCategoryID = itemsBean.getId();
                ArrayList<BindProduct.CommodityRoadBeanX.CommodityRoadBean> list = vemConfigAdapter.getList();
                if (list.size() > 0) {
                    BindProduct.CommodityRoadBeanX commodityRoadBeanX = new BindProduct.CommodityRoadBeanX();
                    commodityRoadBeanX.setCommodityRoad(list);
                    bindProductBody.setCommodityRoad(commodityRoadBeanX);
                }
                bindProductBody.setFK_ProductCategoryID(productCategoryID);
                bindProductBody.setFK_UserID(Constant.userId);
                bindProductBody.setIsBind(true);
                bindProductBody.setCommodityRoadCount(vemConfigAdapter.getCount());
                HttpUtil.getInstance().bindProduct2(bindProductBody).subscribe(
                        bindStr -> {
                            if (bindStr.contains("\"success\":true")) {
                                ToastUtil.getInstance().showToast("绑定成功", 2000, activity);
                                bindProductBody = new BindProduct();
                                binding.setBody(bindProductBody);
                                parentFragment.back();
                            } else {
                                Toast.makeText(activity, "绑定失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });

        binding.productType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productTypeArray != null && productTypeArray.length > 0) {
                    TypeSelect bankTypeSelect = new TypeSelect(activity, productTypeArray);
                    bankTypeSelect.setSelectResponse(BindProduct2Fragment.this);
                    bankTypeSelect.show();
                }
            }
        });


    }


    public void initView() {
    }

    @Override
    protected void lazyLoad() {
    }


    private int selectPostion;

    @Override
    public void select(int pos) {
        this.selectPostion = pos;
        binding.productType.setText(productTypeArray[pos]);
    }
}
