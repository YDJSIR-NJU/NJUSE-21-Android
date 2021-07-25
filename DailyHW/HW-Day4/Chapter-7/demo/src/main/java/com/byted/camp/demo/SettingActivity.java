package com.byted.camp.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    private static final String KEY_COMMENT = "key_comment";

    private Switch commentSwitch;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSharedPreferences = getSharedPreferences("custom_settings", Context.MODE_PRIVATE);
        boolean isOpen = mSharedPreferences.getBoolean(KEY_COMMENT, false);

        commentSwitch = findViewById(R.id.switch_comment);
        commentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(KEY_COMMENT, isChecked);
                editor.commit();
            }
        });
        commentSwitch.setChecked(isOpen);
    }

}
