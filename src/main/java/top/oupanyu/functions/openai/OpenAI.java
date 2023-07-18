package top.oupanyu.functions.openai;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OpenAI {
    public class messages{
        public String role = "user";
        public String content;
        public messages(String sender, String content){
            role = sender;
            this.content = content;
        }
        public messages(String content){
            this.content = content;
        }
    }
    private final String model = "gpt-3.5-turbo";
    public List<messages> messages = new ArrayList<>();

    private void update(){
        if (messages.size() >= 2){
            messages.remove(0);
        }
    }

    public void reset(){
        messages.clear();
    }

    public void addAsk(String content) {

        messages.add(new messages(content));
        update();
    }
    public void addAnswer(String answer){
        messages.add(new messages("assistant",answer));
        update();
    }
    public String getLastAnswer(){
        Gson gson = new Gson();
        OpenAIResponse openAIResponse = gson.fromJson(messages.get(messages.size()-1).content, OpenAIResponse.class);
        return openAIResponse.choices.get(0).message.content;
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
