package com.example.godcode.ui.fragment.deatailFragment;


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
import com.example.godcode.R;
import com.example.godcode.bean.MobileRecharge;
import com.example.godcode.bean.MobileRechargeList;
import com.example.godcode.constant.Constant;
import com.example.godcode.databinding.FragmentMobileRechargeBinding;
import com.example.godcode.handler.ActivityResultHandler;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.HandlerStrategy;
import com.example.godcode.ui.adapter.MobileRechargeAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.view.KeyBoard;
import com.example.godcode.ui.view.PsdPopupWindow;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.PayPsdSetting;
import com.example.godcode.utils.StringUtil;
import java.util.List;

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
            String title = StringUtil.getString(activity, R.string.sjcz);
            binding.mobileRechargeToolbar.title.setText(title);
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
        String phoneNum="";
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
        }catch (Exception e){

        }

        return phoneNum;
    }

}
