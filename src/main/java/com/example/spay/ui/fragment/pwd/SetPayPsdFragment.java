package com.example.spay.ui.fragment.pwd;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.spay.R;
import com.example.spay.bean.ChangePsd;
import com.example.spay.bean.SetPayPsd;
import com.example.spay.databinding.FragmentSetpaypsdBinding;
import com.example.spay.greendao.entity.User;
import com.example.spay.greendao.option.UserOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.view.KeyBoard;
import com.example.spay.utils.LogUtil;

public class SetPayPsdFragment extends BaseFragment {
    private FragmentSetpaypsdBinding binding;
    private View view;
    private String psd1;
    private String originalPayPass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setpaypsd, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            if (getArguments() != null) {
                originalPayPass = getArguments().getString("OriginalPayPass");
                String title = getArguments().getString("title");
                binding.setPsdTitle.setText(title);
            }
            LogUtil.log("==============originalPayPass============"+originalPayPass);
            initView();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.setPayPsdPsdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!keyBoard.isShowing()) {
                    keyBoard.show(view);
                }
            }
        });

        binding.btnSetPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserOption.getInstance().querryUser(Constant.userId);
                boolean setPwd = user.getSetPwd();
                if(setPwd){
                    ChangePsd changePsd = new ChangePsd();
                    changePsd.setfK_UserID(Constant.userId);
                    changePsd.setOriginalPayPass(originalPayPass);
                    changePsd.setPayPass(psd1);
                    HttpUtil.getInstance().changePsd(changePsd).subscribe(
                            changePsdStr -> {
                                Toast.makeText(activity, "密码修改成功", Toast.LENGTH_SHORT).show();
                                Presenter.getInstance().back();
                            }
                    );
                }else {
                    SetPayPsd setPayPsd = new SetPayPsd();
                    setPayPsd.setFK_UserID(Constant.userId);
                    setPayPsd.setPayPass(psd1);
                    HttpUtil.getInstance().setPayPsd(setPayPsd).subscribe(
                            setPsdStr -> {
                                Toast.makeText(activity, "支付密码设置成功", Toast.LENGTH_SHORT).show();
                                Presenter.getInstance().back();
                            }, throwable -> {
                            }
                    );
                }


            }
        });
    }



    private KeyBoard keyBoard;

    public void initView() {
        keyBoard = new KeyBoard(activity, new ClickSureListener() {
            @Override
            public void checkPwd(String pwd) {
                if (index == 0) {
                    psd1 = keyBoard.getPsd();
                    binding.setPayPsdPsdView.setPsLength(0);
                    keyBoard.clearPsd();
                    binding.setPsdTitle.setText("请再次填写以确认");
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
        keyBoard.show(view);
    }

    @Override
    protected void lazyLoad() {
    }


    private int index;

    @Override
    public void onStop() {
        super.onStop();
        if (keyBoard != null) {
            keyBoard.dismiss();
        }
    }
}
