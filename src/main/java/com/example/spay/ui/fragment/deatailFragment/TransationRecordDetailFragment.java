package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.bean.FrcationRecord;
import com.example.spay.bean.MobileRechargeRecord;
import com.example.spay.bean.QRPay;
import com.example.spay.bean.QrcodeGathering;
import com.example.spay.bean.RechargeDetail;
import com.example.spay.bean.Refound;
import com.example.spay.bean.SHSR;
import com.example.spay.bean.ShXf;
import com.example.spay.bean.TranferAccountDetail;
import com.example.spay.bean.TransationDetail;
import com.example.spay.bean.TranscationMsg;
import com.example.spay.bean.TxDetail;
import com.example.spay.bean.ZzIncome;
import com.example.spay.databinding.FragmentTransactionDetailBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.utils.DateUtil;
import com.example.spay.utils.FormatUtil;
import com.example.spay.utils.GsonUtil;

import java.text.DecimalFormat;

public class TransationRecordDetailFragment extends BaseFragment {
    private FragmentTransactionDetailBinding binding;
    private View view;
    private int id;
    private int relatedKey;
    private int transactionType;
    private String[] typeArray = {"", "转账支出", "二维码收款", "二维码付款", "商户消费", "充值", "提现", "退款", "转账收入", "产品营收", "手机充值","购买积分"};
    private DecimalFormat decimalFormat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            id = getArguments().getInt("id");
            relatedKey = getArguments().getInt("relatedKey");
            transactionType = getArguments().getInt("transactionType");
            decimalFormat = new DecimalFormat("0.00");
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_detail, container, false);
            binding.setPresenter(presenter);
            binding.transationDetailToolBar.title.setText("交易详情");
            binding.transationTitle.setText(typeArray[transactionType]);
            view = binding.getRoot();
            initData();
        }
        return view;
    }

    private void initData() {
        // String transferExpense = StringUtil.getString(activity, R.string.transferExpense);
        TransationDetail transationDetail = new TransationDetail();
        transationDetail.setRelatedKey(relatedKey);
        transationDetail.setId(id);
        transationDetail.setTransationType(transactionType + "");
        HttpUtil.getInstance().getTransationDetail(transationDetail).subscribe(
                transactionStr -> {
                    TranscationMsg transcationMsg = new TranscationMsg();
                    switch (transactionType) {
                        case 1:
                            TranferAccountDetail tranferAccountDetail = GsonUtil.getInstance().fromJson(transactionStr, TranferAccountDetail.class);
                            TranferAccountDetail.ResultBean result = tranferAccountDetail.getResult();
                            transcationMsg.setDate(changeDate(result.getPaymentTime()));
                            transcationMsg.setMoney(decimalFormat.format(result.getPaymentAmount()));
                            transcationMsg.setPayType(payType[result.getPaymentGenre()]);
                            transcationMsg.setPayWay(payWay[result.getPaymentMode()]);
                            transcationMsg.setTranscationObj(result.getDisbursUserName());
                            break;
                        case 2:
                            QrcodeGathering qrcodeGathering = GsonUtil.getInstance().fromJson(transactionStr, QrcodeGathering.class);
                            QrcodeGathering.ResultBean result1 = qrcodeGathering.getResult();
                            transcationMsg.setDate(changeDate(result1.getIncomeTime()));
                            transcationMsg.setMoney(FormatUtil.getInstance().get2double(result1.getIncomeAmount()));
                            transcationMsg.setPayType(incomeType[result1.getIncomeGenre()]);
                            transcationMsg.setPayWay(payWay[result1.getIncomeMode()]);
                            transcationMsg.setTranscationObj(result1.getDisbursUserName());
                            break;
                        case 3:
                            QRPay qrPay = GsonUtil.getInstance().fromJson(transactionStr, QRPay.class);
                            QRPay.ResultBean result3 = qrPay.getResult();
                            transcationMsg.setDate(changeDate(result3.getPaymentTime()));
                            transcationMsg.setMoney(FormatUtil.getInstance().get2double(result3.getPaymentAmount()));
                            transcationMsg.setTranscationObj(result3.getDisbursUserName());
                            transcationMsg.setPayWay(payType[result3.getPaymentGenre()]);
                            transcationMsg.setPayWay(payWay[result3.getPaymentMode()]);
                            break;
                        case 4:
                            ShXf shXf = GsonUtil.getInstance().fromJson(transactionStr, ShXf.class);
                            ShXf.ResultBean result4 = shXf.getResult();
                            transcationMsg.setOrderNum(result4.getOrderNumber());
                            transcationMsg.setProductName(result4.getProduceName());
                            transcationMsg.setMoney(decimalFormat.format(result4.getSumOrder()));
                            transcationMsg.setDate(changeDate(result4.getPayDate()));
                            transcationMsg.setPayType(payType[result4.getPaymentGenre()]);
                            transcationMsg.setPayWay(payWay[result4.getPaymentMode()]);
                            break;
                        case 5://充值
                            RechargeDetail rechargeDetail = GsonUtil.getInstance().fromJson(transactionStr, RechargeDetail.class);
                            RechargeDetail.ResultBean result2 = rechargeDetail.getResult();
                            transcationMsg.setMoney(decimalFormat.format(result2.getSumTotal()));
                            transcationMsg.setDate(changeDate(result2.getRecordTime()));
                            break;
                        case 6://提现
                            TxDetail txDetail = GsonUtil.getInstance().fromJson(transactionStr, TxDetail.class);
                            TxDetail.ResultBean result6 = txDetail.getResult();
                            transcationMsg.setMoney(decimalFormat.format(result6.getSumTotal()));
                            transcationMsg.setTxTime(changeDate(result6.getRecordTime()));
                            transcationMsg.setFactorage(decimalFormat.format(result6.getPutFactorage()));
                            transcationMsg.setStatus(txStatus[result6.getPutStatus()]);
                            transcationMsg.setBankName(result6.getBankNumber());
                            break;
                        case 7:
                            Refound refound = GsonUtil.getInstance().fromJson(transactionStr, Refound.class);
                            Refound.ResultBean result7 = refound.getResult();
                            transcationMsg.setOrderNum(result7.getOrderNumber());
                            transcationMsg.setProductName(result7.getProduceName());
                            transcationMsg.setJqbm(result7.getProductNumber());
                            transcationMsg.setMoney(decimalFormat.format(result7.getSumOrder()));
                            transcationMsg.setDate(changeDate(result7.getPayDate()));
                            transcationMsg.setRefoundDate(changeDate(result7.getRefundTime()));
                            transcationMsg.setStatus(result7.getResultCode());
                            break;
                        case 8://转账收入
                            ZzIncome zzIncome = GsonUtil.getInstance().fromJson(transactionStr, ZzIncome.class);
                            ZzIncome.ResultBean result8 = zzIncome.getResult();
                            transcationMsg.setMoney(FormatUtil.getInstance().get2double(result8.getIncomeAmount()));
                            transcationMsg.setDate(changeDate(result8.getIncomeTime()));
                            transcationMsg.setTranscationObj(result8.getDisbursUserName());
                            transcationMsg.setPayType(incomeType[result8.getIncomeGenre()]);
                            transcationMsg.setPayWay(payWay[result8.getIncomeMode()]);
                            break;
                        case 9:
                            SHSR shsr = GsonUtil.getInstance().fromJson(transactionStr, SHSR.class);
                            SHSR.ResultBean result9 = shsr.getResult();
                            transcationMsg.setPayType(incomeType[result9.getIncomeGenre()]);
                            transcationMsg.setMoney(FormatUtil.getInstance().get2double(result9.getIncomeAmount()));
                            transcationMsg.setDate(changeDate(result9.getIncomeTime()));
                            break;
                        case 10:
                            MobileRechargeRecord mobileRechargeRecord = GsonUtil.getInstance().fromJson(transactionStr, MobileRechargeRecord.class);
                            MobileRechargeRecord.ResultBean result10 = mobileRechargeRecord.getResult();
                            transcationMsg.setDescription(result10.getDescription());
                            transcationMsg.setPaymentAmount(result10.getPaymentAmount());
                            transcationMsg.setRechargeAmount(result10.getRechargeAmount());
                            transcationMsg.setOrderNum(result10.getRechargeOrderNumber());
                            transcationMsg.setRechargePhone(result10.getRechargePhoneNumber());
                            transcationMsg.setRechargeDate(changeDate(result10.getAddDateTime()));
                            break;
                        case 11:
                            FrcationRecord frcationRecord = GsonUtil.fromJson(transactionStr, FrcationRecord.class);
                            FrcationRecord.ResultBean result11 = frcationRecord.getResult();
                            transcationMsg.setMoney(result11.getMoney()+"");
                            transcationMsg.setDate(result11.getAddDateTime());
                            transcationMsg.setFraction(result11.getFraction()+"");
                            break;
                    }
                    binding.setMsg(transcationMsg);
                }
        );
    }


    public String changeDate(String dateStr) {
        String time = DateUtil.getInstance().formatDate(dateStr);
        return time;
    }

    private String[] payWay = {"微信", "支付宝", "余额", "银行卡"};

    private String[] payType = {"消费支出", "转账支出", "", "盈利分成", "扫码支出"};
    private String[] txStatus = {"未审核", "正在处理", "提现成功", "提现失败", "审核不通过"};
    private String[] incomeType = {"消费收入", "退款收入", "转账收入", "扫码收入", "盈利分成"};

    @Override
    protected void lazyLoad() {
    }

}
