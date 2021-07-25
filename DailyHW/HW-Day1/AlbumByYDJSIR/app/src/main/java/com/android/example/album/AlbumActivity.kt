package com.android.example.album

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.example.album.R

/**
 * 相册页面
 */
class AlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        // 设置页面标题
        supportActionBar?.title = getString(R.string.album_app_name)

        val ivAlbum = findViewById<ImageView>(R.id.iv_album)
        val images = mutableListOf(
            R.drawable.scene0,
            R.drawable.scene1,
            R.drawable.scene2,
            R.drawable.scene3,
        )
        var curIndex = 0
        ivAlbum.setImageResource(images[curIndex])

        /**
         * 查看上一张
         */
        findViewById<Button>(R.id.btn_prev).setOnClickListener {
            if (curIndex == 0) {
                Toast.makeText(this, R.string.album_no_prev, Toast.LENGTH_SHORT).show()
            } else {
                curIndex--
            }
            ivAlbum.setImageResource(images[curIndex])
        }

        /**
         * 查看下一张
         */
        findViewById<Button>(R.id.btn_next).setOnClickListener {
            if (curIndex == images.size - 1) {
                Toast.makeText(this, R.string.album_no_next, Toast.LENGTH_SHORT).show()
            } else {
                curIndex++
            }
            ivAlbum.setImageResource(images[curIndex])
        }

        /**
         * 登出相册
         */
        findViewById<Button>(R.id.btn_exit).setOnClickListener {
            UserCenter.signInUser = null
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}