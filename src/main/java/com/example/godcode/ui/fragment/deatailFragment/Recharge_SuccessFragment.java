package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentRechargeSuccessBinding;
import com.example.godcode.ui.base.BaseFragment;

public class Recharge_SuccessFragment extends BaseFragment {

    private FragmentRechargeSuccessBinding binding;
    private View view;
    private RechargeFragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            parentFragment = (RechargeFragment) getParentFragment();
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recharge_success, container, false);
            view = binding.getRoot();
            initView();
        }
        return view;
    }

    private void initView() {
        binding.setMoney(parentFragment.getMoney());
        binding.setTitle("充值成功");
        binding.rechrageSuccessBankName.setText(parentFragment.getBank());
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {
    }


}
