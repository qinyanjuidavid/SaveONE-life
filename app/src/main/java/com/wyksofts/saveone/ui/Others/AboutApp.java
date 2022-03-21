package com.wyksofts.saveone.ui.Others;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.wyksofts.saveone.BuildConfig;
import com.wyksofts.saveone.R;

public class AboutApp extends AppCompatActivity {

    TextView version, tmc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        getWindow().setStatusBarColor(getResources().getColor(R.color.orange1, this.getTheme()));

        version = findViewById(R.id.version);

        String versionName = BuildConfig.VERSION_NAME;

        version.setText(versionName);
    }
}