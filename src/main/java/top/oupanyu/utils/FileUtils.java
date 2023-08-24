package top.oupanyu.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static File getOrCreateFile(String path){
        File folder = new File(getFolder(path));
        File file = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        return file;
    }


    public static String getFolder(String path){
        String[] pathArr;
        if (path.contains("\\")){
            pathArr = path.split("\\\\");
        }else {
            pathArr = path.split("/");
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<pathArr.length-1;i++){
            sb.append(pathArr[i]);
            sb.append("/");
        }
        return sb.toString();
    }

}
