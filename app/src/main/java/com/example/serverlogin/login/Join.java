package com.example.serverlogin.login;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.serverlogin.DefaultAlertBuild;
import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class Join extends AppCompatActivity {

    Button fin, idCheck;
    EditText id_etc,pwd_etc, pwdOk_etc, nick_etc;
    TextView pwdok_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_join);

        // user_join의 요소와 연결
        id_etc = findViewById(R.id.id_etc);
        pwd_etc = findViewById(R.id.pwd_etc);
        nick_etc = findViewById(R.id.nick_etc);
        fin = findViewById(R.id.btn_send);
        pwdOk_etc = findViewById(R.id.pwdok_etc);
        pwdok_Text = findViewById(R.id.pwdok_Text);
        idCheck = findViewById(R.id.idCheck);

        final LinkHttp link = new LinkHttp();
        final DefaultAlertBuild alert = new DefaultAlertBuild();
        idCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이디 중복확인 버튼 클릭 시 DB에 중복되는 아이디가 있는지 확인
                if(id_etc.getText().toString().length()!=0) {
                    URL url = null;
                    try {
                        url = link.LinkHttp(getResources().getString(R.string.idCheck_url), id_etc.getText().toString(), null, null);
                    } catch (MalformedURLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    new HttpConnection().execute(url);
                } else{
                    alert.DefaultAlert("아이디를 입력해주세요.","Error",Join.this);
                }
            }
        });

            pwd_etc.addTextChangedListener(new TextWatcher() {
                // pwd_etc EditText 값이 변경되면
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(pwd_etc.getText().toString().equals(pwdOk_etc.getText().toString())){
                        // 비밀번호가 일치하면

                        // TextView 활성화
                        pwdok_Text.setVisibility(View.VISIBLE);
                        pwdok_Text.setText("비밀번호가 일치합니다.");
                        pwdok_Text.setTextColor(Color.WHITE);
                    }
                    else if(pwdOk_etc.getText().toString().length() != 0){
                        // 비밀번호 확인칸이 null이 아니면서 비밀번호칸과 값이 일치하지 않다면

                        // TextView 활성화
                        pwdok_Text.setVisibility(View.VISIBLE);
                        pwdok_Text.setText("비밀번호가 일치하지 않습니다.");
                        pwdok_Text.setTextColor(Color.RED);
                    }
                    if(pwd_etc.getText().toString().length() == 0){
                        // 비밀번호 칸 값이 null이면 TextView 비활성화
                        pwdok_Text.setVisibility(View.INVISIBLE);
                    }
                }
            });

            pwdOk_etc.addTextChangedListener(new TextWatcher() {
                // pwdOk_etc EditText 값이 변경되면
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                   // System.out.println("Editable: "+s);
                    if(pwd_etc.getText().toString().equals(pwdOk_etc.getText().toString())){
                        // 비밀번호가 일치하면

                        // TextView 활성화
                        pwdok_Text.setVisibility(View.VISIBLE);
                        pwdok_Text.setText("비밀번호가 일치합니다.");
                        pwdok_Text.setTextColor(Color.WHITE);
                    }
                    else if(pwd_etc.getText().toString().length() != 0){
                        // 비밀번호 칸이 null이 아니면서 비밀번호 확인칸과 값이 일치하지 않다면

                        // TextView 활성화
                        pwdok_Text.setVisibility(View.VISIBLE);
                        pwdok_Text.setText("비밀번호가 일치하지 않습니다.");
                        pwdok_Text.setTextColor(Color.RED);
                    }
                    if(pwdOk_etc.getText().toString().length() == 0){
                        // 비밀번호 확인칸 값이 null이면 TextView 비활성화
                        pwdok_Text.setVisibility(View.INVISIBLE);
                    }

                }
            });



        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 가입 버튼 클릭시
                if(id_etc.getText().toString().length()!=0
                        && pwd_etc.getText().toString().length()!=0
                        && nick_etc.getText().toString().length() != 0){
                    // id_etc, pwd_etc, nick_etc 값이 null이 아니면 join.jsp와 연결
                    URL url = null;
                    try {
                        url = link.LinkHttp(getResources().getString(R.string.join_url),id_etc.getText().toString(),pwd_etc.getText().toString(),nick_etc.getText().toString());
                    } catch (MalformedURLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    new HttpConnection().execute(url);
                } else {
                    alert.DefaultAlert("작성되지 않은 항목이 존재합니다.\n작성해주세요.","Error",Join.this);
                }
            }
        });

    }

    private class HttpConnection extends AsyncTask<URL, Integer, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("Join s "+ s);
            DefaultAlertBuild alertBuild = new DefaultAlertBuild();
            if(s.trim().equals("회원가입 실패")){ // 회원가입, 서버 연결 실패 또는 기타 문제
                alertBuild.DefaultAlert("가입에 실패하셨습니다. \n다시 시도해주십시오.",s.trim(),Join.this);
            }else if(s.trim().equals("가입 성공")){ // 회원가입, DB에 데이터 삽입 성공
                alertBuild.ChageView("회원가입이 성공적으로 마쳤습니다. \n로그인하십시오.","회원가입 완료",Join.this, Login.class);
            }else if(s.trim().equals("사용가능한 아이디입니다.")){ // 아이디 중복확인, DB에 중복되는 값이 없을때
                alertBuild.DefaultAlert(s.trim(),"사용가능 아이디",Join.this);
            }else if(s.trim().equals("사용불가능한 아이디입니다.")) { // 아이디 중복확인, DB에 중복되는 값이 있을때
                alertBuild.DefaultAlert(s.trim(), "사용불가능 아이디", Join.this);
            } else{ // 기타 문제
                alertBuild.DefaultAlert("시스템 Error","Error",Join.this);
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            String data = "";

            if(urls.length==0)
                return "URL is empty";

            try{
                RequestHttpURLConnection connection = new RequestHttpURLConnection();
                data = connection.request(urls[0]);
            } catch (Exception e){
                data = e.getMessage();
            }

            return data;
        }
    }
}