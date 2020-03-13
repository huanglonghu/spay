package com.example.spay.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;

import com.example.spay.R;
import com.example.spay.databinding.FragmentMainBinding;
import com.example.spay.greendao.entity.Friend;
import com.example.spay.handler.ActivityResultHandler;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.ui.activity.BaseActivity;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.base.GodCodeApplication;
import com.example.spay.ui.fragment.bindproduct.BindProductFragment;
import com.example.spay.ui.fragment.deatailFragment.AddFriendFragment;
import com.example.spay.ui.fragment.deatailFragment.Asset_1_Fragment;
import com.example.spay.ui.fragment.deatailFragment.BalanceFragment;
import com.example.spay.ui.fragment.deatailFragment.BankCardFragment;
import com.example.spay.ui.fragment.deatailFragment.ContactDetailFragment;
import com.example.spay.ui.fragment.deatailFragment.GatheringFragment;
import com.example.spay.ui.fragment.deatailFragment.MyAgent;
import com.example.spay.ui.fragment.deatailFragment.NewFriendFragment;
import com.example.spay.ui.fragment.deatailFragment.PayMentFragment;
import com.example.spay.ui.fragment.deatailFragment.PresonalFragment;
import com.example.spay.ui.fragment.deatailFragment.ProductCenterFragment;
import com.example.spay.ui.fragment.deatailFragment.ServiceRemainderFragment;
import com.example.spay.ui.fragment.pwd.SetPayPsdFragment;
import com.example.spay.ui.fragment.deatailFragment.SettingFragment;
import com.example.spay.ui.fragment.deatailFragment.TransationRecordFragment;
import com.example.spay.ui.fragment.deatailFragment.TransferAccountDetailFragment;
import com.example.spay.ui.fragment.deatailFragment.TxSuccessFragment;
import com.example.spay.ui.fragment.deatailFragment.VisitingCardFragment;
import com.example.spay.ui.fragment.deatailFragment.YSJLFragment;
import com.example.spay.ui.fragment.mainActivity.FriendFragment;
import com.example.spay.ui.view.widget.ExitDialog;
import com.example.spay.ui.view.QrcodeDialog;
import com.example.spay.utils.CommonUtil;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Presenter {
    FragmentManager fm;

    public BaseActivity getActivity() {
        return activity;
    }

    private BaseActivity activity;
    private GodCodeApplication application;

    private Presenter() {
        fragmentMap = new HashMap<>();
    }

    private static Presenter defaultInstance;

    public void clickItem(int type, Friend friend) {
        switch (type) {
            case 2:
                RxEvent rxEvent = new RxEvent(EventType.EVENTTYPE_SELECT_FRIEND);
                rxEvent.setId(friend.getUserId());
                RxBus.getInstance().post(rxEvent);
                back();
                break;
            case 3:
                //进入转账详情界面
                TransferAccountDetailFragment transferAccountDetailFragment = new TransferAccountDetailFragment();
                transferAccountDetailFragment.initData(friend.getUserId(), 0, 2);
                step2Fragment(transferAccountDetailFragment, "tadf");
                break;
            default:
                ContactDetailFragment cdf = new ContactDetailFragment();
                cdf.initData(friend.getUserId());
                step2Fragment(cdf, "cdf");
                break;
        }
    }

    public int getWindowWidth() {
        return application.getWindownWidth();
    }

    public int getWidowHeight() {
        return application.getWindowHeight();
    }

    public static Presenter getInstance() {
        Presenter presenter = defaultInstance;
        if (defaultInstance == null) {
            synchronized (Presenter.class) {
                presenter = new Presenter();
                defaultInstance = presenter;
            }
        }
        return presenter;
    }

    private int tag;
    private int TAG_LOGIN = 0;
    private int TAG_MAIN = 1;

    public void back() {
        fm.popBackStack();
    }

    public void initFragmentManager(BaseActivity activity, FragmentManager fm, int tag) {
        this.activity = activity;
        this.fm = fm;
        this.tag = tag;
        application = (GodCodeApplication) activity.getApplication();
    }

    private FragmentMainBinding binding;

    private int index;

    public void togglePager(int index) {
        this.index = index;
        binding.setPosition(index);
        binding.mainViewPager.setCurrentItem(index, false);
    }

    public void showNew(int flag) {
        int newsCount = binding.getNewsCount();
        FriendFragment friendFragment = (FriendFragment) getFragments().get(1);
        if (flag == 1) {
            binding.setNewsCount(newsCount + 1);
            friendFragment.showNews(newsCount + 1);
        } else if (flag == 2) {
            binding.setNewsCount(0);
            friendFragment.showNews(0);
        }

    }


    public void step2Fragment(String name) {
        if (tag == TAG_LOGIN) {
            fm.beginTransaction().replace(R.id.login_fragmentContainer, getFragment(name)).addToBackStack(name).commit();
        } else if (tag == TAG_MAIN) {
            fm.beginTransaction().replace(R.id.mainActivity_fragmentContainer, getFragment(name)).addToBackStack(name).commit();
        }
    }

    public void step2Fragment(BaseFragment fragment) {
        if (tag == TAG_LOGIN) {
            fm.beginTransaction().replace(R.id.login_fragmentContainer, fragment).commit();
        } else if (tag == TAG_MAIN) {
            fm.beginTransaction().replace(R.id.mainActivity_fragmentContainer, fragment).commit();
        }
    }


    public void removeFragment(BaseFragment fragment) {
        fm.beginTransaction().remove(fragment).commit();
    }

    public void step2Fragment(BaseFragment fragment, String name) {
        if (tag == TAG_LOGIN) {
            fm.beginTransaction().replace(R.id.login_fragmentContainer, fragment).addToBackStack(name).commit();
        } else if (tag == TAG_MAIN) {
            fm.beginTransaction().replace(R.id.mainActivity_fragmentContainer, fragment).addToBackStack(name).commit();
        }
    }


    public void showQrDialog(String productNum) {
        QrcodeDialog qrcodeDialog = new QrcodeDialog(activity, productNum);
        qrcodeDialog.show();
    }

    private HashMap<String, BaseFragment> fragmentMap;

    public BaseFragment getFragment(String name) {

        if (fragmentMap.get(name) == null) {
            BaseFragment fragment = null;
            switch (name) {
                case "myAsset":
                    fragment = new Asset_1_Fragment();
                    break;
                case "productCenter":
                    fragment = new ProductCenterFragment();
                    break;
                case "payment":
                    fragment = new PayMentFragment();
                    break;
                case "gathering":
                    fragment = new GatheringFragment();
                    break;
                case "serviceRemainder":
                    fragment = new ServiceRemainderFragment();
                    break;
                case "bankCard":
                    fragment = new BankCardFragment();
                    break;
                case "transationRecord":
                    fragment = new TransationRecordFragment();
                    break;
                case "setting":
                    fragment = new SettingFragment();
                    break;
                case "personal":
                    fragment = new PresonalFragment();
                    break;
                case "balance":
                    fragment = new BalanceFragment();
                    break;
                case "txSuccess":
                    fragment = new TxSuccessFragment();
                    break;
                case "newFriend":
                    fragment = new NewFriendFragment();
                    break;
                case "addFriend":
                    fragment = new AddFriendFragment();
                    break;
                case "visitingcard":
                    fragment = new VisitingCardFragment();
                    break;
                case "setPayPsd":
                    fragment = new SetPayPsdFragment();
                    break;
                case "bindProduct":
                    fragment = new BindProductFragment();
                    break;
                case "myAgent":
                    fragment = new MyAgent();
                    break;
            }
            fragmentMap.put(name, fragment);
            return fragment;
        } else {
            return fragmentMap.get(name);
        }
    }


    public void sys() {
        if (CommonUtil.isCameraCanUse()) {
            Intent intent = new Intent(activity, CaptureActivity.class);
            new ActivityResultHandler.Builder().activity(activity).intent(intent).requestCode(ActivityResultHandler.REQUEST_SCAN).build().startActivityForResult();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
    }

    public void step2Ysjl(int type) {
        YSJLFragment ysjlFragment = new YSJLFragment();
        ysjlFragment.initData(type);
        fm.beginTransaction().replace(R.id.mainActivity_fragmentContainer, ysjlFragment).addToBackStack("ysjl").commit();
    }

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public void setFragments(ArrayList<BaseFragment> fragments) {
        this.fragments = fragments;
    }

    public ArrayList<BaseFragment> getFragments() {
        return fragments;
    }

    public void setMainBinding(FragmentMainBinding binding) {
        this.binding = binding;
    }

    public void exit(Context context) {
        ExitDialog exitDialog = new ExitDialog(context);
        exitDialog.show();
    }

}
