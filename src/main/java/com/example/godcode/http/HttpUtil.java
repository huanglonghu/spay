package com.example.godcode.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.godcode.bean.ChangePsd;
import com.example.godcode.bean.EditPresonal;
import com.example.godcode.bean.AddFriend;
import com.example.godcode.bean.BindBankCard;
import com.example.godcode.bean.BindProductBody;
import com.example.godcode.bean.CheckPsd;
import com.example.godcode.bean.ConcurAddFriend;
import com.example.godcode.bean.CreateDivide;
import com.example.godcode.bean.CreateOrder;
import com.example.godcode.bean.EditBankCard;
import com.example.godcode.bean.EditProduct;
import com.example.godcode.bean.EditProductPrice;
import com.example.godcode.bean.GetVerification;
import com.example.godcode.bean.Id;
import com.example.godcode.bean.LoginBody;
import com.example.godcode.bean.LoginResponse;
import com.example.godcode.bean.MobileRecharge;
import com.example.godcode.bean.PayByBalance;
import com.example.godcode.bean.RechargeBody;
import com.example.godcode.bean.RefreshToken;
import com.example.godcode.bean.RegisterBody;
import com.example.godcode.bean.ReturnEquity;
import com.example.godcode.bean.SetPayPsd;
import com.example.godcode.bean.TransationDetail;
import com.example.godcode.bean.TransferBody;
import com.example.godcode.bean.TransferDivide;
import com.example.godcode.bean.Tx;
import com.example.godcode.bean.UpdateFriend;
import com.example.godcode.bean.WxPay;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.NetLoading;
import com.example.godcode.utils.ErrrCodeShow;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.SharepreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {
    private HttpInterface httpInterface;
    private HttpLoggingInterceptor loggingInterceptor;
    private Retrofit retrofit;
    private OkHttpClient client;
    private NetLoading netLoading;


    private HttpUtil() {
        //打印retrofit日志
        loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                LogUtil.log(message);
            }
        });
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl oldHttpUrl = request.url();
                Request.Builder builder = request.newBuilder();
                String path = oldHttpUrl.encodedPath();
                if (!path.contains("RegisterMobileUser") && !path.contains("MoblieWXAuthToken") && !path.contains("Authenticate")) {
                    String accessToken = SharepreferenceUtil.getInstance().getAccessToken();
                    String loginToken = SharepreferenceUtil.getInstance().getLoginToken();
                    request = builder.addHeader("Authorization", "Bearer " + accessToken).addHeader("LoginToken", loginToken + "&" + Constant.userId).build();
                }

//                okhttp3.Response proceed = chain.proceed(request);//判断token是否刷新
//                LogUtil.log("-------code-----" + proceed.code());
//                if (isTokenExpired(proceed)) {
//                    String newToken = getNewToken();
//                    LogUtil.log("-------refreshToken-------" + newToken);
//                }

                if (path.equals("/MoblieWeChatAuth/MoblieWXAuthToken") || path.equals("/WeChatPay/MoblieWXPayRequest")) {
                    HttpUrl newBaseUrl = HttpUrl.parse("http://godcodepay.joinvalue.com:8901/");
                    HttpUrl newFullUrl = oldHttpUrl
                            .newBuilder()
                            .scheme(newBaseUrl.scheme())
                            .host(newBaseUrl.host())
                            .port(newBaseUrl.port())
                            .build();
                    return chain.proceed(builder.url(newFullUrl).build());
                } else {
                    return chain.proceed(request);
                }
            }
        };


        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS).addInterceptor(loggingInterceptor).addInterceptor(interceptor)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS).build();
        retrofit = new Retrofit.Builder().client(client).baseUrl(Constant.baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        httpInterface = retrofit.create(HttpInterface.class);
    }


    private String getNewToken() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().client(client).baseUrl(Constant.baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        httpInterface = retrofit.create(HttpInterface.class);
        Call<ResponseBody> call = retrofit.create(HttpInterface.class).refreshAccessToken(new RefreshToken(Constant.uniquenessToken));
        Response<ResponseBody> s = call.execute();
        String s1 = s.body().string();
        return s1;
    }


    private boolean isTokenExpired(okhttp3.Response response) {
        if (response.code() == 401) {
            return true;
        }
        return false;
    }


    private static HttpUtil defauleInstance;

    public static HttpUtil getInstance() {
        HttpUtil httpUtil = defauleInstance;
        if (defauleInstance == null) {
            synchronized (HttpUtil.class) {
                httpUtil = new HttpUtil();
                defauleInstance = httpUtil;
            }
        }
        return httpUtil;
    }

    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public Observable<String> login(LoginBody body) {
        Call<ResponseBody> call = httpInterface.login(body);
        return enqueueCall(call);
    }

    public Observable<String> register(RegisterBody body) {
        Call<ResponseBody> call = httpInterface.register(body);
        return enqueueCall(call);
    }

    public Observable<String> getAllProductCategorys() {
        Call<ResponseBody> call = httpInterface.getAllProductCategory();
        return enqueueCall(call);
    }

    public Observable<String> getPagedProductList(HashMap<String, String> map) {
        Call<ResponseBody> call = httpInterface.getPagedProducts(map);
        return enqueueCall(call);
    }

    public Observable<String> getProductById(int id) {
        Call<ResponseBody> call = httpInterface.getProductById(id);
        return enqueueCall(call);
    }

    public Observable<String> getProductByType(HashMap<String, String> map) {
        Call<ResponseBody> call = httpInterface.getProductByType(map);
        return enqueueCall(call);
    }

    public Observable<String> getFriendList(HashMap<String, String> map) {
        Call<ResponseBody> call = httpInterface.getFriendList(map);
        return enqueueCall(call);
    }

    public Observable<String> getUserMsgById(int id) {
        Call<ResponseBody> call = httpInterface.getUserById(id);
        return enqueueCall(call);
    }

    public Observable<String> bindProduct(BindProductBody body) {
        Call<ResponseBody> call = httpInterface.bindProduct(body);
        return enqueueCall(call);
    }

    public Observable<String> querryBalance(int id) {
        Call<ResponseBody> call = httpInterface.queryBalance(id);
        return enqueueCall(call);
    }

    public Observable<String> getRevenueDivideById(int id) {
        Call<ResponseBody> call = httpInterface.getRevenueDivideById(id);
        return enqueueCall(call);
    }

    public Observable<String> createDivide(CreateDivide createDivide) {
        Call<ResponseBody> call = httpInterface.createRevenueDivide(createDivide);
        return enqueueCall(call);
    }

    public Observable<String> addFriend(AddFriend addFriend) {
        Call<ResponseBody> call = httpInterface.addFriend(addFriend);
        return enqueueCall(call);
    }


    public Observable<String> concurAddFriend(ConcurAddFriend concurAddFriend) {
        Call<ResponseBody> call = httpInterface.concurAddFriend(concurAddFriend);
        return enqueueCall(call);
    }


    public Observable<String> editProductPrice(EditProductPrice editProductPrice) {
        Call<ResponseBody> call = httpInterface.editProductPrice(editProductPrice);
        return enqueueCall(call);
    }

    public Observable<String> getTeansantionTypeList() {
        Call<ResponseBody> call = httpInterface.getTeansantionTypeList();
        return enqueueCall(call);
    }

    public Observable<String> getTeansantion(HashMap<String, String> map) {
        Call<ResponseBody> call = httpInterface.getTransaction(map);
        return enqueueCall(call);
    }

    public Observable<String> hasPsd(int id) {
        Call<ResponseBody> call = httpInterface.getPsyPsdById(id);
        return enqueueCall(call);
    }

    public Observable<String> setPayPsd(SetPayPsd setPayPsd) {
        Call<ResponseBody> call = httpInterface.createPsd(setPayPsd);
        return enqueueCall(call);
    }

    public Observable<String> changePsd(ChangePsd changePsd) {
        Call<ResponseBody> call = httpInterface.changePsd(changePsd);
        return enqueueCall(call);
    }

    public Observable<String> transfer(TransferBody transferBody) {
        Call<ResponseBody> call = httpInterface.transfer(transferBody);
        return enqueueCall(call);
    }


    public Observable<String> getPublicKey(int id) {
        Call<ResponseBody> call = httpInterface.getPublicKey(id);
        return enqueueCall(call);
    }

    public Observable<String> getTypeList2() {
        Call<ResponseBody> call = httpInterface.getTeansantionTypeList2();
        return enqueueCall(call);
    }

    public Observable<String> getDivideIncome(int id) {
        Call<ResponseBody> call = httpInterface.getDivideIncomeById(id);
        return enqueueCall(call);
    }

    public Observable<String> extractDivideIncome(Id id) {
        Call<ResponseBody> call = httpInterface.extractDivideIncome(id);
        return enqueueCall(call);
    }

    public Observable<String> getBankCardsByUserID(int id) {
        Call<ResponseBody> call = httpInterface.getBankCardsByUserID(id);
        return enqueueCall(call);
    }

    public Observable<String> checkPsyPsd(CheckPsd checkPsd) {
        Call<ResponseBody> call = httpInterface.checkPsyPsd(checkPsd);
        return enqueueCall(call);
    }

    public Observable<String> sendBankCardMsg(EditBankCard editBankCard) {
        Call<ResponseBody> call = httpInterface.sendBankCardMsg(editBankCard);
        return enqueueCall(call);
    }

    public Observable<String> deleteRevenueDivide(int id) {
        Call<ResponseBody> call = httpInterface.deleteRevenueDivide(id);
        return enqueueCall(call);
    }


    public Observable<String> bindBankCard(BindBankCard bindBankCard) {
        Call<ResponseBody> call = httpInterface.bindBankCard(bindBankCard);
        return enqueueCall(call);
    }

    public Observable<String> tx(Tx tx) {
        Call<ResponseBody> call = httpInterface.tx(tx);
        return enqueueCall(call);
    }

    public Observable<String> getPrepareOrder(WxPay wxPay) {
        Call<ResponseBody> call = httpInterface.getPrepareOrder(wxPay);
        return enqueueCall(call);
    }

    public Observable<String> getYSRecord(String time, String time2, int page) {
        Call<ResponseBody> call = httpInterface.getYSRecord(time, time2, Constant.userId, page, 100);
        return enqueueCall(call);
    }

    public Observable<String> getVerificationCode(GetVerification getVerification) {
        Call<ResponseBody> call = httpInterface.getVerificationCode(getVerification);
        return enqueueCall(call);
    }

    public Observable<String> getOpenId(String code) {
        Call<ResponseBody> call = httpInterface.getOpenId(code);
        return enqueueCall(call);
    }

    public Observable<String> searchFriend(String userName) {
        Call<ResponseBody> call = httpInterface.getUserByUserName(userName);
        return enqueueCall(call);
    }

    public Observable<String> deleteFriend(int id1, int id2) {
        Call<ResponseBody> call = httpInterface.deleteFriend(id1, id2);
        return enqueueCall(call);
    }

    public Observable<String> payByBalance(PayByBalance payByBalance) {
        Call<ResponseBody> call = httpInterface.payByBanlance(payByBalance);
        return enqueueCall(call);
    }

    public Observable<String> isBind(String productNumber) {
        Call<ResponseBody> call = httpInterface.isBind(productNumber);
        return enqueueCall(call);
    }

    public Observable<String> getOrderNumber(CreateOrder createOrder) {
        Call<ResponseBody> call = httpInterface.getOrderNumber(createOrder);
        return enqueueCall(call);
    }

    public Observable<String> refundPayment(PayByBalance payByBalance) {
        Call<ResponseBody> call = httpInterface.refundPayment(payByBalance);
        return enqueueCall(call);
    }


    public Observable<String> recharge(RechargeBody rechargeBody) {
        Call<ResponseBody> call = httpInterface.recharge(rechargeBody);
        return enqueueCall(call);
    }

    public Observable<String> getTransationDetail(TransationDetail transationDetail) {
        Call<ResponseBody> call = httpInterface.getTransactionDetail(transationDetail);
        return enqueueCall(call);
    }

    public Observable<String> deleteBankCard(int id) {
        Call<ResponseBody> call = httpInterface.deleteBankCard(id);
        return enqueueCall(call);
    }

    public Observable<String> exit(int id, String diviceTOken) {
        Call<ResponseBody> call = httpInterface.exit(id, diviceTOken);
        return enqueueCall(call);
    }

    public Observable<String> editPresonal(EditPresonal editPresonal) {
        Call<ResponseBody> call = httpInterface.editPresonal(editPresonal);
        return enqueueCall(call);
    }

    public Observable<String> getNoticeById(long id) {
        Call<ResponseBody> call = httpInterface.getNoticeById(id);
        return enqueueCall(call);
    }

    public Observable<String> getAllNotice(int page, int limit) {
        Call<ResponseBody> call = httpInterface.getNotices(page, limit);
        return enqueueCall(call);
    }

    public Observable<String> upload(MultipartBody.Part parts, int type) {
        Call<ResponseBody> call = httpInterface.uploadPicture(type, parts);
        return enqueueCall(call);
    }

    public Observable<String> updateFriend(UpdateFriend updateFriend) {
        Call<ResponseBody> call = httpInterface.updateFriend(updateFriend);
        return enqueueCall(call);
    }

    public Observable<String> editProduct(EditProduct editProduct) {
        Call<ResponseBody> call = httpInterface.editProduct(editProduct);
        return enqueueCall(call);
    }

    public Observable<String> refreshAccessToken() {

        Call<ResponseBody> call = httpInterface.refreshAccessToken(new RefreshToken(Constant.uniquenessToken));
        return enqueueCall(call);
    }

    public Observable<String> tranferDivide(TransferDivide transferDivide) {
        Call<ResponseBody> call = httpInterface.transferDivide(transferDivide);
        return enqueueCall(call);
    }

    public Observable<String> returnEquity(ReturnEquity returnEquity) {
        Call<ResponseBody> call = httpInterface.returnEquity(returnEquity);
        return enqueueCall(call);
    }

    public Observable<String> getMobileRechargeList() {
        Call<ResponseBody> call = httpInterface.getMobileRechargeList();
        return enqueueCall(call);
    }

    public Observable<String> mobileRecharge(MobileRecharge mobileRecharge) {
        Call<ResponseBody> call = httpInterface.mobileRecharge(mobileRecharge);
        return enqueueCall(call);
    }

    public Observable<String> getGroup(int periodType) {
        Call<ResponseBody> call = httpInterface.getGroup(Constant.userId, periodType);
        return enqueueCall(call);
    }

    public Observable<String> getGroupById(HashMap<String, String> map) {
        Call<ResponseBody> call = httpInterface.getGroupById(map);
        return enqueueCall(call);
    }

    HashMap<Call<ResponseBody>, NetLoading> map = new HashMap<>();

    @NonNull
    private Observable<String> enqueueCall(Call<ResponseBody> call) {
        netLoading = new NetLoading(context);
        netLoading.show();
        map.put(call, netLoading);
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                a(observableEmitter, call);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    private void a(ObservableEmitter<String> observableEmitter, Call<ResponseBody> call) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call1, Response<ResponseBody> response) {
                if (map.get(call) != null) {
                    NetLoading netLoading = map.get(call);
                    netLoading.cancel();
                    netLoading = null;
                    map.remove(netLoading);
                }

                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        observableEmitter.onNext(body);
                        LogUtil.log("--success---" + body);
                    } else {
                        String errorBody = response.errorBody().string();
                        if (!TextUtils.isEmpty(errorBody) && errorBody.contains("message")) {
                            String errorCodeStr = errorBody.substring(errorBody.indexOf("\"code\":") + "\"code\":".length(), errorBody.indexOf(",\"message\""));
                            int errorCode = Integer.parseInt(errorCodeStr.trim());
                            LogUtil.log(errorBody + "--unsuccess---" + errorCodeStr);
                            if (errorCode == 2008 || errorCode == 2000 || errorCode == 3002 || errorCode == 4006) {
                                if (errorCode == 4006) {
                                    Presenter.getInstance().exit(context);
                                } else {
                                    observableEmitter.onNext(errorBody);
                                }
                            } else {
                                ErrrCodeShow.showToast(errorCode, context, errorBody);
                                if (errorCode == 2007) {
                                    observableEmitter.onNext("no");
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call1, Throwable t) {
                if (map.get(call) != null) {
                    NetLoading netLoading = map.get(call);
                    netLoading.cancel();
                    map.remove(netLoading);
                }

                Toast.makeText(context, "网络异常,请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
