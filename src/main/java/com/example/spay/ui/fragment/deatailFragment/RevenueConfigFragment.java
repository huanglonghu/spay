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
import com.example.spay.bean.CreateDivide;
import com.example.spay.bean.MyAssetList;
import com.example.spay.bean.RevenueDivide;
import com.example.spay.bean.RevenueDivideItem;
import com.example.spay.bean.TransferDivide;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.FragmentRevenueconfigBinding;
import com.example.spay.greendao.entity.Friend;
import com.example.spay.greendao.option.FriendOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.EtStrategy;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.ui.adapter.RevenueConfigListAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.widget.EtItemDialog;
import com.example.spay.utils.StringUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RevenueConfigFragment extends BaseFragment {
    private FragmentRevenueconfigBinding binding;
    private View view;
    private RevenueConfigListAdapter adapter;
    private List<RevenueDivide.ResultBean.ItemsBean> revenueList;
    private int fk_productID;
    private int fkUserIDOwner;
    private int mainDivideRate;
    private RevenueDivideItem currentRevenueItem;
    private MyAssetList.ResultBean.DataBean bean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_revenueconfig, container, false);
            bean = (MyAssetList.ResultBean.DataBean) getArguments().getSerializable("bean");
            initData();
            String title = StringUtil.getString(activity, R.string.revenueSetting);
            binding.revenueconfigToolbar.title.setText(title);
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

        RxBus.getInstance().toObservable(RxEvent.class).subscribe(new Observer<RxEvent>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(RxEvent rxEvent) {
                switch (rxEvent.getEventType()) {
                    case EventType.EVENTTYPE_SELECT_FRIEND:
                        int id = rxEvent.getId();
                        for (RevenueDivideItem revenueDivideItem : revenueDivideList) {
                            if (revenueDivideItem.getUserId() == id) {
                                Toast.makeText(activity, "该分成用户已存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        isAdd = true;
                        Friend friend = FriendOption.getInstance(activity).querryFriend(id);
                        currentRevenueItem = getRevenueItem(friend.getUserName(), 0, null, id);
                        revenueDivideList.add(currentRevenueItem);
                        binding.lvRevenueconfig.setAdapter(adapter);
                        break;
                }

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void lazyLoad() {
    }

    private boolean isAdd;

    private ArrayList<RevenueDivideItem> revenueDivideList;

    private void initData() {
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
                if (currentRevenueItem == null) {
                    Toast.makeText(getContext(), "请先添加分成商家", Toast.LENGTH_SHORT).show();
                    return;
                }
                int divideRate = revenueDivideList.get(revenueDivideList.size() - 1).getDivideRate();
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
                            initData();
                            adapter.notifyDataSetChanged();
                        }
                );
            }
        });
    }

    public void alertDialog(int position) {
        RevenueDivideItem revenueDivideItem = revenueDivideList.get(position);
        int divideRate = revenueDivideItem.getDivideRate();
        EtItemDialog dialog = new EtItemDialog.Builder().
                context(activity).
                etStragety(new EtDivideStrategy()).
                title("修改分成").
                hint("分成").
                type(1).
                min(0).
                max(divideRate + mainDivideRate).
                position(position).
                build();
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
                        initData();
                    }
            );
        }
    }

    public void transferDivide(int position) {
        RevenueDivideItem revenueDivideItem = revenueDivideList.get(position);
        int divideRate = revenueDivideItem.getDivideRate();
        EtItemDialog dialog = new EtItemDialog.Builder().
                context(activity).
                etStragety(new TransferEquityStrategy()).
                title("移交产权").
                hint("比例").
                type(1).
                position(position).
                min(0).
                max(divideRate + mainDivideRate).
                build();
        dialog.show();
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


    public class TransferEquityStrategy extends EtStrategy {
        @Override
        public void etComplete(String str, int position) {
            int value = Integer.parseInt(str);
            RevenueDivideItem revenueDivideItem = revenueDivideList.get(position);
            String parentID = bean.getParentID();
            if (!TextUtils.isEmpty(parentID)) {
                if (parentID.contains(revenueDivideItem.getUserId() + "")) {
                    Toast.makeText(activity, "不能向父商家移交产权", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            CreateDivide.RevenueDivideBean divideBean = getDivideBean(null, revenueDivideItem.getUserId(), value);
            CreateDivide createDivide = new CreateDivide();
            createDivide.setRevenueDivide(divideBean);
            HttpUtil.getInstance().createDivide(createDivide).subscribe(
                    updateDivideStr -> {
                        int i = value - revenueDivideItem.getDivideRate();
                        revenueDivideItem.setDivideRate(value);
                        adapter.notifyDataSetChanged();
                        mainDivideRate = mainDivideRate - i;
                        revenueDivideItem.setDivideRate(value);
                        binding.setMainPresent(mainDivideRate);
                        TransferDivide transferDivide = new TransferDivide();
                        transferDivide.setFK_UserID(revenueDivideItem.getUserId());
                        transferDivide.setHostMerchantUserID(Constant.userId);
                        transferDivide.setId(bean.getId());
                        HttpUtil.getInstance().tranferDivide(transferDivide).subscribe(
                                transferDivideStr -> {
                                    Toast.makeText(activity, "产权交换成功", Toast.LENGTH_SHORT).show();
                                    presenter.back();
                                    presenter.back();
                                }
                        );
                    }
            );
        }
    }

    public class EtDivideStrategy extends EtStrategy {
        @Override
        public void etComplete(String str, int position) {
            int value = Integer.parseInt(str);
            RevenueDivideItem revenueDivideItem = revenueDivideList.get(position);
            CreateDivide.RevenueDivideBean divideBean = getDivideBean(String.valueOf(revenueDivideItem.getId()), revenueDivideItem.getUserId(), value);
            CreateDivide createDivide = new CreateDivide();
            createDivide.setRevenueDivide(divideBean);
            HttpUtil.getInstance().createDivide(createDivide).subscribe(
                    updateDivideStr -> {
                        Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                        int i = value - revenueDivideItem.getDivideRate();
                        revenueDivideItem.setDivideRate(value);
                        revenueDivideList.set(position, revenueDivideItem);
                        adapter.refresData(position);
                        mainDivideRate = mainDivideRate - i;
                        binding.setMainPresent(mainDivideRate);
                    }
            );
        }

    }


}
