package com.example.serverlogin.sns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.serverlogin.DefaultAlertBuild;
import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;
import com.example.serverlogin.login.Login;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class Chat extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    String s; String[] sp;
    Button btn_send; boolean click = false; // click은 댓글 입력 여부를 표시한 것
    EditText chat_etc;
    SwipeRefreshLayout swipe;
    LinkHttp link = new LinkHttp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_chatscreen);

        // board_chatscreen의 요소와 연결
        btn_send = findViewById(R.id.btn_send);
        chat_etc = findViewById(R.id.chat_etc);
        swipe = findViewById(R.id.swipe); // 새로고침을 위해 사용
        swipe.setOnRefreshListener(this);



        Intent getintent = getIntent();
        s = getintent.getStringExtra("Info");
        // ReadBoard 또는 Profile에서 게시글의 댓글 이미지를 클릭하면
        // DB와 연결하여 해당 게시글 및 댓글 정보를 불러오고 그 값을 intent로 전송한 것.

        sp = s.trim().split("___");
        // 게시글과 댓글을 분리하기 위함

        System.out.println("sp[0]: "+sp.length);
        //System.out.println("sp[1]: "+sp[1]);

        if(sp.length>1){ // 게시글과 댓글이 같이 불러와졌다면
            addInfoAdapter(sp[0]); // 게시글 전용 커스텀 리스트뷰 형성
            addChatAdapter(sp[1]); // 댓글 전용 커스텀 리스트뷰 형성
        }else { // 게시글만 불러와졌다면
            addInfoAdapter(sp[0]); // 게시글 전용 커스텀 리스트뷰 형성
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 챗 추가 버튼 클릭시
                click = true; // 새로고침 필요
                onRefresh(); // 새로고침
            }
        });

    }
    private void addInfoAdapter(String s){ // 게시글 전용 커스텀 리스트뷰 형성 메소드
        // String s는 DB에서 가져온 게시글 정보
        ListView lv = findViewById(R.id.infoView);
        listItemAdapter adapter= new listItemAdapter();
        // 커스텀 리스트뷰 세팅 목적으로 전용 객체 생성
        //adapter.getOthercontext(this);
        String[] sp = s.trim().split("//.//"); // 1차 분리

      /*  for(int i=0; i<sp.length; i++){
            System.out.println(sp[i]);
            //spvalue+=sp[i];
        }
        */
        String spno="",spcontent="", spnick="", sprecom="", spregdate="";
        // 1차 분리한 것을 분배하기 위해 준비

        for(int i=0; i<sp.length; i++){ // 1차 분배 시작
            if(sp[i].matches(".*no_.*")){
                // "no_"가 있다면, "no_"와 바로 뒤에 있는 값까지 spno에 저장
                spno += sp[i];
            }
            else if(sp[i].matches(".*cont_.*")){
                // "cont_"가 있다면, "cont_"와 바로 뒤에 있는 값까지 spcontent에 저장
                spcontent += sp[i];
            }else if(sp[i].matches(".*nick_.*")){
                // "nick_"가 있다면, "nick_"와 바로 뒤에 있는 값까지 spnick에 저장
                spnick += sp[i];
            }else if(sp[i].matches(".*recom_.*")){
                // "recom_"가 있다면, "recom_"와 바로 뒤에 있는 값까지 sprecom에 저장
                sprecom += sp[i];
            }else if(sp[i].matches(".*reg_.*")){
                // "reg_"가 있다면, "reg_"와 바로 뒤에 있는 값까지 spregdate에 저장
                spregdate += sp[i];
            }
        }

        // 2차 분리 및 2차 분배
        String[] NoArray = spno.split("no_");
        String[] ContentArray = spcontent.split("cont_");
        String[] NickArray = spnick.split("nick_");
        String[] RecomArray = sprecom.split("recom_");
        String[] regDateArray = spregdate.split("reg_");

        for(int i=0; i<NoArray.length-1; i++){
            // 2차 분배까지 완료된 DB값들을 커스텀 리스트뷰에 지정
            System.out.println("NoArray: "+NoArray[i]);
            adapter.addItem(new BoardDto(NoArray[i+1],ContentArray[i+1],NickArray[i+1],regDateArray[i+1],RecomArray[i+1], null));
            // addItem()를 통해 각 자리에 해당 값들을 지정
        }
        adapter.notifyDataSetChanged(); // 커스텀 ListView 값 초기화
        lv.setAdapter(adapter); // board_chatscreen.xml에 있는 ListView에 커스텀리스트뷰 적용
    }

    private void addChatAdapter(String s){ // 댓글 전용 커스텀 리스트뷰 형성
        // String s는 DB에서 가져온 댓글 정보

        ListView lv = findViewById(R.id.chatview);
        lv.setVisibility(View.VISIBLE); // 댓글 정보가 있다면 리스트뷰 활성화
        ChatItemAdapter adapter= new ChatItemAdapter();
        // 댓글 전용 커스텀 리스트뷰 세팅 목적으로 객체 생성
        //adapter.getOthercontext(this);
        String[] sp = s.trim().split("//.//"); // 1차 분리

//        for(int i=0; i<sp.length; i++){
//            System.out.println(sp[i]);
//            //spvalue+=sp[i];
//        }
        String spno="",spchat="", spnick="", spregdate="";
        // 1차 분배를 위한 준비

        for(int i=0; i<sp.length; i++){ // 1차 분배 시작
            if(sp[i].matches(".*no_.*")){
                // "no_"가 있다면, "no_"와 바로 뒤에 있는 값까지 spno에 저장
                spno += sp[i];
            }
            else if(sp[i].matches(".*chat_.*")){
                // "chat_"가 있다면, "chat_"와 바로 뒤에 있는 값까지 spchat에 저장
                spchat += sp[i];
            }else if(sp[i].matches(".*nick_.*")){
                // "nick_"가 있다면, "nick_"와 바로 뒤에 있는 값까지 spnick에 저장
                spnick += sp[i];
            }else if(sp[i].matches(".*reg_.*")){
                // "reg_"가 있다면, "reg_"와 바로 뒤에 있는 값까지 spregdate에 저장
                spregdate += sp[i];
            }
        }

        // 2차 분리 및 2차 분배
        String[] NoArray = spno.split("no_");
        String[] ChatArray = spchat.split("chat_");
        String[] NickArray = spnick.split("nick_");
        String[] regDateArray = spregdate.split("reg_");

//        for(int i=0; i<NoArray.length; i++){ // Test
//            System.out.println("NoArray["+i+"]: "+NoArray[i]);
//            System.out.println("ChatArray["+i+"]: "+ChatArray[i]);
//            System.out.println("NickArray["+i+"]: "+NickArray[i]);
//            System.out.println("regDateArray["+i+"]: "+regDateArray[i]);
//        }

        for(int i=0; i<NoArray.length-1; i++){
            // 2차 분배까지 완료된 DB값들을 커스텀 리스트뷰에 지정
           //  System.out.println("NoArray: "+NoArray[i]);

            adapter.addItem(new ChatDto(NoArray[i+1],ChatArray[i+1],NickArray[i+1],regDateArray[i+1]));
            // addItem()를 통해 각 자리에 해당 값들을 지정
        }
        adapter.notifyDataSetChanged(); // 커스텀 ListView 값 초기화
        lv.setAdapter(adapter); // profile.xml에 있는 ListView에 커스텀리스트뷰 적용
    }

    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // 로그인한 id와 게시글 번호를 불러온다.
                String id = Login.session.getString("userId","");
                String chatno = Login.session.getString("ChatNo","");
//                System.out.println("userId session:"+id);
//                System.out.println("ChatNo session:"+chatno);
                if(click == true){ // 댓글 입력 됬다면
                    if(id.equals("")) {
                        DefaultAlertBuild alert = new DefaultAlertBuild();
                        alert.ChageView("오류발생","Error",Chat.this, Login.class);
                        Login.id.clear();
                        Login.id.commit();
                    }else{
                        try { click = false; // click은 입력 전으로 리셋 후 DB에 새로운 채팅 정보 추가
                            URL url = link.BoardLink(getResources().getString(R.string.setcominfo_url),chatno,chat_etc.getText().toString(),null,null,id);// id를 no대신 전송
                            new HttpConnection().execute(url);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                } else{ // 일반적인 새로고침이라면 게시글 정보 및 댓글 정보를 불러온다.
                    try {
                        URL url = link.BoardLink(getResources().getString(R.string.getcominfo_url),chatno,null,null,null,null);
                        new HttpConnection().execute(url);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

                swipe.setRefreshing(false);
            }
        },1000); // 1초 동안 윗 값들 진행
    }

    private class HttpConnection extends AsyncTask<URL, Integer, String> {

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            // 새로고침으로 인해 발생.
            // 커스텀 리스트뷰 재형성 시작
            sp = s.trim().split("___");

            if(sp.length>1){ // 일반적인 새로고침
                addInfoAdapter(sp[0]);
                addChatAdapter(sp[1]);
            }else { // 댓글 입력으로 인한 새로고침
                addInfoAdapter(sp[0]);
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
