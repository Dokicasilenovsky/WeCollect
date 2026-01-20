package com.example.wecollectfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.Objects;
import java.util.Set;

public class Settings extends AppCompatActivity {

    Switch aSwitch;
    ImageView ivLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_WeCollectFireBase);
        }else{
            setTheme(R.style.Theme_WeCollectFireBase);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        ivLanguage = findViewById(R.id.ivLanguage);
        aSwitch = findViewById(R.id.switchTheme);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            aSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    setTheme(R.style.Theme_WeCollectFireBase);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }


        });

        ivLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, Language.class));
            }
        });
    }


}