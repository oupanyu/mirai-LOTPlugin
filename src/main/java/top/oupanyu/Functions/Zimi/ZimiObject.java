package top.oupanyu.Functions.Zimi;

import com.alibaba.fastjson2.JSONObject;
import top.oupanyu.Main;
import top.oupanyu.request.Request;

import java.util.Arrays;

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

    public void getZimi(){
        try {
            String result = Request.get("https://api.tianapi.com/zimi/index?key="+ Main.configloader.getTiankey());
            JSONObject obj = JSONObject.parseObject(result);
            JSONObject newslist = obj.getJSONArray("newslist").getJSONObject(0);
            this.question = newslist.getString("content");
            this.answer = newslist.getString("answer");
            this.reason = newslist.getString("reason");

        }catch (Exception e){
            this.answer = this.reason = this.question ="出现错误！请联系开发者,错误原因:\n" + Arrays.toString(e.getStackTrace());
        }
    }
}
