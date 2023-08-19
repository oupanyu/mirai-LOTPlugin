package top.oupanyu.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import top.oupanyu.Main;

import java.io.IOException;
import java.net.Socket;

import static top.oupanyu.Main.configloader;

public class Reconnect extends JRawCommand {

    public static Reconnect INSTANCE = new Reconnect();

    private Reconnect() {
        super(Main.INSTANCE,"reconnect");
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        // 处理指令
        try {
            Main.logger.info("Reconnecting...");
            Main.socket.close();
            Main.socket = new Socket(configloader.transmission.server_ip, configloader.transmission.server_port);
            Main.logger.info("Reconnect done!");
        } catch (IOException e) {
            Main.logger.warning("Reconnect failed!");
        }

    }
}
