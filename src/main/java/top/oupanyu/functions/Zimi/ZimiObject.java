package top.oupanyu.functions.Zimi;

import com.google.gson.Gson;
import top.oupanyu.Main;
import top.oupanyu.request.Request;

import java.util.Arrays;
import java.util.List;

public class ZimiObject {
    private String answer;
    private String reason;
    private String question;

    public int chances = 5;

    public ZimiObject(){}

    public String getAnswer(){return this.answer;}

    public String getReason(){return this.reason;}

    public String getQuestion(){return this.question;}

    public int getChances(){return this.chances;}

    public List<newslist> newslist;
    public class newslist{
        public String content;
        public String answer;
        public String reason;
    }

    public void getZimi(){
        try {
            String result = Request.get("https://api.tianapi.com/zimi/index?key="+ Main.configloader.getTiankey());
            ZimiObject obj = new Gson()
                    .fromJson(result, ZimiObject.class);
            newslist newslist = obj.newslist.get(0);
            this.question = newslist.content;
            this.answer = newslist.answer;
            this.reason = newslist.reason;

        }catch (Exception e){
            this.answer = this.reason = this.question ="出现错误！请联系开发者,错误原因:\n" + Arrays.toString(e.getStackTrace());
        }
    }
}
