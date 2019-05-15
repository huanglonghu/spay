package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import com.example.godcode.bean.Teansantion;
import com.example.godcode.databinding.ItemLvTransationrecordBinding;
import com.example.godcode.greendao.option.TransationOption;
import com.example.godcode.utils.DateUtil;
import com.example.godcode.utils.FormatUtil;
import java.util.List;

public class TransationRecordListAdapter extends BaseListAdapter {

    public TransationRecordListAdapter(Context context, List<Teansantion.DataBean> datas, int res) {
        super(context, datas, res);
    }

    @Override
    View initView(LayoutInflater layoutInflater, int res, List datas, int position) {
        ItemLvTransationrecordBinding binding = DataBindingUtil.inflate(layoutInflater, res, null, false);
        Teansantion.DataBean dataBean = (Teansantion.DataBean) datas.get(position);
        int transactionType = dataBean.getTransactionType();
        if (transactionType == 1 || transactionType == 3 || transactionType == 4 || transactionType == 6 || transactionType == 10) {
            binding.setIsIncome(false);
        } else {
            binding.setIsIncome(true);
        }
        String money = FormatUtil.getInstance().get2double(dataBean.getMoney());
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
        binding.setBean(dataBean);
        return binding.getRoot();
    }


}
