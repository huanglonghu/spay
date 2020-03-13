package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.spay.R;
import com.example.spay.bean.PackageList;
import com.example.spay.databinding.ListPackageItemBinding;
import com.example.spay.ui.view.widget.PackageDialog;

import java.util.List;

public class PackageListAdapter extends BaseAdapter {
    private Context context;
    private List<PackageList.ResultBean.DataBean> datas;
    private PackageDialog packageDialog;


    public PackageListAdapter(Context context, List<PackageList.ResultBean.DataBean> datas, PackageDialog packageDialog) {
        this.context = context;
        this.datas = datas;
        this.packageDialog=packageDialog;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListPackageItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_package_item, parent, false);
        PackageList.ResultBean.DataBean dataBean = datas.get(position);
        binding.setItem(dataBean);
        binding.setDialog(packageDialog);
        return binding.getRoot();
    }
}
