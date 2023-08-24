package top.oupanyu.config;

import top.oupanyu.database.Drivers;

public class Config {

    public database database;
    public class database{
        public Drivers drivers;
        public String db;
        public String address;
        public String username;
        public String password;
    }



    //TianXinAPI part
    public String tiankey;

    public transmission transmission;
    public class transmission{
        public boolean transmission;
        public String qqnum;
        public String server_ip;
        public Integer server_port;
        public String groupnum;
    }
    //OpenAI part
    public openai openai;
    public class openai{
        public boolean openai_enable;
        public String openai_url;
        public String openai_key;
    }

    public baidu_fanyi baidu_fanyi;
    public class baidu_fanyi{
        public String baidu_fanyi_key;
        public String baidu_fanyi_appid;
    }

    public xunfeiSpark xunfeiSpark;
    public class xunfeiSpark{
        public String appid;
        public String apiKey;
        public String apiSecret;
    }


    public String pixiv_token;




    public void setDB(Drivers drivers){
        this.database.drivers = drivers;
    }
    public Drivers getDB(){
        return this.database.drivers;
    }
}
