package top.oupanyu.config;

public class Config {
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


    public String pixiv_token;
}
