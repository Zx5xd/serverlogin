package com.example.serverlogin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.serverlogin.DefaultAlertBuild;
import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;
import com.example.serverlogin.sns.Board;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateInfo extends AppCompatActivity {

    TextView IdInfo;
    EditText updateInfo_pwd, updateInfo_nick, getUpdateInfo_pwdok;
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_update);

        // user_update의 요소와 연결
        IdInfo = findViewById(R.id.info_id);
        updateInfo_nick = findViewById(R.id.updateInfo_nick);
        updateInfo_pwd = findViewById(R.id.updateInfo_pwd);
        getUpdateInfo_pwdok = findViewById(R.id.updateInfo_pwdok);
        btn_send = findViewById(R.id.btn_send);

        // 로그인한 id와 닉네임을 불러온다.
        final String id = Login.session.getString("userId","");
        final String nick = Login.session.getString("userNick","");

        if(id != null){
            IdInfo.setText(id);
        }else{ // 로그인한 정보가 없다면 Task 초기화 후 로그인 창으로 이동
            Intent intent = new Intent(getApplicationContext(),Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        updateInfo_nick.setHint(nick);
        // id 값이 null이 아니면 nick 값도 null이 아님.

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 시
                DefaultAlertBuild error = new DefaultAlertBuild();
                LinkHttp link = new LinkHttp();
                String path; URL url;
                if(updateInfo_pwd.getText().toString().length() == 0 | getUpdateInfo_pwdok.getText().toString().length() == 0){
                    error.DefaultAlert("비밀번호를 입력해주세요.","Error",UpdateInfo.this);
                }else{ // 비밀번호 칸과 비밀번호 확인 칸 값이 null이 아니면
                    if(updateInfo_pwd.getText().toString().equals(getUpdateInfo_pwdok.getText().toString())){ // 일치 확인
                        if(updateInfo_nick.getText().toString().length() == 0){
                            // 비밀번호 수정
                            try{
                                path = getResources().getString(R.string.updateinfo_url);
                                url = link.LinkHttp(path,id,updateInfo_pwd.getText().toString(),nick);
                                new HttpConnection().execute(url);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        } else{ // 닉네임 수정 및 비밀번호 변경
                            try {
                                Login.id = Login.session.edit();
                                Login.id.remove("userNick");
                                Login.id.putString("userNick",updateInfo_nick.getText().toString());
                                Login.id.commit();
                                path = getResources().getString(R.string.updateinfo_url);
                                url = link.LinkHttp(path,id,updateInfo_pwd.getText().toString(),updateInfo_nick.getText().toString());
                                new HttpConnection().execute(url);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    } else{
                        error.DefaultAlert("비밀번호가 일치하지 않습니다.\n다시 입력해주세요.","Error",UpdateInfo.this);
                    }
                }
            }
        });

    }
    private class HttpConnection extends AsyncTask<URL, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("user_update s:"+s);
            DefaultAlertBuild alert = new DefaultAlertBuild();
            if(s.trim().equals("회원정보 수정 완료")){
                alert.ChageView("회원정보 수정이 완료되었습니다.","정보수정 성공",UpdateInfo.this, Board.class);
            }else if(s.trim().equals("닉네임 중복")){
                alert.DefaultAlert("닉네임 중복 발생\n다른 닉네임을 입력해주세요.","닉네임 중복",UpdateInfo.this);
            }else{
                alert.DefaultAlert("시스템 에러.\n다음에 다시 시도해주시기 바랍니다.","System Error",UpdateInfo.this);
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
