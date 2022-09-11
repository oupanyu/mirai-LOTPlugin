package top.oupanyu.Functions;

import com.alibaba.fastjson2.JSONObject;
import net.mamoe.mirai.console.data.PluginDataStorage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.request.Request;

public class APictureADay {
    public static void getPic(MessageChain chain, GroupMessageEvent event){
        try {
            String picinfo = Request.get("http://free.mxbizhi.com/bing/image.php/bing?info=true");
            JSONObject object = JSONObject.parseObject(picinfo);
            String title = object.getString("title"),picURL = object.getString("url");
            Request.downloadFile(picURL,"data/picture");
        }catch (Exception e){
            event.getSubject().sendMessage(e.getMessage());
        }

    }
}
