package com.wyksofts.saveone.ui.homeUI.HelperClasses;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.Constants.Constants;


public class showTermsDiag extends View {

    Dialog tmc;
    Context mContext;
    WebView webView;
    ProgressBar progressBar;

    public showTermsDiag(Context context) {
        super(context);
        this.mContext = context;
        tmc = new Dialog(mContext);
    }

    @SuppressLint({"SetJavaScriptEnabled", "SdCardPath"})
    public void showTCDialog() {
        //set content view
        tmc.setContentView(R.layout.tmc_layout);
        webView = tmc.findViewById(R.id.webview);
        progressBar= tmc.findViewById(R.id.progressBar);


        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(200);
                    progressBar.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    progressBar.setVisibility(View.GONE);
                }
            }
        };

        //get terms and condition layout
        String url = Constants.TC_URL;

        //set webview settings
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        //save caches to sdcard
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCachePath("/data/data/" + mContext.getPackageName() + "/cache");
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        //close btn
        tmc.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
            public void onClick(View param1View) {
                tmc.dismiss();
            }
        });

        tmc.setCancelable(false);
        tmc.getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(0));
        tmc.show();
    }
}
