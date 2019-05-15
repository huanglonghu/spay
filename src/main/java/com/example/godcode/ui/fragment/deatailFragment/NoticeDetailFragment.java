package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentNoticeDetailBinding;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;

public class NoticeDetailFragment extends BaseFragment {
    private FragmentNoticeDetailBinding binding;

    private View view;
    private int noticeId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            noticeId = getArguments().getInt("noticeId");
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice_detail, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            initView();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.noticeDetailTitle.exitWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.back();
            }
        });
    }


    public void initView() {
        loadWebView();
    }

    private void loadWebView() {
        String url = Constant.baseUrl+"/Notice/index?id=" + noticeId;
        binding.wbNotice.getSettings().setUseWideViewPort(true);
        binding.wbNotice.getSettings().setLoadWithOverviewMode(true);
        binding.wbNotice.getSettings().setJavaScriptEnabled(true);
        binding.wbNotice.loadUrl(url);//加载webView
        binding.wbNotice.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });
        binding.wbNotice.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }
        });

        binding.wbNotice.setWebChromeClient(
                new WebChromeClient() {
                    @Override
                    public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                        if (newProgress == 100) {
                            binding.pb.setVisibility(View.GONE);
                        } else {
                            binding.pb.setVisibility(View.VISIBLE);
                            binding.pb.setProgress(newProgress);
                        }
                    }
                }
        );

    }


    @Override
    protected void lazyLoad() {

    }



}
