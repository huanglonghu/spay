package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.bean.ChangePsd;
import com.example.godcode.bean.SetPayPsd;
import com.example.godcode.databinding.FragmentSetpaypsdBinding;
import com.example.godcode.greendao.entity.User;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.view.KeyBoard;

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
            if(getArguments()!=null){
                originalPayPass = getArguments().getString("OriginalPayPass");
            }
            User user = UserOption.getInstance().querryUser(Constant.userId);
            if (user.getSetPwd()) {
                binding.setPsdTitle.setText("请设置新的支付密码");
            } else {
                binding.setPsdTitle.setText("请设置支付密码");
            }
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
        User user = UserOption.getInstance().querryUser(Constant.userId);
        binding.btnSetPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getSetPwd()) {
                    ChangePsd changePsd = new ChangePsd();
                    changePsd.setfK_UserID(Constant.userId);
                    changePsd.setOriginalPayPass(originalPayPass);
                    changePsd.setPayPass(psd1);
                    HttpUtil.getInstance().changePsd(changePsd).subscribe(
                            changePsdStr->{
                                Toast.makeText(activity,"密码修改成功",Toast.LENGTH_SHORT).show();
                                if (type == 2) {
                                    AddBankCardFragment addBankCardFragment = new AddBankCardFragment();
                                    presenter.step2Fragment(addBankCardFragment);
                                } else if (type == 1) {
                                    presenter.back();
                                }
                            }
                    );
                } else {
                    SetPayPsd setPayPsd = new SetPayPsd();
                    setPayPsd.setFK_UserID(Constant.userId);
                    setPayPsd.setPayPass(psd1);
                    HttpUtil.getInstance().setPayPsd(setPayPsd).subscribe(
                            setPsdStr -> {
                                Toast.makeText(activity, "支付密码设置成功", Toast.LENGTH_SHORT).show();
                                //跳进绑定银行卡界面
                                if (type == 2) {
                                    AddBankCardFragment addBankCardFragment = new AddBankCardFragment();
                                    presenter.step2Fragment(addBankCardFragment);
                                } else if (type == 1) {
                                    presenter.back();
                                }

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
        if (keyBoard.isShowing()) {
            keyBoard.dismiss();
        }
    }


    private int type;

    public void initData(int type) {
        this.type = type;
    }


}
