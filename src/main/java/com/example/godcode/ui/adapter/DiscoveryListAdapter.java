package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.godcode.R;
import com.example.godcode.bean.Notices;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.ItemLvDiscoveryBinding;
import com.example.godcode.utils.DateUtil;

import java.util.HashMap;
import java.util.List;

public class DiscoveryListAdapter extends BaseAdapter {

    private List<Notices.DataBean> data;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private LayoutInflater layoutInflater;
    private Context context;

    public DiscoveryListAdapter(Context context, List<Notices.DataBean> data) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
        if (viewMap.get(position) == null) {
            ItemLvDiscoveryBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_discovery, parent, false);
            Notices.DataBean dataBean = data.get(position);
            binding.setBean(dataBean);
            if (!TextUtils.isEmpty(dataBean.getTitlePageImgUrl())) {
                String url = dataBean.getTitlePageImgUrl().replace("\\", "/");
                RxImageLoader.with(context).load(url).into(binding.titleImage);
            }
            String s = DateUtil.getInstance().formatDate(dataBean.getPublishTime());

            binding.time.setText(s);
            convertView = binding.getRoot();
            viewMap.put(position, convertView);
        }
        return viewMap.get(position);
    }
}
