package top.oupanyu.functions;

import com.google.gson.Gson;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.request.Request;

import java.io.File;

public class APictureADay implements GroupMessageExecuter {

    private String title;
    private String url;


    public static void getPic(GroupMessageEvent event){
        try {
            //https://api.leafone.cn/api/bing
            APictureADay aPictureADay;
            Gson gson = new Gson();
            aPictureADay = gson.fromJson(Request.get("http://api.muvip.cn/api/bing?info=true&rand=true"),
                    APictureADay.class);
            String URL = aPictureADay.url;
            Request.downloadFile(URL,"data/cache","picture.jpg");//从API下载图片并保存在本地
            File file = new File("data/cache/picture.jpg");
            Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(),file);
            MessageChain messages = new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append(aPictureADay.title)
                    .append(image)//构建消息链
                    .build();

            event.getSubject().sendMessage(messages);//发送消息
            file.delete();
        }catch (Exception e){
            event.getSubject().sendMessage(e.getMessage());
        }

    }

    @Override
    public void onRun(GroupMessageEvent event) {
        getPic(event);
    }
}
