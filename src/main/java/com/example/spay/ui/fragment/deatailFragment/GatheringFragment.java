package com.example.spay.ui.fragment.deatailFragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.spay.R;
import com.example.spay.databinding.FragmentGatheringBinding;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.utils.BitmapUtil;
import com.example.spay.utils.EncryptUtil;
import com.example.spay.utils.FormatUtil;
import com.example.spay.utils.StringUtil;
import com.google.zxing.encoding.EncodingHandler;

public class GatheringFragment extends BaseFragment implements SetMoneyFragment.MoneyResponse {
    private FragmentGatheringBinding binding;
    private ImageView gatheringCode;
    private View view;
    private Bitmap mBitmap;
    private double money;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gathering, container, false);
            view = binding.getRoot();
            binding.setPresenter(presenter);
            binding.gatheringToolbar.toolbar2.setBackgroundResource(R.color.theme_color);
            String title = StringUtil.getString(activity, R.string.qrcodeGather);
            binding.gatheringToolbar.title.setText(title);
            gatheringCode = (ImageView) view.findViewById(R.id.gatheringCode);
            initView();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.tvSetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (money == 0) {
                    SetMoneyFragment setMoneyFragment = new SetMoneyFragment();
                    setMoneyFragment.setMoneyResponse(GatheringFragment.this);
                    presenter.step2Fragment(setMoneyFragment, "setMoney");
                } else {
                    money = 0;
                    binding.setMoney(money);
                    generatingQrCode();
                }
            }
        });

        binding.saveBitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtil.getInstance().saveBitmap(mBitmap, "收款码", activity);
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }


    public void initView() {
        generatingQrCode();
    }

    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void generatingQrCode() {
        mBitmap = null;
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.logo);
            String qrStr = Constant.payUrl + "/WeChatPay/AppRecharge?Ciphertext=" + getQrStr(money);
            int i = dp2px(activity, 250);
            mBitmap = EncodingHandler.createQRCode(qrStr, i, i, null);
            if (mBitmap != null) {
                gatheringCode.setBackground(new BitmapDrawable(getResources(), mBitmap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getQrStr(double money) {
        String qrStr = null;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(Constant.userName + "|");
            stringBuffer.append(Constant.userId + "|");
            stringBuffer.append(money + "|");
            stringBuffer.append("1");
            //进行加密
            qrStr = EncryptUtil.aesEncrypt(stringBuffer.toString(), "87a4d115c0956912b495d6bb8b7c0013");
            qrStr = qrStr.replaceAll("\\+", "~").replaceAll("/", "!").replaceAll("=", "-");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qrStr;
    }


    @Override
    public void setMoney(double money) {
        this.money = money;
        binding.setMoney(money);
        binding.gatheringMoney.setText(FormatUtil.getInstance().get2double(money));
        generatingQrCode();
    }


}
