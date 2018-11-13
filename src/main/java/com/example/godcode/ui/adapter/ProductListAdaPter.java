package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.R;
import com.example.godcode.bean.ProductList;
import com.example.godcode.databinding.ItemLvProductcenterBinding;
import com.example.godcode.ui.base.Constant;
import java.util.HashMap;
import java.util.List;

public class ProductListAdaPter extends BaseAdapter {
    private List<ProductList.DataBean> datas;
    private LayoutInflater layoutInflater;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private Context context;

    public ProductListAdaPter(List<ProductList.DataBean> datas, Context context) {
        this.context = context;
        this.datas = datas;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemLvProductcenterBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_productcenter, parent, false);
            ProductList.DataBean dataBean = datas.get(position);
            binding.setBean(dataBean);
            convertView = binding.getRoot();
            String url = dataBean.getThumbnailImgPath();
            RxImageLoader.with(context).load(Constant.baseUrl + url).into(binding.ivProduct);
            viewMap.put(position, convertView);
        }
        return viewMap.get(position);
    }

    public void clearView() {
        viewMap.clear();
    }

}
