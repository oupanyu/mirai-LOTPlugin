package top.oupanyu.helper;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper {


    public static List<String> parseStringToList(String origin){
        //String origin = "char at \" hel\\\"lo\" is good";
        //System.out.println(origin);
        char[] cOrigin = origin.toCharArray();
        List<String> list = new ArrayList<>();
        int listCount = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<cOrigin.length;i++){
            if (cOrigin[i] == '"'){
                for (int j = i+1;j<cOrigin.length;j++){
                    if (cOrigin[j] == '"' && cOrigin[j-1] != '\\'){
                        list.add(listCount, stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                        listCount++;
                        i++;
                        break;
                    }
                    stringBuilder.append(cOrigin[j]);
                    i++;
                }
                i++;
                continue;
            }
            if (cOrigin[i] == ' '){
                list.add(listCount,stringBuilder.toString());
                stringBuilder = new StringBuilder();
                listCount++;
                continue;
            }
            stringBuilder.append(cOrigin[i]);
            if (i == cOrigin.length-1){
                list.add(listCount,stringBuilder.toString());
            }
        }
        return list;
    }


}
