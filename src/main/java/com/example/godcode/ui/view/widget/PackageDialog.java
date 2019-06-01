package com.example.godcode.ui.view.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.example.godcode.R;
import com.example.godcode.bean.BindPackage;
import com.example.godcode.bean.EditPackage;
import com.example.godcode.bean.PackageItem;
import com.example.godcode.bean.PackageList;
import com.example.godcode.bean.ProductPackageSettingBean;
import com.example.godcode.constant.Constant;
import com.example.godcode.databinding.DialogPackageLayout1Binding;
import com.example.godcode.databinding.DialogPackageLayout2Binding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.EtStrategy;
import com.example.godcode.interface_.SelectPakageStrategy;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.adapter.PackageListAdapter;
import com.example.godcode.ui.adapter.PayViewPageAdapter;
import com.example.godcode.ui.fragment.deatailFragment.PackageFragment;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageDialog extends Dialog {
    private int way;
    private Context context;
    private SelectPakageStrategy selectStrategy;
    private int WAY_SELECT = 0;
    private int WAY_ADD = 1;
    private int WAY_Bind = 2;
    private int productCategoryID;
    private int productId;
    private ArrayList<PackageList.ResultBean.DataBean> datas;
    private ViewPager viewPager;
    private DialogPackageLayout2Binding binding2;
    private PackageListAdapter packageListAdapter;
    private Integer Id;
    private Map<Integer, ProductPackageSettingBean> packageSettingBeanMap;


    private PackageDialog(Builder builder) {
        super(builder.context, R.style.dialog2);
        setCancelable(false);
        this.context = builder.context;
        this.way = builder.way;
        this.selectStrategy = builder.selectStrategy;
        this.productCategoryID = builder.productCategoryID;
        this.productId = builder.productId;
        this.Id = builder.Id;
        this.packageSettingBeanMap = builder.packageSettingBeanMap;
        viewPager = new ViewPager(context);
        ArrayList<View> views = new ArrayList<>();
        DialogPackageLayout1Binding binding1 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_package_layout1, null, false);
        binding2 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_package_layout2, null, false);
        datas = new ArrayList<PackageList.ResultBean.DataBean>();
        packageListAdapter = new PackageListAdapter(context, datas, this);
        binding1.lvPackage.setAdapter(packageListAdapter);
        views.add(binding1.getRoot());
        views.add(binding2.getRoot());
        PayViewPageAdapter pageAdapter = new PayViewPageAdapter(views);
        viewPager.setAdapter(pageAdapter);
        step2PackageList();
        setContentView(viewPager);
        initListener(binding1, binding2);
    }

    private void initListener(DialogPackageLayout1Binding binding1, DialogPackageLayout2Binding binding2) {
        binding1.lvPackage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageList.ResultBean.DataBean dataBean = datas.get(position);
                ProductPackageSettingBean productPackageSettingBean = new ProductPackageSettingBean();
                productPackageSettingBean.setFK_PackageID(dataBean.getId());
                if (Id != null) {
                    productPackageSettingBean.setId(Id);
                } else {
                    LogUtil.log("=====================");
                    if (packageSettingBeanMap.containsKey(dataBean.getId())) {
                        ToastUtil.getInstance().showToast("请勿重复该套餐", 1500, context);
                        return;
                    }
                }

                if (way == WAY_Bind) {
                    selectStrategy.bind(productPackageSettingBean, dataBean.getPrice(), dataBean.getCoinCount());
                } else {
                    productPackageSettingBean.setFK_ProductID(productId);
                    BindPackage bindPackage = new BindPackage();
                    bindPackage.setProductPackageSetting(productPackageSettingBean);
                    HttpUtil.getInstance().bindPackage(bindPackage).subscribe(
                            str -> {
                                selectStrategy.edit(dataBean, Id);
                            }
                    );
                }

                dismiss();

            }
        });
        binding1.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding1.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step2AddPackage();
            }
        });
        binding2.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        binding2.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageItem packageItem = binding2.getPackageItem();
                EditPackage editPackage = new EditPackage();
                editPackage.setProductPackage(packageItem);
                Integer price = packageItem.getPrice();
                Integer coinCount = packageItem.getCoinCount();
                if (price == 0) {
                    ToastUtil.getInstance().showToast("价格不能为0", 1500, context);
                    return;
                }
                if(coinCount==0){
                    ToastUtil.getInstance().showToast("次数不能为0", 1500, context);
                    return;
                }
                HttpUtil.getInstance().editPackage(editPackage).subscribe(
                        str -> {
                            Integer id = packageItem.getId();
                            if (id != null && packageSettingBeanMap.containsKey(id)) {
                                selectStrategy.change(packageItem);
                            }
                            step2PackageList();
                        }
                );
            }
        });
    }


    public void edit(PackageList.ResultBean.DataBean dataBean) {
        viewPager.setCurrentItem(1);
        PackageItem packageItem = new PackageItem();
        packageItem.setFK_ProductCategoryID(productCategoryID);
        packageItem.setFK_UserID(Constant.userId);
        packageItem.setPrice(dataBean.getPrice());
        packageItem.setCoinCount(dataBean.getCoinCount());
        packageItem.setId(dataBean.getId());
        binding2.setPackageItem(packageItem);
    }

    public void delete(PackageList.ResultBean.DataBean dataBean) {
        boolean isCotain = packageSettingBeanMap.containsKey(dataBean.getId());
        if (isCotain) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("您已添加该套餐,您确定要继续删除吗?");
            builder.setPositiveButton("是", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete(dataBean, isCotain);
                }
            });
            builder.setNegativeButton("否", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            delete(dataBean, isCotain);
        }
    }

    private void delete(PackageList.ResultBean.DataBean dataBean, boolean isCotain) {
        HttpUtil.getInstance().deletePackage(dataBean.getId()).subscribe(
                str -> {
                    ToastUtil.getInstance().showToast("删除成功", 1500, context);
                    datas.remove(dataBean);
                    if (isCotain) {
                        selectStrategy.delete(dataBean.getId());
                    }
                    packageListAdapter.notifyDataSetChanged();
                }
        );
    }


    private void step2PackageList() {
        viewPager.setCurrentItem(0);
        HttpUtil.getInstance().getPackageList(productCategoryID).subscribe(
                str -> {
                    PackageList packageList = GsonUtil.fromJson(str, PackageList.class);
                    PackageList.ResultBean result = packageList.getResult();
                    if (result != null) {
                        List<PackageList.ResultBean.DataBean> data = result.getData();
                        datas.clear();
                        datas.addAll(data);
                        packageListAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    private void step2AddPackage() {
        viewPager.setCurrentItem(1);
        PackageItem packageItem = new PackageItem();
        packageItem.setFK_ProductCategoryID(productCategoryID);
        packageItem.setFK_UserID(Constant.userId);
        binding2.setPackageItem(packageItem);

    }


    public static class Builder {
        private int way;
        private Context context;
        private SelectPakageStrategy selectStrategy;
        private Integer Id;
        private int productCategoryID;
        private int productId;
        private Map<Integer, ProductPackageSettingBean> packageSettingBeanMap;


        public Builder packageSettingBeanMap(Map<Integer, ProductPackageSettingBean> packageSettingBeanMap) {
            this.packageSettingBeanMap = packageSettingBeanMap;
            return this;
        }


        public Builder way(int way) {
            this.way = way;
            return this;
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder etStrategy(SelectPakageStrategy selectStrategy) {
            this.selectStrategy = selectStrategy;
            return this;
        }

        public Builder productCategoryID(int productCategoryID) {
            this.productCategoryID = productCategoryID;
            return this;
        }

        public Builder productId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder Id(Integer Id) {
            this.Id = Id;
            return this;
        }


        public PackageDialog build() {
            return new PackageDialog(this);
        }


    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Presenter.getInstance().getWindowWidth();
        layoutParams.height = Presenter.getInstance().getWidowHeight() / 2;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        getWindow().setAttributes(layoutParams);
    }
}
