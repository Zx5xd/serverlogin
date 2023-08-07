package com.example.serverlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.serverlogin.login.Login;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Loading();//메소드실행
    }
    private void Loading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // finish();//종료
                Toast.makeText(getApplicationContext(),"당신의 하루를 들려주세요",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoadingActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                // BackPress로 로딩창으로 넘어오는 것을 방지하기 위해 TASK 초기화
                startActivity(intent);
            }
        },2000);//2초
    }
}
