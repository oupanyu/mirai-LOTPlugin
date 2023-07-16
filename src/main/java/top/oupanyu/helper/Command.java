package top.oupanyu.helper;

import java.util.List;

public class Command {
    private List<String> list;
    private String origin;
    private boolean isUsable = false;
    private boolean needFirstKey = false;
    private boolean hasSecondKey = false;
    private String firstKey;
    private String secondKey;




    public Command(String originStr){
        try {
            list = CommandHelper.parseStringToList(originStr);
            origin = originStr;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Command getInstance(String src){
        return new Command(src);
    }

    public Command(List<String> list){
        this.list = list;
    }

    public Command setFirstKey(String key){
        firstKey = key;
        isUsable = true;
        return this;
    }

    public String getOriginContent() {
        return origin;
    }

    public Command setSecondKey(String key){
        hasSecondKey = true;
        this.secondKey = key;
        return this;
    }
    public void setUsable(){
        isUsable = true;
    }
    public Command setWhetherNeedFirstKeyOrNot(boolean bool){
        this.needFirstKey = bool;
        return this;
    }
    public boolean hasFistKey(){
        return firstKey != null;
    }
    public boolean hasSecondKey() {return secondKey!= null;}
    public boolean isFirstKeyEquals(){
        return isUsable && list.get(0).equals(firstKey);
    }

    /**
     *传入List中的序号和关键词，如果等于则返回true,反之为false
     **/
    public boolean equals(Integer i,String keyWord){

        if (!isUsable){
            return false;
        }

        if (hasFistKey()){
            try {
                return list.get(i).equals(keyWord) && isFirstKeyEquals();
            }catch (Exception ignored){
                return false;
            }
        }


        String sendWords = (String) this.list.get(i);
        return sendWords.equals(keyWord);
    }
    public boolean equalsWithoutCheckingFirstKey(Integer i,String keyWord){
        if (!isUsable){
            return false;
        }
        String sendWords = (String) this.list.get(i);
        return sendWords.equals(keyWord);
    }

    public String getContent(int i){
        return list.get(i);
    }

    public String getContent(Integer i){
        return isUsable ? (String) list.get(i) : "" ;
    }

    /**
     *
     * @return 该命令分解出的列表大小
     */
    public int getLength(){
        return list.size();
    }
}
