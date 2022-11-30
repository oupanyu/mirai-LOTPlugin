package top.oupanyu;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.MiraiLogger;
import top.oupanyu.Functions.APictureADay;
import top.oupanyu.Functions.Bilibili.GetBVideoInfo;
import top.oupanyu.Functions.KugouAPI;
import top.oupanyu.Functions.PixivPic;
import top.oupanyu.Functions.RandomPoem;
import top.oupanyu.Functions.Zimi.Zimi;
import top.oupanyu.request.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;


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
            if (chain.contentToString().contains(".音乐搜索")){
                KugouAPI.getMusic(chain,event);
            }else if (chain.contentToString().contains(".mv")){
                KugouAPI.getMV(chain,event);
            }else if (chain.contentToString().contains("..每日一图")){
                APictureADay.getPic(chain,event);
            } else if (chain.contentToString().contains("..p站随机图片")) {
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
        

        //listener.complete(); // 停止监听

        logger.info("Plogin load done!");
    }
}