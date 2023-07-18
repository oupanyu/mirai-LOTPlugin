package top.oupanyu.functions.Bilibili.json;

public class BiliJson {
    public int code;

    public data data;

    public class data{
        public int views;
        public int videos;
        public String pic;
        public String title;

        public stat stat;

        public owner owner;

        public class stat{
            public int view;
            public int danmaku;
            public int reply;
            public int favorite;
            public int coin;
            public int his_rank;
            public int like;
        }
        public class owner{
         public String name;
        }
    }

}
