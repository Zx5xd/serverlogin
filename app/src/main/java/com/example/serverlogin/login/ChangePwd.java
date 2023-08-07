package com.example.serverlogin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.serverlogin.DefaultAlertBuild;
import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class ChangePwd extends AppCompatActivity {

    Button find;
    EditText pwd_etc, pwd2_etc;
    URL url;
    final DefaultAlertBuild da = new DefaultAlertBuild();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_chagepwd);

        // user_changepwd의 요소와 연결
        pwd_etc = findViewById(R.id.pwd_etc);
        pwd2_etc = findViewById(R.id.pwd2_etc);
        find = findViewById(R.id.btn_send);

        final String tempid = FindPwd.temp.getString("tempId","");
        // FindPwd 클래스에 있는 전역변수 temp(SharedPreferences) 이용
        // tempId란 특수 키와 매칭되는 값을 tempid에 저장.
        // 매칭되는 키가 null이라면 "" 반환

        if(tempid == null){ // 임시로 저장된 id 값이 없다면

            // TASK 목록 초기화 후 Login 창으로 이동
            Intent intent = new Intent(getApplicationContext(),Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        final LinkHttp link = new LinkHttp();

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pwd_etc.getText().toString().equals(pwd2_etc.getText().toString())){
                    // 비밀번호 값과 비밀번화 확인값이 일치하면
                    try {
                        url = link.LinkHttp(getResources().getString(R.string.changepwd_url),tempid,pwd_etc.getText().toString(),null);
                        // FindPwd 클래스에서 임시로 입력한 id와 pwd_etc 값을 url 경로에 정렬
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    new HttpConnection().execute(url); // DB 연결 시도
                }else{
                    da.DefaultAlert("비밀번호가 일치하지 않습니다.","Failed Chaging Password",ChangePwd.this);
                }
            }
        });
    }

    private class HttpConnection extends AsyncTask<URL, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(this.getClass().getName(), "onPostExecute s: "+s);
            if(s.trim().equals("비밀번호 변경 성공")){
                FindPwd.tempedt.clear().commit(); // tempedt로 작성한 모든 정보들을 지워버리고 적용
            da.ChageView(s,"비밀번호 변경 성공",ChangePwd.this, Login.class);
            // Login 창으로 이동
            }else{
                da.DefaultAlert(s,"비밀번호 변경 실패",ChangePwd.this);
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            String data = "";

            if (urls.length == 0)
                return "URL is empty";

            try {
                RequestHttpURLConnection connection = new RequestHttpURLConnection();
                data = connection.request(urls[0]);
            } catch (Exception e) {
                data = e.getMessage();
            }

            return data;
        }
    }
}
