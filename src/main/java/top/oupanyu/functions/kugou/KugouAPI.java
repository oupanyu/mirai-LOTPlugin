package top.oupanyu.functions.kugou;

import com.google.gson.Gson;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.request.Request;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class KugouAPI {

    private static HashMap<Long, List<KugouJson.data.info>> kMusicMap = new HashMap<>();

    public static void getMusic(MessageChain chain, GroupMessageEvent event){
        if (chain.contentToString().contains("&kugou")) {
            try {
            String post = URLEncoder.encode(chain.contentToString().replace("&kugou ",""),"UTF-8");
            String httpResult1 = Request.get("http://mobilecdn.kugou.com/api/v3/search/song?keyword="+post);//从X狗获取JSON对象
            KugouJson kugouJson;
            Gson gson = new Gson();
            kugouJson = gson.fromJson(httpResult1, KugouJson.class);
            List<KugouJson.data.info> infoArray = kugouJson.data.info;

            StringBuilder result = new StringBuilder("获取到的结果为：\n");
            ArrayList<KugouJson.data.info> jObjectArray = new ArrayList<>();

            for (int i = 0;i<5;i++){
                KugouJson.data.info obj1 = infoArray.get(i);
                jObjectArray.add(i,obj1);
                result.append(String.format("%s:%s-%s\n",i,obj1.singername,obj1.songname));
                if (i == infoArray.size()-1){
                    break;
                }
            }
            kMusicMap.put(event.getGroup().getId(),jObjectArray);
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
            List<KugouJson.data.info> jsonArray = kMusicMap.get(event.getGroup().getId());
            KugouJson.data.info musicJSONObj = jsonArray.get(kid);
            String singer = musicJSONObj.singername;
            String songName = musicJSONObj.songname;
            String hash = musicJSONObj.hash;
            String httpResult2 = Request.get(String.format("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=%s",hash));//获取下载地址JSON
            String URL = new Gson()
                    .fromJson(httpResult2,KugouMusicResponse.class)
                    .url;

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
        if (chain.contentToString().contains("&kgmv")) {
            try {
                Integer kid = Integer.valueOf(event.getMessage().contentToString().replace("&kgmv ",""));
                List<KugouJson.data.info> jsonArray = kMusicMap.get(event.getGroup().getId());
                KugouJson.data.info musicJSONObj = jsonArray.get(kid);
                String singer = musicJSONObj.singername;
                String songName = musicJSONObj.songname;
                String hash = musicJSONObj.mvhash;
                String httpResult2 = Request.get(String.format("http://m.kugou.com/app/i/mv.php?cmd=100&hash="+hash+"&ismp3=1&ext=mp4"));

                String URL = new Gson()
                        .fromJson(httpResult2, KugouMusicResponse.class)
                        .url;
                String message = "歌曲名:"+ singer + "-" + songName +"\nMV地址为:"+ URL;
                /* String httpResult1 = Request.get("http://mobilecdn.kugou.com/api/v3/search/song?keyword="+post);
                JSONObject obj = JSON.parseObject(JSON.parseObject(httpResult1).getString("data"));
                JSONObject info = obj.getJSONArray("info").getJSONObject(0);

                String hash = info.getString("hash");
                String name = info.getString("filename");
                String httpResult2 = Request.get(String.format("http://m.kugou.com/app/i/mv.php?cmd=100&hash="+hash+"&ismp3=1&ext=mp4"));
                String URL = JSON.parseObject(httpResult2).getString("url");

                 */
                if (URL == null){event.getSubject().sendMessage("错误：找不到相应的MV");}
                    else {event.getSubject().sendMessage(message); }// 回复消息


            } catch (NumberFormatException ignored){
                event.getGroup().sendMessage("出现错误！只能使用整型参数搜索！");
            }catch (NullPointerException ignored){
                event.getGroup().sendMessage("还没有搜索音乐！请先使用&kugou来搜索音乐");
            }catch (IndexOutOfBoundsException ignored){
                event.getGroup().sendMessage("号码超出范围！请在0-5范围内查找");
            }
        }

    }
}
