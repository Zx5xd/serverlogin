package com.example.serverlogin;

import android.widget.EditText;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LinkHttp{
    // jsp에서 msg로 받는 값들을 정렬하는 클래스
    // 코딩을 최소화하기 위해 생성

    public URL LinkHttp(String getpath, String id_etc, String pwd_etc, String nick_etc) throws MalformedURLException, UnsupportedEncodingException {
        // 클래스 패키지 중 login 패키지에서 사용
        String getquery;
        getpath += "?msg=";
        if(nick_etc == null) {
            if(pwd_etc == null){ // 아이디 중복체크
                getquery = URLEncoder.encode(id_etc, "UTF-8");
            }else{ // 로그인, 비밀번호 변경
                getquery = URLEncoder.encode(id_etc, "UTF-8") + "/" +
                        URLEncoder.encode(pwd_etc, "UTF-8");
            }
        }  else if(pwd_etc==null){
            if(id_etc == null && pwd_etc == null){ // 아이디 찾기
                getquery = URLEncoder.encode(nick_etc,"UTF-8");
            }else{ // 비밀번호 변경 체크
                getquery = URLEncoder.encode(id_etc, "UTF-8") + "/" +
                        URLEncoder.encode(nick_etc,"UTF-8");
            } } else{ // 회원가입, 정보수정
            getquery = URLEncoder.encode(id_etc, "UTF-8") + "/" +
                    URLEncoder.encode(pwd_etc, "UTF-8")+ "/"+
                    URLEncoder.encode(nick_etc,"UTF-8"); }

        URL linkUrl = new URL(getpath+getquery);

        return linkUrl;
    }
    public URL BoardLink(String getpath, String no, String chat, String nick, String open, String id) throws UnsupportedEncodingException, MalformedURLException {
        // 클래스 패키지 중 sns, flg(Board) 패키지에서 사용
        String getquery = null;
        getpath += "?msg=";
        if(no != null){
            if(id == null & open == null){ // 댓글창
                getquery = URLEncoder.encode(no,"UTF-8");
            }else if(open == null){ // 댓글 저장
                getquery = URLEncoder.encode(no,"UTF-8") + "/" +URLEncoder.encode(chat,"UTF-8")+"/"+
                        URLEncoder.encode(id,"UTF-8");
            }else{
                getquery = URLEncoder.encode(no,"UTF-8") + "/" +URLEncoder.encode(chat,"UTF-8")+"/"+
                        URLEncoder.encode(open,"UTF-8");
            }
        } else if(no == null){
            if(chat != null && open != null){ // 글 작성 (chat == content) 및 글 수정
                getquery = URLEncoder.encode(chat,"UTF-8")+"/"+URLEncoder.encode(nick,"UTF-8")+"/"+
                        URLEncoder.encode(open,"UTF-8");
            } else{ // 프로필 게시글 요청
                getquery = URLEncoder.encode(nick,"UTF-8");
            }
        }
        URL linkUrl = new URL(getpath+getquery);
        // System.out.println("linkUrl: "+linkUrl.toString());

        return linkUrl;
    }
}
