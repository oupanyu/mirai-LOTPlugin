package top.oupanyu.functions.xunfei.spark.ws;

import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.oupanyu.Main;
import top.oupanyu.functions.xunfei.spark.SparkAuthorization;
import top.oupanyu.functions.xunfei.spark.json.ChatObj;
import top.oupanyu.functions.xunfei.spark.json.SparkRequest;
import top.oupanyu.functions.xunfei.spark.json.SparkResponse;

import java.io.IOException;
import java.util.List;

public class RequestWebsocket extends WebSocketListener {
    Gson gson = new Gson();
    StringBuffer stringBuffer = new StringBuffer();
    private String userId;
    private Boolean wsCloseFlag;

    public RequestWebsocket(String userid){
        this.userId = userid;
        this.wsCloseFlag = false;
    }
    public String sendRequest(String content){
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(SparkAuthorization.getAuthUrl()).build();
        WebSocket webSocket = client.newWebSocket(request,
                new RequestWebsocket(userId));
        SparkRequest request1 = new SparkRequest(Main.configloader.xunfeiSpark.appid,userId, SparkAuthorization.Type.V2,0.5);
        request1.append(content);
        webSocket.send(gson.toJson(request1));
        while (true){
            if (getRequest() != null){
                return getRequest();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public String getRequest(){
        if (wsCloseFlag){
            return stringBuffer.toString();
        }
        return null;
    }





    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        SparkResponse sparkResponse = gson.fromJson(text, SparkResponse.class);
        if (sparkResponse.header.code != 0) {
            System.out.println("发生错误，错误码为：" + sparkResponse.header.code);
            System.out.println("本次请求的sid为：" + sparkResponse.header.sid);
            webSocket.close(1000, "");
        }
        List<ChatObj> textList = sparkResponse.payload.choices.text;
        for (ChatObj temp : textList) {
            stringBuffer.append(temp.content);
        }
        if (sparkResponse.header.status == 2) {
            // 可以关闭连接，释放资源
            wsCloseFlag = true;
        }
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        Main.LOGGER.error("CLOSED!");
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        try {
            if (null != response) {
                int code = response.code();
                Main.LOGGER.error("onFailure code:" + code);
                Main.LOGGER.error("onFailure body:" + response.body().string());
                if (101 != code) {
                    Main.LOGGER.error("connection failed");
                    //System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
