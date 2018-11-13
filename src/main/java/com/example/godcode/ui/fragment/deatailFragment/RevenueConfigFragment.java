package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.bean.CreateDivide;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.bean.RevenueDivide;
import com.example.godcode.bean.RevenueDivideItem;
import com.example.godcode.bean.TransferDivide;
import com.example.godcode.databinding.FragmentRevenueconfigBinding;
import com.example.godcode.databinding.ItemLvRevenueconfigBinding;
import com.example.godcode.greendao.entity.Friend;
import com.example.godcode.greendao.option.FriendOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.adapter.RevenueConfigListAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.EtItemDialog;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RevenueConfigFragment extends BaseFragment implements EtItemDialog.EtResponse, Presenter.FriendIdResponse {
    private FragmentRevenueconfigBinding binding;
    private View view;
    private RevenueConfigListAdapter adapter;
    private List<RevenueDivide.ResultBean.ItemsBean> revenueList;
    private int fk_productID;
    private int fkUserIDOwner;
    private int mainDivideRate;
    private EtItemDialog dialog;
    private RevenueDivideItem currentRevenueItem;
    private MyAssetList.ResultBean.DataBean bean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_revenueconfig, container, false);
            bean = (MyAssetList.ResultBean.DataBean) getArguments().getSerializable("bean");
            presenter.setFriendIdResponse(this);
            initData(bean);
            binding.revenueconfigToolbar.title.setText("设置营收分成");
            binding.setPresenter(presenter);
            view = binding.getRoot();
            revenueDivideList = new ArrayList<>();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.addRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAdd) {
                    TransferAccountFragment transferAccountFragment = new TransferAccountFragment();
                    transferAccountFragment.setType(2);
                    presenter.step2Fragment(transferAccountFragment, "transferAccount");
                } else {
                    Toast.makeText(activity, "有分成商家未分成", Toast.LENGTH_SHORT).show();
                }
            }
        });
        createDivide();
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {
    }


    private boolean isAdd;

    private ArrayList<RevenueDivideItem> revenueDivideList;

    private void initData(MyAssetList.ResultBean.DataBean bean) {
        HttpUtil.getInstance().getRevenueDivideById(bean.getFK_ProductID()).subscribe(
                revenueDivideStr -> {
                    RevenueDivide revenueDivide = new Gson().fromJson(revenueDivideStr, RevenueDivide.class);
                    revenueList = revenueDivide.getResult().getItems();
                    RevenueDivide.ResultBean.ItemsBean mainBean = null;
                    for (RevenueDivide.ResultBean.ItemsBean itemBean : revenueList) {
                        if (itemBean.getFK_UserIDDivide() == itemBean.getFK_UserIDOwner()) {
                            mainBean = itemBean;
                            break;
                        }
                    }
                    if (mainBean != null) {
                        mainDivideRate = mainBean.getDivideRate();
                        fk_productID = mainBean.getFK_ProductID();
                        fkUserIDOwner = mainBean.getFK_UserIDOwner();
                        binding.setMainPresent(mainDivideRate);
                        binding.mainSeller.setText("主商家-" + mainBean.getDivideUserName());
                        revenueList.remove(mainBean);
                        revenueDivideList.clear();
                        for (int i = 0; i < revenueList.size(); i++) {
                            RevenueDivide.ResultBean.ItemsBean itemsBean = revenueList.get(i);
                            String parentID = bean.getParentID();
                            if (!TextUtils.isEmpty(parentID)) {
                                if (!parentID.contains(itemsBean.getFK_UserIDDivide() + "") && !parentID.contains(itemsBean.getLastModifyID() + "")) {
                                    RevenueDivideItem revenueItem = getRevenueItem(itemsBean.getDivideUserName(), itemsBean.getDivideRate(), String.valueOf(itemsBean.getId()), itemsBean.getFK_UserIDDivide());
                                    revenueDivideList.add(revenueItem);
                                }
                            } else {
                                RevenueDivideItem revenueItem = getRevenueItem(itemsBean.getDivideUserName(), itemsBean.getDivideRate(), String.valueOf(itemsBean.getId()), itemsBean.getFK_UserIDDivide());
                                revenueDivideList.add(revenueItem);
                            }
                        }


                        adapter = new RevenueConfigListAdapter(activity, revenueDivideList, RevenueConfigFragment.this);
                        binding.lvRevenueconfig.setAdapter(adapter);
                    }
                }
        );
    }

    private void createDivide() {
        binding.commitRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = adapter.getView(revenueDivideList.size()-1, null, null);
                ItemLvRevenueconfigBinding binding = (ItemLvRevenueconfigBinding) view.getTag();
                int divideRate = 0;
                Editable text = binding.etPresent.getText();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(activity, "请输入分成", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    divideRate = Integer.parseInt(text.toString());
                }
                if (divideRate > mainDivideRate) {
                    Toast.makeText(activity, "超出最大可分成值", Toast.LENGTH_SHORT).show();
                    return;
                }

                CreateDivide createDivide = new CreateDivide();
                CreateDivide.RevenueDivideBean divideBean = getDivideBean(null, currentRevenueItem.getUserId(), divideRate);
                createDivide.setRevenueDivide(divideBean);
                HttpUtil.getInstance().createDivide(createDivide).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        addRevenueDivideStr -> {
                            isAdd = false;
                            Toast.makeText(activity, "添加成功", Toast.LENGTH_SHORT).show();
                            initData(bean);
                            adapter.notifyDataSetChanged();
                        }
                );
            }
        });
    }

    public void alertDialog(int position) {
        type = 1;
        RevenueDivideItem revenueDivideItem = revenueDivideList.get(position);
        int divideRate = revenueDivideItem.getDivideRate();
        dialog = new EtItemDialog(activity, "修改分成", "分成", "1%-" + (divideRate + mainDivideRate) + "%",1);
        dialog.setPosition(position);
        dialog.setEtResponse(this);
        dialog.show();
    }

    public void deleteDivideItem(RevenueDivideItem revenueDivideItem) {
        if (revenueDivideItem.getId() == null) {
            //直接删除
            revenueDivideList.remove(revenueDivideList.size() - 1);
            adapter.notifyDataSetChanged();
            isAdd = false;
        } else {
            HttpUtil.getInstance().deleteRevenueDivide(Integer.parseInt(revenueDivideItem.getId())).subscribe(
                    deleteDivideStr -> {
                        Toast.makeText(activity, "删除成功", Toast.LENGTH_SHORT).show();
                        initData(bean);
                    }
            );
        }
    }

    private int type;

    public void transferDivide(int position) {
        type = 2;
        RevenueDivideItem revenueDivideItem = revenueDivideList.get(position);
        int divideRate = revenueDivideItem.getDivideRate();
        dialog = new EtItemDialog(activity, "移交产权", "比例", "0%-" + (divideRate + mainDivideRate) + "%",1);
        dialog.setPosition(position);
        dialog.setEtResponse(this);
        dialog.show();
    }

    private void transferEquity(int value, int position) {
        RevenueDivideItem revenueDivideItem = revenueDivideList.get(position);
        String parentID = bean.getParentID();
        if (!TextUtils.isEmpty(parentID)) {
            if (parentID.contains(revenueDivideItem.getUserId() + "")) {
                Toast.makeText(activity, "不能向父商家移交产权", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (value == 0) {
            Toast.makeText(activity, "请输入移交比例", Toast.LENGTH_SHORT).show();
            return;
        }
        if (value > revenueDivideItem.getDivideRate() + mainDivideRate) {
            Toast.makeText(activity, "超出最大可分成值", Toast.LENGTH_SHORT).show();
            return;
        }
        CreateDivide.RevenueDivideBean divideBean = getDivideBean(null, revenueDivideItem.getUserId(), value);
        CreateDivide createDivide = new CreateDivide();
        createDivide.setRevenueDivide(divideBean);
        HttpUtil.getInstance().createDivide(createDivide).subscribe(
                updateDivideStr -> {
                    int i = value - revenueDivideItem.getDivideRate();
                    revenueDivideItem.setDivideRate(value);
                    adapter.notifyDataSetChanged();
                    mainDivideRate=mainDivideRate-i;
                    revenueDivideItem.setDivideRate(value);
                    binding.setMainPresent(mainDivideRate);
                    TransferDivide transferDivide = new TransferDivide();
                    transferDivide.setFK_UserID(revenueDivideItem.getUserId());
                    transferDivide.setHostMerchantUserID(Constant.userId);
                    transferDivide.setId(bean.getId());
                    HttpUtil.getInstance().tranferDivide(transferDivide).subscribe(
                            transferDivideStr -> {
                                Toast.makeText(activity, "产权交换成功", Toast.LENGTH_SHORT).show();
                                AssetFragment assetFragment = (AssetFragment) presenter.getFragment("myAsset");
                                assetFragment.setIndex(0);
                                dialog.dismiss();
                                presenter.back();
                            }
                    );
                }
        );
    }

    public CreateDivide.RevenueDivideBean getDivideBean(String id, int fK_UserIDDivide, int divideRate) {
        CreateDivide.RevenueDivideBean revenueDivideBean = new CreateDivide.RevenueDivideBean();
        revenueDivideBean.setId(id);
        revenueDivideBean.setFK_ProductID(fk_productID);
        revenueDivideBean.setFK_UserIDOwner(fkUserIDOwner);
        revenueDivideBean.setFK_UserIDDivide(fK_UserIDDivide);
        revenueDivideBean.setDivideRate(divideRate);
        revenueDivideBean.setIsValid(true);
        return revenueDivideBean;
    }

    public RevenueDivideItem getRevenueItem(String name, int divideRate, String id, int userId) {
        RevenueDivideItem revenueDivideItem = new RevenueDivideItem();
        revenueDivideItem.setDivideRate(divideRate);
        revenueDivideItem.setId(id);
        revenueDivideItem.setName(name);
        revenueDivideItem.setUserId(userId);
        return revenueDivideItem;
    }

    @Override
    public void hanlderEt(String str, int position) {
        int value=Integer.parseInt(str);
        if (type == 1) {
            updateDivide(value, position);
        } else if (type == 2) {
            transferEquity(value, position);
        }
    }

    private void updateDivide(int value, int position) {
        RevenueDivideItem revenueDivideItem = revenueDivideList.get(position);
        if (value < revenueDivideItem.getDivideRate() + mainDivideRate - 1) {
            CreateDivide.RevenueDivideBean divideBean = getDivideBean(String.valueOf(revenueDivideItem.getId()), revenueDivideItem.getUserId(), value);
            CreateDivide createDivide = new CreateDivide();
            createDivide.setRevenueDivide(divideBean);
            HttpUtil.getInstance().createDivide(createDivide).subscribe(
                    updateDivideStr -> {
                        View view = adapter.getView(position, null, null);
                        ItemLvRevenueconfigBinding itemBinding = (ItemLvRevenueconfigBinding) view.getTag();
                        Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                        int i = value - revenueDivideItem.getDivideRate();
                        revenueDivideItem.setDivideRate(value);
                        itemBinding.etPresent.setText(value + "");
                        mainDivideRate=mainDivideRate-i;
                        binding.setMainPresent(mainDivideRate);
                        dialog.dismiss();
                    }
            );
        } else {
            Toast.makeText(activity, "超出最大可分成值", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getFriendId(int friendId) {
        for (RevenueDivideItem revenueDivideItem : revenueDivideList) {
            if (revenueDivideItem.getUserId() == friendId) {
                Toast.makeText(activity, "该分成用户已存在", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        isAdd = true;
        Friend friend = FriendOption.getInstance(activity).querryFriend(friendId);
        currentRevenueItem = getRevenueItem(friend.getUserName(), 0, null, friendId);
        revenueDivideList.add(currentRevenueItem);
        binding.lvRevenueconfig.setAdapter(adapter);
    }
}
