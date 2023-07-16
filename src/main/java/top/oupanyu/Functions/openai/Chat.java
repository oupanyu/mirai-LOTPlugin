package top.oupanyu.Functions.openai;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import okhttp3.*;
import top.oupanyu.Main;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Chat extends Thread{
    private static String url = String.format("https://%s/v1/chat/completions",Main.configloader.getOpenai_url());

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static boolean onProccessing = false;
    private static HashMap<Long,OpenAI> hashMap = new HashMap<>();

    private static String post(Long groupID) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS).build();
        RequestBody body = RequestBody.create(JSON, hashMap.get(groupID).toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Content-Type","application/json")
                .header("Authorization","Bearer "+Main.configloader.getOpenai_key())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
        /*return "{\"id\":\"chatcmpl-75dJRnE5dtUfZsSiQmBy27Zjs0B4n\"," +
                "\"object\":\"chat.completion\"," +
                "\"created\":1681577113," +
                "\"model\":\"gpt-3.5-turbo-0301\"," +
                "\"usage\":{\"prompt_tokens\":10,\"completion_tokens\":10,\"total_tokens\":20}," +
                "\"choices\":[{\"message\":{\"role\":\"assistant\",\"content\":\"Hello there! How can I assist you today?\"},\"finish_reason\":\"stop\",\"index\":0}]}";
    */
    }


    public static void reset(Long gid){
        hashMap.get(gid).reset();
    }

    public static void chatOrCreate(GroupMessageEvent event){
        onProccessing=true;
        String content = event.getMessage().contentToString().replace(".chat ","");
        if (!hashMap.containsKey(event.getGroup().getId())){
            hashMap.put(event.getGroup().getId(),new OpenAI());
        }
        OpenAI openAI = hashMap.get(event.getGroup().getId());
        openAI.addAsk(content);
        try {
            String answer = post(event.getGroup().getId());
            openAI.addAnswer(answer);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(event.getGroup().getId()).append("--").append(event.getSenderName())
                            .append("--").append(content).append("--Ans:").append(openAI.getLastAnswer());
            Main.logger.info(stringBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        onProccessing=false;
        event.getGroup().sendMessage(openAI.getLastAnswer());
    }

    public static String chatOrCreate(String content){
        onProccessing=true;
        Long id=13162645L;
        if (!hashMap.containsKey(id)){
            hashMap.put(id,new OpenAI());
        }
        OpenAI openAI = hashMap.get(id);
        openAI.addAsk(content);
        try {
            String answer = post(id);
            openAI.addAnswer(answer);
            Main.logger.info(openAI.getLastAnswer());

        } catch (IOException e) {
            onProccessing=false;
            throw new RuntimeException(e);
        }
        onProccessing=false;
        return openAI.getLastAnswer();
    }

}
