package com.example.serverlogin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class FindPwd extends AppCompatActivity {

    public static SharedPreferences temp;
    public static SharedPreferences.Editor tempedt;
    EditText id_etc, nick_etc;
    Button find;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_findpwd);

        // user_findpwd의 요소와 연결
        id_etc = findViewById(R.id.id_etc);
        nick_etc = findViewById(R.id.nick_etc);
        find = findViewById(R.id.btn_send);

        final LinkHttp link = new LinkHttp();

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭시 findpwd.jsp 연결
                try {
                    url = link.LinkHttp(getResources().getString(R.string.findpwd_url),id_etc.getText().toString(),null,nick_etc.getText().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new HttpConnection().execute(url);
            }
        });
    }

    private class HttpConnection extends AsyncTask<URL, Integer, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DefaultAlertBuild da = new DefaultAlertBuild();

            if (s.trim().equals("비밀번호 변경 불가능")) {
                da.DefaultAlert(s, "Find Password",FindPwd.this);
            } else {
                // 비밀번호를 변경할 수 있다면
                temp = getSharedPreferences("temporary",MODE_PRIVATE);
                // 기본 모드로 temporary 파일 생성
                tempedt = temp.edit(); // temp의 수정은 tempedt가 맡음
                tempedt.putString("tempId",id_etc.getText().toString()); // tempId 키와 id_etc 값을 추가
                tempedt.commit(); // 적용
                da.ChageView(s,"Find Password",FindPwd.this,ChangePwd.class); // ChagePwd.class로 이동
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
