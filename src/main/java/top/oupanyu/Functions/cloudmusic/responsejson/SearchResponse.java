package top.oupanyu.Functions.cloudmusic.responsejson;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
    public class songs{
        public class artists{
            public String name;
            public String id;
        }
        public List<artists> artists;
        public long copyrightId;
        public String id;
        public String name;
        public String lsongname = "";
        public List<String> transNames;
    }
    public class result{
        public boolean hasMore;
        public int songCount;
        public List<songs> songs;
    }

    public int code;
    public result result;

}
