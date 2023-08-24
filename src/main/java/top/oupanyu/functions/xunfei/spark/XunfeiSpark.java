package top.oupanyu.functions.xunfei.spark;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.functions.xunfei.spark.ws.RequestWebsocket;

public class XunfeiSpark implements GroupMessageExecuter {

    @Override
    public void onRun(GroupMessageEvent event) {
        String content = event.getMessage().contentToString().replaceFirst(".spark ","");
        RequestWebsocket requestWebsocket = new RequestWebsocket(String.valueOf(event.getGroup().getId()));
        System.out.println(requestWebsocket.sendRequest(content));
    }
}
