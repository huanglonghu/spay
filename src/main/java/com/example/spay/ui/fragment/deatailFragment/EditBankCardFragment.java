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
import com.example.spay.bean.BankCard;
import com.example.spay.bean.EditBankCard;
import com.example.spay.databinding.FragmentEditbankcardBinding;
import com.example.spay.greendao.entity.City;
import com.example.spay.greendao.entity.Province;
import com.example.spay.greendao.entity.Zone;
import com.example.spay.greendao.gen.CityDao;
import com.example.spay.greendao.gen.ZoneDao;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.customview.BankCardEditText;
import com.example.spay.ui.view.TypeSelect;
import com.example.spay.utils.GreenDaoUtil;
import com.youth.picker.PickerView;
import com.youth.picker.entity.PickerData;
import com.youth.picker.listener.OnPickerClickListener;

import java.util.HashMap;
import java.util.List;

public class EditBankCardFragment extends BaseFragment implements BankCardEditText.BankCardLengthListener, TypeSelect.SelectResponse {

    private FragmentEditbankcardBinding binding;
    private PickerView pickerView;
    private EditBankCard.BankCardBean bankCardBean;
    private String[] bankArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_editbankcard, null, false);
        binding.setPresenter(presenter);
        binding.editBankCardToolbar.title.setText("编辑银行卡");
        binding.etCardNumber.setBankCardLengthListener(this);
        bankCardBean = new EditBankCard.BankCardBean();
        if (getArguments() != null) {
            BankCard.ResultBean bean = (BankCard.ResultBean) getArguments().getSerializable("bankCardBean");
            bankCardBean.setBankID(bean.getBankID());
            bankCardBean.setArea(bean.getArea());
            bankCardBean.setAccountName(bean.getAccountName());
            bankCardBean.setBankCardNumber(bean.getBankCardNumber());
            bankCardBean.setBankName(bean.getBankName());
            bankCardBean.setBindMoney((int) bean.getBindMoney());
            bankCardBean.setBankNumber(bean.getBankNumber());
            bankCardBean.setBindType(bean.getBindType()+"");
            bankCardBean.setFK_UserID(bean.getFK_UserID());
            bankCardBean.setId(bean.getId());
            bankCardBean.setMobile(bean.getMobile());
            bankCardBean.setNetwork(bean.getNetwork());
            binding.setBean(bankCardBean);
        }

        initPickerData();
        initListener();
        return binding.getRoot();
    }

    private void initListener() {
        bankArray = new String[]{"中国银行", "中国农业银行", "中国工商银行", "中国建设银行", "交通银行", "招商银行", "广发银行", "中国民生银行", "浦发银行", "中国光大银行", "中国邮政", "中信银行", "兴业银行", "汇丰中国银行"};
        binding.etBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeSelect bankTypeSelect = new TypeSelect(activity, bankArray);
                bankTypeSelect.setSelectResponse(EditBankCardFragment.this);
                bankTypeSelect.show();
            }
        });

        binding.bankArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.show(binding.getRoot());
                pickerView.setOnPickerClickListener(new OnPickerClickListener() {
                    @Override
                    public void OnPickerClick(PickerData pickerData) {
                    }

                    @Override
                    public void OnPickerConfirmClick(PickerData pickerData) {
                        String selectText = pickerData.getSelectText();
                        if (!TextUtils.isEmpty(selectText)) {
                            binding.bankArea.setText(selectText);
                        }
                        pickerView.dismiss();
                    }
                });
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankCardNumber = binding.etCardNumber.getTextWithoutSpace();
                bankCardBean.setBankCardNumber(bankCardNumber);
                if (TextUtils.isEmpty(bankCardBean.getAccountName())) {
                    Toast.makeText(activity, "开户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(bankCardBean.getBankName())) {
                    Toast.makeText(activity, "卡户行不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(bankCardBean.getArea())) {
                    Toast.makeText(activity, "开户地区不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(bankCardBean.getNetwork())) {
                    Toast.makeText(activity, "开户网点不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditBankCard editBankCard = new EditBankCard();
                editBankCard.setBankCard(bankCardBean);
                HttpUtil.getInstance().editBankCard(editBankCard).subscribe(
                        sendBankStr -> {
                            Toast.makeText(activity, "银行卡编辑成功", Toast.LENGTH_SHORT).show();
                            presenter.back();
                        }
                );

            }
        });


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
    protected void lazyLoad() {

    }


    @Override
    public void length(boolean isPass) {
        if (isPass) {
            if (!binding.save.isEnabled()) {
                binding.save.setEnabled(true);
            }
        } else {
            if (binding.save.isEnabled()) {
                binding.save.setEnabled(false);
            }
        }
    }


    @Override
    public void select(int pos) {
        binding.etBankName.setText(bankArray[pos]);
    }
}
