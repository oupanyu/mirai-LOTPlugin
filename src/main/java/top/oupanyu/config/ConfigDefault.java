package top.oupanyu.config;

public class ConfigDefault {

    public Config.database database;
    public class database{
        public String db = "sqlite";
        public String address = "";
        public String username = "";
        public String password = "";
    }

    //TianXinAPI part
    public String tiankey = "";

    public transmission transmission = new transmission();
    public class transmission{
        public boolean transmission = false;
        public String qqnum = "";
        public String server_ip = "";
        public Integer server_port = 0;
        public String groupnum = "";
    }
    //OpenAI part
    public openai openai = new openai();
    public class openai{
        public boolean openai_enable = false;
        public String openai_url = "api.openai.com";
        public String openai_key = "your_key_here";
    }

    public baidu_fanyi baidu_fanyi = new baidu_fanyi();
    public class baidu_fanyi{
        public String baidu_fanyi_key = "";
        public String baidu_fanyi_appid = "";
    }


    public String pixiv_token = "";


}
