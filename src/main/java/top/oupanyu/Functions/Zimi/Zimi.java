package top.oupanyu.Functions.Zimi;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;


public class Zimi {

    public static void guess(MessageChain chain, GroupMessageEvent event,ZimiObject zimiObject){
            String guess = chain.contentToString().replace(".猜 ","");
            if (guess.equals(zimiObject.getAnswer())){
                ZimiBuffer.groupGuessingZimi.remove(event.getGroup().getId());
                event.getSubject().sendMessage("回答正确！\n答案是："+ zimiObject.getAnswer()+"\n原因：" + zimiObject.getReason());
            }else if (zimiObject.getChances() >= 1){
                zimiObject.chances--;
                event.getSubject().sendMessage("回答错了!还有" + zimiObject.getChances() + "次机会");
            }else {
                ZimiBuffer.groupGuessingZimi.remove(event.getGroup().getId());
                event.getSubject().sendMessage("回答错了!机会已用尽！\n答案应为：" + zimiObject.getAnswer() + "\n解析：" + zimiObject.getReason());
            }
        }

    public void start(MessageChain chain,GroupMessageEvent event){
        if (chain.contentToString().equals(".猜字谜") && !ZimiBuffer.groupGuessingZimi.containsKey(event.getGroup().getId())) {
            ZimiObject zimiObject = new ZimiObject();
            zimiObject.getZimi();
            ZimiBuffer.groupGuessingZimi.put(event.getGroup().getId(),zimiObject);
            event.getSubject().sendMessage("猜字谜开始！\n问题：" + zimiObject.getQuestion()+ "\n使用（.猜）来输入答案");
        }else {Zimi.guess(chain,event,ZimiBuffer.groupGuessingZimi.get(event.getGroup().getId()));}
    }
}
