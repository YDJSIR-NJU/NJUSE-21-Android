package com.example.douin.user;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.douin.R;
import com.example.douin.user.db.RDBHelper;

/**
 * 注册页面
 *
 * @author 方昊
 */
public class RegisterActivity extends AppCompatActivity {
    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        EditText eName = findViewById(R.id.name);
        EditText ePassword = findViewById(R.id.email);
        EditText eRepassword = findViewById(R.id.password);
        Button register = findViewById(R.id.register);
        context = this;

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eName.getText().toString();
                String email = ePassword.getText().toString();
                String password = eRepassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "参数不合法", Toast.LENGTH_SHORT).show();
                    return;
                }
                RDBHelper r = new RDBHelper(context);
                if (r.insert(name, email, password)) {
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
