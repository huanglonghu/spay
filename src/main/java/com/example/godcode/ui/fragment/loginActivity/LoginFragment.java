package com.example.godcode.ui.fragment.loginActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.GetVerification;
import com.example.godcode.bean.LoginBody;
import com.example.godcode.bean.LoginResponse;
import com.example.godcode.bean.YZM;
import com.example.godcode.databinding.FragmentLoginBinding;
import com.example.godcode.greendao.entity.LoginResult;
import com.example.godcode.greendao.option.LoginResultOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.activity.MainActivity;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.utils.FormatCheckUtil;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.SharepreferenceUtil;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginFragment extends BaseFragment {

    private FragmentLoginBinding binding;

    private LoginBody loginBody;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
            binding.setPresenter(presenter);
            initListener();
        }
        initData();
        return binding.getRoot();
    }

    private void initData() {
        loginBody = new LoginBody();
        loginBody.setDeviceToken(Constant.diviceToken);
        binding.setLoginBody(loginBody);
    }

    private void initListener() {
        binding.loginGetyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.etAccount.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(activity, "账号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                GetVerification getVerification = new GetVerification();
                if (phoneNumber.contains("@")) {
                    getVerification.setEmailAddress(phoneNumber);
                } else {
                    getVerification.setPhoneNumber(phoneNumber);
                }
                getVerification.setType("2");
                HttpUtil.getInstance().getVerificationCode(getVerification).subscribe(
                        yzmStr -> {
                            YZM yzm = GsonUtil.getInstance().fromJson(yzmStr, YZM.class);
                            String result = yzm.getResult();
                            if (result.equals("OK")) {
                                binding.loginGetyzm.setEnabled(false);
                                yzmCountDoownTime();
                            } else if (result.equals("触发小时级流控Permits:5")) {
                                Toast.makeText(activity, "一小时内只能发5次验证码", Toast.LENGTH_SHORT).show();
                            } else if (result.equals("触发天级流控Permits:10")) {
                                Toast.makeText(activity, "一天内只能发10次验证码", Toast.LENGTH_SHORT).show();
                            } else if (result.equals("True")) {
                                binding.loginGetyzm.setEnabled(false);
                                yzmCountDoownTime();
                            }
                        }
                );


            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
    }


    public void login() {
        String account = binding.etAccount.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(activity, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        loginBody.setUserName(account);
        if (FormatCheckUtil.getInstance().checkYzm(loginBody.getVerificationCode())) {
            HttpUtil.getInstance().login(loginBody).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    loginStr -> {
                        if (loginStr.contains("\"success\":false")) {
                            Presenter.getInstance().step2Fragment("register");
                        } else {
                            LoginResponse loginResponse = new Gson().fromJson(loginStr, LoginResponse.class);
                            LoginResponse.ResultBean result = loginResponse.getResult();
                            LoginResult loginResult = new LoginResult(Constant.uniquenessToken, result.getPayServerUrl(), result.getUserId());
                            Constant.uniquenessToken = result.getUniquenessToken();
                            loginResult.setUniquenessToken(Constant.uniquenessToken);
                            LoginResultOption.getInstance().insertLoginResult(loginResult);
                            Constant.expireInSeconds = result.getExpireInSeconds();
                            Constant.userId = result.getUserId();
                            long time = System.currentTimeMillis();
                            SharepreferenceUtil.getInstance().saveAccessToken(result.getAccessToken(), Constant.uniquenessToken, time);
                            Constant.payUrl = result.getPayServerUrl();
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                        }
                    }
            );
        } else {
            Toast.makeText(activity, "验证码格式有误", Toast.LENGTH_SHORT).show();
        }


    }

    public void yzmCountDoownTime() {
        Observable.interval(0, 1, TimeUnit.SECONDS, Schedulers.io())
                .take(60).observeOn(AndroidSchedulers.mainThread()).map(new Function<Long, Long>() {
            @Override
            public Long apply(Long aLong) throws Exception {
                return 59 - aLong;
            }
        }).subscribe(
                time -> {
                    binding.loginGetyzm.setText(time + "秒后重发");
                    if (time == 0) {
                        binding.loginGetyzm.setEnabled(true);
                        binding.loginGetyzm.setText("获取验证码");
                    }
                }
        );
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void refreshData() {

    }
}