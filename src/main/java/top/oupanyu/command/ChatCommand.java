package top.oupanyu.command;

import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import top.oupanyu.Functions.openai.Chat;
import top.oupanyu.Functions.transmission.PacketSender;
import top.oupanyu.Main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
