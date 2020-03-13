package com.example.spay.ui.fragment.deatailFragment;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.bean.MobileRecharge;
import com.example.spay.bean.MobileRechargeList;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.FragmentMobileRechargeBinding;
import com.example.spay.handler.ActivityResultHandler;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.interface_.HandlerStrategy;
import com.example.spay.ui.adapter.MobileRechargeAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.KeyBoard;
import com.example.spay.ui.view.PsdPopupWindow;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.PayPwdSetting;
import com.example.spay.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MobileRechargeFragment extends BaseFragment {
    private FragmentMobileRechargeBinding binding;
    private View view;
    private List<MobileRechargeList.ResultBean> result;
    private MobileRechargeList.ResultBean resultBean;
    private MobileRechargeAdapter mobileRechargeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_recharge, container, false);
            binding.setPresenter(presenter);
            String title = StringUtil.getString(activity, R.string.sjcz);
            binding.mobileRechargeToolbar.title.setText(title);
            view = binding.getRoot();
            initView();
            initData();
            initListener();
        }
        return view;
    }

    private void initData() {
        HttpUtil.getInstance().getMobileRechargeList().subscribe(
                str -> {
                    MobileRechargeList mobileRechargeList = GsonUtil.getInstance().fromJson(str, MobileRechargeList.class);
                    List<MobileRechargeList.ResultBean> datas = mobileRechargeList.getResult();
                    if (datas != null && datas.size() > 0) {
                        result.addAll(datas);
                        mobileRechargeAdapter.notifyDataSetChanged();
                    }
                }
        );
    }


    private void initListener() {
        binding.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phoneNumber = binding.phoneNumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(activity, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneNumber.length() != 11 || phoneNumber.charAt(0) != '1') {
                    Toast.makeText(activity, "手机号格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                resultBean = result.get(position);
                mobileRechargeAdapter.select(position);
                if (resultBean.isIsEnable()) {
                    double money = resultBean.getSellAmount();
                    PayPwdSetting.getInstance().checkPwd(money, new ClickSureListener() {
                        @Override
                        public void isPwdExit(boolean isPwdExit) {
                            if (isPwdExit) {
                                KeyBoard keyBoard = new KeyBoard(activity, new ClickSureListener() {
                                    @Override
                                    public void checkPwd(String pwd) {
                                        toCheck(pwd);
                                    }
                                });
                                PsdPopupWindow.getInstance(activity).show("手机充值", money, view, keyBoard);
                            }
                        }
                    });
                }


            }
        });

        binding.txl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                new ActivityResultHandler.Builder().activity(activity).intent(intent).requestCode(ActivityResultHandler.REQUEST_SELECT_CONTACTS).hadlerStrategy(new HandlerStrategy() {
                    @Override
                    public void onActivityResult(String text) {
                        binding.phoneNumber.setText(text);
                    }
                }).build().startActivityForResult();
            }
        });
    }


    public void initView() {
        result = new ArrayList<>();
        mobileRechargeAdapter = new MobileRechargeAdapter(result, activity);
        binding.grid.setAdapter(mobileRechargeAdapter);
    }

    @Override
    public void onKeyDown() {
        if (PsdPopupWindow.getInstance(activity) != null && PsdPopupWindow.getInstance(activity).isShowing()) {
            PsdPopupWindow.getInstance(activity).exit();
        } else {
            presenter.back();
        }
    }


    @Override
    protected void lazyLoad() {

    }


    public void toCheck(String psd) {
        if (resultBean != null) {

            MobileRecharge mobileRecharge = new MobileRecharge();
            MobileRecharge.RechargeRecordBean rechargeRecordBean = new MobileRecharge.RechargeRecordBean();
            rechargeRecordBean.setFK_UserID(Constant.userId);
            rechargeRecordBean.setPayPassword(psd);
            rechargeRecordBean.setRechargePhoneNumber(binding.phoneNumber.getText().toString());
            rechargeRecordBean.setRechargeSettingId(resultBean.getId());
            mobileRecharge.setRechargeRecord(rechargeRecordBean);
            HttpUtil.getInstance().mobileRecharge(mobileRecharge).subscribe(
                    str -> {
                        Toast.makeText(activity, "充值成功", Toast.LENGTH_SHORT).show();
                        PsdPopupWindow.getInstance(activity).exit();
                    }
            );

        }

    }

    private String getPhoneNumber(Uri uri) {
        String phoneNum = "";
        //得到ContentResolver对象
        ContentResolver cr = activity.getContentResolver();
        //取得电话本中开始一项的光标
        try {
            Cursor cursor = cr.query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                //取得电话号码
                String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
                if (phone != null) {
                    phone.moveToFirst();
                    String number = ContactsContract.CommonDataKinds.Phone.NUMBER;
                    int columnIndex = phone.getColumnIndex(number);
                    phoneNum = phone.getString(columnIndex);
                }
                phone.close();
                cursor.close();
            } else {
                return null;
            }
        } catch (Exception e) {

        }

        return phoneNum;
    }

}
