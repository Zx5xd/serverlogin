package com.example.serverlogin.sns;

public class ChatDto {
    private String no;
    private String regdate;
    private String chat;
    private String nickName;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ChatDto(String no, String chat, String nick, String regdate){
        this.no = no;
        this.chat = chat;
        this.nickName = nick;
        this.regdate = regdate;
    }
}
