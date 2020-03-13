package com.example.spay.ui.fragment.loginActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.bean.GetVerification;
import com.example.spay.bean.LoginBody;
import com.example.spay.bean.LoginResponse;
import com.example.spay.bean.RegisterBody;
import com.example.spay.bean.YZM;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.FragmentRegisterBinding;
import com.example.spay.greendao.entity.LoginResult;
import com.example.spay.greendao.option.LoginResultOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.activity.MainActivity;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.SharepreferenceUtil;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RegisterFragment extends BaseFragment {

    private FragmentRegisterBinding binding;
    private RegisterBody registerBody;
    private boolean isEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
            binding.setFragment(this);
            binding.setIsEmail(isEmail);
            binding.setPresenter(presenter);
            initListener();
        }
        binding.setRegisterBody(registerBody);
        return binding.getRoot();
    }

    private void initListener() {

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        binding.cbEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEmail = isChecked;
                binding.setIsEmail(isEmail);
            }
        });

        binding.getYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYzCode();
            }
        });
    }

    public void getYzCode() {
        String phoneNumber = registerBody.getPhoneNumber();
        if (isEmail) {
            if (TextUtils.isEmpty(registerBody.getEmailAddress())) {
                Toast.makeText(activity, "请输入邮箱", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (!registerBody.getEmailAddress().contains("@")) {
                    Toast.makeText(activity, "邮箱格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(activity, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        GetVerification getVerification = new GetVerification();
        getVerification.setType("1");
        if (isEmail) {
            getVerification.setEmailAddress(registerBody.getEmailAddress());
        }
        getVerification.setPhoneNumber(phoneNumber);
        HttpUtil.getInstance().getVerificationCode(getVerification).subscribe(
                yzmStr -> {
                    YZM yzm = GsonUtil.getInstance().fromJson(yzmStr, YZM.class);
                    String result = yzm.getResult();
                    if (result.equals("OK")||result.equals("True")) {
                        binding.getYzm.setEnabled(false);
                        yzmCountDownTime(binding.getYzm);
                    } else {
                        Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                    }
                }
        );

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


    public void register() {
        if (isEmail) {
            if (TextUtils.isEmpty(registerBody.getEmailAddress())) {
                Toast.makeText(activity, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (TextUtils.isEmpty(registerBody.getPhoneNumber())) {
            Toast.makeText(activity, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(registerBody.getVerificationCode())) {
            Toast.makeText(activity, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(registerBody.getPassword())) {
            Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(registerBody.getNickName())) {
            registerBody.setNickName(registerBody.getPhoneNumber());
        }
        HttpUtil.getInstance().register(registerBody).subscribe(
                registerStr -> {
                    if (registerBody.getOpenID() == null) {
                         registerBody = new RegisterBody();
                        binding.setRegisterBody(registerBody);
                        LoginFragment loginFragment = new LoginFragment();
                        presenter.step2Fragment(loginFragment, "login");


                    } else {
                        LoginBody loginBody = new LoginBody();
                        loginBody.setOpenID(registerBody.getOpenID());
                        loginBody.setDeviceToken(Constant.diviceToken);
                        HttpUtil.getInstance().login(loginBody).subscribe(
                                loginStr -> {
                                    LoginResponse loginResponse = new Gson().fromJson(loginStr, LoginResponse.class);
                                    LoginResponse.ResultBean result = loginResponse.getResult();
                                    LoginResult loginResult = new LoginResult(Constant.uniquenessToken, result.getPayServerUrl(), result.getUserId());
                                    Constant.uniquenessToken = result.getUniquenessToken();
                                    loginResult.setUniquenessToken(Constant.uniquenessToken);
                                    Constant.balances = result.getBalances();
                                    Constant.toDayMoney = result.getToDayMoney();
                                    Constant.yesterDayMoney = result.getYesterDayMoney();
                                    LoginResultOption.getInstance().insertLoginResult(loginResult);
                                    Constant.userId = result.getUserId();
                                    Constant.expireInSeconds = result.getExpireInSeconds();
                                    long time = System.currentTimeMillis();
                                    SharepreferenceUtil.getInstance().saveAccessToken(result.getAccessToken(), Constant.uniquenessToken, time);
                                    Constant.payUrl = result.getPayServerUrl();
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    activity.startActivity(intent);
                                }
                        );
                    }
                }
        );
    }


    @Override
    protected void lazyLoad() {
    }


    public void initData(RegisterBody registerBody) {
        this.registerBody = registerBody;
    }


}
