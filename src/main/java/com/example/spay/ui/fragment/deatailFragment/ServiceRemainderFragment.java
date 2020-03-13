package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.example.spay.R;
import com.example.spay.databinding.FragmentServicereaminderBinding;
import com.example.spay.greendao.entity.Notification;
import com.example.spay.greendao.option.NotificationOption;
import com.example.spay.ui.adapter.ServiceRemainderListAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.fragment.dm.MyScore;
import com.example.spay.utils.StringUtil;
import java.util.Collections;
import java.util.List;

public class ServiceRemainderFragment extends BaseFragment {
    private FragmentServicereaminderBinding binding;
    private View view;
    private List<Notification> notifications;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_servicereaminder, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            String title = StringUtil.getString(activity, R.string.fwtx);
            binding.serviceRemainderToolbar.title.setText(title);
            lazyLoad();
            initListener();
        }
        initView();
        return binding.getRoot();
    }

    private void initListener() {
        binding.lvServiceRemainder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification notification = notifications.get(position);
                int type = notification.getType();
                switch (type){
                    case 1:
                        TransationRecordDetailFragment trdf = new TransationRecordDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", notification.getTransactionId());
                        bundle.putInt("relatedKey", notification.getRelatedKey());
                        bundle.putInt("transactionType", notification.getTransactionType());
                        trdf.setArguments(bundle);
                        presenter.step2Fragment(trdf, "trdf");
                        break;
                    case 2:
                        presenter.step2Fragment("bankCard");
                        break;
                    case 3:

                        break;
                    case 4:
                        presenter.step2Fragment("myAsset");
                        break;
                    case 5:
                        MyScore myScore = new MyScore();
                        presenter.step2Fragment(myScore, "myScore");
                        break;

                }

            }
        });
    }

    public void initView() {
        notifications = NotificationOption.getInstance().getAllNotification(Constant.userId);
        Collections.reverse(notifications);
        ServiceRemainderListAdapter adapter = new ServiceRemainderListAdapter(notifications, activity);
        binding.lvServiceRemainder.setAdapter(adapter);
    }

    @Override
    protected void lazyLoad() {
    }


}
