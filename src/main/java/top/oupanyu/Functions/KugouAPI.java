package top.oupanyu.Functions;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.request.Request;

import java.net.URLEncoder;
import java.util.HashMap;


public class KugouAPI {

    private static HashMap<Long,JSONArray> kMusicMap = new HashMap<>();

    public static void getMusic(MessageChain chain, GroupMessageEvent event){
        if (chain.contentToString().contains("&kugou")) {
            try {
            String post = URLEncoder.encode(chain.contentToString().replace("&kugou ",""),"UTF-8");
            String httpResult1 = Request.get("http://mobilecdn.kugou.com/api/v3/search/song?keyword="+post);//从X狗获取JSON对象
            JSONObject obj = JSON.parseObject(JSON.parseObject(httpResult1).getString("data"));
            JSONArray infoArray = obj.getJSONArray("info");
            JSONArray JObjectArray = new JSONArray();
            StringBuilder result = new StringBuilder("获取到的结果为：\n");

            for (int i = 0;i<5;i++){
                JSONObject obj1 = infoArray.getJSONObject(i);
                JObjectArray.add(i,obj1);
                result.append(String.format("%s:%s-%s\n",i,obj1.getString("singername"),obj1.getString("songname")));
                if (i == infoArray.size()-1){
                    break;
                }
            }
            kMusicMap.put(event.getGroup().getId(),JObjectArray);
            result.append("获取音乐地址请输入&kid 数字代号");

            event.getSubject().sendMessage(result.toString()); // 回复消息



            } catch (Exception e) {//错误反馈到群内
                if (e.getMessage().equals("Index 0 out of bounds for length 0")){
                    event.getSubject().sendMessage("出现错误！错误原因：找不到相应的音乐\n控制台输出："+ e.getMessage());
                }
                else {event.getSubject().sendMessage("未知错误！\n错误信息：" + e.getMessage());}
            }
        }
    }

    public static void getMusicURL(GroupMessageEvent event){
        try {
            Integer kid = Integer.valueOf(event.getMessage().contentToString().replace("&kid ",""));
            JSONArray jsonArray = kMusicMap.get(event.getGroup().getId());
            JSONObject musicJSONObj = jsonArray.getJSONObject(kid);
            String singer = musicJSONObj.getString("singername");
            String songName = musicJSONObj.getString("songname");
            String hash = musicJSONObj.getString("hash");
            String httpResult2 = Request.get(String.format("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=%s",hash));//获取下载地址JSON
            String URL = JSON.parseObject(httpResult2).getString("url");

            String message = "歌曲名:"+ singer + "-" + songName +"\n歌曲地址为:"+ URL;
            event.getGroup().sendMessage(message);

        }catch (NumberFormatException ignored){
            event.getGroup().sendMessage("出现错误！只能使用整型参数搜索！");
        }catch (NullPointerException ignored){
            event.getGroup().sendMessage("还没有搜索音乐！请先使用&kugou来搜索音乐");
        }catch (IndexOutOfBoundsException ignored){
            event.getGroup().sendMessage("号码超出范围！请在0-5范围内查找");
        }

    }



    public static void getMV(MessageChain chain, GroupMessageEvent event){
        if (chain.contentToString().contains(".酷狗mv")) {
            try {
                String post = URLEncoder.encode(chain.contentToString().replace(".酷狗mv ",""),"UTF-8");
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
