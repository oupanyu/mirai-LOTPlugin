package top.oupanyu.functions.baike.baidu;

import com.google.gson.Gson;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.request.Request;

import java.io.IOException;
import java.net.URLEncoder;

public class BaiduBaike implements GroupMessageExecuter {
    public static void search(GroupMessageEvent event){
        try {
            String key = event.getMessage().contentToString().replace(".baike","");
            String url = String.format("https://baike.baidu.com/api/openapi/BaikeLemmaCardApi?format=json&appid=379020&bk_key=%s&bk_length=600"
                                            , URLEncoder.encode(key,"UTF-8"));
            String response = Request.get(url);
            Gson gson = new Gson();
            Baike baike = null;
            baike = gson.fromJson(response, Baike.class);
            baike.sendImage(event);
        } catch (IOException e) {
            event.getGroup().sendMessage("处理时出现错误！错误内容：\n"+e.getMessage()+"\n请稍后再试！若多次尝试仍然报错请联系管理员");
            e.printStackTrace();
        }

    }

    @Override
    public void onRun(GroupMessageEvent event) {
        search(event);
    }
}
