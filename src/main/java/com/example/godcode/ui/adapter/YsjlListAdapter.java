package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.bean.YSRecord;
import com.example.godcode.databinding.ItemLvYsjlBinding;
import com.example.godcode.utils.DateUtil;
import com.example.godcode.utils.FormatUtil;
import java.util.List;

public class YsjlListAdapter extends BaseListAdapter {

    public YsjlListAdapter(Context context, List<YSRecord.ResultBean.DataBean> datas, int res) {
        super(context, datas, res);
    }
    @Override
    View initView(LayoutInflater layoutInflater, int res, List datas, int position, ViewGroup parent) {
        ItemLvYsjlBinding binding = DataBindingUtil.inflate(layoutInflater, res, parent, false);
        YSRecord.ResultBean.DataBean bean = (YSRecord.ResultBean.DataBean) datas.get(position);
        binding.setBean(bean);
        if(bean.isIsRefund()){
            binding.setIsIncome(false);
        }else {
            binding.setIsIncome(true);
        }
        String money = FormatUtil.getInstance().get2double(bean.getDivideMoney());
        binding.setMoney(money);
        String time  = DateUtil.getInstance().formatDate(bean.getOrderDate());
        binding.ysjlDate.setText(time);
        return    binding.getRoot();
    }

}
