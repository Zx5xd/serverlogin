package com.example.serverlogin.flg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;
import com.example.serverlogin.sns.BoardDto;
import com.example.serverlogin.sns.listItemAdapter;

import java.net.MalformedURLException;
import java.net.URL;

public class ReadBoard extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    String path; URL url;
    SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.board_read,container,false);
        // board_read xml을 View 객체로 전환시키기 위한 과정.
        // inflater.inflate(View 객체로 전환하고자한 xml, 객체화된 view를 viewgroup에 지정, 부모뷰(Board)에서 작동할 것인지)

        allboard();
        // DB에 저장된 게시글 정보 출력

        swipe = view.findViewById(R.id.swipe);
        // 새로고침 레이아웃
        swipe.setOnRefreshListener(this);
        // 새로고침 영역을 board_read로 지정

        return view;
        // 세팅된 view(board_read.xml) 객체를 화면에 출력
    }

    private void addAdapter(String s){
        // 자신이 작성한 게시글 목록 형성
        // String s는 DB에서 가져온 값

        ListView lv = view.findViewById(R.id.lv);
        // board_read xml에 있는 리스트뷰 연결

        listItemAdapter adapter= new listItemAdapter();
        // 커스텀 리스트뷰 세팅 목적 및 여러 메소드 사용을 위해 객체 생성
        // 커스텀 리스트뷰 어댑터 객체 생성 (DAO 느낌)

        adapter.getOthercontext(view.getContext());
        // chat창으로 이동하기 위해 필요한 Context 제공

        String[] sp = s.trim().split("//.//"); // 1차 분리

        /*
        for(int i=0; i<sp.length; i++){ // DB에서 값이 잘 와졌는지 확인
            System.out.println(sp[i]);
            //spvalue+=sp[i];
        } */

        String spno="",spcontent="", spnick="", sprecom="", spregdate="";
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
           // System.out.println("NoArray: "+NoArray[i]);

            adapter.addItem(new BoardDto(NoArray[i+1],ContentArray[i+1],NickArray[i+1],regDateArray[i+1],RecomArray[i+1], null));
            // addItem()를 통해 각 자리에 해당 값들을 지정
        }
        adapter.notifyDataSetChanged(); // 커스텀 ListView 값 초기화
        lv.setAdapter(adapter); // profile.xml에 있는 ListView에 커스텀리스트뷰 적용

    }

    public void allboard(){
        try {
            // System.out.println("allboard"+getResources().getString(R.string.allboard_url));
            path = getResources().getString(R.string.allboard_url);
            // alboard.jsp 경로
            url = new URL(path); // 경로지정
            new HttpConnection().execute(url); // jsp 연결 시도
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        // SwipeRefreshLayout를 이용하기 위해선 onRefresh()를 이용해야함.

        swipe.setRefreshing(true); // 새로고침 시작
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                allboard(); // 게시글 목록 출력
                swipe.setRefreshing(false); // 새로고침 종료
            }
        },1000); // 1초동안
    }

    private class HttpConnection extends AsyncTask<URL, Integer, String> {

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);

           //  Log.d(this.getClass().getName(), "onPostExecute: "+s);

            addAdapter(s); // 커스텀 리스트뷰 형성

//            if(recyle==false){
//                CardView(s);
//            }else{
//                onRestart();
//            }


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
