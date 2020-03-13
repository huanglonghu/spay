package com.example.spay.ui.fragment.loginActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.bean.RegisterBody;
import com.example.spay.databinding.FragmentLoginselectBinding;
import com.example.spay.ui.base.BaseFragment;

public class LoginSelectFragment extends BaseFragment {

    private FragmentLoginselectBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loginselect, container, false);
            binding.setPresenter(presenter);
            initListener();
        }
        return binding.getRoot();
    }

    private void initListener() {
        binding.zhdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                presenter.step2Fragment(loginFragment, "login");
            }
        });


        binding.step2regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                RegisterBody registerBody = new RegisterBody();
                registerFragment.initData(registerBody);
                presenter.step2Fragment(registerFragment,"register");
            }
        });

    }


    @Override
    protected void lazyLoad() {

    }

}
