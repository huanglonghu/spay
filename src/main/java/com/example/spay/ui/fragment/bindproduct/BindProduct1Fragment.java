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
import com.example.spay.bean.PackageItem;
import com.example.spay.bean.ProductCategory;
import com.example.spay.bean.ProductPackageSettingBean;
import com.example.spay.databinding.FragmentBindproduct1Binding;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.SelectPakageStrategy;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.view.TypeSelect;
import com.example.spay.ui.view.widget.PackageDialog;
import com.example.spay.utils.LogUtil;
import com.example.spay.utils.ToastUtil;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindProduct1Fragment extends BaseFragment implements TypeSelect.SelectResponse {
    private FragmentBindproduct1Binding binding;
    private View view;
    private BindProduct bindProductBody;
    private String[] productTypeArray;
    private BindProductFragment parentFragment;
    private HashMap<String, Integer> categryMap = new HashMap<>();
    private Map<Integer, String> packageStrMap;
    private Map<Integer, ProductPackageSettingBean> packageSettingBeanMap;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bindproduct1, container, false);
        binding.setFrgament(this);
        parentFragment = (BindProductFragment) getParentFragment();
        view = binding.getRoot();
        initListener();
        getProductCategorys();
        packageStrMap=new HashMap<>();
        packageSettingBeanMap=new HashMap<>();
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        bindProductBody = new BindProduct();
        binding.setBody(bindProductBody);
        binding.productPackage.setText("");
        binding.productType.setText("");
        bindProductBody.setProductNumber(parentFragment.getProductNumber());
    }

    private void initListener() {
        binding.productType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productTypeArray != null && productTypeArray.length > 0) {
                    TypeSelect bankTypeSelect = new TypeSelect(activity, productTypeArray);
                    bankTypeSelect.setSelectResponse(BindProduct1Fragment.this);
                    bankTypeSelect.show();
                }
            }
        });

        binding.productPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindProductBody.getFK_ProductCategoryID() == 0) {
                    ToastUtil.getInstance().showToast("请先选择产品类型", 1500, getContext());
                    return;
                }
                LogUtil.log("===========categoryId============="+bindProductBody.getFK_ProductCategoryID());
                PackageDialog.Builder builder = new PackageDialog.Builder();
                PackageDialog packageDialog = builder.way(2).context(getContext()).packageSettingBeanMap(packageSettingBeanMap).productCategoryID(bindProductBody.getFK_ProductCategoryID()).etStrategy(new Bind1FragmentStrategy()).build();
                packageDialog.show();
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
                        categryMap.put(productType, id);
                    }
                }
        );
    }


    public void initView() {
    }

    public void bind() {
        if (checkData(bindProductBody)) {
            bindProductBody.setFK_UserID(Constant.userId);
            bindProductBody.setIsBind(true);
            if (packageSettingBeanMap.size() > 0) {
                ArrayList<BindProduct.ProductPackageSettingBeanX> list = new ArrayList<>();
                for (int key : packageSettingBeanMap.keySet()) {
                    BindProduct.ProductPackageSettingBeanX productPackageSettingBeanX = new BindProduct.ProductPackageSettingBeanX();
                    ProductPackageSettingBean productPackageSettingBean = packageSettingBeanMap.get(key);
                    productPackageSettingBeanX.setProductPackageSetting(productPackageSettingBean);
                    list.add(productPackageSettingBeanX);
                }
                bindProductBody.setProductPackageSetting(list);
            }
            HttpUtil.getInstance().bindProduct(bindProductBody).subscribe(
                    bindProductStr -> {
                        if (bindProductStr.contains("\"success\":true")) {
                            Toast.makeText(activity, "绑定成功", Toast.LENGTH_SHORT).show();
                            bindProductBody = new BindProduct();
                            binding.setBody(bindProductBody);
                            packageStrMap.clear();
                            packageSettingBeanMap.clear();
                            setPackageStr();
                            binding.productType.setText("");
                            parentFragment.back();
                        } else {
                            Toast.makeText(activity, "绑定失败", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    private boolean checkData(BindProduct body) {
        if (TextUtils.isEmpty(body.getProductNumber())) {
            Toast.makeText(activity, "请输入产品编号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(body.getPrice())) {
            Toast.makeText(activity, "请输入产品价格", Toast.LENGTH_SHORT).show();
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
    public void select(int pos) {
        String productType = productTypeArray[pos];
        int productCategoryId = categryMap.get(productType);
        bindProductBody.setFK_ProductCategoryID(productCategoryId);
        packageStrMap.clear();
        packageStrMap.clear();
        setPackageStr();
        binding.productType.setText(productType);
    }


    public class Bind1FragmentStrategy extends SelectPakageStrategy {
        @Override
        public void bind(ProductPackageSettingBean productPackageSettingBean, Integer price, Integer coinCount) {
            if (packageSettingBeanMap.size() >= 3) {
                ToastUtil.getInstance().showToast("最多只能添加3个套餐", 1500, getContext());
            } else {
                packageSettingBeanMap.put(productPackageSettingBean.getFK_PackageID(), productPackageSettingBean);
                String packageStr = price + "元=" + coinCount + "次";
                packageStrMap.put(productPackageSettingBean.getFK_PackageID(), packageStr);
                setPackageStr();
            }
        }

        @Override
        public void delete(int packageId) {
            packageSettingBeanMap.remove(packageId);
            packageStrMap.remove(packageId);
            setPackageStr();
        }

        @Override
        public void change(PackageItem packageItem) {
            Integer id = packageItem.getId();
            String packageStr = packageItem.getPrice() + "元=" + packageItem.getCoinCount() + "次";
            packageStrMap.put(id, packageStr);
            setPackageStr();
        }
    }

    private void setPackageStr() {
        StringBuffer sb = new StringBuffer();
        for (int i : packageStrMap.keySet()) {
            String s = packageStrMap.get(i);
            if (sb.length() == 0) {
                sb.append(s);
            } else {
                sb.append("," + s);
            }
        }
        binding.productPackage.setText(sb.toString());
    }
}
