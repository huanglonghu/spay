package com.example.spay.ui.fragment.loginActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.spay.R;
import com.example.spay.bean.GetVerification;
import com.example.spay.bean.LoginBody;
import com.example.spay.bean.LoginResponse;
import com.example.spay.bean.YZM;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.FragmentLoginBinding;
import com.example.spay.greendao.entity.LoginResult;
import com.example.spay.greendao.option.LoginResultOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.activity.MainActivity;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.utils.FormatUtil;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.SharepreferenceUtil;
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
        binding.logByPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PwdLoginFragment pwdLoginFragment = new PwdLoginFragment();
                Presenter.getInstance().step2Fragment(pwdLoginFragment, "pwdLogin");
            }
        });

        binding.getYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.etAccount.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(activity, "手机号不能为空", Toast.LENGTH_SHORT).show();
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
                            if(yzmStr.contains("\"success\":true")){
                                YZM yzm = GsonUtil.getInstance().fromJson(yzmStr, YZM.class);
                                String result = yzm.getResult();
                                if ("OK".equals(result)||"True".equals(result)) {
                                    binding.getYzm.setEnabled(false);
                                    yzmCountDownTime(binding.getYzm);
                                } else {
                                    Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                                }
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
            Toast.makeText(activity, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        loginBody.setUserName(account);
        if (FormatUtil.getInstance().checkYzm(loginBody.getVerificationCode())) {
            HttpUtil.getInstance().login(loginBody).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    loginStr -> {
                        if (loginStr.contains("\"success\":false")) {
                            Presenter.getInstance().step2Fragment("register");
                        } else {
                            LoginResponse loginResponse = new Gson().fromJson(loginStr, LoginResponse.class);
                            LoginResponse.ResultBean result = loginResponse.getResult();
                            LoginResult loginResult = new LoginResult(Constant.uniquenessToken, result.getPayServerUrl(), result.getUserId());
                            Constant.balances = result.getBalances();
                            Constant.toDayMoney = result.getToDayMoney();
                            Constant.yesterDayMoney = result.getYesterDayMoney();
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

    public void yzmCountDownTime(TextView tv) {
        Observable.interval(0, 1, TimeUnit.SECONDS, Schedulers.io())
                .take(60).observeOn(AndroidSchedulers.mainThread()).map(new Function<Long, Long>() {
            @Override
            public Long apply(Long aLong) throws Exception {
                return 59 - aLong;
            }
        }).subscribe(
                time -> {
                    tv.setText(time + "s");
                    if (time == 0) {
                        tv.setEnabled(true);
                        tv.setText("");
                    }
                }
        );
    }


    @Override
    protected void lazyLoad() {

    }

}
