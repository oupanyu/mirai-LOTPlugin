package top.oupanyu.Functions.cloudmusic;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.oupanyu.Functions.SendErrorMessage;
import top.oupanyu.Functions.cloudmusic.responsejson.SearchResponse;
import top.oupanyu.excuter.EventExecuter;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.request.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class NeteaseCloudMusic implements GroupMessageExecuter {

    private static HashMap<Long, List<SearchResponse.songs>> NCMusicMap = new HashMap<>();

    public static void getMusic(GroupMessageEvent event){
        String chain = event.getMessage().contentToString();
        long groupNum = event.getGroup().getId();
        String post = null;
        try {
            post = URLEncoder.encode(chain.replace(".ncm ",""),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            SendErrorMessage.send(event,e);
        }
        String httpResult = Request.get("http://cloud-music.pl-fe.cn/search?keywords="+post);

        Gson gson = new Gson();
        SearchResponse searchResponse = gson.fromJson(httpResult, SearchResponse.class);
        NCMusicMap.put(event.getGroup().getId(),searchResponse.result.songs);
        StringBuilder result = new StringBuilder("获取到的结果为：");//StringBuilder构建返回信息

        for (int i=0;i<6;i++){
            //获取艺术家与歌名开始
            if(i == NCMusicMap.get(groupNum).size()-1){
                break;
            }
            SearchResponse.songs jsonSong = NCMusicMap.get(groupNum).get(i);
            List<SearchResponse.songs.artists> artistArray = jsonSong.artists;
            //JSONArray artistarray = jsonSong.getJSONArray("artists");
            StringBuilder artists = new StringBuilder();
            for (int j = 0; j < artistArray.size(); j++) {
                artists.append(artistArray.get(j).name).append(" ");
            }
            String songName = artists + "- " + jsonSong.name;

            if (jsonSong.transNames != null){
                String transName = "(" + jsonSong.transNames.get(0) + ")";
                songName += transName;
            }//结束获取艺术家与歌名
            NCMusicMap.get(groupNum).get(i).lsongname = songName;
            result.append("\n").
                    append(i).
                    append("、").
                    append(songName);
        }
        event.getGroup().sendMessage(String.valueOf(result.append("\n获取音乐地址请输入.nid 数字代号")));

    }

    @Override
    public void onRun(GroupMessageEvent event)
    {
        String content = event.getMessage().contentToString();
        if (content.contains(".ncm")) {
            NeteaseCloudMusic.getMusic(event);
        } else if (content.contains(".nid ")) {
            NeteaseCloudMusic.getMusicURL(event);
        } else if (content.contains(".nmv ")){
            NeteaseCloudMusic.getMV(event);
        }
    }

    public static void register(){
        NeteaseCloudMusic neteaseCloudMusic = new NeteaseCloudMusic();
        EventExecuter.register(".ncm",neteaseCloudMusic);
        EventExecuter.register(".nid",neteaseCloudMusic);
        EventExecuter.register(".nmv",neteaseCloudMusic);

    }


    protected class MusicGetResponse{
        public data data;
        public class data{
            public String url;
        }
    }

    public static void getMusicURL(GroupMessageEvent event){

        try {
            Integer ncmid = Integer.valueOf(event.getMessage().contentToString().replace(".nid ",""));
            List<SearchResponse.songs> jsonArray = NCMusicMap.get(event.getGroup().getId());
            SearchResponse.songs musicJSONObj = jsonArray.get(ncmid);//从JSON数组中获取音乐JSON对象
            String songName = musicJSONObj.lsongname;
            String id = musicJSONObj.id;
            String musicURL = new Gson()
                    .fromJson(
                            Request.get(String.format("https://api.gmit.vip/Api/Netease?id=%s",id)),
                            MusicGetResponse.class)
                    .data
                    .url;//获取网易云音乐URL

            String message = "歌曲名:"+ songName +"\n歌曲地址为:"+ musicURL;
            event.getGroup().sendMessage(message);

        }catch (NumberFormatException ignored){
            event.getGroup().sendMessage("出现错误！只能使用整型参数搜索！");
        }catch (NullPointerException ignored){
            event.getGroup().sendMessage("还没有搜索音乐！请先使用.ncm来搜索音乐");
        }catch (IndexOutOfBoundsException ignored){
            event.getGroup().sendMessage("号码超出范围！请在0-5范围内查找");
        }


    }



    protected class NCMMVJson{
        public result result;
        public class result{

            public List<songs> songs;
            public class songs{
                public List<artists> artists;
                public String name;
                public List<String> transNames;
                public Integer mvid;

                public class artists{
                    public String name;
                    public List<String> transNames;
                }

            }
        }
    }

    protected class mvURLJson{
        public data data;
        public class data{
            public mv mv;
            public class mv{
                @SerializedName("480")
                public String url_480;
            }
        }
    }

    public static void getMV(GroupMessageEvent event) {
        String chain = event.getMessage().contentToString();
        String post = null;
        try {
            post = URLEncoder.encode(chain.replace(".nmv ",""),"UTF-8");//URL编码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String httpResult = Request.get("http://cloud-music.pl-fe.cn/search?keywords="+post);
        //System.out.println(post);

        //System.out.println(object);
        NCMMVJson.result.songs jsonSong = new Gson()
                .fromJson(httpResult,NCMMVJson.class)
                .result
                .songs
                .get(0);//获取第一首歌曲的JSONObject

        //获取艺术家与歌名开始
        List<NCMMVJson.result.songs.artists> artistArray = jsonSong.artists;
        String artists = "";
        for (int i = 0; i < artistArray.size(); i++) {
            artists += (artistArray.get(i).name + " ");
        }
        String songName = artists + "- " + jsonSong.name;
        if (jsonSong.transNames != null){//判断是否有译名
            String transName = "(" + jsonSong.transNames.get(0) + ")";
            songName += transName;
        }//结束获取艺术家与歌名
        Integer mvID = null;
        if (jsonSong.mvid != 0){//判断是否有MV
            mvID = jsonSong.mvid;
            String mvURL = new Gson()
                    .fromJson(Request.get(String.format("https://tenapi.cn/wyymv/?id=%s",mvID)),mvURLJson.class)
                    .data
                    .mv
                    .url_480;

            String message = "歌曲名:"+ songName +"\nMV地址为:"+ mvURL;
            event.getSubject().sendMessage(message);
        }else {
            event.getSubject().sendMessage("看起来这个音乐没有MV哦");
        }





    }

}
