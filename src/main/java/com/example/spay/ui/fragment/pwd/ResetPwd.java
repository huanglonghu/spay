package com.example.spay.ui.fragment.pwd;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.spay.R;
import com.example.spay.bean.ResetPwdBean;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.ResetpwdBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.KeyBoard;
import com.example.spay.utils.ToastUtil;

public class ResetPwd extends BaseFragment {

    private ResetpwdBinding binding;

    @Override
    protected void lazyLoad() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.resetpwd, container, false);
        initListen();
        return binding.getRoot();
    }

    private KeyBoard keyBoard;
    private int index;
    private String psd1;

    private void initListen() {
        binding.setPsdTitle.setText("请输入6位数字的新密码");
        keyBoard = new KeyBoard(activity, new ClickSureListener() {
            @Override
            public void checkPwd(String pwd) {
                if (index == 0) {
                    psd1 = keyBoard.getPsd();
                    binding.setPayPsdPsdView.setPsLength(0);
                    keyBoard.clearPsd();
                    binding.setPsdTitle.setText("请再次填写新密码");
                } else if (index >= 1) {
                    if (psd1.equals(keyBoard.getPsd())) {
                        binding.btnSetPsd.setEnabled(true);
                    } else {
                        Toast.makeText(activity, "两次输入的密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
                        binding.setPayPsdPsdView.setPsLength(0);
                    }
                }
                index++;
            }
        });
        keyBoard.setRefreshPsd(binding.setPayPsdPsdView);


        binding.setPayPsdPsdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!keyBoard.isShowing()) {
                    keyBoard.show(binding.getRoot());
                }
            }
        });

        int verifyCodeType = getArguments().getInt("verifyCodeType");
        String verifyCode = getArguments().getString("verifyCode");
        binding.btnSetPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPwdBean resetPwdBean = new ResetPwdBean();
                resetPwdBean.setNewPayPassword(keyBoard.getPsd());
                resetPwdBean.setVerificationCodeType(verifyCodeType);
                resetPwdBean.setUserID(Constant.userId);
                resetPwdBean.setVerificationCode(verifyCode);
                HttpUtil.getInstance().resetPassword(resetPwdBean).subscribe(
                        str -> {
                            ToastUtil.getInstance().showToast("密码重置成功", 1000, getContext());
                            back();
                        }
                );

            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });


    }

    private void back() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack("authverifyCode",1);
        fm.popBackStack();
    }

    @Override
    public void onKeyDown() {
        back();
    }
}
