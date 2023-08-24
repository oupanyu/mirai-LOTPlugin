package top.oupanyu.functions.xunfei.spark.json;

import java.util.List;

public class SparkResponse {
    public header header;
    public payload payload;


    public class header{
        public int code;
        public String message;
        public String sid;
        public int status;
    }

    public class payload{
        public choices choices;
        public usage usage;
        public class choices{
            public int status;
            public int seq;
            public List<ChatObj> text;

        }
        public class usage{
            public int prompt_tokens;
            public int completion_tokens;
        }
    }

}
