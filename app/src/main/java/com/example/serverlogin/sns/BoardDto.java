package com.example.serverlogin.sns;

public class BoardDto {
    private String no;
    private String nick;
    private String regdate;
    private String recom;
    private String content;
    private String com;
    private String open;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getRecom() {
        return recom;
    }

    public void setRecom(String recom) {
        this.recom = recom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BoardDto(String no, String content, String nick, String regdate, String recom, String open) {
        this.no = no;
        this.content = content;
        this.nick = nick;
        this.recom = recom;
        this.regdate = regdate;
        this.open = open;
    }
}
