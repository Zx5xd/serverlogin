package com.example.serverlogin.sns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.serverlogin.LinkHttp;
import com.example.serverlogin.R;
import com.example.serverlogin.flg.ReadBoard;
import com.example.serverlogin.flg.WriteBoard;
import com.example.serverlogin.flg.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Board extends AppCompatActivity {

    LinearLayout xmllinear;


    LinkHttp link = new LinkHttp();

    TextView topText;
    BottomNavigationView bn;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    private ReadBoard readBoard;
    private WriteBoard writeBoard;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_main);

       // System.out.println("session: "+ Login.session.getString("userId",""));

//  sc = findViewById(R.id.sc);

        // board_main의 요소와 연결
        bn = findViewById(R.id.bottom); // 하단 네비게이션

        // 프래그먼트 객체 생성
        readBoard = new ReadBoard();
        writeBoard = new WriteBoard();
        profile = new Profile();

        setFrag(0); // 프래그먼트 교체 실행문
        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.reader:
                        // 하단 네비게이션 "글보기"를 클릭했다면 readBoard.class(fragment)로 이동
                        setFrag(0);
                        break;
                    case R.id.action_write:
                        // 하단 네비게이션 "글작성"을 클릭했다면 writeBoard.class(fragment)로 이동
                        setFrag(1);
                        break;
                    case R.id.profile:
                        // 하단 네비게이션 "프로필"을 클릭했다면 Profile.class(fragment)로 이동
                        setFrag(2);
                        break;
                }
                return true;
            }
        });
    }

    // 프래그먼트 교체 실행문.
    public void setFrag(int n){
        topText = findViewById(R.id.topText);
        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction(); // 프래그먼트 교체선언
        switch (n){
            case 0:
                topText.setText("글");
                fragmentTransaction.replace(R.id.main_frame, readBoard);
                // main_frame 영역을 readBoard로 대체
                fragmentTransaction.commit();
                // 적용
                break;
            case 1:
                topText.setText("글 작성");
                fragmentTransaction.replace(R.id.main_frame, writeBoard);
                // main_frame 영역을 writeBoard로 대체
                fragmentTransaction.commit();
                // 적용
                break;
            case 2:
                topText.setText("프로필");
                fragmentTransaction.replace(R.id.main_frame, profile);
                // main_frame 영역을 profile로 대체
                fragmentTransaction.commit();
                // 적용
                break;
        }
    }
}
