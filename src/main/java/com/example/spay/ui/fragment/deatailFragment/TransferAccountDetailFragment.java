package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.spay.R;
import com.example.spay.bean.User;
import com.example.spay.bean.PublicKey;
import com.example.spay.bean.Transfer;
import com.example.spay.bean.TransferBody;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.databinding.FragmentTransferaccountDetailBinding;
import com.example.spay.greendao.entity.Friend;
import com.example.spay.greendao.option.FriendOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.view.KeyBoard;
import com.example.spay.ui.view.MyEditText;
import com.example.spay.ui.view.PsdPopupWindow;
import com.example.spay.utils.EncryptUtil;
import com.example.spay.utils.LogUtil;
import com.example.spay.utils.MoneyTextWatcher;
import com.example.spay.utils.PayPwdSetting;
import com.example.spay.utils.StringUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

public class TransferAccountDetailFragment extends BaseFragment implements  MyEditText.MoneyValueListener {
    private FragmentTransferaccountDetailBinding binding;
    private View view;
    private Transfer transfer;
    private String key;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transferaccount_detail, container, false);
            binding.transferMoney.setMoneyValueListener(this);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            initView();
            initListener();
        }
        return view;
    }

    private int userId;
    private void initView() {
        String title = StringUtil.getString(activity, R.string.transfer);
        binding.transferdetailToolbar.title.setText(title);
        binding.transferMoney.addTextChangedListener(new MoneyTextWatcher(binding.transferMoney));
        Friend friend = FriendOption.getInstance(activity).querryFriend(friendId);
        if (friend == null) {
            //不是朋友
            HttpUtil.getInstance().getUserMsgById(friendId).subscribe(
                    userStr -> {
                        User user = new Gson().fromJson(userStr, User.class);
                        User.ResultBean result = user.getResult();
                        userId=result.getId();
                        binding.transferName.setText(result.getNickName());
                        String headImgUrl = result.getHeadImgUrl();
                        setHeadImg(headImgUrl);
                    }
            );
        } else {
            binding.transferName.setText(friend.getUserName());
            String headImageUrl = friend.getHeadImageUrl();
            setHeadImg(headImageUrl);
            userId=friend.getUserId();
        }
        if(money>0){
            binding.transferMoney.setText(money+"");
            binding.transferMoney.setSelection(binding.transferMoney.getText().length());
        }
    }

    private void initTransfer(int userId) {
        transfer = new Transfer();
        transfer.setFeeType("cny");
        transfer.setFK_UserIDDisburs(Constant.userId);
        transfer.setIncomeGenreType(type);
        transfer.setFK_UserIDIncome(userId);
        transfer.setMoney(money);
        binding.setTransfer(transfer);
    }

    private int type;

    private void setHeadImg(String headImageUrl) {
        if (!TextUtils.isEmpty(headImageUrl)) {
            if (!headImageUrl.contains("http")) {
                headImageUrl += Constant.baseUrl + headImageUrl;
            }
            RxImageLoader.with(activity).load(headImageUrl).into(binding.transferPhoto);
        }
    }


    private int friendId;

    public void initData(int friendId, double money,int type) {//1朋友转账  2非朋友之间的转账
        this.friendId = friendId;
        this.money = money;
        this.type=type;
    }


    private double money;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    private void initListener() {
        binding.btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPublicKey();
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onKeyDown() {
        if (PsdPopupWindow.getInstance(activity) != null && PsdPopupWindow.getInstance(activity).isShowing()) {
            PsdPopupWindow.getInstance(activity).exit();
        } else {
            presenter.back();
        }
    }


    public void toCheck(String psd) {
        TransferBody transferBody = new TransferBody();
        transfer.setPassword(psd);
        if(!TextUtils.isEmpty(binding.transferDescribe.getText())){
            transfer.setDescribe(binding.transferDescribe.getText().toString());
        }
        String json = new Gson().toJson(transfer);
        String encryptStr = EncryptUtil.encryptByPublic(json, key);
        transferBody.setEncryptStr(encryptStr);
        transferBody.setUserID(Constant.userId);
        HttpUtil.getInstance().transfer(transferBody).subscribe(
                transferStr -> {

                    JSONObject jo= new JSONObject(transferStr);
                    boolean success = jo.getBoolean("success");
                    if(success){
                        Toast.makeText(activity, "转账成功", Toast.LENGTH_SHORT).show();
                        PsdPopupWindow.getInstance(activity).exit();
                        presenter.back();
                    }else {
                        JSONObject error = jo.getJSONObject("error");
                        String message = error.getString("message");
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        PsdPopupWindow.getInstance(activity).clear();
                    }
                });
    }


    public void getPublicKey() {
        HttpUtil.getInstance().getPublicKey(Constant.userId).subscribe(
                publicKeyStr -> {
                    PublicKey publicKey = new Gson().fromJson(publicKeyStr, PublicKey.class);
                    key = publicKey.getResult().getXmlKey();
                    initTransfer(userId);
                    PayPwdSetting.getInstance().checkPwd(money, new ClickSureListener() {
                        @Override
                        public void isPwdExit(boolean isPwdExit) {
                            if(isPwdExit){
                                KeyBoard keyBoard = new KeyBoard(activity, new ClickSureListener() {
                                    @Override
                                    public void checkPwd(String pwd) {
                                        toCheck(pwd);
                                    }
                                });
                                PsdPopupWindow.getInstance(activity).show("转账", money, view, keyBoard);
                            }
                        }
                    });

                }
        );
    }

    @Override
    public void setEnable(boolean enable, double money) {
        if (enable) {
            if (!binding.btnTransfer.isEnabled()) {
                binding.btnTransfer.setEnabled(true);
            }
        } else {
            if (binding.btnTransfer.isEnabled()) {
                binding.btnTransfer.setEnabled(false);
            }
        }
        this.money=money;
        LogUtil.log("22-----money------"+money);
    }


}
