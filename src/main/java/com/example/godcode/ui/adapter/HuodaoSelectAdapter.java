package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.godcode.R;
import com.example.godcode.bean.CommodityRoadList;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.constant.Constant;
import com.example.godcode.databinding.ItemHuodaoSelectBinding;

import java.util.HashMap;

public class HuodaoSelectAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private boolean[] checkStateArray;
    private CommodityRoadList.ResultBean.DataBean[] datas;
    private String productNumber;

    public HuodaoSelectAdapter(Context context, CommodityRoadList.ResultBean.DataBean[] datas, String productNumber) {
        this.context = context;
        this.datas = datas;
        this.productNumber = productNumber;
        inflater = LayoutInflater.from(context);
        checkStateArray = new boolean[datas.length];
    }

    @Override
    public int getCount() {
        return datas.length;
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
            ItemHuodaoSelectBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_huodao_select, null, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
            binding.setCommodityNumber(position + 1);
            binding.setProductName(productNumber);
            CommodityRoadList.ResultBean.DataBean data = datas[position];
            if (data != null) {
                binding.presentName.setText(data.getPresentName());
                String imgUrl = data.getImgUrl();
                if (!TextUtils.isEmpty(imgUrl)) {
                    if (!imgUrl.contains("http")) {
                        imgUrl = Constant.baseUrl +imgUrl;
                    }
                    RxImageLoader.with(context).load(imgUrl).into(binding.presentImg);
                }
            }
            viewMap.put(position, convertView);
            binding.rlIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean selected = binding.circle.isSelected();
                    binding.circle.setSelected(!selected);
                    checkStateArray[position] = !selected;
                }
            });
        }
        return viewMap.get(position);
    }

    public void checkAll(boolean state) {
        for (int i = 0; i < checkStateArray.length; i++) {
            View view = getView(i, null, null);
            ItemHuodaoSelectBinding binding = (ItemHuodaoSelectBinding) view.getTag();
            binding.circle.setSelected(state);
            checkStateArray[i] = state;
        }
    }


    public boolean[] getCheckStateArray() {
        return checkStateArray;
    }

    public void setCheckStateArray(boolean[] checkStateArray) {
        this.checkStateArray = checkStateArray;
    }
}
