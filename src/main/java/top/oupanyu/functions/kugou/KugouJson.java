package top.oupanyu.functions.kugou;

import java.util.List;

public class KugouJson {

    public data data;

    public int status;

    public class data{

        public List<info> info;

        public class info{
            public String singername;
            public String songname;
            public String filename;
            public String hash;
            public String mvhash;
        }
    }
}
