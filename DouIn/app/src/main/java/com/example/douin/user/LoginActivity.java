package com.example.douin.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.douin.R;
import com.example.douin.user.db.RDBHelper;
import com.example.douin.play.ViewPagerLayoutManagerActivity;

/**
 * 登陆页面
 *
 * @author 方昊
 */
public class LoginActivity extends AppCompatActivity {
    Context context=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_in);
        context=this;

      findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              // 点击事件
               ProgressBar progressBar = findViewById(R.id.progressBar);
                      progressBar.setVisibility(View.VISIBLE);

               String email = ((EditText)findViewById(R.id.editText)).getText().toString();
               String password = ((EditText)findViewById(R.id.editText2)).getText().toString();

               if(email.equals("")){
                   Toast.makeText(context, "账户不能为空！", Toast.LENGTH_SHORT).show();
                   progressBar.setVisibility(View.INVISIBLE);
               return;
                       }
              if(password.equals("")){
                  Toast.makeText(context, "密码不能为空！", Toast.LENGTH_SHORT).show();
                  progressBar.setVisibility(View.INVISIBLE);
              return;
              }
               RDBHelper r=new RDBHelper(context);
               String return_password = r.findPassword(email);

              if(return_password==null){
                  Toast.makeText(context, "无此用户！", Toast.LENGTH_SHORT).show();
                  progressBar.setVisibility(View.INVISIBLE);
              return;
              }
              if(!password.equals(return_password)){
                  Toast.makeText(context, "密码错误！", Toast.LENGTH_SHORT).show();
                  progressBar.setVisibility(View.INVISIBLE);
              return;
              }

              Intent i = new Intent(context, ViewPagerLayoutManagerActivity.class);
              Info.getinstance().user_email=email;
              startActivity(i);
              finish();
          }
      });


        findViewById(R.id.textView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
