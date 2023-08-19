package top.oupanyu.functions.translation.baidu;


import com.google.gson.Gson;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.codec.digest.DigestUtils;
import top.oupanyu.Main;
import top.oupanyu.helper.Command;

import java.io.IOException;
import java.util.Calendar;

public class BaiduTranslate {

    private static String appid = Main.configloader.baidu_fanyi.baidu_fanyi_appid;
    private static String key = Main.configloader.baidu_fanyi.baidu_fanyi_key;

    public static final String url = "https://fanyi-api.baidu.com/api/trans/vip/translate?q=%s&from=%s&to=%s&appid=%s&salt=%s&sign=%s";
    public static void translateIntoChinese(GroupMessageEvent e,String src){
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        int salt = Calendar.getInstance().hashCode();
        String md5 = DigestUtils.md5Hex(appid+src+salt+key);
        Request request = new Request.Builder()
                .url(String.format(url,src,"auto","zh",appid,salt,md5))
                .get()
                .build();
        try {
            String response = client.newCall(request).execute().body().string();
            Gson gson = new Gson();
            ResponseJson responseJson = gson.fromJson(response, ResponseJson.class);
            MessageChain message = new MessageChainBuilder()
                    .append(new QuoteReply(e.getMessage()))
                    .append("翻译结果：\n")
                    .append(responseJson.trans_result.get(0).dst)
                    .build();
            e.getGroup().sendMessage(message);
        } catch (IOException ex) {
            e.getGroup().sendMessage("Error!\n"+ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param from 源语言
     * @param to 目标语言
     * @param src 需要翻译的内容
     * @return 翻译结果，若翻译出错则返回错误消息
     */

    public static String translate(String from,String to,String src){
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        int salt = Calendar.getInstance().hashCode();
        String md5 = DigestUtils.md5Hex(appid+src+salt+key);
        Request request = new Request.Builder()
                .url(String.format(url,src,from,to,appid,salt,md5))
                .get()
                .build();
        try {
            String response = client.newCall(request).execute().body().string();
            Gson gson = new Gson();
            ResponseJson responseJson = gson.fromJson(response, ResponseJson.class);
            MessageChain message = new MessageChainBuilder()
                    .append("翻译结果：\n")
                    .append(responseJson.trans_result.get(0).dst)
                    .build();
            return message.contentToString();
        } catch (IOException ex) {
            return "Error!\n"+ex.getMessage();
        }
    }

    public static void init(GroupMessageEvent event){
        String content = event.getMessage().contentToString();
        Command command = Command.getInstance(content);
        if (command.equals(0,"/translate")){
            if (command.equals(1,"auto")){
                translateIntoChinese(event,command.getContent(2));
            }else {
                String result = translate(command.getContent(2),
                        command.getContent(3),
                        command.getContent(3));
                MessageChain messages = new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append(result)
                        .build();
                event.getGroup().sendMessage(messages);
            }

        }
    }

    public static void freeTranslation(GroupMessageEvent event){
    }
}
