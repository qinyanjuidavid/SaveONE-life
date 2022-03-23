package com.wyksofts.saveone.ui.Others;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wyksofts.saveone.BuildConfig;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.showTermsDiag;

public class AboutApp extends AppCompatActivity {

    TextView version, tmc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        getWindow().setStatusBarColor(getResources().getColor(R.color.orange1, this.getTheme()));

        version = findViewById(R.id.version);
        tmc = findViewById(R.id.terms);

        //get version name
        String versionName = BuildConfig.VERSION_NAME;
        version.setText(versionName);


        //show terms
        tmc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new showTermsDiag(AboutApp.this).showTCDialog();
            }
        });
    }
}