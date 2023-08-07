package com.example.serverlogin.sns;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;
import com.example.serverlogin.flg.Profile;
import com.example.serverlogin.login.Login;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProfilelistAapter extends BaseAdapter {
    ArrayList<BoardDto> boardDtoItem = new ArrayList<BoardDto>();
    Context context, othercontext;
    TextView no, content, nick, regdate, comment, recomment, open;
    ImageView chat, thumb, setting;

    @Override
    public int getCount() {
        return boardDtoItem.size();
    }

    @Override
    public Object getItem(int position) {
        return boardDtoItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        context = parent.getContext();
        BoardDto item = boardDtoItem.get(position); // 배열에 저장되어있던 n번째 DTO 객체를 불러온다.

        if(convertView == null){ // 간단하게 말하면 가상 레이아웃을 현실화시키는 느낌?
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // LayoutInflater 사용 준비단계
            convertView = inf.inflate(R.layout.listview_profile,parent,false);
            // ViewGroup 내 있는 자식뷰들을 객체화 시켜 레이아웃에 된
            // 레이아웃을 converView에 복붙.
        }

        // ViewGroup에 속한 View 설정
        CardView profileCard = convertView.findViewById(R.id.profileCard);
        no = convertView.findViewById(R.id.no);
        content = convertView.findViewById(R.id.content);
        nick = convertView.findViewById(R.id.nickName);
        regdate = convertView.findViewById(R.id.regdate);
        comment = convertView.findViewById(R.id.com);
        chat = convertView.findViewById(R.id.chat);
        recomment = convertView.findViewById(R.id.recom);
        thumb = convertView.findViewById(R.id.thumb);
        open = convertView.findViewById(R.id.open);
        setting = convertView.findViewById(R.id.setting);

        no.setText(item.getNo()); // 배열 속 n번째 no 값
        content.setText(item.getContent()); // 배열 속 n번째 Content 값
        nick.setText(item.getNick()); // 배열 속 n번째 Nick 값
        regdate.setText(item.getRegdate()); // 배열 속 n번째 RegDate 값
        comment.setText("댓글"); // 배열 속 n번째 comment 값 지정
        chat.setImageResource(R.drawable.ic_insert_comment_black_24dp);  // 배열 속 n번째 이미지 지정
        recomment.setText(item.getRecom()); // 배열 속 n번째 추천수
        thumb.setImageResource(R.drawable.ic_thumb_up_black_24dp); // 배열 속 n번째 이미지 지정
        open.setText(item.getOpen()); // 배열 속 n번째 open 값
        setting.setImageResource(R.drawable.ic_settings_black_24dp); // 배열 속 n번째 이미지 지정

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // n번째 게시글 댓글 이미지 클릭시 댓글창 정보값과 함께 댓글창 이동
                System.out.println("Chat Click!");
                try {
                    LinkHttp link = new LinkHttp();
                    String path = context.getResources().getString(R.string.getcominfo_url);
                    URL url = link.BoardLink(path,no.getText().toString(),null,null,null,null);
                    Login.id.putString("ChatNo",no.getText().toString()); // session 파일에 ChatNo 키와 게시글 번호 값 추가
                    Login.id.commit(); // 적용
                    new HttpConnection().execute(url);
                } catch (MalformedURLException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // n 번째 게시글의 톱니바퀴 이미지 클릭시

                // 게시글 번호와 내용, 작성시간을 추가하고 적용
                Login.id.putString("BoardNo",no.getText().toString())
                        .putString("BoardContent",content.getText().toString())
                        .putString("BoardRegdate",regdate.getText().toString())
                        .commit();
                // UpdateBoard.class 로 이동
                Intent intent = new Intent(context,UpdateBoard.class);
                context.startActivity(intent);
            }
        });
        // System.out.println("listItemAdapter no: "+no.getText().toString());

        return convertView; // 위에 설정된 값들을 토대로한 레이아웃을 반환.
    }

    public void getOthercontext(Context context){
        this.othercontext = context;
    }

    // 자식뷰 value값 불러온다. (SNS 클래스로 이동)
    public void addItem(BoardDto item){
        boardDtoItem.add(item);
    }

    private class HttpConnection extends AsyncTask<URL, Integer, String>{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!(s.trim().equals("추천추가 성공"))){ // 댓글 이미지 선택시
                Intent intent = new Intent(othercontext,Chat.class);
                intent.putExtra("Info",s);
                othercontext.startActivity(intent);
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