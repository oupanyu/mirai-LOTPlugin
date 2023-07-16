package top.oupanyu.excuter;

import net.mamoe.mirai.event.events.GroupMemberEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.oupanyu.helper.Command;

public class EventExecuter {
    private static EventRegister<GroupMessageExecuter> eventRegister;
    public static void initGroup(){
        eventRegister = new EventRegister<>();
    }
    public static boolean register(String key,GroupMessageExecuter executer){
        return eventRegister.register(key,executer);
    }

    public static void execute(GroupMessageEvent event){
        Command command = Command.getInstance(event.getMessage().contentToString());
        String key = command.getContent(0);
        if (eventRegister.getMethodMap().containsKey(key)){
            GroupMessageExecuter executer = eventRegister.getMethodMap().get(key);
            executer.onRun(event);
        }
    }
}
