package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.EditBankCard;
import com.example.godcode.databinding.FragmentBindbankcardBinding;
import com.example.godcode.greendao.entity.City;
import com.example.godcode.greendao.entity.Province;
import com.example.godcode.greendao.entity.User;
import com.example.godcode.greendao.entity.Zone;
import com.example.godcode.greendao.gen.CityDao;
import com.example.godcode.greendao.gen.ZoneDao;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.BKLCDialog;
import com.example.godcode.ui.view.BankCardEditText;
import com.example.godcode.ui.view.DeleteDialog;
import com.example.godcode.utils.GreenDaoUtil;
import com.youth.picker.PickerView;
import com.youth.picker.entity.PickerData;
import com.youth.picker.listener.OnPickerClickListener;

import java.util.HashMap;
import java.util.List;


public class AddBankCardFragment extends BaseFragment implements BankCardEditText.BankCardLengthListener, DeleteDialog.OnClickSureListerer {

    private FragmentBindbankcardBinding binding;
    private View view;
    private EditBankCard editBankCard;
    private PickerView pickerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bindbankcard, container, false);
            binding.etCardNumber.setBankCardLengthListener(this);
            editBankCard = new EditBankCard();
            editBankCard.setFK_UserID(Constant.userId);
            User user = UserOption.getInstance().querryUser(Constant.userId);
            editBankCard.setMobile(user.getPhoneNumer());
            binding.setBean(editBankCard);
            view = binding.getRoot();
            initView();
            initData();
            initListener();
        }
        return view;
    }

    private void initData() {
        initPickerData();
    }


    private void initListener() {
        binding.addBankCardToolbar.toolbar3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyDown();
            }
        });

        binding.bankArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.show(view);
                pickerView.setOnPickerClickListener(new OnPickerClickListener() {
                    @Override
                    public void OnPickerClick(PickerData pickerData) {
                    }

                    @Override
                    public void OnPickerConfirmClick(PickerData pickerData) {
                        String selectText = pickerData.getSelectText();
                        if (!TextUtils.isEmpty(selectText)) {
                            binding.bankArea.setText(selectText);
                            editBankCard.setArea(selectText);
                        }
                        pickerView.dismiss();
                    }
                });
            }
        });

        binding.bindNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBankCard.setBankCardNumber(binding.etCardNumber.getTextWithoutSpace());
                if (TextUtils.isEmpty(editBankCard.getAccountName())) {
                    Toast.makeText(activity, "开户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(editBankCard.getBankCardNumber())) {
                    Toast.makeText(activity, "银行卡号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(editBankCard.getBankName())) {
                    Toast.makeText(activity, "卡户行不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(editBankCard.getArea())) {
                    Toast.makeText(activity, "开户地区不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(editBankCard.getNetwork())) {
                    Toast.makeText(activity, "开户网点不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpUtil.getInstance().sendBankCardMsg(editBankCard).subscribe(
                        sendBankStr -> {
                            Toast.makeText(activity, "银行卡添加完成，请等待审核", Toast.LENGTH_SHORT).show();
                            clear();
                            presenter.back();
                        }
                );
            }
        });


        binding.bklc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BKLCDialog bklcDialog = new BKLCDialog(activity);
                bklcDialog.show();
            }
        });
    }

    public void initView() {
        binding.addBankCardToolbar.title.setText("添加银行卡");
    }


    public void clear() {
        getChildFragmentManager().beginTransaction().remove(AddBankCardFragment.this).commit();

    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void onKeyDown() {
        super.onKeyDown();
        String title = "是否放弃绑定银行卡?";
        DeleteDialog deleteDialog = new DeleteDialog(activity, title);
        deleteDialog.setListerer(this);
        deleteDialog.show();
    }

    @Override
    public void clickSure() {
        clear();
        presenter.back();
    }

    private void initPickerData() {
        PickerData data = new PickerData();
        List<Province> provinces = GreenDaoUtil.getSingleton().getDaoSession().getProvinceDao().loadAll();
        String[] mProvinceDatas = new String[provinces.size()];
        HashMap<String, String[]> mCitisDatasMap = new HashMap<>();
        HashMap<String, String[]> mDistrictDatasMap = new HashMap<>();
        for (int i = 0; i < provinces.size(); i++) {
            Province province = provinces.get(i);
            String proName = province.getProName();
            mProvinceDatas[i] = proName;
            int proSort = province.getProSort();
            List<City> cityList = GreenDaoUtil.getSingleton().getDaoSession().getCityDao().queryBuilder().where(CityDao.Properties.ProId.eq(proSort)).list();
            String[] cityArray = new String[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
                City city = cityList.get(j);
                String cityName = city.getCityName();
                long citySort = city.getCitySort();
                cityArray[j] = cityName;
                List<Zone> zoneList = GreenDaoUtil.getSingleton().getDaoSession().getZoneDao().queryBuilder().where(ZoneDao.Properties.CityId.eq(citySort)).list();
                String[] zoneArray = new String[zoneList.size()];
                for (int k = 0; k < zoneList.size(); k++) {
                    zoneArray[k] = zoneList.get(k).getZoneName();
                }
                mDistrictDatasMap.put(cityName, zoneArray);
            }
            mCitisDatasMap.put(proName, cityArray);
        }
        //设置数据，有多少层级自己确定
        data.setFirstDatas(mProvinceDatas);
        data.setSecondDatas(mCitisDatasMap);
        data.setThirdDatas(mDistrictDatasMap);
        pickerView = new PickerView(activity, data);
    }

    @Override
    public void length(boolean isPass) {
        if (isPass) {
            if (!binding.bindNext.isSelected()) {
                binding.bindNext.setEnabled(true);
            }
        } else {
            if (binding.bindNext.isSelected()) {
                binding.bindNext.setEnabled(false);
            }
        }
    }
}
