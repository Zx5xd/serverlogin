package com.example.serverlogin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class DefaultAlertBuild {
            // Alert 코딩을 최소화하기 위해 작성된 클래스
    public void DefaultAlert(String s, String title, final Context con){
        // 일반적인 Alert창
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(con);
        alertBuilder.setTitle(title);

        alertBuilder.setMessage(s);
        alertBuilder.setPositiveButton("확인",null);

        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public void ChageView(final String s, String title, final Context con, final Class nextcon){
        // 화면 이동이 필요할 때 사용하는 Alert창
        final String[] split = s.split("/");

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(con);
        alertBuilder.setTitle(title);

        if(split.length > 1 ){
                alertBuilder.setMessage(split[0]);
            alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(con,nextcon);
                    intent.putExtra("info1",s);
                    con.startActivity(intent);
                }
            });
        }else{
            alertBuilder.setMessage(s);
            alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(con,nextcon);
                    con.startActivity(intent);
                }
            });
        }


        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}
