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
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.bean.BindProductBody;
import com.example.godcode.bean.EditProduct;
import com.example.godcode.bean.EditProductPrice;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.bean.ReturnEquity;
import com.example.godcode.bean.UploadResponse;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.FragmentMyassetConfigBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.EtItemDialog;
import com.example.godcode.ui.view.DeleteDialog;
import com.example.godcode.utils.BitmapUtil;
import com.example.godcode.utils.FileUtil;
import com.example.godcode.utils.GsonUtil;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Asset_2_Fragment extends BaseFragment implements DeleteDialog.OnClickSureListerer, EditAssetFragment.AssetUpdate, EtItemDialog.EtResponse {
    private FragmentMyassetConfigBinding binding;
    private View view;
    private MyAssetList.ResultBean.DataBean bean;
    private EtItemDialog dialog;
    private AssetFragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myasset_config, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            parentFragment = (AssetFragment) getParentFragment();
            initListener();
        }
        initView();
        return view;
    }


    private void initListener() {
        binding.relieveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = null;
                if (type == 1) {
                    title = "是否解除资产绑定?";
                } else if (type == 2) {
                    title = "是否返还产权?";
                }
                DeleteDialog deleteDialog = new DeleteDialog(activity, title);
                deleteDialog.setListerer(Asset_2_Fragment.this);
                deleteDialog.show();
            }
        });
        binding.revenueconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RevenueConfigFragment revenueConfigFragment = new RevenueConfigFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                revenueConfigFragment.setArguments(bundle);
                presenter.step2Fragment(revenueConfigFragment, "revenue");
            }
        });
        binding.etPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPrice();
            }
        });
        binding.ivZc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtil.getInstance().fromImg(activity);
            }
        });
        binding.etAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAssetFragment editAssetFragment = new EditAssetFragment();
                editAssetFragment.initData(bean);
                editAssetFragment.setAssetUpdate(Asset_2_Fragment.this);
                presenter.step2Fragment(editAssetFragment, "etAsset");
            }
        });

    }

    private int type;

    public void initView() {
        bean = parentFragment.getDataBean();
        if (bean.getPrimaevalUserID() == 0) {
            type = 1;
        } else {
            if (bean.getPrimaevalUserID() == Constant.userId) {
                type = 1;
            } else {
                type = 2;
            }
        }
        String productImgUrl = bean.getProductImgUrl();
        if (!TextUtils.isEmpty(productImgUrl)) {
            if (!productImgUrl.contains("http")) {
                productImgUrl = Constant.baseUrl + productImgUrl;
            }
            RxImageLoader.with(activity).load(productImgUrl).into(binding.ivZc);
        }
        binding.setAssetBean(bean);
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {
    }

    private void showEditPrice() {
        dialog = new EtItemDialog(activity, "修改价格", "价格", "",1);
        dialog.setEtResponse(this);
        dialog.show();
    }

    public void hanlderPrice(int price) {
        EditProductPrice editProductPrice = new EditProductPrice();
        EditProductPrice.ProductPriceBean productPriceBean = new EditProductPrice.ProductPriceBean();
        productPriceBean.setPrice(price);
        productPriceBean.setFK_ProductID(bean.getFK_ProductID());
        productPriceBean.setFK_UserID(bean.getFK_UserID());
        productPriceBean.setId(bean.getFK_PriceID());
        productPriceBean.setIsValid(true);
        editProductPrice.setProductPrice(productPriceBean);
        HttpUtil.getInstance().editProductPrice(editProductPrice).subscribe(
                editPriceStr -> {
                    if (editPriceStr.contains("\"success\":true")) {
                        Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                        bean.setPrice(price);
                        binding.setAssetBean(bean);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(activity, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void a(Intent data) {
        final Uri uri = data.getData();
        if (uri == null) {
            return;
        }
        String cropImagePath = FileUtil.getRealFilePathFromUri(activity, uri);
        Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
        binding.ivZc.setImageBitmap(bitMap);
        File file = new File(cropImagePath);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file));
        HttpUtil.getInstance().upload(filePart, 1).subscribe(
                uploadStr -> {
                    UploadResponse uploadResponse = GsonUtil.getInstance().fromJson(uploadStr, UploadResponse.class);
                    String picturePath = uploadResponse.getResult().get(0);
                    EditProduct editProduct = new EditProduct();
                    EditProduct.ProductBean productBean = new EditProduct.ProductBean();
                    productBean.setId(bean.getFK_ProductID());
                    productBean.setFK_ProductCategoryID(bean.getProductCategoryID());
                    productBean.setProductName(bean.getProductName());
                    productBean.setProductNumber(bean.getProductNumber());
                    productBean.setMachineAddress(bean.getMachineAddress());
                    productBean.setIsValid(true);
                    productBean.setThumbnailImgPath(picturePath);
                    editProduct.setProduct(productBean);
                    HttpUtil.getInstance().editProduct(editProduct).subscribe(
                    );
                }
        );
    }

    @Override
    public void clickSure() {
        if (type == 1) {
            unBindProduct();
        } else if (type == 2) {
            returnEquity();
        }
    }

    public void returnEquity() {
        ReturnEquity returnEquity = new ReturnEquity();
        returnEquity.setFK_UserID(bean.getFK_UserID());
        returnEquity.setId(bean.getId());
        HttpUtil.getInstance().returnEquity(returnEquity).subscribe(
                returnEquityStr -> {
                    Toast.makeText(activity, "产权返还成功", Toast.LENGTH_SHORT).show();
                    parentFragment.toogleFragment(0);
                }
        );
    }


    private void unBindProduct() {
        BindProductBody body = new BindProductBody();
        body.setFK_UserID(bean.getFK_UserID());
        body.setIsBind(false);
        body.setProductName(bean.getProductName());
        body.setProductNumber(bean.getProductNumber());
        body.setMachineAddress(bean.getMachineAddress());
        body.setFK_ProductCategoryID(bean.getProductCategoryID());
        body.setPrice(bean.getPrice() + "");
        HttpUtil.getInstance().bindProduct(body).subscribe(
                bindProductStr -> {
                    Toast.makeText(activity, "解绑成功", Toast.LENGTH_SHORT).show();
                    parentFragment.toogleFragment(0);
                }
        );
    }

    @Override
    public void assetUpdate(String productName, String adress) {
        bean.setProductName(productName);
        bean.setMachineAddress(adress);
        binding.setAssetBean(bean);
    }


    @Override
    public void hanlderEt(String str,int position) {
        int value=Integer.parseInt(str);
        hanlderPrice(value);
    }
}
