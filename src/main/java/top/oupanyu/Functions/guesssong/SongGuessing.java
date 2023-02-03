package top.oupanyu.Functions.guesssong;

import java.sql.SQLException;

public class SongGuessing {
    private String answer = null;
    private String question = null;
    private String content2Chinese = null;

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }
    public SongGuessing(String question ,String answer ,String content2Chinese){
        this.answer = answer;
        this.question = question;
        this.content2Chinese = content2Chinese;
    }
    public SongGuessing(){}

    public String getContent2Chinese() {
        return content2Chinese;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setContent2Chinese(String content2Chinese) {
        this.content2Chinese = content2Chinese;
    }

    public void appendToDatabase() throws SQLException {
        GuessSong.appendVSong(this);
    }
}
