package com.android.example.album

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.example.album.R

/**
 * 忘记密码页面
 *
 * @author YDJSIR
 */
class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        // 设置页面标题
        supportActionBar?.title = getString(R.string.album_app_name)

        findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            val name = findViewById<EditText>(R.id.et_username).text.toString()
            val email = findViewById<EditText>(R.id.et_useremail).text.toString()
            val user = UserCenter.getUserByEmail(email)
            when {
                user == null -> {
                    Toast.makeText(this, R.string.forget_pass_no_user, Toast.LENGTH_SHORT).show()
                }
                user.name != name -> {
                    Toast.makeText(this, R.string.forget_pass_name_not_match, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(
                        this,
                        "${getString(R.string.forget_pass_result)}${user.password}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}