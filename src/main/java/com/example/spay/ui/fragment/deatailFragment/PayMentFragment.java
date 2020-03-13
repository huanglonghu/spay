package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.spay.R;
import com.example.spay.databinding.FragmentPaymentBinding;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.utils.EncryptUtil;
import com.google.zxing.WriterException;
import com.google.zxing.encoding.EncodingHandler;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayMentFragment extends BaseFragment {
    private FragmentPaymentBinding binding;
    private ImageView paymentQrcode;
    private ImageView paymentBarCode;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.paymentToolBar.toolbar2.setBackgroundResource(R.color.bg4);
            binding.paymentToolBar.title.setText("向商家付款");
            paymentQrcode = (ImageView) view.findViewById(R.id.payment_qrcode);
            paymentBarCode = (ImageView) view.findViewById(R.id.payment_barCode);
            refreshQRCode();
            initView();
        }
        return view;
    }

    private void refreshQRCode() {
      Observable.interval(0, 1, java.util.concurrent.TimeUnit.MINUTES, Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                time -> {
                    long l = System.currentTimeMillis();
                    Bitmap mBitmap = null;
                    try {
                        String qrStr = getQrStr(l);
                        Bitmap barBitmap = EncodingHandler.createOneDCode("1234");
                        if (barBitmap != null) {
                            paymentBarCode.setImageBitmap(barBitmap);
                        }
                        mBitmap = EncodingHandler.createQRCode(qrStr, 500);
                        if (mBitmap != null) {
                            paymentQrcode.setImageBitmap(mBitmap);
                        }
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }, throwable -> {
                }
        );
    }

    @Override
    protected void lazyLoad() {
    }


    public void initView() {
        refreshQRCode();
    }

    public String getQrStr(long time) {
        String qrStr = null;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(Constant.userName + "|");
            stringBuffer.append(Constant.userId + "|");
            stringBuffer.append(time + "|");
            stringBuffer.append("2");
            //进行加密
            qrStr = EncryptUtil.aesEncrypt(stringBuffer.toString(), "87a4d115c0956912b495d6bb8b7c0013");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qrStr;
    }

    //隔一分钟刷新一次
}
