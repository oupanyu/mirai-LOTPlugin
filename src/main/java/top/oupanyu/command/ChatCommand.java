package top.oupanyu.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import top.oupanyu.functions.openai.Chat;
import top.oupanyu.Main;

public class ChatCommand extends JRawCommand {
    public static ChatCommand INSTANCE = new ChatCommand();
    public ChatCommand() {
        super(Main.INSTANCE, "chat");
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        // 处理指令
        Chat.chatOrCreate(args.contentToString());

    }
}
