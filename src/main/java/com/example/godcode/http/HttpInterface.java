package com.example.godcode.http;

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

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface HttpInterface {

    @POST("/api/TokenAuth/Authenticate")
    Call<ResponseBody> login(@Body LoginBody body);

    @POST("api/services/app/User/RegisterMobileUser")
    Call<ResponseBody> register(@Body RegisterBody body);

    @GET("api/services/app/ProductCategory/GetAllProductCategorys")
    Call<ResponseBody> getAllProductCategory();

    @GET("api/services/app/Product/GetPagedProducts")
    Call<ResponseBody> getPagedProducts(@QueryMap Map<String, String> map);

    @GET("api/services/app/Product/GetProductByIdAsync")
    Call<ResponseBody> getProductById(@Query("Id") int Id);


    @GET("api/services/app/MerchantOwner/GetPagedMobileMerchantOwners")
    Call<ResponseBody> getProductByType(@QueryMap Map<String, String> map);

    @GET("api/services/app/Friends/GetFriendsByUserId")
    Call<ResponseBody> getFriendList(@QueryMap Map<String, String> map);

    @GET("api/services/app/User/GetMobileUserByID")
    Call<ResponseBody> getUserById(@Query("Id") int Id);

    @GET("api/services/app/User/GetUserByUserName")
    Call<ResponseBody> getUserByUserName(@Query("userNameOrPhoneNumber") String userName);

    @GET("api/services/app/Friends/GetFriendsByIdAsync")
    Call<ResponseBody> getFriendById(@Query("Id") int Id);

    @POST("api/services/app/Friends/CreateOrUpdateFriends")
    Call<ResponseBody> addFriend(@Body AddFriend addFriend);

    @POST("api/services/app/Friends/CreateOrUpdateFriends")
    Call<ResponseBody> updateFriend(@Body UpdateFriend updateFriend);

    @POST("api/services/app/MerchantOwner/BindOrRelieveBindMerchantOwner")
    Call<ResponseBody> bindProduct(@Body BindProductBody body);

    @GET("api/services/app/Balance/GetBalanceByUserIdAsync")
    Call<ResponseBody> queryBalance(@Query("Id") int Id);

    @GET("api/services/app/RevenueDivide/GetRevenueDivideByPitureIdAsync")
    Call<ResponseBody> getRevenueDivideById(@Query("Id") int Id);

    @POST("api/services/app/RevenueDivide/CreateOrUpdateRevenueDivide")
    Call<ResponseBody> createRevenueDivide(@Body CreateDivide createDivide);

    @POST("api/services/app/Friends/ConcurAddFriend")
    Call<ResponseBody> concurAddFriend(@Body ConcurAddFriend concurAddFriend);

    @POST("api/services/app/ProductPrice/CreateOrUpdateProductPrice")
    Call<ResponseBody> editProductPrice(@Body EditProductPrice editProductPrice);

    @GET("api/services/app/Enum/GetTeansactionInTypeList")
    Call<ResponseBody> getTeansantionTypeList();

    @GET("api/services/app/Enum/GetTransactionOutTypeList")
    Call<ResponseBody> getTeansantionTypeList2();

    @GET("api/services/app/TransactionReport/GetPagedTransactionReports")
    Call<ResponseBody> getTransaction(@QueryMap Map<String, String> map);

    @POST("api/services/app/CommonPaymentAppServices/TransferToUser")
    Call<ResponseBody> transfer(@Body TransferBody transferBody);

    @GET("api/services/app/CommonPaymentAppServices/GetPaymentKey")
    Call<ResponseBody> getPublicKey(@Query("Id") int Id);

    @PUT("api/services/app/PayPassword/UpdatePayPasswordAsync")
    Call<ResponseBody> changePsd(@Body ChangePsd changePsd);

    @POST("api/services/app/PayPassword/CreatePayPasswordAsync")
    Call<ResponseBody> createPsd(@Body SetPayPsd setPayPsd);

    @POST("api/services/app/PayPassword/VerificationPayPassword")
    Call<ResponseBody> checkPsyPsd(@Body CheckPsd checkPsd);

    @GET("api/services/app/PayPassword/GetPayPasswordByIdAsync")
    Call<ResponseBody> getPsyPsdById(@Query("Id") int Id);

    @GET("api/services/app/DivideIncome/GetDivideIncomesByUserID")
    Call<ResponseBody> getDivideIncomeById(@Query("Id") int Id);

    @POST("api/services/app/DivideIncome/ExtractDivideIncome")
    Call<ResponseBody> extractDivideIncome(@Body Id id);

    @POST("/api/services/app/BankCard/BindBankCard")
    Call<ResponseBody> bindBankCard(@Body BindBankCard bindBankCard);

    @GET("api/services/app/BankCard/GetBankCardsByUserID")
    Call<ResponseBody> getBankCardsByUserID(@Query("Id") int Id);

    @POST("api/services/app/BankCard/PresenBindBankCard")
    Call<ResponseBody> sendBankCardMsg(@Body EditBankCard editBankCard);

    @DELETE("api/services/app/RevenueDivide/DeleteRevenueDivide")
    Call<ResponseBody> deleteRevenueDivide(@Query("Id") int Id);

    @POST("api/services/app/PutMoney/CreateMobilePutMoney")
    Call<ResponseBody> tx(@Body Tx tx);

    @POST("WeChatPay/MoblieWXPayRequest")
    Call<ResponseBody> getPrepareOrder(@Body WxPay wxPay);

    @GET("MoblieWeChatAuth/MoblieWXAuthToken")
    Call<ResponseBody> getOpenId(@Query("code") String code);

    @GET("api/services/app/PayOrder/GetMobliePagedPayOrders")
    Call<ResponseBody> getYSRecord(@Query("StratTime") String time, @Query("EndTime") String time2, @Query("UserId") long userId, @Query("page") int page, @Query("limit") int limit);

    @POST("api/services/app/User/QueryMoblieVerificationCode")
    Call<ResponseBody> getVerificationCode(@Body GetVerification getVerification);

    @DELETE("api/services/app/Friends/DeleteFriends")
    Call<ResponseBody> deleteFriend(@Query("FK_UserID") int id1, @Query("ToUserID") int id2);

    @POST("api/services/app/CommonPaymentAppServices/TerracePayment")
    Call<ResponseBody> payByBanlance(@Body PayByBalance payByBalance);

    @GET("api/services/app/Product/GetProductByNumberAsync")
    Call<ResponseBody> isBind(@Query("ProductNumber") String produceNumber);

    @POST("api/services/app/PayOrder/MobilePaymentProduct")
    Call<ResponseBody> getOrderNumber(@Body CreateOrder createOrder);

    @POST("api/services/app/CommonPaymentAppServices/RefundPayment")
    Call<ResponseBody> refundPayment(@Body PayByBalance payByBalance);

    @POST("api/services/app/PayMoney/CreateOrUpdatePayMoney")
    Call<ResponseBody> recharge(@Body RechargeBody body);

    @POST("api/services/app/TransactionReport/QueryTransationDetail")
    Call<ResponseBody> getTransactionDetail(@Body TransationDetail transationDetail);


    @DELETE("api/services/app/BankCard/DeleteBankCard")
    Call<ResponseBody> deleteBankCard(@Query("Id") int id);


    @POST("api/TokenAuth/MobileLoginOut")
    Call<ResponseBody> exit(@Query("UserId") int UserId, @Query("UniquenessToken") String diviceToken);


    @POST("api/services/app/User/MoblieUpdateUser")
    Call<ResponseBody> editPresonal(@Body EditPresonal editPresonal);


    @Multipart
    @POST("/api/PictureApi/UploadPicture")
        //1产品图片 2 用户头像 3产品类别图片
    Call<ResponseBody> uploadPicture(@Query("type") int type, @Part MultipartBody.Part file);


    @GET("api/services/app/Notice/GetNoticeByIdAsync")
    Call<ResponseBody> getNoticeById(@Query("Id") long id);

    @GET("api/services/app/Notice/GetPagedNotices")
    Call<ResponseBody> getNotices(@Query("page") int page, @Query("limit") int limit);

    @POST("api/services/app/Product/CreateOrUpdateProduct")
    Call<ResponseBody> editProduct(@Body EditProduct editProduct);


    @POST("/api/TokenAuth/RefreshAuthenticate")
    Call<ResponseBody> refreshAccessToken(@Body RefreshToken refreshToken);


    @POST("/api/services/app/MerchantOwner/TransferEquity")
    Call<ResponseBody> transferDivide(@Body TransferDivide transferDivide);

    @POST("/api/services/app/MerchantOwner/ReturnEquity")
    Call<ResponseBody> returnEquity(@Body ReturnEquity returnEquity);

    @GET("/api/services/app/RechargeSetting/GetRechargeSettingList")
    Call<ResponseBody> getMobileRechargeList();

    @POST("/api/services/app/RechargeRecord/Recharge")
    Call<ResponseBody> mobileRecharge(@Body MobileRecharge mobileRecharge);


    @GET("/api/services/app/MerchantOwner/GetMobileGroupMerchantOwners")
    Call<ResponseBody> getGroup(@Query("UserId") int userId, @Query("PeriodType") int periodType);

    @GET("/api/services/app/MerchantOwner/GetPagedMobileMerchantOwnersOne")
    Call<ResponseBody> getGroupById(@QueryMap Map<String, String> map);
}