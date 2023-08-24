package top.oupanyu.functions.xunfei.spark.json;

import top.oupanyu.functions.xunfei.spark.SparkAuthorization;

import java.util.ArrayList;
import java.util.List;

public class SparkRequest {
    public header header = new header();
    public class header{
        private String app_id;
        private String uid;
    }
    public parameter parameter = new parameter();
    public class parameter{
        public chat chat = new chat();
        public class chat{
            private String domain;
            private double temperature;
            private int max_tokens = 2048;
            private String chat_id;
        }
    }
    public payload payload = new payload();
    public class payload{

        public message message = new message();
        public class message{
            private List<ChatObj> text = new ArrayList<>();
        }
    }

    public SparkRequest(String appid, String uid, SparkAuthorization.Type type, double temp){
        this.header.app_id = appid;
        this.header.uid = uid;
        if (type.equals(SparkAuthorization.Type.V2)){
            this.parameter.chat.domain = "generalv2";
        }else {
            this.parameter.chat.domain = "general";
        }
        this.parameter.chat.temperature = temp;
        this.parameter.chat.chat_id = uid;
    }

    public SparkRequest append(String content){
        if (payload.message.text.size() > 6){
            payload.message.text.remove(0);
        }
        payload.message.text.add(new ChatObj(content, ChatObj.Type.USER));
        return this;
    }



}
