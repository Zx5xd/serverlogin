package com.example.serverlogin.flg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;
import com.example.serverlogin.login.Login;
import com.example.serverlogin.sns.BoardDto;
import com.example.serverlogin.sns.ProfilelistAapter;
import com.example.serverlogin.login.UpdateInfo;
import com.example.serverlogin.sns.UpdateBoard;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class Profile extends Fragment {

    private View Rootview;
    TextView profileNick;
    Button userInfo, logout;
    LinkHttp link = new LinkHttp();
    String path; URL url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Rootview = inflater.inflate(R.layout.profile, container,false); // 플래그먼트 생성
        // profile xml을 View 객체로 전환시키기 위한 과정.
        // inflater.inflate(View 객체로 전환하고자한 xml, 객체화된 view를 viewgroup에 지정, 부모뷰(Board)에서 작동할 것인지)

        String nick = Login.session.getString("userNick","");
        // session 파일에 "userNick"에 관한 값이 존재하면 불러오고 없다면 ""로 반환

        // Rootview에다가 profile xml에 있는 요소들 지정
        profileNick = Rootview.findViewById(R.id.profileNick);
        userInfo = Rootview.findViewById(R.id.userInfo);
        logout = Rootview.findViewById(R.id.logout);

        if(nick != null)
            // 저장되어 있던 nick값이 존재하면

        profileNick.setText(nick);

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Profile.xml에서 톱니바퀴 그림을 클릭하면

                Intent intent = new Intent(Rootview.getContext(), UpdateInfo.class);
                startActivity(intent);
                // UpdateInfo(유저정보관리).class로 이동
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃 버튼 클릭시

                Login.id.clear();
                // Login 클래스의 SharedPreferences.Edit인 id 목록을 초기화
                // 즉, id를 통해 저장한 정보들을 초기화시키는 것
                Login.id.commit();
                // 적용

            Intent intent = new Intent(Rootview.getContext(),Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            // TASK 목록 초기화
            startActivity(intent);
            // Login 창으로 이동
            }
        });

        try{
                path = getResources().getString(R.string.profile_url);
                // profile.jsp 경로

                url = link.BoardLink(path, null,null,nick,null,null);
                // profile.jsp로 보낼 데이터를 get방식으로 url 세팅

            new HttpConnection().execute(url);
            // url 값을 HttpConnection() 메소드로 이동
            // 즉, doInBackground()로 url 값을 전송하면서 호출
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return Rootview;
        // 세팅된 Rootview(Profile.xml) 객체를 화면에 출력
    }
    private void InfoAdapter(String s){
        // 자신이 작성한 게시글 목록 형성
        // String s는 DB에서 가져온 값

        ListView lv = Rootview.findViewById(R.id.myboard);
        // profile.xml에 있는 listview 연결

        final ProfilelistAapter adapter= new ProfilelistAapter();
        // 커스텀 리스트뷰 세팅 목적 및 여러 메소드 사용을 위해 객체 생성
        // 커스텀 리스트뷰 어댑터 객체 생성 (DAO 느낌)

        adapter.getOthercontext(Rootview.getContext());
        // Chat 이미지 클릭시 intent로 이동하기 위한 Context 제공

        String[] sp = s.trim().split("//.//");
        // DB에서 가져온 값의 공백을 없애준 후 1차 분리

       /* for(int i=0; i<sp.length; i++){ // Test
            System.out.println(sp[i]);
            //spvalue+=sp[i];
        }*/

        String spno="",spcontent="", spnick="", sprecom="", spregdate="", spopen="";
        // 1차 분리한 값 저장을 위한 객체 생성

        for(int i=0; i<sp.length; i++){ // 1차 분배
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
            } else if (sp[i].matches((".*open_.*"))) {
                // "open_"가 있다면, "open_"과 바로 뒤에 있는 값까지 spopen에 저장
                spopen += sp[i];
            }
        }

        // 2차 분리 및 2차 분배
        String[] NoArray = spno.split("no_");
        String[] ContentArray = spcontent.split("cont_");
        String[] NickArray = spnick.split("nick_");
        String[] RecomArray = sprecom.split("recom_");
        String[] regDateArray = spregdate.split("reg_");
        String[] OpenArray = spopen.split("open_");

        for(int i=0; i<NoArray.length-1; i++){
            // 2차 분배까지 완료된 DB값들을 커스텀 리스트뷰에 지정
            // System.out.println("NoArray: "+NoArray[i]);
            adapter.addItem(new BoardDto(NoArray[i+1],ContentArray[i+1],NickArray[i+1],regDateArray[i+1],RecomArray[i+1],OpenArray[i+1]));
            // addItem()를 통해 각 자리에 해당 값들을 지정
        }

        adapter.notifyDataSetChanged(); // 커스텀 ListView 값 초기화
        lv.setAdapter(adapter); // profile.xml에 있는 ListView에 커스텀리스트뷰 적용

    }
            private class HttpConnection extends AsyncTask<URL, Integer, String> {

                @Override
                protected void onPostExecute(final String s) {
                    super.onPostExecute(s);

                    InfoAdapter(s); // 커스텀 리스트뷰 형성
                  //   Log.d(this.getClass().getName(), "onPostExecute: " + s);


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

