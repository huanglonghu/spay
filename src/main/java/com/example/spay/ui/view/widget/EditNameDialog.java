package com.example.spay.ui.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.bean.EditGroupItemName;
import com.example.spay.databinding.LayoutEditGroupBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.presenter.Presenter;
import com.example.spay.constant.Constant;

public class EditNameDialog extends Dialog {


    private LayoutEditGroupBinding binding;
    private Context context;
    private int groupId;
    private TextView tvGroupName;

    public EditNameDialog(@NonNull Context context, int groupId, TextView tvGroupName) {
        super(context);
        this.context = context;
        this.groupId = groupId;
        this.tvGroupName = tvGroupName;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_edit_group, null, false);
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = binding.etGroupName.getText();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(context, "请输入分组名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditGroupItemName editGroupItemName = new EditGroupItemName();
                EditGroupItemName.GroupAppellationBean groupAppellationBean = new EditGroupItemName.GroupAppellationBean();
                groupAppellationBean.setGroupName(text.toString());
                groupAppellationBean.setUserID(Constant.userId);
                groupAppellationBean.setToUserID(groupId);
                editGroupItemName.setGroupAppellation(groupAppellationBean);
                HttpUtil.getInstance().editGroupItemName(editGroupItemName).subscribe(
                        editGroupStr -> {
                            tvGroupName.setText(text);
                            Toast.makeText(context, "分组名称修改成功", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                );
            }
        });
        setContentView(binding.getRoot());
        setCancelable(false);
    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = 4 * Presenter.getInstance().getWindowWidth() / 5;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        getWindow().setWindowAnimations(R.style.popupStyle);
        getWindow().setAttributes(layoutParams);
    }


}
