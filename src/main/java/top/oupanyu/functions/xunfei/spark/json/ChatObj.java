package top.oupanyu.functions.xunfei.spark.json;

public class ChatObj {
        public String role;
        public String content;
        public ChatObj(String content,Type type){
            this.content = content;
            this.role = type.getRole();
        }
        public enum Type{
            USER("user"),ASSISTANT("assistant");
            Type(String role){
                this.role = role;
            }
            private String role;
            public String getRole(){return this.role;}
        }

}
