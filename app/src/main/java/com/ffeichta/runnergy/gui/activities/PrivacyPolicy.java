package com.ffeichta.runnergy.gui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.ffeichta.runnergy.R;

/**
 * Created by Fabian on 08.02.2016.
 */
public class PrivacyPolicy extends Activity {

    private WebView webView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_activity);

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("http://www.ffeichta.com/runnergy/privacy-policy.html");
    }
}
