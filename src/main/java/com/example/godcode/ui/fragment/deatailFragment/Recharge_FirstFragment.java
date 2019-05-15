package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentRechargeFirstBinding;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.view.MyEditText;

public class Recharge_FirstFragment extends BaseFragment implements MyEditText.MoneyValueListener {
    private FragmentRechargeFirstBinding binding;
    private View view;
    private RechargeFragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            parentFragment = (RechargeFragment) getParentFragment();
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recharge_first, container, false);
            binding.etRechargeMoney.setMoneyValueListener(this);
            view = binding.getRoot();
            initView();
            lazyLoad();
        }
        return view;
    }

    public void initView() {
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double money = Double.parseDouble(binding.etRechargeMoney.getText().toString());
                if (money > 0) {
                    parentFragment.setMoney(money);
                    parentFragment.toggle(1);
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void setEnable(boolean enable,double money) {
        if (enable) {
            if (!binding.btnNext.isEnabled()) {
                binding.btnNext.setEnabled(true);
            }
        } else {
            if (binding.btnNext.isEnabled()) {
                binding.btnNext.setEnabled(false);
            }
        }
    }
}
