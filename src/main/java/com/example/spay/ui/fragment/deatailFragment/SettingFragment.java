package com.example.spay.ui.fragment.deatailFragment;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.databinding.FragmentSettingBinding;
import com.example.spay.greendao.entity.VersionMsg;
import com.example.spay.greendao.option.LoginResultOption;
import com.example.spay.greendao.option.VersionMsgOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.fragment.pwd.CheckPayPsdFragment;
import com.example.spay.ui.fragment.pwd.SetPayPsdFragment;
import com.example.spay.ui.view.UpdateDialog;
import com.example.spay.utils.PayPwdSetting;
import com.example.spay.utils.StringUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class SettingFragment extends BaseFragment {
    private FragmentSettingBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            String title = StringUtil.getString(activity, R.string.setting);
            binding.settingToolbar.title.setText(title);
            initView();
            initListener();
        }
        return view;
    }

    private void initListener() {

        binding.pwdRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPwdSetting.getInstance().verifyPwd(new ClickSureListener() {
                    @Override
                    public void isPwdExit(boolean isPwdExit) {
                        if (isPwdExit) {
                            CheckPayPsdFragment checkPayPsdFragment = new CheckPayPsdFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("eventType", EventType.EVETNTTYPE_SETTING_CHECKPWD);
                            checkPayPsdFragment.setArguments(bundle);
                            Presenter.getInstance().step2Fragment(checkPayPsdFragment, "checkPwd");
                        }
                    }
                });
            }
        });

        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.getInstance().exit(Constant.userId, Constant.uniquenessToken).subscribe(
                        exitStr -> {

                        }
                );
                LoginResultOption.getInstance().exit();
                System.exit(0);
            }
        });

        binding.versionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionMsg versionMsg = VersionMsgOption.getInstance().querryVersion();
                if (versionMsg != null) {
                    String versionCode = versionMsg.getVersionCode();
                    String versionDes = versionMsg.getVersionDes();
                    createUpdateDialog(versionDes, versionCode);
                } else {
                    Toast.makeText(activity, "当前版本是最新版本", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RxBus.getInstance().toObservable(RxEvent.class).subscribe(new Observer<RxEvent>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(RxEvent rxEvent) {
                //处理事件
                if (rxEvent.getEventType() == EventType.EVETNTTYPE_SETTING_CHECKPWD) {
                    String originalPayPass = rxEvent.getBundle().getString("OriginalPayPass");
                    SetPayPsdFragment setPayPsdFragment = new SetPayPsdFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "请设置新密码");
                    bundle.putString("OriginalPayPass", originalPayPass);
                    setPayPsdFragment.setArguments(bundle);
                    Presenter.getInstance().step2Fragment(setPayPsdFragment, "setPwd");
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

//        binding.LanguageConfig.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LanguageConfigDialog languageConfigDialog = new LanguageConfigDialog(activity);
//                languageConfigDialog.show();
//            }
//        });


    }

    private void createUpdateDialog(String des, String versionCode) {
        UpdateDialog updateDialog = new UpdateDialog(activity);
        updateDialog.show();
        updateDialog.setDescibe(des, versionCode);
    }

    public void initView() {
        try {
            String versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
            binding.versionCode.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void lazyLoad() {
    }

}
