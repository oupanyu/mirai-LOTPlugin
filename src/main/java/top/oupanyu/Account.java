package top.oupanyu;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.BotConfiguration;

public interface Account {
    public Bot newBot(Long qq, String passwd, BotConfiguration configuration);
}
