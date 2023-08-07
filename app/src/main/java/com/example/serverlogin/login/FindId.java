package com.example.serverlogin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class FindId extends AppCompatActivity {

    EditText nick_etc;
    Button find;
    URL url;
    private String get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_findid);

        nick_etc = findViewById(R.id.nick_etc);
        find = findViewById(R.id.btn_send);

        
        final LinkHttp link = new LinkHttp();

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭시 findId.jsp 연결
                try {
                    url = link.LinkHttp(getResources().getString(R.string.findid_url),null,null,nick_etc.getText().toString());
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
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            DefaultAlertBuild defaultalert = new DefaultAlertBuild();
            super.onPostExecute(s);
            if (s.trim().equals("아이디찾기 실패")) {
                defaultalert.DefaultAlert(s.trim(),s.trim(),FindId.this);
            } else {
               get = s.trim();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FindId.this);
                alertBuilder.setTitle("Find ID").setMessage(get);
                alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
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
