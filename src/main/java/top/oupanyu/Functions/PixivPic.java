package top.oupanyu.Functions;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.FileMessage;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.request.Request;

import java.io.File;
import java.util.Arrays;

public class PixivPic {

    public static void getPic(MessageChain chain, GroupMessageEvent event){
        try {
            Request.downloadFile("https://px2.rainchan.win/random","data/cache","pixiv.jpg");
            File file = new File("data/cache/pixiv.jpg");
            Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(),file);
            event.getSubject().sendMessage(image);
            file.delete();

        }catch (Exception e){
            event.getSubject().sendMessage("出现错误！\n" + e.getMessage());
        }

    }
}
