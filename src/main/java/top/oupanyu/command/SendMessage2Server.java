package top.oupanyu.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import top.oupanyu.functions.transmission.PacketSender;
import top.oupanyu.Main;

public class SendMessage2Server extends JRawCommand {

    public static final SendMessage2Server INSTANCE = new SendMessage2Server();

    public SendMessage2Server() {
        super(Main.INSTANCE, "send2server");
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        // 处理指令
        PacketSender.send(args.toString());

    }
}
