package com.example.douin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.douin.play.ViewPagerLayoutManagerActivity;
import com.example.douin.user.Info;
import com.example.douin.user.LoginActivity;


/**
 * 默认启动Activity，会自动跳转到对应页面
 *
 * @author 方昊
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent i = new Intent(this, LoginActivity.class);
//        i = new Intent(this, DEMONetworkActivity.class);
        startActivity(i);
//        finish(); // 直接会回退回来就白屏了
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Info.getinstance().user_email.equals("EMPTY")) {
            Intent i = new Intent(this, LoginActivity.class); // 一旦回退到主活动，就跳转到播放器页面
//        i = new Intent(this, DEMONetworkActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, ViewPagerLayoutManagerActivity.class); // 一旦回退到主活动，就跳转到播放器页面
//        i = new Intent(this, DEMONetworkActivity.class);
            startActivity(i);
        }

    }
}