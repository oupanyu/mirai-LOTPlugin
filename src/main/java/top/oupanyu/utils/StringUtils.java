package top.oupanyu.utils;

public class StringUtils {
    public static int getWordCountWithoutSpace(String str){
        char[] strArr = str.toCharArray();
        int count = 0;
        for (char singleWord : strArr) {
            if (singleWord != ' ') {
                ++count;
            }
        }
        return count;
    }
    public static String changeString2Asterisk(String str){
        char[] strArr = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (char singleWord : strArr) {
            if (singleWord != ' ') {
                stringBuffer.append('*');
            }else {
                stringBuffer.append(' ');
            }
        }
        return stringBuffer.toString();
    }


    /**
     *  要开的字符串必须与原字符串字数相同
     * @param asterisk 带星号字符串
     * @param origin 原字符串
     * @param word 要开的字
     * @return 开字后的字符串，若字数不同则返回空字符串
     */
    public static String OpenWordFromAsterisk(String asterisk,String origin,char word){
        if (asterisk.length() != origin.length()){
            return "";
        }
        char[] asteriskArray = asterisk.toCharArray();
        char[] originArray = asterisk.toCharArray();
        for (int i=0;i<asterisk.length();i++){
            if (originArray[i] == word){
                asteriskArray[i] = word;
            }
        }
        return String.valueOf(asteriskArray);
    }

    public static boolean isStringSimilar(String str1,String str2){
        char[] str1C = str1.toCharArray();
        char[] str2C = str2.toCharArray();
        int allCount = str2C.length;
        int correct = 0;
        try {
            for (int i=0;i<str1C.length;i++){
                if (str1C[i] == str2C[i]){
                    ++correct;
                }
            }
        }catch (Exception ignored){
        }
        double percent = correct / allCount;
        return percent > 0.7;

    }
}
