package com.example.spay.ui.view;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.spay.R;
import com.example.spay.databinding.LayoutInputPsdBinding;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.fragment.auth.SelectAuthWay;
import com.example.spay.utils.FormatUtil;
import com.example.spay.utils.PayPwdSetting;
import com.example.spay.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PsdPopupWindow extends PopupWindow {
    private LayoutInputPsdBinding binding;
    private Activity activity;
    private static PsdPopupWindow defaultInstance;

    public static PsdPopupWindow getInstance(Activity activity) {
        PsdPopupWindow psdPopupWindow = defaultInstance;
        if (defaultInstance == null) {
            synchronized (PsdPopupWindow.class) {
                if (defaultInstance == null) {
                    psdPopupWindow = new PsdPopupWindow(activity);
                    defaultInstance = psdPopupWindow;
                }
            }
        }
        return psdPopupWindow;
    }

    private PsdPopupWindow(Activity activity) {
        this.activity = activity;
        binding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.layout_input_psd, null, false);
        binding.txPayPsdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!keyBoard.isShowing()) {
                    keyBoard.show(view);
                }
            }
        });
        binding.closeInputDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPwdSetting.getInstance().verifyPwd(new ClickSureListener() {
                    @Override
                    public void isPwdExit(boolean isPwdExit) {
                        if (isPwdExit) {
                            SelectAuthWay selectAuthWay = new SelectAuthWay();
                            Presenter.getInstance().step2Fragment(selectAuthWay, "selectAuthWay");
                            exit();
                        } else {
                            ToastUtil.getInstance().showToast("您还未设置支付密码,请前往设置界面设置密码", 1000, activity);
                        }
                    }
                });

            }
        });

        setContentView(binding.getRoot());
        setWidth(Presenter.getInstance().getWindowWidth() - 100);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        setFocusable(false);
        setBackgroundDrawable(new BitmapDrawable());

        RxBus.getInstance().toObservable(RxEvent.class).subscribe(
                new Observer<RxEvent>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(RxEvent rxEvent) {
                        if (rxEvent.getEventType() == EventType.EVENTTYPE_CHANGEPWD_BACK) {
                            showAtLocation(view, Gravity.TOP, 0, 300);
                            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                            lp.alpha = 0.6f;
                            activity.getWindow().setAttributes(lp);
                            if(keyBoard!=null){
                                keyBoard.show(view);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }
        );
    }


    private View view;

    public void show(String name, double money, View view, KeyBoard keyBoard) {
        this.keyBoard = keyBoard;
        this.view = view;
        binding.setName(name);
        binding.money.setText(FormatUtil.getInstance().get2double(money));

        showAtLocation(view, Gravity.TOP, 0, 300);
        keyBoard.show(view);
        keyBoard.setRefreshPsd(binding.txPayPsdView);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.6f;
        activity.getWindow().setAttributes(lp);


    }

    private KeyBoard keyBoard;

    public void exit() {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().setAttributes(lp);
        binding.txPayPsdView.setPsLength(0);
        if (keyBoard != null) {
            keyBoard.clearPsd();
            keyBoard.dismiss();
        }
        dismiss();
    }

    public void clear() {
        keyBoard.clearPsd();

        binding.txPayPsdView.setPsLength(0);
    }

}
