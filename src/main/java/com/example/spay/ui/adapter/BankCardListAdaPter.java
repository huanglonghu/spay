package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.spay.R;
import com.example.spay.bean.BankCard;
import com.example.spay.databinding.ItemLvBankcardBinding;
import com.example.spay.ui.fragment.deatailFragment.BankCardFragment;
import java.util.HashMap;
import java.util.List;

public class BankCardListAdaPter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private List<BankCard.ResultBean> result;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private BankCardFragment fragment;

    public BankCardListAdaPter(Context context, List<BankCard.ResultBean> result, BankCardFragment fragment) {
        layoutInflater = LayoutInflater.from(context);
        this.fragment = fragment;
        this.result = result;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int[] bankIcon = {R.drawable.bank_cn,R.drawable.bank_gs, R.drawable.bank_hx, R.drawable.bank_js, R.drawable.bank_ny, R.drawable.bank_ms, R.drawable.bank_zs};

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemLvBankcardBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_bankcard, parent, false);
            BankCard.ResultBean resultBean = result.get(position);
            binding.setFragment(fragment);
            String bankCardNumber = resultBean.getBankCardNumber();
            String substring = bankCardNumber.substring(bankCardNumber.length() - 4, bankCardNumber.length());
            binding.tvEnd.setText(substring);
            String bankName = resultBean.getBankName();
            int bankRes;
            switch (bankName) {
                case "中国银行":
                    bankRes=bankIcon[0];
                    break;
                case "中国工商银行":
                    bankRes=bankIcon[1];
                    break;
                case "工商银行":
                    bankRes=bankIcon[1];
                    break;
                case "华夏银行":
                    bankRes=bankIcon[2];
                    break;
                case "中国建设银行":
                    bankRes=bankIcon[3];
                    break;
                case "建设银行":
                    bankRes=bankIcon[3];
                    break;
                case "中国农业银行":
                    bankRes=bankIcon[4];
                    break;
                case "农业银行":
                    bankRes=bankIcon[4];
                    break;
                case "中国民生银行":
                    bankRes=bankIcon[5];
                    break;
                case "民生银行":
                    bankRes=bankIcon[5];
                    break;
                case "招商银行":
                    bankRes=bankIcon[6];
                    break;
                default:
                    bankRes=bankIcon[0];
                    break;
            }
            binding.ivBank.setBackgroundResource(bankRes);
            binding.bankName.setText(bankName);
            int bindType = resultBean.getBindType();
            binding.setBindType(bindType);
            binding.setPosition(position);
            switch (bindType) {
                case 1:
                    binding.ivBg.setBackgroundResource(R.drawable.checking);
                    break;
                case 2:
                    binding.ivBg.setBackgroundResource(R.drawable.check_success);
                    break;
                case 3:
                    break;
                case 4:
                    binding.ivBg.setBackgroundResource(R.drawable.check_failed);
                    break;
            }
            convertView = binding.getRoot();
            convertView.setTag(binding);
            viewMap.put(position, convertView);
        }


        return viewMap.get(position);
    }
}
