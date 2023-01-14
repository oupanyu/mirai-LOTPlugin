package top.oupanyu.tools;


import java.util.ArrayList;

import java.util.List;

public class ListHelper {
    public static List splitStingByCharLength(String content, int length) {
        if (null == content || content.equals("")) {
            return null;
        }
        if (length <= 0) {
            return null;
        }
        List<String> result = new ArrayList<>();
        String[] contentArray = content.split("");
        for (long i = 0,k = 0;i<contentArray.length ; i++) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int j = 0; j < length; j++) {
                stringBuffer.append(contentArray[(int) k]);
                i++;
                k++;
                if (k == contentArray.length){
                    break;
                }
            }
            result.add(String.valueOf(stringBuffer));
        }
        return result;
    }
}