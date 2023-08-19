package top.oupanyu.functions.Bilibili;

import com.google.gson.Gson;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import top.oupanyu.functions.Bilibili.Exception.NoSuchVideoVideoException;
import top.oupanyu.functions.Bilibili.Exception.WrongBVIDTypeException;
import top.oupanyu.functions.Bilibili.json.BiliJson;
import top.oupanyu.Main;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.request.Request;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GetBVideoInfo implements GroupMessageExecuter {
    private static BiliJson bj;

    public static void getbyAID(MessageChain chain, GroupMessageEvent event) throws NumberFormatException{
        try {
            Integer avid = Integer.parseInt(chain.contentToString().replace(".B站 ",""));
            OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS).build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(String.format("http://api.bilibili.com/x/web-interface/view?aid=%s",avid))
                    .get()
                    .header("Content-Type","application/json")
                    .header("Authorization","Bearer "+ Main.configloader.openai.openai_key)
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            //String result = Request.get(String.format("http://api.bilibili.com/x/web-interface/view?aid=%s",avid));//GET BilibiliAPI
            //System.out.println(result);
            Gson gson = new Gson();
            BiliJson bj = gson.fromJson(result, BiliJson.class);

            if (bj.code != 0){//检测是否存在该视频，如果无，抛出异常
                throw new NoSuchVideoVideoException();
            }
            int views,danmaku,likes;
            String title,owner;

            views = bj.data.stat.view;
            danmaku = bj.data.stat.danmaku;
            likes = bj.data.stat.like;
            title = bj.data.title;
            owner = bj.data.owner.name;
            String fn = "data/cache/bilipic.jpg";
            File file = new File(fn);
            Request.downloadFile(bj.data.pic,"data/cache","bilipic.jpg");
            Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(), file);
            //event.getSubject().sendMessage(image);
            file.delete();
            MessageChain schain = new MessageChainBuilder()
                    .build().plus(image)
                    .plus(String.format("视频：%s\n拥有者：%s\n观看数：%s\n点赞数：%s\n弹幕数：%s",title,owner,views,likes,danmaku));
            event.getSubject().sendMessage(schain);

            //event.getSubject().sendMessage(result);

        } catch (NoSuchVideoVideoException e) {
            event.getSubject().sendMessage("出现错误！没有" + chain.contentToString().replace(".B站 ","") + "这个视频！");
        }/*catch (NumberFormatException e){
            event.getSubject().sendMessage("出现错误！\n" + e.getMessage());
        }*/ catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getByBID(MessageChain chain,GroupMessageEvent event) {
        String LETTER_DIGIT_REGEX = "^[a-z0-9A-Z]+$";

        String bvid = chain.contentToString().replace(".B站 ","");

        if(!bvid.matches(LETTER_DIGIT_REGEX)){
            try {
                throw new WrongBVIDTypeException();
            } catch (WrongBVIDTypeException e) {
                event.getSubject().sendMessage("出现错误！没有" + bvid + "这个视频！");
            }
        }
        try{
            OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS).build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(String.format("http://api.bilibili.com/x/web-interface/view?bvid=%s",bvid))
                    .get()
                    .header("Content-Type","application/json")
                    .header("Authorization","Bearer "+ Main.configloader.openai.openai_key)
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            //String result = Request.get(String.format("http://api.bilibili.com/x/web-interface/view?aid=%s",avid));//GET BilibiliAPI
            //System.out.println(result);
            Gson gson = new Gson();
            BiliJson bj = gson.fromJson(result, BiliJson.class);

            if (bj.code != 0){//检测是否存在该视频，如果无，抛出异常
                throw new NoSuchVideoVideoException();
            }
            int views,danmaku,likes;
            String title,owner;

            views = bj.data.stat.view;
            danmaku = bj.data.stat.danmaku;
            likes = bj.data.stat.like;
            title = bj.data.title;
            owner = bj.data.owner.name;
            String fn = "data/cache/bilipic.jpg";
            File file = new File(fn);
            Request.downloadFile(bj.data.pic,"data/cache","bilipic.jpg");
            Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(), file);
            //event.getSubject().sendMessage(image);
            file.delete();
            MessageChain schain = new MessageChainBuilder()
                    .build().plus(image)
                    .plus(String.format("视频：%s\n拥有者：%s\n观看数：%s\n点赞数：%s\n弹幕数：%s",title,owner,views,likes,danmaku));
            event.getSubject().sendMessage(schain);

        //event.getSubject().sendMessage(result);

        } catch (NoSuchVideoVideoException e) {
            event.getSubject().sendMessage("出现错误！没有" + chain.contentToString().replace(".B站 ","") + "这个视频！");
        }catch (NumberFormatException e){
            event.getSubject().sendMessage("出现错误！\n" + e.getMessage());
        }catch (Exception e){
            event.getSubject().sendMessage(e.getMessage());
        }



    }

    @Override
    public void onRun(GroupMessageEvent event) {
        MessageChain chain = event.getMessage();
        try {
            GetBVideoInfo.getbyAID(chain,event);
        }catch (NumberFormatException ignored){
            GetBVideoInfo.getByBID(chain,event);
        }
    }
}
