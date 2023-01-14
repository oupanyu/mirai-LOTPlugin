package top.oupanyu.Functions;

import com.alibaba.fastjson2.JSONObject;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.oupanyu.Main;
import top.oupanyu.request.Request;

import java.net.URLEncoder;
import java.util.Map;

public class GPT3 extends Thread{

    public static boolean onProcessing = false;

    public void run(GroupMessageEvent event) {
        try {
            GPT3.onProcessing = true;
            String cache = event.getMessage().contentToString().replace(".ai ", "");
            String send = URLEncoder.encode(cache,"UTF-8");
            String result = Request.get(String.format("http://%s:%s/gpt3?content=%s",
                                                            Main.configloader.getGPT3_host(),
                                                            Main.configloader.getGPT3_port(),
                                                            send));
            String msg = JSONObject.parseObject(result).getString("content");
            event.getGroup().sendMessage(msg);
            GPT3.onProcessing = false;
            return;
        }catch (Exception e){
            e.printStackTrace();
                event.getGroup().sendMessage("出现错误！请联系bot管理员");
                GPT3.onProcessing = false;
        }

    }

    public static void reset(GroupMessageEvent event){
        Request.get(String.format("http://%s:%s/gpt3/reset",
                Main.configloader.getGPT3_host(),
                Main.configloader.getGPT3_port()));
        event.getGroup().sendMessage("重置完成!");

    }
}
