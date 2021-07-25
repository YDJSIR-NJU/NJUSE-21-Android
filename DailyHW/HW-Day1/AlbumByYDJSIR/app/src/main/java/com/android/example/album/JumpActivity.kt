package com.android.example.album

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.android.example.album.R

/**
 * 相册的默认启动页面
// * 显示1秒钟开屏广告 有bug
 * 会自动判断当前用户是否登陆，如果已经登陆就直接进入相册页面，否则进入登陆页面
 *
 *@author YDJSIR
 */
class JumpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.open_screen_ad)
//
//        Thread.sleep(2000)
        if (UserCenter.signInUser != null) {
            startActivity(Intent(this, AlbumActivity::class.java))
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        // 就是一个跳转页面，没有必要留存
        finish()
    }
}