package com.example.serverlogin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.LoadingActivity;
import com.example.serverlogin.RequestHttpURLConnection;
import com.example.serverlogin.sns.Board;
import com.example.serverlogin.R;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {

   public static SharedPreferences session;
   // 변수를 앱 내의 모든 클래스에서 이용할 수 있도록 저장 및 공유를 지원하는 객체
   // 파일을 생성하여 거기에다가 특수 키와 변수값을 매칭하여 저장한 형태로
   // 사용할 때도 특수이름을 통해 변수값을 불러온다.

   public static SharedPreferences.Editor id;
   // 생성한 파일에다가 변수를 수정 및 추가하기 위한 객체

    TextView findId, findPwd, join;
    EditText id_etc, pwd_etc;
    Button btn_send;
    Switch autologin;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Intent intent = new Intent(this, LoadingActivity.class);//여기에 로딩클래스 불러오기
        startActivity(intent);*/

        final LinkHttp link = new LinkHttp();
        // 코딩 최소화 하기 위해 생성한 class
        // url 정리를 위한 객체

        // activity_main에 지정된 요소 연결
        id_etc = findViewById(R.id.id_etc); pwd_etc = findViewById(R.id.pwd_etc);
        btn_send = findViewById(R.id.btn_send);
        autologin = findViewById(R.id.autologin);
        findId = findViewById(R.id.findId); findPwd = findViewById(R.id.findPwd);
        join = findViewById(R.id.join);

       // final String filename = "autologinText.txt";
       // final String filePath = getApplicationContext().getFilesDir().getPath().toString() + filename;


        if((session = getApplicationContext().getSharedPreferences("session",MODE_PRIVATE))
                .getString("autoLoginId","") != ""){
            // session 파일에 autoLoginId란 이름을 가진 변수값이 null이 아니라면
            try {
                // session 파일에 저장된 id와 password로 로그인
                url = link.LinkHttp(getResources().getString(R.string.login_url), session.getString("autoLoginId",""),
                        session.getString("autoLoginPwd",""), null);
                new HttpConnection().execute(url);
            } catch (MalformedURLException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        btn_send.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭시
                    try {
                        // id_etc에 적힌 값과 pwd_etc에 적힌 값으로 로그인 시도
                        url = link.LinkHttp(getResources().getString(R.string.login_url), id_etc.getText().toString(), pwd_etc.getText().toString(), null);
                        new HttpConnection().execute(url);
                    } catch (MalformedURLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "회원가입" 클릭시 회원가입 창 이동
                Intent intent = new Intent(getApplicationContext(),Join.class);
                startActivity(intent);

            }
        });

        findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "아이디찾기" 클릭시 아이디찾기 창 이동
                Intent intent = new Intent(getApplicationContext(),FindId.class);
                startActivity(intent);
            }
        });

        findPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "비밀번호찾기" 클릭시 비밀번호찾기 창 이동
                Intent intent = new Intent(getApplicationContext(),FindPwd.class);
                startActivity(intent);
            }
        });
    }


    private class HttpConnection extends AsyncTask<URL, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.trim().equals("로그인 실패")) {
                Toast.makeText(getApplicationContext(), "일치하는 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                session = getSharedPreferences("session",MODE_PRIVATE);  // 파일이름 및 모드 설정
                id = session.edit(); // 데이터 저장 및 수정 객체
                // session 파일의 edit 역할 수행
                id.putString("userId",id_etc.getText().toString()) // "userId"란 특수 키와 id_etc 값 추가
                  .putString("userNick",s.trim()).commit(); // "userNick"이란 특수 키와 DB에서 불러온 Nick 추가하고 적용

                if(autologin.isChecked() == true) {
                    // switch가 on 되어있다면
                  id.putString("autoLoginId", id_etc.getText().toString()) // "autoLoginId"란 특수 키와 id_etc 값 추가
                    .putString("autoLoginPwd", pwd_etc.getText().toString()) // "autoLoginPwd"란 특수 키와 pwd_etc 값 추가
                    .commit(); // 적용
                }

                // Log.d(this.getClass().getName(), "onPostExecute login: "+s); // 일종의 System.out.println
                Toast.makeText(getApplicationContext(), s.trim() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Board.class);
                startActivity(intent); // Board.class(board_read.xml)로 이동
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

