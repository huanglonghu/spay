package com.example.spay.ui.fragment.deatailFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.bean.PackageItem;
import com.example.spay.bean.PackageList;
import com.example.spay.bean.PackageSettingList;
import com.example.spay.bean.ProductPackageSettingBean;
import com.example.spay.databinding.FragmentPackageBinding;
import com.example.spay.databinding.LayoutPackageItemBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.SelectPakageStrategy;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.widget.PackageDialog;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageFragment extends BaseFragment {

    private FragmentPackageBinding binding;
    private int productCategoryID;
    private int productId;
    private Integer productSettingId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_package, container, false);
        binding.setPresenter(presenter);
        binding.packageToolbar.title.setText("套餐设置");
        packageSettingBeanMap= new HashMap<>();
        initData();
        initListener();
        return binding.getRoot();
    }

    private Map<Integer, ProductPackageSettingBean> packageSettingBeanMap;

    private void initData() {
        Bundle bundle = getArguments();
        productId = bundle.getInt("productId");
        productCategoryID = bundle.getInt("productCategoryID");
        String psId = bundle.getString("productSettingId");
        if (psId != null) {
            productSettingId = Integer.parseInt(psId);
        }

        HttpUtil.getInstance().getPackageSettingList(productId).subscribe(
                str -> {
                    PackageSettingList packageSettingList = GsonUtil.fromJson(str, PackageSettingList.class);
                    List<PackageSettingList.ResultBean> result = packageSettingList.getResult();
                    if (result != null && result.size() > 0) {
                        for (int i = 0; i < result.size(); i++) {
                            PackageSettingList.ResultBean resultBean = result.get(i);
                            packageSettingBeanMap.put(resultBean.getFK_PackageID(), null);
                            LayoutPackageItemBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_package_item, binding.packageContent, false);
                            binding2.setItem(resultBean);
                            binding2.setFragment(PackageFragment.this);
                            binding.packageContent.addView(binding2.getRoot());
                        }
                    }
                }
        );

    }

    private void initListener() {
        binding.addPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = binding.packageContent.getChildCount();
                if (childCount < 3) {
                    LayoutPackageItemBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_package_item, binding.packageContent, false);
                    binding2.setFragment(PackageFragment.this);
                    binding.packageContent.addView(binding2.getRoot());
                } else {
                    ToastUtil.getInstance().showToast("最多添加三个套餐", 1500, getContext());
                }
            }
        });

    }

    @Override
    protected void lazyLoad() {

    }


    private LayoutPackageItemBinding selectBinding;

    public void selectPackage(View view) {
        selectBinding = DataBindingUtil.findBinding(view);
        PackageSettingList.ResultBean item = selectBinding.getItem();
        PackageDialog.Builder builder = new PackageDialog.Builder();
        if (item == null) {
            builder.way(1);
        } else {
            builder.way(0).Id(item.getId());
        }
        PackageDialog packageDialog = builder.context(getContext()).packageSettingBeanMap(packageSettingBeanMap).productCategoryID(productCategoryID).productId(productId).etStrategy(new PackageFragmentStrategy()).build();
        packageDialog.show();
    }

    public void deletePackage(View view) {
        LayoutPackageItemBinding binding2 = DataBindingUtil.findBinding(view);
        PackageSettingList.ResultBean item = binding2.getItem();
        if (item == null) {
            binding.packageContent.removeView(binding2.getRoot());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("您确定要删除该套餐吗?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HttpUtil.getInstance().deletePackageSetting(item.getId()).subscribe(
                            str -> {
                                binding.packageContent.removeView(binding2.getRoot());
                            }
                    );
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    public class PackageFragmentStrategy extends SelectPakageStrategy {

        @Override
        public void delete(int packageId) {
            int childCount = binding.packageContent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = binding.packageContent.getChildAt(i);
                LayoutPackageItemBinding binding2 = DataBindingUtil.findBinding(child);
                PackageSettingList.ResultBean item = binding2.getItem();
                if (item != null && item.getFK_PackageID() == packageId) {
                    HttpUtil.getInstance().deletePackageSetting(packageId).subscribe(
                            str -> {
                                packageSettingBeanMap.remove(packageId);
                                binding.packageContent.removeView(binding2.getRoot());
                            }
                    );
                    break;
                }
            }
        }

        @Override
        public void edit(PackageList.ResultBean.DataBean dataBean, Integer id) {
            if (id != null) {
                PackageSettingList.ResultBean item = selectBinding.getItem();
                item.setPrice(dataBean.getPrice());
                item.setCoinCount(dataBean.getCoinCount());
                item.setFK_PackageID(dataBean.getId());
                selectBinding.setItem(item);
                packageSettingBeanMap.put(dataBean.getId(), null);
            } else {
                HttpUtil.getInstance().getPackageSettingList(productId).subscribe(
                        str -> {
                            PackageSettingList packageSettingList = GsonUtil.fromJson(str, PackageSettingList.class);
                            List<PackageSettingList.ResultBean> result = packageSettingList.getResult();
                            if (result != null && result.size() > 0) {
                                for (int i = 0; i < result.size(); i++) {
                                    PackageSettingList.ResultBean resultBean = result.get(i);
                                    int fk_packageID = resultBean.getFK_PackageID();
                                    if (!packageSettingBeanMap.containsKey(fk_packageID)) {
                                        selectBinding.setItem(resultBean);
                                        packageSettingBeanMap.put(fk_packageID, null);
                                        break;
                                    }
                                }
                            }
                        }
                );
            }
        }

        @Override
        public void change(PackageItem packageItem) {
            int childCount = binding.packageContent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = binding.packageContent.getChildAt(i);
                LayoutPackageItemBinding binding2 = DataBindingUtil.findBinding(child);
                PackageSettingList.ResultBean item = binding2.getItem();
                if (item != null && item.getFK_PackageID() == packageItem.getId()) {
                    item.setCoinCount(packageItem.getCoinCount());
                    item.setPrice(packageItem.getPrice());
                    binding2.setItem(item);
                    break;
                }
            }
        }
    }


}
