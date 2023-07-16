package top.oupanyu.excuter;

import net.mamoe.mirai.event.events.GroupMemberEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public interface GroupMessageExecuter {
    void onRun(GroupMessageEvent event);
}
