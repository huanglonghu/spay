package com.example.spay.ui.fragment.pwd;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.spay.R;
import com.example.spay.bean.CheckPsd;
import com.example.spay.databinding.FragmentCheckpaypsdBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.view.KeyBoard;
import com.example.spay.utils.LogUtil;

public class CheckPayPsdFragment extends BaseFragment {
    private FragmentCheckpaypsdBinding binding;
    private View view;
    private KeyBoard keyBoard;
    private int eventType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkpaypsd, container, false);
        binding.setPresenter(presenter);
        view = binding.getRoot();
        eventType = getArguments().getInt("eventType");
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
            @SuppressLint("CheckResult")
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
                                LogUtil.log("===============EventType============"+eventType);
                                presenter.back();
                                RxEvent rxEvent = new RxEvent(eventType);
                                Bundle bundle = new Bundle();
                                bundle.putString("OriginalPayPass",pwd);
                                rxEvent.setBundle(bundle);
                                RxBus.getInstance().post(rxEvent);
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
}
