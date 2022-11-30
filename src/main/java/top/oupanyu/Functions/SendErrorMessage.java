package top.oupanyu.Functions;

import net.mamoe.mirai.event.events.GroupMessageEvent;

public class SendErrorMessage {
    public static void send(GroupMessageEvent event,Exception e){
        event.getSubject().sendMessage("出现错误！错误信息：\n" + e.getMessage());
    }
}
