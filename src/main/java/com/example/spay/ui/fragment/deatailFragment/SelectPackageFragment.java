package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.bean.OrderDetail;
import com.example.spay.bean.ProductScan;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.FragmentSelectpackageBinding;
import com.example.spay.databinding.SelectPackageItemBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.utils.GsonUtil;

import java.util.List;

public class SelectPackageFragment extends BaseFragment {

    private FragmentSelectpackageBinding binding;
    private ProductScan.ResultBean productScanResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selectpackage, container, false);
            binding.setPresenter(presenter);
            initView();
        }
        return binding.getRoot();
    }

    private void initView() {
        binding.selectPackageToolbar.title.setText("选择套餐");
        Bundle bundle = getArguments();
        productScanResult = (ProductScan.ResultBean) bundle.getSerializable("productScanResult");
        boolean isFree = bundle.getBoolean("isFree");
        binding.setIsFreePlay(isFree);
        String picturePath = productScanResult.getThumbnailImgPath();
        if (!TextUtils.isEmpty(picturePath)) {
            if (!picturePath.contains("http")) {
                picturePath = Constant.baseUrl + "/" + picturePath;
            }
            RxImageLoader.with(getContext()).load(picturePath).into(binding.productPicture);
        }
        binding.productName.setText(productScanResult.getProductName());
        List<ProductScan.ResultBean.ProductPackageListBean> productPackageList = productScanResult.getProductPackageList();
        if (productPackageList != null && productPackageList.size() > 0) {
            for (int i = 0; i < productPackageList.size(); i++) {
                ProductScan.ResultBean.ProductPackageListBean productPackageListBean = productPackageList.get(i);
                SelectPackageItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.select_package_item, binding.llPackageContent, false);
                itemBinding.setBean(productPackageListBean);
                itemBinding.setFragment(this);
                binding.llPackageContent.addView(itemBinding.getRoot());
            }
        }else {
            ProductScan.ResultBean.ProductPackageListBean bean = new ProductScan.ResultBean.ProductPackageListBean();
            bean.setCoinCount(1);
            bean.setPrice((int) productScanResult.getMoney());
            bean.setFK_ProductID(productScanResult.getId());
            SelectPackageItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.select_package_item, binding.llPackageContent, false);
            itemBinding.setBean(bean);
            itemBinding.setFragment(this);
            binding.llPackageContent.addView(itemBinding.getRoot());
        }
    }

    @Override
    protected void lazyLoad() {

    }

    public void selectPackagePay(ProductScan.ResultBean.ProductPackageListBean productPackageListBean) {
        HttpUtil.getInstance().getOrderNumber(productPackageListBean.getFK_ProductID(), productPackageListBean.getPrice(), productPackageListBean.getCoinCount())
                .subscribe(getOrderNumberStr -> {
                            OrderDetail orderDetail = GsonUtil.getInstance().fromJson(getOrderNumberStr, OrderDetail.class);
                            OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("orderDetail", orderDetail);
                            bundle.putSerializable("productScanResult", productScanResult);
                            orderDetailFragment.setArguments(bundle);
                            presenter.step2Fragment(orderDetailFragment, "orderDetail");
                        }
                );
    }


}
