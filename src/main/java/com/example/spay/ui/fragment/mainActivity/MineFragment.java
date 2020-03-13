package com.example.spay.ui.fragment.mainActivity;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.bean.User;
import com.example.spay.bean.WsHeart;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.FragmentMineBinding;
import com.example.spay.greendao.option.UserOption;
import com.example.spay.greendao.option.VersionMsgOption;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.base.GodCodeApplication;
import com.example.spay.ui.fragment.deatailFragment.PresonalFragment;
import com.example.spay.utils.ImagUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MineFragment extends BaseFragment {
    private boolean isPrepared;
    private View view;
    private FragmentMineBinding binding;
    private User.ResultBean result;
    private com.example.spay.greendao.entity.User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.setTitle("我");
            initListener();
        }
        return view;
    }

    private void initListener() {

        RxBus.getInstance().toObservable(RxEvent.class).subscribe(new Observer<RxEvent>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(RxEvent rxEvent) {
                switch (rxEvent.getEventType()) {
                    case EventType.EVENTTYPE_HEART:
                        WsHeart wsHeart = (WsHeart) rxEvent.getBundle().getSerializable("heart");
                        String androidVer = wsHeart.getData().getAndroidVer();
                        try {
                            String versionName = GodCodeApplication.getInstance().getPackageManager().getPackageInfo(GodCodeApplication.getInstance().getPackageName(), 0).versionName;
                            int version1 = Integer.parseInt(versionName.replace(".", ""));
                            int version2 = Integer.parseInt(androidVer.replace(".", ""));
                            if (version2 > version1) {
                                String androidVerDes = wsHeart.getData().getAndroidVerDes();
                                VersionMsgOption.getInstance().updateVersion(androidVer, androidVerDes);
                                MainFragment main = (MainFragment) getParentFragment();
                                main.getBinding().setUpdate(true);
                            }
                        } catch (PackageManager.NameNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        binding.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PresonalFragment presonalFragment = new PresonalFragment();
                Presenter.getInstance().step2Fragment(presonalFragment, "presonal");
            }
        });


    }


    private boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {
            setHeadImage();
        }
    }

    @Override
    protected void lazyLoad() {

    }

    private void setHeadImage() {

        user = UserOption.getInstance().querryUser(Constant.userId);
        if (user != null) {
            binding.setUser(user);
//            String headImageUrl = user.getHeadImageUrl();
//            String url = ImagUtil.handleUrl(headImageUrl);
//            if (!TextUtils.isEmpty(url)) {
//                RxImageLoader.with(getContext()).getBitmap(url).subscribe(
//                        imageBean -> {
//
//                            Bitmap bitmap = imageBean.getBitmap();
//                            Drawable drawable = ImagUtil.circle(bitmap);
//                            binding.head.setImageDrawable(drawable);
//
//                        }
//                );
//            }

        }

    }

    public void refreshData() {
        setHeadImage();
    }


}
