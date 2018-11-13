package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.godcode.R;
import com.example.godcode.bean.Teansantion;
import com.example.godcode.databinding.ItemLvTransationrecordBinding;
import com.example.godcode.greendao.option.TransationOption;
import com.example.godcode.utils.DateUtil;
import com.example.godcode.utils.FormatCheckUtil;

import java.util.HashMap;
import java.util.List;


public class TransationRecordListAdapter extends BaseAdapter {
    private List<Teansantion.DataBean> data;
    private Context context;
    private final LayoutInflater layoutInflater;
    private HashMap<Integer, View> viewMap = new HashMap<>();


    public TransationRecordListAdapter(Context context, List<Teansantion.DataBean> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private String[] typeArray = {"", "转账支出", "二维码收款", "二维码付款", "商户消费", "充值", "提现", "退款", "转账收入", "盈利分成"};
    // - + - - + - + + +


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemLvTransationrecordBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_transationrecord, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
            Teansantion.DataBean dataBean = data.get(position);
            int transactionType = dataBean.getTransactionType();
            if (transactionType == 1 || transactionType == 3 || transactionType == 4 || transactionType == 6 || transactionType == 10) {
                binding.setIsIncome(false);
            } else {
                binding.setIsIncome(true);
            }
            String money = FormatCheckUtil.getInstance().get2double(dataBean.getMoney());
            binding.setMoney(money);
            String transationName;
            if (transactionType == 10) {
                transationName = "手机充值";
            } else {
                transationName = TransationOption.getInstance().getTransationName(transactionType);
            }
            binding.setType(transationName);
            long stringToDate = DateUtil.getInstance().getStringToDate(dataBean.getAddTime(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
            String time = DateUtil.getInstance().formatTime(stringToDate);
            binding.setTime(time);
            binding.setBean(data.get(position));
            convertView = binding.getRoot();
            viewMap.put(position, convertView);
        }
        return viewMap.get(position);
    }

    public void clearView() {
        viewMap.clear();
    }

}
