package com.example.spay.ui.fragment.bindproduct;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.bean.EditProductSetting;
import com.example.spay.bean.ProductCategory;
import com.example.spay.bean.ProductSetting;
import com.example.spay.databinding.FragmentJtconfigListBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.TypeSelect;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.ToastUtil;

import java.util.List;

public class JtConfigListFragment extends BaseFragment implements TypeSelect.SelectResponse {
    private FragmentJtconfigListBinding binding;
    private View view;
    private List<ProductCategory.ResultBean.ItemsBean> items;
    private int productId;
    private EditProductSetting.ProductSettingBean productSettingBean;
    private String[] businessTypeArray = {"福袋机", "口红机", "售卖机", "冰淇淋机"};
    private ProductSetting.ResultBean result;
    private boolean first;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jtconfig_list, container, false);
            binding.setPresenter(presenter);
            binding.setFragment(this);
            binding.jtcsConfigListToolbar.title.setText("机台参数设置列表");

            view = binding.getRoot();
            Bundle arguments = getArguments();
            if (arguments != null) {
                productId = arguments.getInt("productId");
            }
            productSettingBean = new EditProductSetting.ProductSettingBean();
            productSettingBean.setGameType(1);
            productSettingBean.setFK_ProductID(productId);
            initData();
            initListen();
        }
        return view;
    }

    private void initData() {
        HttpUtil.getInstance().getProductSettingMsg(productId).subscribe(
                productSettingStr -> {
                    ProductSetting productSetting = GsonUtil.fromJson(productSettingStr, ProductSetting.class);
                    result = productSetting.getResult();
                    if (result != null) {
                        productSettingBean.setId(result.getId());
                        int businessType = result.getBusinessType();
                        productSettingBean.setBusinessType(businessType);
                        binding.busines.setText(businessTypeArray[businessType - 1]);
                        binding.switchBuy.setChecked(result.getIsBuy()==1);
                        binding.switchTest.setChecked(result.getIsAttempt()==1);
                        binding.switchGame.setChecked(result.getIsGameModel()==1);
                        binding.switchSelectMore.setChecked(result.getIsMulti()==1);
                        productSettingBean.setIsAttempt(result.getIsAttempt());
                        productSettingBean.setIsGameModel(result.getIsGameModel());
                        productSettingBean.setIsMulti(result.getIsMulti());
                        productSettingBean.setIsBuy(result.getIsBuy());
                        productSettingBean.setBuyLimit(result.getBuyLimit());
                        binding.setProductSettingBean(productSettingBean);
                    }
                }
        );
    }


    private void initListen() {
        binding.selectBusines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeSelect bankTypeSelect = new TypeSelect(activity, businessTypeArray);
                bankTypeSelect.setSelectResponse(JtConfigListFragment.this);
                bankTypeSelect.show();
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.getInstance().editProductSetting(productSettingBean).subscribe(
                        epsStr -> {
                            ProductSetting productSetting = GsonUtil.fromJson(epsStr, ProductSetting.class);
                            result = productSetting.getResult();
                            ToastUtil.getInstance().showToast("修改成功", 2000, activity);
                        }
                );
            }
        });


    }


    public void initView() {
    }

    @Override
    protected void lazyLoad() {
    }


    public void switchConfig(View view, int isConfig) {
        if (!first) {
            if (isConfig == 0) {
                isConfig = 1;
            } else {
                isConfig = 0;
            }
            switch (view.getId()) {
                case R.id.switch_buy:
                    productSettingBean.setIsBuy(isConfig);
                    break;
                case R.id.switch_selectMore:
                    productSettingBean.setIsMulti(isConfig);
                    break;
                case R.id.switch_game:
                    productSettingBean.setIsGameModel(isConfig);
                    break;
                case R.id.switch_test:
                    productSettingBean.setIsAttempt(isConfig);
                    break;
            }
            binding.setProductSettingBean(productSettingBean);
        }
    }


    @Override
    public void select(int pos) {
        binding.busines.setText(businessTypeArray[pos]);
        productSettingBean.setBusinessType(pos + 1);
    }
}
