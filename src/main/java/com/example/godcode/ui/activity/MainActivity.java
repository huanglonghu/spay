package com.example.godcode.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.bean.CreateOrder;
import com.example.godcode.bean.OrderResult;
import com.example.godcode.bean.ProductScan;
import com.example.godcode.databinding.ActivityMainBinding;
import com.example.godcode.greendao.entity.Friend;
import com.example.godcode.greendao.option.FriendOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.service.TimeService;
import com.example.godcode.service.WebSocketService;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.fragment.deatailFragment.AddBankCardFragment;
import com.example.godcode.ui.fragment.deatailFragment.AssetFragment;
import com.example.godcode.ui.fragment.deatailFragment.BindingProductFragment;
import com.example.godcode.ui.fragment.deatailFragment.ContactDetailFragment;
import com.example.godcode.ui.fragment.deatailFragment.MobileRechargeFragment;
import com.example.godcode.ui.fragment.deatailFragment.NoticeDetailFragment;
import com.example.godcode.ui.fragment.deatailFragment.OrderDetailFragment;
import com.example.godcode.ui.fragment.deatailFragment.PaySuccessFragment;
import com.example.godcode.ui.fragment.deatailFragment.PresonalFragment;
import com.example.godcode.ui.fragment.deatailFragment.RechargeFragment;
import com.example.godcode.ui.fragment.deatailFragment.TransationRecordDetailFragment;
import com.example.godcode.ui.fragment.deatailFragment.TransferAccountDetailFragment;
import com.example.godcode.ui.fragment.deatailFragment.TxFragment;
import com.example.godcode.ui.fragment.deatailFragment.UserFragment;
import com.example.godcode.ui.fragment.deatailFragment.YSJLDetailFragment;
import com.example.godcode.ui.fragment.mainActivity.MainFragment;
import com.example.godcode.ui.view.PsdPopupWindow;
import com.example.godcode.utils.EncryptUtil;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.PayPsdSetting;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private FragmentManager supportFragmentManager;
    private WebSocketService.MyBinder myBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpUtil.getInstance().init(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainFragment mainFragment = new MainFragment();
        supportFragmentManager = getSupportFragmentManager();
        Presenter.getInstance().initFragmentManager(this, supportFragmentManager, 1);
        supportFragmentManager.beginTransaction().replace(R.id.mainActivity_fragmentContainer, mainFragment).addToBackStack("main").commit();
        PayPsdSetting.getInstance().initContext(this);
        //开启websocket服务
        Intent intent = new Intent(this, WebSocketService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        Intent intent1 = new Intent(this, TimeService.class);
        startService(intent1);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(myBinder == null){
                myBinder = (WebSocketService.MyBinder) service;
            }
            myBinder.connectWebSocket(MainActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        BaseFragment fragment = (BaseFragment) supportFragmentManager.findFragmentById(R.id.mainActivity_fragmentContainer);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragment instanceof MainFragment) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            } else if (fragment instanceof RechargeFragment || fragment instanceof AddBankCardFragment
                    || fragment instanceof TxFragment || fragment instanceof TransferAccountDetailFragment
                    || fragment instanceof OrderDetailFragment || fragment instanceof YSJLDetailFragment
                    || fragment instanceof PresonalFragment || fragment instanceof OrderDetailFragment
                    || fragment instanceof AssetFragment||fragment instanceof PaySuccessFragment||fragment instanceof MobileRechargeFragment
                    ) {
                fragment.onKeyDown();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private int RESULT_OK = 0xA1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.log(resultCode + "------zzzzzzzz------" + requestCode + "==================");
        Fragment fragment = supportFragmentManager.findFragmentById(R.id.mainActivity_fragmentContainer);
        if (resultCode != -1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("qr_scan_result");
                LogUtil.log("-------scan-------" + scanResult);
                //如果是收款信息则跳转 付款界面 如果是产品信息 则跳转绑定产品界面 如果是好友信息 则跳转好友详情界面
                if (scanResult.contains("productNumber")) {
                    handlerProduct(fragment, scanResult);
                } else {
                    handlerContact(scanResult);
                }
            }
        } else {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handlerProduct(Fragment fragment, String scanResult) {
        String[] split = scanResult.split("=");
        String productNumber = split[split.length - 1];
        if (!(fragment instanceof BindingProductFragment)) {
            HttpUtil.getInstance().isBind(productNumber)
                    .subscribe(isBindStr -> {
                                if (isBindStr.contains("\"success\":true")) {
                                    ProductScan productScan = GsonUtil.getInstance().fromJson(isBindStr, ProductScan.class);
                                    if (productScan.getResult().isIsBind()) {
                                        //去支付  1.生成订单号 跳转扫码支付界面
                                        if (productScan.getResult().isIsValid()) {
                                            ProductScan.ResultBean result = productScan.getResult();
                                            CreateOrder createOrder = new CreateOrder();
                                            createOrder.setFK_ProductID(result.getId());
                                            createOrder.setFeeType("CNY");
                                            createOrder.setSumOrder(result.getMoney());
                                            createOrder.setFK_UserID(Constant.userId);
                                            HttpUtil.getInstance().getOrderNumber(createOrder)
                                                    .subscribe(getOrderNumberStr -> {
                                                                OrderResult orderResult = GsonUtil.getInstance().fromJson(getOrderNumberStr, OrderResult.class);
                                                                OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
                                                                orderDetailFragment.initData(orderResult, result);
                                                                supportFragmentManager.beginTransaction()
                                                                        .replace(R.id.mainActivity_fragmentContainer, orderDetailFragment)
                                                                        .addToBackStack("orderDetail")
                                                                        .commitAllowingStateLoss();
                                                            }
                                                    );
                                        } else {
                                            Toast.makeText(this, "设备出现故障无法运行", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        //去绑定
                                        toBindProduct(productNumber);
                                    }
                                } else {
                                    //去绑定
                                    toBindProduct(productNumber);
                                }
                            }
                    );
        } else {
            ((BindingProductFragment) fragment).handlerScanStr(productNumber);
        }
    }

    private void handlerContact(String scanResult) {
        try {
            if (scanResult.contains("WeChatPay")) {
                String cipherText = scanResult.substring(scanResult.indexOf("Ciphertext=") + "Ciphertext=".length());
                cipherText = cipherText.replaceAll("~", "\\+").replaceAll("!", "/").replaceAll("-", "=");
                String s = EncryptUtil.aesDecrypt(cipherText, "87a4d115c0956912b495d6bb8b7c0013");
                LogUtil.log("------decrypt-------" + s);
                String[] split = s.split("\\|");
                TransferAccountDetailFragment tadf = new TransferAccountDetailFragment();
                double money = Double.parseDouble(split[2]);
                int userId = Integer.parseInt(split[1]);
                tadf.initData(userId, money, 3);
                supportFragmentManager.beginTransaction().replace(R.id.mainActivity_fragmentContainer, tadf).addToBackStack("tadf").
                        commitAllowingStateLoss();
            } else {
                String a = scanResult.replaceAll("~", "\\+").replaceAll("!", "/").replaceAll("-", "=");
                String s = EncryptUtil.aesDecrypt(a, "87a4d115c0956912b495d6bb8b7c0013");
                String[] split = s.split("\\|");
                int id = Integer.parseInt(split[1]);
                Friend friend = FriendOption.getInstance(this).querryFriend(id);
                if (friend == null) {
                    UserFragment userFragment = new UserFragment();
                    userFragment.initData(id);
                    supportFragmentManager.beginTransaction().replace(R.id.mainActivity_fragmentContainer, userFragment).addToBackStack("user").
                            commitAllowingStateLoss();
                } else {
                    ContactDetailFragment cdf = new ContactDetailFragment();
                    cdf.initData(id);
                    supportFragmentManager.beginTransaction().replace(R.id.mainActivity_fragmentContainer, cdf).addToBackStack("cdf").
                            commitAllowingStateLoss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toBindProduct(String productNumber) {
        BindingProductFragment bindingProductFragment = new BindingProductFragment();
        Bundle b = new Bundle();
        b.putString("productNumber", productNumber);
        bindingProductFragment.setArguments(b);
        supportFragmentManager.beginTransaction().replace(R.id.mainActivity_fragmentContainer, bindingProductFragment).addToBackStack("bindProduct").commitAllowingStateLoss();
    }

    @Override
    public void notifyFragmentDataChange(BaseFragment fragment) {
        fragment.refreshData();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (PsdPopupWindow.getInstance(this) != null && PsdPopupWindow.getInstance(this).isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int message = intent.getIntExtra("message", 0);
        switch (message) {
            case 0:
                Presenter.getInstance().togglePager(2);
                Bundle bundle0 = intent.getExtras();
                String noticeId = bundle0.getString("noticeId");
                NoticeDetailFragment noticeDetailFragment = new NoticeDetailFragment();
                bundle0.putInt("noticeId", Integer.parseInt(noticeId));
                noticeDetailFragment.setArguments(bundle0);
                Presenter.getInstance().step2Fragment(noticeDetailFragment, "noticeDetail");
                break;
            case 1:
                step2transtaion(intent);
                break;
            case 2:
                Presenter.getInstance().step2Fragment("newFriend");
                break;
            case 3:
                Toast.makeText(this, "您的账号在其他设备上登录，请注意账号安全", Toast.LENGTH_SHORT).show();
                break;
            case 4:
//                ContactDetailFragment cdf = new ContactDetailFragment();
//                //cdf.initData(friend);
//                Presenter.getInstance().step2Fragment(cdf, "cdf");
                break;
            case 5:
                step2transtaion(intent);
                break;
            case 6:
                step2transtaion(intent);
                break;
            case 7:
                step2transtaion(intent);
                break;
        }
    }

    private void step2transtaion(Intent intent) {
        TransationRecordDetailFragment trdf1 = new TransationRecordDetailFragment();
        Bundle extras1 = intent.getExtras();
        trdf1.setArguments(extras1);
        Presenter.getInstance().step2Fragment(trdf1, "transationDetail");
    }



}
