package com.example.spay.ui.view;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import com.example.spay.R;
import com.example.spay.databinding.LayoutQrcodeBinding;
import com.example.spay.constant.Constant;
import com.example.spay.utils.BitmapUtil;
import com.google.zxing.encoding.EncodingHandler;

public class QrcodeDialog extends Dialog {

    public QrcodeDialog(Context context, String productNumber) {
        super(context);
        LayoutQrcodeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_qrcode, null, false);
        String str = Constant.payUrl+"?productNumber=" + productNumber;
        binding.setProductNum(productNumber);
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        Bitmap assetBitmap = EncodingHandler.createQRCode(str, 1000, 1000, null);
        Bitmap bitmap = BitmapUtil.getInstance().drawTextToBitmap(assetBitmap, "机器编码:"+productNumber);
        binding.ivAssetCode.setBackground(new BitmapDrawable(context.getResources(),bitmap));
        binding.icon.setImageBitmap(logo);
        binding.saveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtil.getInstance().saveBitmap(bitmap, "资产二维码", context);
            }
        });
        setContentView(binding.getRoot());
        setCancelable(true);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        getWindow().setAttributes(layoutParams);
    }
}
