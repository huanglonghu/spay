package com.example.spay.interface_;

import com.example.spay.bean.PackageItem;
import com.example.spay.bean.PackageList;
import com.example.spay.bean.ProductPackageSettingBean;
import com.example.spay.databinding.LayoutPackageItemBinding;

public abstract class SelectPakageStrategy {

    public void delete(int packageId) {}

    public void change(PackageItem packageItem) {}

    public void edit(PackageList.ResultBean.DataBean dataBean, Integer id){}

    public void bind(ProductPackageSettingBean productPackageSettingBean,Integer price,Integer coinCount){}


}
