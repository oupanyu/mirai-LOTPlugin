package top.oupanyu.Functions;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.request.Request;

import java.io.File;

public class APictureADay {
    public static void getPic(MessageChain chain, GroupMessageEvent event){
        try {
            //https://api.leafone.cn/api/bing
            String URL = JSONObject.parseObject(Request.get("http://api.muvip.cn/api/bing?info=true&rand=true")).getString("url");
            Request.downloadFile(URL,"data/cache","picture.jpg");//从API下载图片并保存在本地
            File file = new File("data/cache/picture.jpg");
            Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(),file);
            event.getSubject().sendMessage(image);//发送信息
            file.delete();
        }catch (Exception e){
            event.getSubject().sendMessage(e.getMessage());
        }

    }
}
