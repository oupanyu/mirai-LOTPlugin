package top.oupanyu.functions.xunfei.spark;

import io.github.briqt.spark4j.model.SparkMessage;

import java.util.List;

public class SparkHelper {
    private static final int MAX_TOKEN_LENGTH = 2048;
    public static List<SparkMessage> deleteTooLongSentenceOrNot(List<SparkMessage> messages){
        while (true){
            if (!isOverMaxLength(messages)){break;}
            else {
                deleteFirstSentence(messages);
            }
        }
        return messages;
    }
    private static int getMessageCharacterAmount(List<SparkMessage> messages){
        int amount = 0;
        for (SparkMessage sm : messages){
            amount += sm.getContent().toCharArray().length;
        }
        return amount;
    }
    public static boolean isOverMaxLength(List<SparkMessage> messages){
        return MAX_TOKEN_LENGTH < getMessageCharacterAmount(messages);
    }
    public static List<SparkMessage> deleteFirstSentence(List<SparkMessage> messages){
        messages.remove(0);
        return messages;
    }
}
