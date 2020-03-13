package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.bean.Teansantion;
import com.example.spay.databinding.ItemLvTransationrecordBinding;
import com.example.spay.greendao.option.TransationOption;
import com.example.spay.utils.DateUtil;
import com.example.spay.utils.FormatUtil;
import java.util.List;

public class TransationRecordListAdapter extends BaseListAdapter {

    public TransationRecordListAdapter(Context context, List<Teansantion.DataBean> datas, int res) {
        super(context, datas, res);
    }

    @Override
    View initView(LayoutInflater layoutInflater, int res, List datas, int position, ViewGroup parent) {
        ItemLvTransationrecordBinding binding = DataBindingUtil.inflate(layoutInflater, res, parent, false);
        Teansantion.DataBean dataBean = (Teansantion.DataBean) datas.get(position);
        int transactionType = dataBean.getTransactionType();
        if (transactionType == 1 || transactionType == 3 || transactionType == 4 || transactionType == 6 || transactionType == 10||transactionType==11) {
            binding.setIsIncome(false);
        } else {
            binding.setIsIncome(true);
        }
        String money = FormatUtil.getInstance().get2double(dataBean.getMoney());
        binding.setMoney(money);
        String transationName;
        if (transactionType == 10) {
            transationName = "手机充值";
        }else if(transactionType ==11){
           transationName="购买积分";
        } else  {
            transationName = TransationOption.getInstance().getTransationName(transactionType);
        }
        binding.setType(transationName);
        String time = DateUtil.getInstance().formatDate(dataBean.getAddTime());
        binding.setTime(time);
        binding.setBean(dataBean);
        return binding.getRoot();
    }


}
