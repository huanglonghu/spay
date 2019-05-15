package com.example.godcode.utils;

import android.annotation.SuppressLint;
import com.example.godcode.bean.WebSocketNews1;
import com.example.godcode.bean.WebSocketNews2;
import com.example.godcode.bean.WebSocketNews3;
import com.example.godcode.bean.WebSocketNews4;
import com.example.godcode.bean.WsHeart;
import com.example.godcode.handler.WebSocketNewsHandler;
import com.example.godcode.observable.WebSocketNewsObservable;
import com.example.godcode.ui.activity.BaseActivity;
import com.example.godcode.ui.view.UpdateDialog;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import java.net.URI;
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
    private WebSocketNewsObservable<WebSocketNewsHandler> observable;

    public WebSocketUtil(BaseActivity activity, WebSocketNewsObservable<WebSocketNewsHandler> observable) {
        this.activity = activity;
        this.observable = observable;
    }

    @SuppressLint("CheckResult")
    public void connect(String url) {
        Observable.create(new ObservableOnSubscribe<WebSocketNewsHandler.Builder>() {
            @Override
            public void subscribe(ObservableEmitter<WebSocketNewsHandler.Builder> e) throws Exception {
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
                       // message = "{\"EventType\":6,\"Data\":{\"msg\":\"您有新的二维码收款入账！\",\"RelatedKey\":10055,\"Id\":10291,\"TransationType\":2}}";
                       // message="{\"EventType\":9,\"Data\":{\"msg\":\"该账户已在其他设备登录！\"}}";
                       // message="{\"EventType\":19,\"Data\":{\"ProductNumber\":\"4G4\",\"CoinCount\":0,\"PaperMoney\":0.0,\"ScanQRMoney\":0.04,\"DivedeMoney\":0.04,\"MerchantUserIds\":null,\"AwardPosition\":0,\"AwardCount\":1},\"Flag\":\"9022cd79-9c90-46a7-95e4-548423e48cd0\"}";
                        LogUtil.log(message);
                        WebSocketNewsHandler.Builder builder = new WebSocketNewsHandler.Builder();
                        String type = message.substring(message.indexOf("\"EventType\":") + "\"EventType\":".length(), message.indexOf(","));
                        analyWebSocketNews(type, message, builder);
                        getStrategyByType(type, builder);
                        e.onNext(builder);
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
                builder -> {
                    WebSocketNewsHandler webSocketNewsHandler = builder.build();
                    webSocketNewsHandler.setNofication();
                    observable.notifyObservers(webSocketNewsHandler);
                }
        );
    }


    public void analyWebSocketNews(String type, String message, WebSocketNewsHandler.Builder builder) {
        builder.eventType(type);
        switch (type) {
            case "19":
                WebSocketNews1 webSocketNews1 = GsonUtil.getInstance().fromJson(message, WebSocketNews1.class);
                builder.webSocketNews1(webSocketNews1);
                break;
            case "4": case "5": case "6": case "8": case "10": case "11":
                WebSocketNews2 webSocketNews2 = GsonUtil.getInstance().fromJson(message, WebSocketNews2.class);
                builder.webSocketNews2(webSocketNews2);
            case "12":
                WebSocketNews4 webSocketNews4 = GsonUtil.getInstance().fromJson(message, WebSocketNews4.class);
                builder.webSocketNews4(webSocketNews4);
                break;
            case "1": case "2": case "3": case "7": case "13": case "14": case "15": case "16": case "17": case "18":
                WebSocketNews3 webSocketNews3 = GsonUtil.getInstance().fromJson(message, WebSocketNews3.class);
                builder.webSocketNews3(webSocketNews3);
                break;
            case "21":
                WsHeart wsHeart = GsonUtil.getInstance().fromJson(message, WsHeart.class);
                builder.wsHeart(wsHeart);
                break;
        }
    }


    public void getStrategyByType(String type, WebSocketNewsHandler.Builder builder) {
        switch (type) {
            case "2": case "3": case "7": case "13": case "14": case "15": case "16": case "17": case "18":
                builder.handlerType(1);
                break;
            case "9":
                builder.handlerType(2);
                break;
            case "12":
                builder.handlerType(3);
                break;
            case "1":
                builder.handlerType(4);
                break;
            case "19":
                builder.handlerType(5);
                break;
            case "4": case "5": case "6": case "8": case "10": case "11":
                builder.handlerType(6);
                break;
            case "21":
                builder.handlerType(7);
                break;
        }

    }




    // 1 请求添加好友 您有一条好友请求！   /**
    //     * EventType : 1
    //     * Data : {"msg":"用户'sy1535799652'请求添加您为好友！"}
    //     */
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
    //  {"EventType":19,"Data":{"ProductNumber":"SY143","CoinCount":3,"PaperMoney":0.0,"ScanQRMoney":0.0,"DivedeMoney":0.0,"MerchantUserIds":"26/29"},"Flag":"6159558f-f4a1-4c77-8f43-94903cd9407e"}
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
