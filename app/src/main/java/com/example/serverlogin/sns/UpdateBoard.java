package com.example.serverlogin.sns;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.serverlogin.DefaultAlertBuild;
import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.RequestHttpURLConnection;
import com.example.serverlogin.login.Login;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateBoard extends AppCompatActivity {

    String getContent, getNo, getNick, getRegdate, classify;
    EditText Content; TextView Nick, Regdate;
    ImageView BoardDelete;
    Spinner spinner;
    Button updating;
    DefaultAlertBuild alert = new DefaultAlertBuild();
    LinkHttp link = new LinkHttp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_update);

        getContent = Login.session.getString("BoardContent","");
        // session 파일의 BoardContent 키와 매칭 되는 값 반환
        getNick = Login.session.getString("userNick","");
        // session 파일의 userNick 키와 매칭 되는 값 반환
        getNo = Login.session.getString("BoardNo","");
        // session 파일의 BoardNo 키와 매칭 되는 값 반환
        getRegdate = Login.session.getString("BoardRegdate","");
        // session 파일의 BoardRegdate 키와 매칭 되는 값 반환

        // board_update xml의 요소와 연결
        Content = findViewById(R.id.ubcontent);
        Nick = findViewById(R.id.ubnick);
        updating = findViewById(R.id.updating);
        Regdate = findViewById(R.id.regdate);
        BoardDelete = findViewById(R.id.BoardDelete);

        if(getNick != null){ // 로그인 정보가 남아있다면
            Nick.setText(getNick);
        }else{ // 남아 있지 않으면 흔적을 남기지 않고 로그인 창으로 이동
            Intent intent = new Intent(getApplicationContext(),Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if(getNo == null){ // 받아온 게시글 번호가 null이면
            alert.ChageView("에러가 발생했습니다.\n앱 종료 후 다시 시도해주세요.","Error",UpdateBoard.this,Board.class);
        }else{
            if(getContent != null | getRegdate != null) {
                Content.setText(getContent);
                Regdate.setText(getRegdate);
            }else{
                alert.DefaultAlert("죄송합니다.\n기록 불러오는데 실패하였습니다.\n그러나 수정은 진행하셔도 됩니다.","Error",UpdateBoard.this);
            }
        }

        spinner = findViewById(R.id.spinner); // open 설정
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classify = spinner.getItemAtPosition(position).toString();
                // n번째 를 선택할 시 classify에 저장. 0: 공개, 1: 비공개
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                classify = spinner.getItemAtPosition(0).toString();
                // spinner를 클릭하지 않았다면 classify에 "비공개" 저장
            }
        });

        Content.addTextChangedListener(new TextWatcher() {
            // Content 내용이 변경되면
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Content.getText().toString().length() == 0) {
                    // 아직 수정 전이거나 Content 값을 null로 만들었다면

                    // 업데이트 버튼 비활성화
                    updating.setClickable(false);
                    updating.setBackgroundColor(getResources().getColor(R.color.write_button));
                }else {
                    // 업데이트 버튼 활성화
                    updating.setClickable(true);
                    updating.setBackgroundColor(Color.RED);
                    updating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 업데이트 버튼 클릭시 게시글 업데이트 진행
                            try {

                                String path = getResources().getString(R.string.updateboard_url);
                                URL url = link.BoardLink(path,getNo,Content.getText().toString(),null,classify,null);
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

        BoardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 빨간색 X 이미지 클릭시 Alert 재생
                System.out.println("BoardDelete onClick");
                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(UpdateBoard.this);
                deleteBuilder.setTitle("삭제하시겠습니까?");
                deleteBuilder.setMessage("삭제하시면 해당 글을 다시 복구하실 수 없습니다.\n정말로 삭제하시겠습니까?");
                deleteBuilder.setPositiveButton("취소", null);
                deleteBuilder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 확인 클릭시 해당 게시글 삭제 진행
                        try{
                            String path = getResources().getString(R.string.deleteboard_url);
                            URL url = link.BoardLink(path,getNo,null,null,null,null);
                            new HttpConnection().execute(url);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                AlertDialog deleteDialog = deleteBuilder.create();
                deleteDialog.show();
            }

        });

    }

    private class HttpConnection extends AsyncTask<URL, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.trim().equals("수정 성공")){ // 수정 성공했다면
                Login.id.remove("BoardNo")
                        .remove("BoardContent")
                        .remove("BoardRegdate")
                        .commit(); // session 파일 내에 위와 관련된 키들은 지우고 적용시킨다.
                alert.ChageView("게시글 수정이 성공적으로 완료되었습니다.","게시글 수정 성공",UpdateBoard.this,Board.class);
                // board_read.xml로 이동
            }else if(s.trim().equals("삭제 성공")){ // 삭제 성공했다면
                Login.id.remove("BoardNo")
                        .remove("BoardContent")
                        .remove("BoardRegdate")
                        .commit(); // session 파일 내에 위와 관련된 키들은 지우고 적용시킨다.
                alert.ChageView("게시글 삭제가 성공적으로 완료되었습니다.","게시글 삭제 성공",UpdateBoard.this,Board.class);
                // board_read.xml로 이동
            } else{
                alert.DefaultAlert("System Error", "Error", UpdateBoard.this);
                Intent intent = new Intent(UpdateBoard.this, Board.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // 모든 Task를 지우고 board_read.xml로 이동
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            String data = "";

            if(urls.length == 0)
                return "URL is Empty";

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
