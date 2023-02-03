package top.oupanyu.tools;

public class MediaWikiHelper {
    public static String delNonUseKey(String mediawiki){

        return mediawiki.replace("<br>","")
                .replace("</br>","")
                .replace("<ref>","")
                .replace("</ref>","")
                .replace("<ref>","")
                .replace("<del>","")
                .replace("</del>","");

    }
}
