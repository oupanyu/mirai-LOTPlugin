package top.oupanyu;

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


public final class Main extends JavaPlugin {
    public static final Main INSTANCE = new Main();

    public static final MiraiLogger logger = INSTANCE.getLogger();

    public static final ConfigLoader configloader = new ConfigLoader();


    private Main() {
        super(new JvmPluginDescriptionBuilder("top.oupanyu.qqbot", "0.1.0")
                .name("Demo")
                .author("panyuou")
                .info("GroupPlugin")
                .build());
    }

    @Override
    public void onEnable() {
        if (Main.configloader.getTransmission()) {
            new Thread(new PacketListener()).start();
            GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
                if (event.getGroup().getId() == Main.configloader.getGroupnum()) {
                    PacketSender.send(event);
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

        logger.info("Plogin load done!");
    }
}