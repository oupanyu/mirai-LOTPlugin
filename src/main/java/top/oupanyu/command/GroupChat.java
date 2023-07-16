package top.oupanyu.command;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;
import top.oupanyu.Main;
import top.oupanyu.helper.Command;

import static top.oupanyu.Main.logger;

public class GroupChat extends JRawCommand {

    public static GroupChat INSTANCE = new GroupChat();

    public GroupChat() {
        super(Main.INSTANCE,"gchat");
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        try {
            if(args.get(0).contentToString().equals("list")){
                long qq = Long.parseLong(args.get(1).contentToString());
                logger.info(Bot.getInstance(qq).getGroups().toString());
                return;
            }

            //Main.logger.info(args.toString());
            long gid = Long.parseLong(args.get(1).contentToString());
            long qq = Long.parseLong(args.get(0).contentToString());
            StringBuffer stringBuffer = new StringBuffer();
            for (int i=2;i<args.size();i++){
                stringBuffer.append(args.get(i));
                stringBuffer.append(" ");
            }
            //logger.info(stringBuffer.toString());
            Bot.getInstance(qq).getGroups().get(gid).sendMessage(stringBuffer.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
