package top.oupanyu.Functions.Zimi;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.excuter.EventExecuter;
import top.oupanyu.excuter.GroupMessageExecuter;


public class Zimi implements GroupMessageExecuter {

    public static void guess(MessageChain chain, GroupMessageEvent event,ZimiObject zimiObject){
            String guess = chain.contentToString().replace(".猜 ","");
            if (guess.equals(zimiObject.getAnswer())){
                ZimiBuffer.groupGuessingZimi.remove(event.getGroup().getId());
                event.getSubject().sendMessage("回答正确！\n答案是："+ zimiObject.getAnswer()+"\n原因：" + zimiObject.getReason());
            }else if (zimiObject.getChances() >= 2){
                zimiObject.chances--;
                event.getSubject().sendMessage("回答错了!还有" + zimiObject.getChances() + "次机会");
            }else {
                ZimiBuffer.groupGuessingZimi.remove(event.getGroup().getId());
                event.getSubject().sendMessage("回答错了!机会已用尽！\n答案应为：" + zimiObject.getAnswer() + "\n解析：" + zimiObject.getReason());
            }
        }


    public static void giveUp(MessageChain chain,GroupMessageEvent event){
        String result = ZimiBuffer.groupGuessingZimi.get(event.getGroup().getId()).getAnswer();
        String reason = ZimiBuffer.groupGuessingZimi.get(event.getGroup().getId()).getReason();
        ZimiBuffer.groupGuessingZimi.remove(event.getGroup().getId());
        event.getSubject().sendMessage("答案应为： " + result + "\n解析：" + reason);
    }

    public void start(GroupMessageEvent event){
        MessageChain chain = event.getMessage();
        if (chain.contentToString().equals(".猜字谜") && ! ZimiBuffer.groupGuessingZimi.containsKey(event.getGroup().getId())) {
            ZimiObject zimiObject = new ZimiObject();//建立字谜对象
            zimiObject.getZimi();
            ZimiBuffer.groupGuessingZimi.put(event.getGroup().getId(),zimiObject);//将字谜对象加入Hashmap
            event.getSubject().sendMessage("猜字谜开始！\n问题：" + zimiObject.getQuestion()+ "\n使用（.猜）来输入答案\n使用(.放弃)放弃猜字谜");
        }else if (chain.contentToString().contains(".猜")) {
            Zimi.guess(chain,event,ZimiBuffer.groupGuessingZimi.get(event.getGroup().getId()));
        } else if (chain.contentToString().contains(".放弃")) {
            Zimi.giveUp(chain,event);
        }

    }


    @Override
    public void onRun(GroupMessageEvent event) {
            start(event);
    }
    public static void register(){
        Zimi zimi = new Zimi();
        EventExecuter.register(".猜字谜",zimi);
        EventExecuter.register(".猜",zimi);
        EventExecuter.register(".放弃",zimi);
    }
}
