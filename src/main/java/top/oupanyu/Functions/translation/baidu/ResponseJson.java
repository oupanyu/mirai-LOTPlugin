package top.oupanyu.Functions.translation.baidu;

import java.util.List;

public class ResponseJson {
    public class trans_result{
        public String dst;

        public String src;
    }

    public String from;
    public String to;
    public List<trans_result> trans_result;
}
