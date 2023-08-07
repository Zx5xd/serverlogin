package com.example.serverlogin.sns;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ChatItemAdapter extends BaseAdapter {
    ArrayList<ChatDto> chatItem = new ArrayList<ChatDto>();
    Context context;
    @Override
    public int getCount() {
        return chatItem.size();
    }

    @Override
    public Object getItem(int position) {
        return chatItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        context = parent.getContext(); // ViewGroup의 Context 지정
        ChatDto item = chatItem.get(position); // 배열에 저장되어있던 n번째 DTO 객체를 불러온다.

        if(convertView == null){ // 간단하게 말하면 가상 레이아웃을 현실화시키는 느낌?
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // LayoutInflater 사용 준비단계
            convertView = inf.inflate(R.layout.listview_chat,parent,false);
            // ViewGroup 내 있는 자식뷰들을 객체화 시켜 레이아웃에 된
            // 레이아웃을 converView에 복붙.
        }

        // ViewGroup에 속한 View 설정
        final TextView no = convertView.findViewById(R.id.no);
        final TextView chat = convertView.findViewById(R.id.chat);
        final TextView nick = convertView.findViewById(R.id.nickName);
        final TextView regdate = convertView.findViewById(R.id.regdate);

        no.setText(item.getNo()); // 배열 속 n번째 no 값
        nick.setText(item.getNickName()); // 배열 속 n번째 Nick 값
        regdate.setText(item.getRegdate()); // 배열 속 n번째 RegDate 값
        chat.setText(item.getChat()); // 배열 속 n번째 Chat 값

        // 추천 이미지 클릭시 addRecom.jsp(추천수 증가페이지) 이동

        // System.out.println("listItemAdapter no: "+no.getText().toString());

        return convertView; // 위에 설정된 값들을 토대로한 레이아웃을 반환.
    }

    // 자식뷰 value값 불러온다. (SNS 클래스로 이동)
    public void addItem(ChatDto item){
        chatItem.add(item); // 받아온 DB 값들을 DTO에 저장 후 chatItem(ArrayList)에 저장
    }

}
