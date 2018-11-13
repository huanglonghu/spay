package com.example.godcode.ui.fragment.deatailFragment;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.EditPresonal;
import com.example.godcode.bean.MobileRecharge;
import com.example.godcode.bean.MobileRechargeList;
import com.example.godcode.bean.UploadResponse;
import com.example.godcode.databinding.FragmentMobileRechargeBinding;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.adapter.MobileRechargeAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.KeyBoard;
import com.example.godcode.ui.view.PsdPopupWindow;
import com.example.godcode.utils.FileUtil;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.PayPsdSetting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class MobileRechargeFragment extends BaseFragment implements KeyBoard.PsdLengthWatcher {
    private FragmentMobileRechargeBinding binding;
    private View view;
    private List<MobileRechargeList.ResultBean> result;
    private MobileRechargeList.ResultBean resultBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_recharge, container, false);
            binding.setPresenter(presenter);
            binding.mobileRechargeToolbar.title.setText("手机充值");
            view = binding.getRoot();
            initData();
            initView();
            initListener();
        }
        return view;
    }

    private void initData() {
        HttpUtil.getInstance().getMobileRechargeList().subscribe(
                str -> {
                    MobileRechargeList mobileRechargeList = GsonUtil.getInstance().fromJson(str, MobileRechargeList.class);
                    result = mobileRechargeList.getResult();
                    MobileRechargeAdapter mobileRechargeAdapter = new MobileRechargeAdapter(result, activity);
                    binding.grid.setAdapter(mobileRechargeAdapter);
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
                if (resultBean.isIsEnable()) {
                    PayPsdSetting.getInstance().isPayPsdSet("手机充值", resultBean.getSellAmount(), view, MobileRechargeFragment.this, 2);
                }
            }
        });

        binding.txl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                activity.startActivityForResult(intent, REQUEST_PHONE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PHONE:
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                String phoneNum = contacts[1].replace("-", "");
                binding.phoneNumber.setText(phoneNum);
                break;
        }
    }


    private static final int REQUEST_PHONE = 103;

    public void initView() {

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

    @Override
    public void refreshData() {

    }

    @Override
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

    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = activity.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

}
