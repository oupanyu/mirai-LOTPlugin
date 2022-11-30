package top.oupanyu.Functions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.request.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NeteaseCloudMusic {
    public static void getMusic(MessageChain chain, GroupMessageEvent event){

        String post = null;
        try {
            post = URLEncoder.encode(chain.contentToString().replace(".网易云音乐 ",""),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            SendErrorMessage.send(event,e);
        }
        String httpResult = Request.get("http://cloud-music.pl-fe.cn/search?keywords="+post);
        JSONObject object = JSONObject.parseObject(httpResult);
        JSONObject jsonSong = object.getJSONObject("result").getJSONArray("songs").getJSONObject(0);

        //获取艺术家与歌名开始
        JSONArray artistarray = jsonSong.getJSONArray("artists");
        String artists = "";
        for (int i = 0; i < artistarray.size(); i++) {
            artists += (artistarray.getJSONObject(i).getString("name") + " ");
        }
        String songName = artists + "- " + jsonSong.getString("name");
        if (jsonSong.containsKey("transNames")){
            String transName = "(" + jsonSong.getJSONArray("transNames").getString(0) + ")";
            songName += transName;
        }//结束获取艺术家与歌名

        Long musicID = jsonSong.getLong("id");
        try {
            String musicURL = JSONObject.parseObject(Request.get(String.format("http://cloud-music.pl-fe.cn/song/url?id=%s",musicID)))
                    .getJSONArray("data")
                    .getJSONObject(0)
                    .getString("url");

            String message = "歌曲名:"+ songName +"\n歌曲地址为:"+ musicURL;
            event.getSubject().sendMessage(message);
        }catch (Exception e){
            SendErrorMessage.send(event,e);
        }



    }
    public static void getMV(MessageChain chain, GroupMessageEvent event) {
        String post = null;
        try {
            post = URLEncoder.encode(chain.contentToString().replace(".网易云MV ",""),"UTF-8");//URL编码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String httpResult = Request.get("http://cloud-music.pl-fe.cn/search?keywords="+post);
        //System.out.println(post);
        JSONObject object = JSONObject.parseObject(httpResult);
        //System.out.println(object);
        JSONObject jsonSong = object.getJSONObject("result").getJSONArray("songs").getJSONObject(0);//获取第一首歌曲的JSONObject

        //获取艺术家与歌名开始
        JSONArray artistArray = jsonSong.getJSONArray("artists");
        String artists = "";
        for (int i = 0; i < artistArray.size(); i++) {
            artists += (artistArray.getJSONObject(i).getString("name") + " ");
        }
        String songName = artists + "- " + jsonSong.getString("name");
        if (jsonSong.containsKey("transNames")){//判断是否有译名
            String transName = "(" + jsonSong.getJSONArray("transNames").getString(0) + ")";
            songName += transName;
        }//结束获取艺术家与歌名
        Integer mvID = null;
        if (jsonSong.containsKey("mvid")){//判断是否有MV
            mvID = jsonSong.getInteger("mvid");
            String mvURL = JSONObject.parseObject(Request.get(String.format("https://tenapi.cn/wyymv/?id=%s",mvID)))
                    .getJSONObject("data")
                    .getJSONObject("mv")
                    .getString(String.valueOf(480));
            String message = "歌曲名:"+ songName +"\nMV地址为:"+ mvURL;
            event.getSubject().sendMessage(message);
        }else {
            event.getSubject().sendMessage("看起来这个音乐没有MV哦");
        }





    }

}
