package top.oupanyu.functions;

import com.google.gson.Gson;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.Main;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.request.Request;

import java.util.List;

public class RandomPoem implements GroupMessageExecuter {
    public List<newslist> newslist;

    @Override
    public void onRun(GroupMessageEvent event) {
        getRandomPoem(event);
    }

    public class newslist{
        public String content;
        public String source;
    }


    public static void getRandomPoem(GroupMessageEvent event){
        MessageChain chain = event.getMessage();
        if (chain.contentToString().equals(".来首词")){
            String httpResult = Request.get("http://api.tianapi.com/zmsc/index?key=" + Main.configloader.getTiankey());
            List<newslist> obj = new Gson()
                    .fromJson(httpResult,RandomPoem.class)
                    .newslist;
            try {
                String poem = obj.get(0).content;
                String author = obj.get(0).source;
                event.getSubject().sendMessage(poem + "\n" + author); // 回复消息
            }catch (Exception e){
                event.getSubject().sendMessage("出现错误！错误信息："+ e.getMessage());
            }
    }
}
}
