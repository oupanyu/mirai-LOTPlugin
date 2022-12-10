package top.oupanyu;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.MiraiLogger;
import top.oupanyu.Functions.*;
import top.oupanyu.Functions.Bilibili.GetBVideoInfo;
import top.oupanyu.Functions.Zimi.Zimi;
import top.oupanyu.Functions.transmission.PacketListener;
import top.oupanyu.Functions.transmission.PacketSender;
import top.oupanyu.command.Reconnect;
import top.oupanyu.command.SendMessage2Server;

import java.io.IOException;
import java.net.Socket;


public final class Main extends JavaPlugin {
    public static final Main INSTANCE = new Main();

    public static final MiraiLogger logger = INSTANCE.getLogger();

    public static final ConfigLoader configloader = new ConfigLoader();

    public static Socket socket;




    private Main() {
        super(new JvmPluginDescriptionBuilder("top.oupanyu.qqbot", "0.2.0")
                .name("LOT plugin")
                .author("panyuou")
                .info("GroupPlugin")
                .build());
    }

    @Override
    public void onEnable() {



        //System.setProperty("file.encoding","UTF-8");
        if (Main.configloader.getTransmission()) {

            CommandManager.INSTANCE.registerCommand(SendMessage2Server.INSTANCE,false);
            CommandManager.INSTANCE.registerCommand(Reconnect.INSTANCE,false);

            try {
                socket = new Socket(configloader.getServer_ip(), configloader.getServer_port());
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(new PacketListener()).start();
            GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
                if (event.getGroup().getId() == Main.configloader.getGroupnum() && !event.getMessage().contentToString().equals("!重连")) {
                    PacketSender.send(event);
                }else if (event.getMessage().contentToString().equals("!重连")){
                    try {
                        socket.close();
                        socket = new Socket(configloader.getServer_ip(), configloader.getServer_port());
                        event.getSubject().sendMessage("重连成功！");
                    } catch (IOException e) {
                        event.getSubject().sendMessage("重连失败！");
                    }

                }

            });
        }

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
            if (chain.contentToString().equals(".来首词")){
                RandomPoem.getRandomPoem(chain,event);
            } else if (chain.contentToString().equals(".猜字谜") ||
                    chain.contentToString().contains(".猜 ") ||
                    chain.contentToString().equals(".放弃")){
                Zimi zimi = new Zimi();
                zimi.start(chain,event);
            }

        });

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
            if (chain.contentToString().contains(".酷狗音乐")){
                KugouAPI.getMusic(chain,event);
            }else if (chain.contentToString().contains(".酷狗mv")){
                KugouAPI.getMV(chain,event);
            }else if (chain.contentToString().contains(".网易云音乐")) {
                NeteaseCloudMusic.getMusic(chain,event);
            }else if (chain.contentToString().contains(".网易云mv")){
                NeteaseCloudMusic.getMV(chain,event);
            }else if (chain.contentToString().contains("..每日一图")){
                APictureADay.getPic(chain,event);
            }else if (chain.contentToString().contains("..p站随机图片")) {
                PixivPic.getPic(chain,event);
            }


        });

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
            if (chain.contentToString().contains(".B站")){
                try {
                    GetBVideoInfo.getbyAID(chain,event);
                }catch (NumberFormatException ignored){
                    GetBVideoInfo.getByBID(chain,event);
                }


            }

        });


        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
            if (chain.contentToString().contains(".帮助")){
                event.getSubject().sendMessage(Help.helpText);
            }

        });
        

        //listener.complete(); // 停止监听

        logger.info("Plugin load done!");
    }


}