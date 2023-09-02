package top.oupanyu.functions.xunfei.spark;

import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import top.oupanyu.Main;
import top.oupanyu.excuter.GroupMessageExecuter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XunfeiSpark implements GroupMessageExecuter {

    private static HashMap<Long,List<SparkMessage>> messageMap = new HashMap<>();

    @Override
    public void onRun(GroupMessageEvent event) {
        long gid = event.getGroup().getId();

        if (event.getMessage().contentToString().equals(".spark &&clear")){clearContext(gid);
        event.getSubject().sendMessage("上下文清除成功！");
        return;} //Context delete Usage: .spark &&clear
        String content = event.getMessage().contentToString().replaceFirst(".spark ","");
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(new QuoteReply(event.getMessage()));//Add Quote Reply to MessageChain
        if (messageMap.containsKey(gid)){
            List<SparkMessage> messages = messageMap.get(gid);
            messages.add(SparkMessage.userContent(content));//Set chat content(user)
            String response = doRequestToAI(messages);//do request
            messages.add(SparkMessage.assistantContent(response));//Add chat message content(AI)

            messages = SparkHelper.deleteTooLongSentenceOrNot(messages);
            messageMap.replace(gid,messages);
            builder.append(response);
        }else {
            List<SparkMessage> messages = new ArrayList<>();
            messages.add(SparkMessage.userContent(content));
            String response = doRequestToAI(messages);
            messages.add(SparkMessage.assistantContent(response));

            messages = SparkHelper.deleteTooLongSentenceOrNot(messages);
            messageMap.put(gid,messages);
            builder.append(response);
        }
        event.getSubject().sendMessage(builder.build());



    }

    private void clearContext(long gid){
        messageMap.remove(gid);
    }


    private String doRequestToAI(List<SparkMessage> messages){
        SparkClient sparkClient=new SparkClient();
        sparkClient.appid= Main.configloader.xunfeiSpark.appid;
        sparkClient.apiKey=Main.configloader.xunfeiSpark.apiKey;
        sparkClient.apiSecret=Main.configloader.xunfeiSpark.apiSecret;
        //Xunfei spark init

        SparkRequest sparkRequest=SparkRequest.builder()
                .messages(messages)
                .maxTokens(2048)
                .temperature(0.2)
                .apiVersion(SparkApiVersion.V2_0)
                .build();
        SparkSyncChatResponse chatResponse=sparkClient.chatSync(sparkRequest);
        return chatResponse.getContent();
    }
}
