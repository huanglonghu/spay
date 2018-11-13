package com.example.godcode.ui.fragment.deatailFragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.bean.EditPresonal;
import com.example.godcode.R;
import com.example.godcode.bean.UploadResponse;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.FragmentPersonalBinding;
import com.example.godcode.greendao.entity.User;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.activity.ClipImageActivity;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.utils.BitmapUtil;
import com.example.godcode.utils.FileUtil;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class PresonalFragment extends BaseFragment implements EditPresonalFragment.PersonalUpdate {
    private FragmentPersonalBinding binding;
    private View view;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            user = UserOption.getInstance().querryUser(Constant.userId);
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal, container, false);
            binding.setFragment(this);
            binding.setUser(user);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            initView();
            binding.personalToolbar.title.setText("个人信息");
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.rlHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtil.getInstance().fromImg(activity);
            }
        });
    }


    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int RESIZE_REQUEST_CODE = 2;


    private void initView() {
        String headImageUrl = user.getHeadImageUrl();
        if (!TextUtils.isEmpty(headImageUrl)) {
            if (!headImageUrl.contains("http")) {
                headImageUrl = Constant.baseUrl + headImageUrl;
            }
            RxImageLoader.with(activity).load(headImageUrl).into(binding.ivUser);
        }

    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {
    }

    private int type;

    public void editPersonal(int type) {
        this.type = type;
        EditPresonalFragment editPresonalFragment = new EditPresonalFragment();
        editPresonalFragment.setPersonalUpdate(PresonalFragment.this);
        editPresonalFragment.initData(type, user);
        presenter.step2Fragment(editPresonalFragment, "editPersonal");
    }

    @Override
    public void update(int type, String content) {
        switch (type) {
            case 1:
                user.setUserName(content);
                break;
            case 2:
                user.setSex(content);
                break;
            case 3:
                user.setArea(content);
                break;
        }
        UserOption.getInstance().updateUser(user);
        binding.setUser(user);
    }


    @Override
    public void onKeyDown() {
        presenter.back();
    }


    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity, ClipImageActivity.class);
        intent.putExtra("type", 2);//
        intent.setData(uri);
        activity.startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                gotoClipActivity(data.getData());
                break;
            case REQUEST_CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = FileUtil.getRealFilePathFromUri(activity, uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    binding.ivUser.setImageBitmap(bitMap);
                    File file = new File(cropImagePath);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                    HttpUtil.getInstance().upload(filePart, 2).subscribe(
                            uploadStr -> {
                                UploadResponse uploadResponse = GsonUtil.getInstance().fromJson(uploadStr, UploadResponse.class);
                                String headUrl = uploadResponse.getResult().get(0);
                                EditPresonal editPresonal = new EditPresonal();
                                editPresonal.setId(Constant.userId);
                                editPresonal.setHeadImgUrl(Constant.baseUrl + headUrl);
                                HttpUtil.getInstance().editPresonal(editPresonal).subscribe(
                                        editSuccess -> {
                                            user.setHeadImageUrl(Constant.baseUrl + headUrl);
                                            UserOption.getInstance().updateUser(user);
                                            activity.notifyFragmentDataChange(presenter.getFragments().get(3));
                                        }
                                );
                            }
                    );
                }
                break;
        }
    }


    private static final int REQUEST_CROP_PHOTO = 102;

}
