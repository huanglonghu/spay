package com.example.godcode.ui.fragment.deatailFragment;

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
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.view.KeyBoard;

public class CheckPayPsdFragment extends BaseFragment{

    private FragmentCheckpaypsdBinding binding;
    private View view;
    private KeyBoard keyBoard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkpaypsd, container, false);
        binding.setPresenter(presenter);
        view = binding.getRoot();
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
                                if (type == 2) {
                                    AddBankCardFragment addBankCardFragment = new AddBankCardFragment();
                                    presenter.step2Fragment(addBankCardFragment);
                                } else if (type == 1) {
                                    PayPsdFragment paypsd = (PayPsdFragment) presenter.getFragment("paypsd");
                                    SetPayPsdFragment fragment = (SetPayPsdFragment) paypsd.getFragments().get(1);
                                    Bundle bundl = new Bundle();
                                    bundl.putString("OriginalPayPass", pwd);
                                    fragment.setArguments(bundl);
                                    paypsd.toggle(1);
                                }
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



    @Override
    public void onStop() {
        super.onStop();
        if (keyBoard != null) {
            keyBoard.dismiss();
        }
    }


    private int type;

    public void initData(int type) {
        this.type = type;
    }

}
