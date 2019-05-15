package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.databinding.FragmentPaySuccessBinding;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.utils.FormatUtil;

public class PaySuccessFragment extends BaseFragment {
    private FragmentPaySuccessBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay_success, container, false);
            view = binding.getRoot();
            initView();
            initListener();
        }
        return view;
    }


    private void initListener() {
        binding.payComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.removeFragment(PaySuccessFragment.this);
                presenter.back();
            }
        });
    }

    public void initView() {
        binding.setName(name);
        binding.money.setText(FormatUtil.getInstance().get2double(money));
    }

    @Override
    protected void lazyLoad() {

    }


    private double money;
    private String name;

    public void initData(String name, double money) {
        this.name = name;
        this.money = money;
    }

    @Override
    public void onKeyDown() {
        super.onKeyDown();
        presenter.removeFragment(PaySuccessFragment.this);
        presenter.back();

    }
}
