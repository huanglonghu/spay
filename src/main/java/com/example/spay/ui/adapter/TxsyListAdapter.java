package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.bean.TxsyResponse;
import com.example.spay.databinding.ItemLvTxsyBinding;
import com.example.spay.databinding.ItemLvYsjlBinding;
import com.example.spay.utils.DateUtil;
import com.example.spay.utils.FormatUtil;

import java.util.List;

public class TxsyListAdapter extends BaseListAdapter {

    public TxsyListAdapter(Context context, List<TxsyResponse.ResultBean.DataBean> datas, int res) {
        super(context, datas, res);
    }

    @Override
    View initView(LayoutInflater layoutInflater, int res, List datas, int position, ViewGroup parent) {
        ItemLvTxsyBinding binding = DataBindingUtil.inflate(layoutInflater, res, parent, false);
        TxsyResponse.ResultBean.DataBean bean = (TxsyResponse.ResultBean.DataBean) datas.get(position);
        binding.setBean(bean);
        String money = FormatUtil.getInstance().get2double(bean.getIncomeMoney());
        binding.setMoney(money);
        String time = DateUtil.getInstance().formatDate(bean.getPutTime());
        binding.ysjlDate.setText(time);
        return binding.getRoot();
    }

}
