package com.example.serverlogin;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class RequestHttpURLConnection {
    public String request(URL url) {
        HttpURLConnection urlConnection = null;
        Log.d(this.getClass().getName(), "request: "+url.toString());
        try{
            urlConnection = (HttpURLConnection)url.openConnection(); // url 연결
            urlConnection.setRequestMethod("GET"); // 데이터 전송 방식
            urlConnection.setReadTimeout(3000); // 최대 읽기시간
            urlConnection.setConnectTimeout(3000); // 최대 연결시간

           String page = ""; // jsp에서 불러온 값을 저장하는 객체
           try(BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"))){
               // UTF-8 형식으로 url 연결된 페이지에서 출력된 값을 BufferReader 객체인 reader에 저장
               String line;
               while((line = reader.readLine()) != null){
                   // reader에 읽을 수 있는 line이 존재하면, 존재한 값을 page에 저장
                   page += line;
               }
               return page;
           }catch(Exception e){
               e.printStackTrace();
           }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
