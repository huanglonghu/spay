package com.example.godcode.ui.fragment.mainActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.bean.User;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.FragmentMineBinding;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.fragment.deatailFragment.PresonalFragment;
import com.example.godcode.utils.LogUtil;

public class MineFragment extends BaseFragment {
    private boolean isPrepared;
    private View view;
    private FragmentMineBinding binding;
    private User.ResultBean result;
    private com.example.godcode.greendao.entity.User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.presenal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PresonalFragment presonalFragment = new PresonalFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("mine", result);
                presonalFragment.setArguments(bundle);
                presenter.step2Fragment(presonalFragment, "presonal");
            }
        });
    }


    private boolean isVisible;

    @Override
    protected void lazyLoad() {
        if (!isVisible) {
            setHeadImage();
            isVisible = true;
        }
    }

    private void setHeadImage() {
        user = UserOption.getInstance().querryUser(Constant.userId);
        if (user != null) {
            binding.setUser(user);
            String headImageUrl = user.getHeadImageUrl();
            if(!TextUtils.isEmpty(headImageUrl)){
                if (!headImageUrl.contains("http")) {
                    headImageUrl = Constant.baseUrl + headImageUrl;
                }
                LogUtil.log("-------head--------"+headImageUrl);
                RxImageLoader.with(activity).load(headImageUrl).into(binding.ivUser);
            }
        }
    }

    @Override
    public void refreshData() {

        setHeadImage();
    }


}
