package top.oupanyu.Functions.chatgpt;

import com.github.plexpt.chatgpt.Chatbot;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ChatGPT extends Thread{


    public static boolean onProcessing = false;
    public void run(GroupMessageEvent event ,Chatbot chatbot){
        try {
            ChatGPT.onProcessing = true;
            String send = event.getMessage().contentToString().replace(".ai ", "");
            Map<String, Object> chatResponse = chatbot.getChatResponse(send);
            String result = (String) chatResponse.get("message");
            event.getGroup().sendMessage("ChatGPT: " + result);
            ChatGPT.onProcessing = false;
        }catch (Exception e){
            event.getGroup().sendMessage("出现错误！请联系bot管理员");
        }
    }

}
