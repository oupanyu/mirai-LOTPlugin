package top.oupanyu.Functions;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.Functions.API.APIConfig;
import top.oupanyu.request.Request;

public class RandomPoem {
    public static void getRandomPoem(MessageChain chain, GroupMessageEvent event){
        if (chain.contentToString().equals(".来首词")){
            String httpResult = Request.get("http://api.tianapi.com/zmsc/index?key=" + APIConfig.TianAPIKey);
            JSONArray obj = JSON.parseObject(httpResult).getJSONArray("newslist");
            try {
                String poem = JSON.parseObject(obj.getString(0)).getString("content");
                String author = JSON.parseObject(obj.getString(0)).getString("source");
                event.getSubject().sendMessage(poem + "\n" + author); // 回复消息
            }catch (Exception e){
                event.getSubject().sendMessage("出现错误！错误信息："+ e.getMessage());
            }
    }
}
}
