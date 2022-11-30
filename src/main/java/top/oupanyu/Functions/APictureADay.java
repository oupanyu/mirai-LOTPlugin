package top.oupanyu.Functions;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.request.Request;

import java.io.File;

public class APictureADay {
    public static void getPic(MessageChain chain, GroupMessageEvent event){
        try {
            Request.downloadFile("https://api.leafone.cn/api/bing","data/cache","picture.jpg");//从API下载图片并保存在本地
            File file = new File("data/cache/picture.jpg");
            Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(),file);
            event.getSubject().sendMessage(image);//发送信息
            file.delete();
        }catch (Exception e){
            event.getSubject().sendMessage(e.getMessage());
        }

    }
}
