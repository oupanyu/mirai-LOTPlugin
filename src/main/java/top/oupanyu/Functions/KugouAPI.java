package top.oupanyu.Functions;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.request.Request;

import java.net.URLEncoder;


public class KugouAPI {
    public static void getMusic(MessageChain chain, GroupMessageEvent event){
        if (chain.contentToString().contains(".音乐搜索")) {
            try {
            String post = URLEncoder.encode(chain.contentToString().replace(".音乐搜索 ",""),"UTF-8");
            String httpResult1 = Request.get("http://mobilecdn.kugou.com/api/v3/search/song?keyword="+post);
            JSONObject obj = JSON.parseObject(JSON.parseObject(httpResult1).getString("data"));
            JSONObject info = obj.getJSONArray("info").getJSONObject(0);
            String hash = info.getString("hash");
            String name = info.getString("filename");
            String httpResult2 = Request.get(String.format("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=%s",hash));
            String URL = JSON.parseObject(httpResult2).getString("url");
            String message = "歌曲名:"+ name +"\n歌曲地址为:"+ URL;
            event.getSubject().sendMessage(message); // 回复消息


            } catch (Exception e) {
                if (e.getMessage().equals("Index 0 out of bounds for length 0")){
                    event.getSubject().sendMessage("出现错误！错误原因：找不到相应的音乐\n控制台输出："+ e.getMessage());
                }
                else {event.getSubject().sendMessage("未知错误！\n错误信息：" + e.getMessage());}
            }
        }
    }
    public static void getMV(MessageChain chain, GroupMessageEvent event){
        if (chain.contentToString().contains(".mv")) {
            try {
                String post = URLEncoder.encode(chain.contentToString().replace(".mv ",""),"UTF-8");
                String httpResult1 = Request.get("http://mobilecdn.kugou.com/api/v3/search/song?keyword="+post);
                JSONObject obj = JSON.parseObject(JSON.parseObject(httpResult1).getString("data"));
                JSONObject info = obj.getJSONArray("info").getJSONObject(0);
                String hash = info.getString("hash");
                String name = info.getString("filename");
                String httpResult2 = Request.get(String.format("http://m.kugou.com/app/i/mv.php?cmd=100&hash="+hash+"&ismp3=1&ext=mp4"));
                String URL = JSON.parseObject(httpResult2).getString("url");
                String message = "歌曲名:"+ name +"\nMV地址为:"+ URL;
                if (URL == null){event.getSubject().sendMessage("错误：找不到相应的MV");}
                    else {event.getSubject().sendMessage(message); }// 回复消息


            } catch (Exception e) {
                if (e.getMessage().equals("Index 0 out of bounds for length 0")){
                    event.getSubject().sendMessage("出现错误！错误原因：找不到相应的音乐或MV\n控制台输出："+ e.getMessage());
                }
                else {event.getSubject().sendMessage("未知错误！\n错误信息：" + e.getMessage());}
            }
        }

    }
}
