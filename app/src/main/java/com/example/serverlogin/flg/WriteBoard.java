package com.example.serverlogin.flg;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;
import com.example.serverlogin.login.Login;
import com.example.serverlogin.sns.Board;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class WriteBoard extends Fragment {
    private View view;
    String nick,classify;
    TextView nickName;
    EditText content;
    Button writing;
    Spinner kind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.board_write,container,false);
        // board_write xml을 View 객체로 전환시키기 위한 과정.
        // inflater.inflate(View 객체로 전환하고자한 xml, 객체화된 view를 viewgroup에 지정, 부모뷰(Board)에서 작동할 것인지)

       nick = Login.session.getString("userNick","");
        // session 파일에 "userNick"에 관한 값이 존재하면 불러오고 없다면 ""로 반환

        // view에다가 board_write xml에 있는 요소들 지정
        nickName = view.findViewById(R.id.cbnick);
        content = view.findViewById(R.id.cbcontent);
        writing = view.findViewById(R.id.writing);
        kind = view.findViewById(R.id.spinner);

        nickName.setText(nick); // 로그인 된 닋값으로 세팅.

        kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Spinner를 클릭했을 경우. (DB: Open)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classify = parent.getItemAtPosition(position).toString();
                // 선택한 목록? 값을 classify에 저장
                // 0 : 공개, 1 : 비공개
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                classify = parent.getItemAtPosition(0).toString();
                // spinner를 클릭하지 않았을 경우, 0번 위치의 값을 classify에 저장
            }
        });

       content.addTextChangedListener(new TextWatcher() {
           // Content EditText
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               // Content EditText 값이 변경되면 실행
               if(content.getText().toString().length() == 0) {
                   // 변경되기 전 또는 content 값이 null이라면

                   // 쓰기 버튼 비활성화
                   writing.setClickable(false);
                   writing.setBackgroundColor(getResources().getColor(R.color.write_button));
               }else { // 변경되면
                   // 쓰기 버튼 활성화
                   writing.setClickable(true);
                   writing.setBackgroundColor(Color.RED);
                   writing.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           // 업뎃 버튼 클릭시
                           try {
                               // DB에 게시글 정보추가
                               LinkHttp link = new LinkHttp();
                               String path = getResources().getString(R.string.addboard_url);
                               URL url = link.BoardLink(path,null,content.getText().toString(),nick,classify,null);
                               content.setText(""); // content 값은 다시 null로 지정
                               new HttpConnection().execute(url);

                           } catch (MalformedURLException | UnsupportedEncodingException e) {
                               e.printStackTrace();
                           }
                       }
                   });

               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        return view;
        // 세팅된 view(board_write.xml) 객체를 화면에 출력
    }

    private class HttpConnection extends AsyncTask<URL, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.trim().equals("게시 성공")){
                // DB에서 받은 값(공백 제외)이 "게시 성공"이면
                Intent intent = new Intent(view.getContext(),Board.class);
                startActivity(intent); // boardread_xml로 이동
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
