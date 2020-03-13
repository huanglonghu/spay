package com.example.spay.ui.fragment.mainActivity;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spay.R;
import com.example.spay.bean.User;
import com.example.spay.bean.WsHeart;
import com.example.spay.databinding.FragmentMineBinding;
import com.example.spay.greendao.option.VersionMsgOption;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.base.GodCodeApplication;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MineFragment extends BaseFragment {
    private boolean isPrepared;
    private View view;
    private FragmentMineBinding binding;
    private User.ResultBean result;
    private com.example.spay.greendao.entity.User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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

    }

    public void refreshData() {
        setHeadImage();
    }


}
