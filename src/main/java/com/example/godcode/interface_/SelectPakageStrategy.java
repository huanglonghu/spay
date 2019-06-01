package com.example.godcode.interface_;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.example.godcode.bean.PackageItem;
import com.example.godcode.bean.PackageList;
import com.example.godcode.bean.PackageSettingList;
import com.example.godcode.bean.ProductPackageSettingBean;
import com.example.godcode.databinding.LayoutPackageItemBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.utils.GsonUtil;

import java.util.List;

public abstract class SelectPakageStrategy {

    public void delete(int packageId) {}

    public void change(PackageItem packageItem) {}

    public void edit(PackageList.ResultBean.DataBean dataBean, Integer id){}

    public void bind(ProductPackageSettingBean productPackageSettingBean,Integer price,Integer coinCount){}


}
