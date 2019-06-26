package com.example.godcode.ui.fragment.pwd;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.CheckPsd;
import com.example.godcode.databinding.FragmentCheckpaypsdBinding;
import com.example.godcode.greendao.entity.User;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.observable.RxBus;
import com.example.godcode.observable.RxEvent;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.view.KeyBoard;
import com.example.godcode.utils.LogUtil;

public class CheckPayPsdFragment extends BaseFragment {
    private FragmentCheckpaypsdBinding binding;
    private View view;
    private KeyBoard keyBoard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkpaypsd, container, false);
        binding.setPresenter(presenter);
        view = binding.getRoot();
        LogUtil.log("=============NNNNNNNNNNNNNNNNNNNNNNNNNNNNN=================");
        initView();
        initListener();
        return view;
    }

    private void initListener() {

        binding.checkPsdPsdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!keyBoard.isShowing()) {
                    keyBoard.show(view);
                }
            }
        });
    }

    public void initView() {
        keyBoard = new KeyBoard(activity, new ClickSureListener() {
            @Override
            public void checkPwd(String pwd) {
                CheckPsd checkPsd = new CheckPsd();
                checkPsd.setFK_UserID(Constant.userId);
                checkPsd.setPayPass(pwd);
                HttpUtil.getInstance().checkPsyPsd(checkPsd).subscribe(
                        back -> {
                            if (back.contains("\"success\":false")) {
                                keyBoard.clearPsd();
                                binding.checkPsdPsdView.setPsLength(0);
                                Toast.makeText(activity, "密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
                            } else {
//                                User user = UserOption.getInstance().querryUser(Constant.userId);
//                                user.setSetPwd(true);
//                                UserOption.getInstance().updateUser(user);

                                Presenter.getInstance().back();
                                RxBus.getInstance().post(new RxEvent(2));
//                                if (type == 2) {
//                                    AddBankCardFragment addBankCardFragment = new AddBankCardFragment();
//                                    presenter.step2Fragment(addBankCardFragment);
//                                } else if (type == 1) {
//                                    SetPayPsdFragment setPayPsdFragment = new SetPayPsdFragment();
//                                    Bundle bundl = new Bundle();
//                                    bundl.putString("OriginalPayPass", pwd);
//                                    setPayPsdFragment.setArguments(bundl);
//                                    Presenter.getInstance().step2Fragment(setPayPsdFragment, "setPwd");
//                                }
                            }
                        }
                );
            }
        });
        keyBoard.setRefreshPsd(binding.checkPsdPsdView);
        keyBoard.show(view);
    }

    @Override
    protected void lazyLoad() {
    }


}
