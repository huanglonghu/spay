package com.example.godcode.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;

import com.example.godcode.bean.WebSocketNews1;
import com.example.godcode.bean.WebSocketNews2;
import com.example.godcode.bean.WebSocketNews3;
import com.example.godcode.bean.WebSocketNews4;
import com.example.godcode.bean.WsHeart;
import com.example.godcode.greendao.entity.Notification;
import com.example.godcode.greendao.option.FriendOption;
import com.example.godcode.greendao.option.NotificationOption;
import com.example.godcode.greendao.option.VersionMsgOption;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.activity.BaseActivity;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.base.GodCodeApplication;
import com.example.godcode.ui.fragment.deatailFragment.AssetFragment;
import com.example.godcode.ui.fragment.deatailFragment.BalanceFragment;
import com.example.godcode.ui.view.UpdateDialog;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;

public class WebSocketUtil {
    private String TAG = "WebSocket:";

    private WebSocketClient mSocketClient;

    private BaseActivity activity;
    private static Disposable subscribe;
    private Subscription heartSubscribe;
    private UpdateDialog updateDialog;

    public WebSocketUtil(BaseActivity activity) {
        this.activity = activity;
    }

    private String url;

    public void connect(String url) {
        this.url = url;
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> e) throws Exception {
                mSocketClient = new WebSocketClient(new URI(url), new Draft_6455()) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        LogUtil.log(TAG + "打开通道" + handshakedata.getHttpStatus());
                        sendHeart();
                        if (subscribe != null) {
                            subscribe.dispose();
                            subscribe = null;

                        }
                    }

                    @Override
                    public void onMessage(String message) {
                        LogUtil.log(TAG + "接收消息" + message);
                        Message msg = new Message();
                        String eventType = message.split(",")[0].split(":")[1];
                        if (eventType.equals("3") || eventType.equals("7")) {//{"EventType":3,"Data":"您绑定的银行卡已审核通过！"}
                            WebSocketNews3 news = GsonUtil.getInstance().fromJson(message, WebSocketNews3.class);
                            Notification notification = new Notification();
                            notification.setContent(news.getData().getMsg());
                            notification.setUserId(Constant.userId);
                            notification.setDate(getDate());
                            notification.setTitle("银行卡审核");
                            notification.setType(2);
                            NotificationOption.getInstance().memoryNotification(notification);
                            msg.what = 1;
                        } else if (eventType.equals("4") || eventType.equals("5") || eventType.equals("6") || eventType.equals("8") || eventType.equals("10") || eventType.equals("11")) {//{"EventType":4,"Data":{"msg":"您的提现已成功！","RelatedKey":11}}
                            Notification notification = new Notification();
                            notification.setUserId(Constant.userId);
                            notification.setDate(getDate());
                            WebSocketNews2 webSocketNews2 = GsonUtil.getInstance().fromJson(message, WebSocketNews2.class);
                            WebSocketNews2.DataBean data = webSocketNews2.getData();
                            notification.setContent(webSocketNews2.getData().getMsg());
                            notification.setRelatedKey(data.getRelatedKey());
                            notification.setTransactionId(data.getId());
                            notification.setTransactionType(data.getTransactionType());
                            notification.setType(1);
                            NotificationOption.getInstance().memoryNotification(notification);
                            msg.what = 6;
                        } else if (eventType.equals("1") || eventType.equals("2") || eventType.equals("13") || eventType.equals("14") || eventType.equals("15") || eventType.equals("16") || eventType.equals("17") || eventType.equals("18")) {
                            WebSocketNews3 webSocketNews1 = GsonUtil.getInstance().fromJson(message, WebSocketNews3.class);
                            Notification notification = new Notification();
                            notification.setUserId(Constant.userId);
                            notification.setContent(webSocketNews1.getData().getMsg());
                            notification.setDate(getDate());
                            switch (eventType) {
                                case "1":
                                    notification.setTitle("好友请求");
                                    notification.setType(3);
                                    break;
                                case "2":
                                    notification.setType(3);
                                    notification.setTitle("好友同意");
                                    break;
                                case "13":
                                    notification.setType(4);
                                    notification.setTitle("资产分成");
                                    break;
                                case "14":
                                    notification.setType(4);
                                    notification.setTitle("修改资产分成");
                                    break;
                                case "15":
                                    notification.setType(4);
                                    notification.setTitle("删除资产分成");
                                    break;
                                case "16":
                                    notification.setType(4);
                                    notification.setTitle("解除资产绑定");
                                    break;
                                case "17":
                                    notification.setType(4);
                                    notification.setTitle("产权归还");
                                    break;
                                case "18":
                                    notification.setType(4);
                                    notification.setTitle("产权转移");
                                    break;
                            }
                            if (eventType.equals("1")) {
                                msg.what = 4;
                            } else {
                                msg.what = 1;
                            }
                            NotificationOption.getInstance().memoryNotification(notification);
                        } else if (eventType.equals("12")) {
                            WebSocketNews4 webSocketNews4 = GsonUtil.getInstance().fromJson(message, WebSocketNews4.class);
                            int id = webSocketNews4.getData().getRelatedKey();
                            msg.what = 3;
                            msg.obj = id;
                        } else if (eventType.equals("9")) {
                            msg.what = 2;
                        } else if (eventType.equals("19")) {
                            WebSocketNews1 webSocketNews1 = GsonUtil.getInstance().fromJson(message, WebSocketNews1.class);
                            msg.what = 5;
                            msg.obj = webSocketNews1;
                        } else if (eventType.equals("21")) {
                            WsHeart wsHeart = GsonUtil.getInstance().fromJson(message, WsHeart.class);
                            String androidVer = wsHeart.getData().getAndroidVer();
                            try {
                                String versionName = GodCodeApplication.getInstance().getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
                                int version1 = Integer.parseInt(versionName.replace(".", ""));
                                int version2 = Integer.parseInt(androidVer.replace(".", ""));
                                if (version2 > version1) {
                                    msg.what = 7;
                                    msg.obj = wsHeart;
                                }
                            } catch (PackageManager.NameNotFoundException e1) {
                                e1.printStackTrace();
                            }
                        }
                        e.onNext(msg);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        LogUtil.log(TAG + "通道关闭" + reason);
                        if (subscribe == null) {
                            synchronized (WebSocketUtil.class) {
                                if (subscribe == null) {
                                    subscribe = Observable.interval(0, 2, TimeUnit.SECONDS).subscribe(
                                            s -> {
                                                if (s == 1) {
                                                    LogUtil.log("-----cl--------" + s);
                                                    mSocketClient.reconnect();
                                                    subscribe.dispose();
                                                    subscribe = null;
                                                }
                                            }
                                    );
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Exception ex) {
                        LogUtil.log(TAG + "链接错误" + ex.toString());
                    }
                };

                mSocketClient.connect();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                msg -> {
                    switch (msg.what) {
                        case 1:
                            activity.notifyFragmentDataChange(Presenter.getInstance().getFragments().get(0));
                            break;
                        case 2:
                            Presenter.getInstance().exit(activity);
                            break;
                        case 3:
                            int id = (int) msg.obj;
                            FriendOption.getInstance(activity).deleteFriend(id);
                            break;
                        case 4:
                            Presenter.getInstance().showNew(1);
                            activity.notifyFragmentDataChange(Presenter.getInstance().getFragments().get(0));
                            break;
                        case 5:
                            WebSocketNews1 webSocketNews1 = (WebSocketNews1) msg.obj;
                            AssetFragment myAsset = (AssetFragment) Presenter.getInstance().getFragment("myAsset");
                            myAsset.refresh(webSocketNews1);
                            break;
                        case 6:
                            activity.notifyFragmentDataChange(Presenter.getInstance().getFragments().get(0));
                            BalanceFragment balanceFragment = (BalanceFragment) Presenter.getInstance().getFragment("balance");
                            balanceFragment.qurryBalance();
                            break;
                        case 7:
                            if (updateDialog == null) {
                                createUpdateDialog(msg);
                            }
                            break;
                    }
                }
        );
    }

    private void createUpdateDialog(Message msg) {
        WsHeart wsHeart = (WsHeart) msg.obj;
        wsHeart.getData().getAndroidVerDes();
        String androidVerDes = wsHeart.getData().getAndroidVerDes();
        String androidVer = wsHeart.getData().getAndroidVer();
        VersionMsgOption.getInstance().updateVersion(androidVer, androidVerDes);
        updateDialog = new UpdateDialog(activity);
        updateDialog.show();
        updateDialog.setDescibe(androidVerDes, androidVer);
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        return sdf.format(now);
    }


    // 1 请求添加好友 您有一条好友请求！
    // 2 同意添加好友 您的好友'{0}'同意了您的添加请求！
    // {"EventType":3,"Data":{"msg":"您绑定的银行卡已审核通过！"}}
    //4 提现成功 您的提现已成功
    // 5好友转账 您的好友向您转账
    // 6二维码收款 您有新的二维码收款入账
    // {"EventType":7,"Data":{"msg":"您绑定的银行卡审核不通过！"}}
    //8 提现失败  "您的提现申请审核失败已驳回！
    // 9接收消息{"EventType":9,"Data":{"msg":"该账户已在其他设备登录！"}}
    //10 您的退款已到账
    //11 您的产品营收已到账
    //WebSocket:接收消息{"EventType":12,"Data":{"msg":"删除好友","RelatedKey":20109}}
    //{"EventType":11,"Data":{"msg":"您的产品营收已转入您的余额！","RelatedKey":10313,"Id":50942,"TransactionType":9}}
    //{"EventType":13,"Data":{"msg":"您已获得新设备'xdq'的分成比例'10%'！"}}
    //{"EventType":14,"Data":{"msg":"设备'xdq'的分成比例已变更为'1%'！"}}
    //{"EventType":15,"Data":{"msg":"主商家已删除您对设备'xdq''的分成占比！"}}
    // WebSocket:接收消息{"EventType":19,"Data":{"ProductNumber":"SY155","CoinCount":0,"PaperMoney":0.0,"ScanQRMoney":1.00},"Flag":"78c0d03d-83f8-4129-bdd3-26224e30b1ec"}

    public void stopWebSocket() {
        if (mSocketClient != null) {
            mSocketClient.close();
        }
    }


    public void sendHeart() {

        if (heartSubscribe == null) {
            heartSubscribe = rx.Observable.interval(0, 30, TimeUnit.SECONDS, rx.schedulers.Schedulers.io()).subscribe(
                    (Long time) -> {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("EventType", 20);
                            JSONObject data = new JSONObject();
                            jsonObject.put("Data", data);
                            String s = jsonObject.toString();
                            mSocketClient.send(s);
                        } catch (Exception e) {
                            LogUtil.log("-----心跳异常------" + e.toString());
                        }
                    }
            );
        }

    }

}
