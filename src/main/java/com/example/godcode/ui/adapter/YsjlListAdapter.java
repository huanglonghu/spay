package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.godcode.R;
import com.example.godcode.bean.YSRecord;
import com.example.godcode.databinding.ItemLvYsjlBinding;
import com.example.godcode.utils.DateUtil;
import com.example.godcode.utils.FormatCheckUtil;

import java.util.HashMap;
import java.util.List;

public class YsjlListAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private List<YSRecord.ResultBean.DataBean> data;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private String[] orderStatus = {"未支付", "已支付到账", "支付失败", "已退款"};

    public YsjlListAdapter(Context context, List<YSRecord.ResultBean.DataBean> data) {
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemLvYsjlBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_ysjl, parent, false);
            YSRecord.ResultBean.DataBean bean = data.get(position);
            binding.setBean(bean);
            convertView = binding.getRoot();
            if(bean.isIsRefund()){
                binding.setIsIncome(false);
            }else {
                binding.setIsIncome(true);
            }
            String money = FormatCheckUtil.getInstance().get2double(bean.getDivideMoney());
            binding.setMoney(money);
            long stringToDate = DateUtil.getInstance().getStringToDate(bean.getOrderDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
            String time = DateUtil.getInstance().formatTime(stringToDate);
            binding.ysjlDate.setText(time);
            viewMap.put(position, convertView);
        }

        return viewMap.get(position);
    }


    public void clearView() {
        viewMap.clear();
    }
}
