package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentPaypsdBinding;
import com.example.godcode.greendao.entity.User;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.utils.PayPsdSetting;
import java.util.ArrayList;


public class PayPsdFragment extends BaseFragment {
    private FragmentPaypsdBinding binding;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_paypsd, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            initListener();
        }
        fragments.clear();
        initData();
        return view;
    }

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public ArrayList<BaseFragment> getFragments() {
        return fragments;
    }

    private void initData() {
        CheckPayPsdFragment cpp = new CheckPayPsdFragment();
        cpp.initData(1);
        SetPayPsdFragment setPayPsdFragment = new SetPayPsdFragment();
        setPayPsdFragment.initData(1);
        fragments.add(cpp);
        fragments.add(setPayPsdFragment);
        User user = UserOption.getInstance().querryUser(Constant.userId);
        if (user.getSetPwd()) {
            toggle(0);
            binding.paypsdToolbar.title.setText("修改支付密码");
        } else {
            HttpUtil.getInstance().hasPsd(Constant.userId).subscribe(
                    isPayPsdSetStr -> {
                        if (isPayPsdSetStr.equals("no")) {
                            toggle(1);
                            binding.paypsdToolbar.title.setText("设置支付密码");
                        } else {
                            toggle(0);
                            binding.paypsdToolbar.title.setText("修改支付密码");
                        }
                    }
            );
        }
    }


    public void toggle(int index) {
        getChildFragmentManager().beginTransaction().replace(R.id.paypsd_container, fragments.get(index)).commit();
    }

    private void initListener() {


    }



    @Override
    protected void lazyLoad() {

    }



}
