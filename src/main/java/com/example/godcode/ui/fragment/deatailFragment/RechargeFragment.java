package com.example.godcode.ui.fragment.deatailFragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentRechargeBinding;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.view.PsdPopupWindow;
import java.util.ArrayList;

public class RechargeFragment extends BaseFragment {
    private FragmentRechargeBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recharge, container, false);
            binding.rechargeToolbar.title.setText("充值");
            view = binding.getRoot();
            initView();
            initListener();
        }
        return view;
    }

    private double money;
    private String bank;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    private void initListener() {
        binding.rechargeToolbar.toolbar3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyDown();
            }
        });
    }

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public void initView() {
        Recharge_FirstFragment recharge_firstFragment = new Recharge_FirstFragment();
        Recharge_SecondFragment recharge_secondFragment = new Recharge_SecondFragment();
        Recharge_SuccessFragment recharge_successFragment = new Recharge_SuccessFragment();
        fragments.add(recharge_firstFragment);
        fragments.add(recharge_secondFragment);
        fragments.add(recharge_successFragment);
        toggle(0);
    }


    private int index;

    public int toggle(int index) {
        this.index = index;
        return getChildFragmentManager().beginTransaction().replace(R.id.recharge_container, fragments.get(index)).commit();
    }


    @Override
    public void onKeyDown() {
        if (PsdPopupWindow.getInstance(activity) != null && PsdPopupWindow.getInstance(activity).isShowing()) {
            PsdPopupWindow.getInstance(activity).exit();
        } else {
            switch (index) {
                case 0:
                    presenter.back();
                    break;
                case 1:
                    toggle(0);
                    break;
                case 2:
                    toggle(0);
                    break;
            }
        }

    }


    @Override
    protected void lazyLoad() {

    }

}
