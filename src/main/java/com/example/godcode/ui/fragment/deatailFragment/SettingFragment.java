package com.example.godcode.ui.fragment.deatailFragment;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentSettingBinding;
import com.example.godcode.greendao.entity.VersionMsg;
import com.example.godcode.greendao.option.LoginResultOption;
import com.example.godcode.greendao.option.VersionMsgOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.view.UpdateDialog;
import com.example.godcode.ui.view.widget.LanguageConfigDialog;
import com.example.godcode.utils.StringUtil;


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
        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.getInstance().exit(Constant.userId, Constant.uniquenessToken).subscribe(
                        exitStr -> {
                            LoginResultOption.getInstance().exit();
                            System.exit(0);
                        }
                );
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
