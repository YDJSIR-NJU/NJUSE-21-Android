package com.byted.chapter5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText eName = findViewById(R.id.name);
        EditText ePassword = findViewById(R.id.password);
        EditText eRepassword = findViewById(R.id.repassword);
        Button register = findViewById(R.id.register);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class); // 这里已经构建好了

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eName.getText().toString();
                String password = ePassword.getText().toString();
                String repassword = eRepassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)) {
                    Toast.makeText(RegisterActivity.this, "参数不合法", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.equals(password, repassword)) {
                    Toast.makeText(RegisterActivity.this, "密码不相等", Toast.LENGTH_SHORT).show();
                    return;
                }
                // todo 做网络请求
                NewUser newUser = new NewUser(name, password, repassword);
                Call<NewUser> registerCall = apiService.register(name, password, repassword);
                registerCall.enqueue(new Callback<NewUser>() {
                    @Override
                    public void onResponse(Call<NewUser> call, Response<NewUser> response) {
                        System.out.println(response.toString());
                        Log.d("registerResponse", response.toString());
                    }

                    @Override
                    public void onFailure(Call<NewUser> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        Log.d("registerResponse", "NetworkFailed");
                    }
                });
            }
        });
    }
}
