package top.oupanyu.functions;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.request.Request;

import java.io.File;

public class PixivPic implements GroupMessageExecuter {

    public static void getPic(GroupMessageEvent event){
        try {
            Request.downloadFileWithOkHTTP("https://px2.rainchan.win/random","data/cache","pixiv.jpg");
            File file = new File("data/cache/pixiv.jpg");
            Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(),file);

            MessageChain messages = new MessageChainBuilder().build()
                                        .plus(new QuoteReply(event.getMessage()))
                                        .plus(image);

            event.getSubject().sendMessage(messages);
            file.delete();

        }catch (Exception e){
            event.getSubject().sendMessage("出现错误！\n" + e.getMessage());
        }

    }

    @Override
    public void onRun(GroupMessageEvent event) {
        getPic(event);
    }
}
