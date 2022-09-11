package top.oupanyu;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.Functions.APictureADay;
import top.oupanyu.Functions.KugouAPI;
import top.oupanyu.Functions.RandomPoem;
import top.oupanyu.Functions.Zimi.Zimi;
import top.oupanyu.Functions.Zimi.ZimiObject;

import java.util.HashMap;


public final class Main extends JavaPlugin {
    public static final Main INSTANCE = new Main();


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
            if (chain.contentToString().equals(".来首词")){RandomPoem.getRandomPoem(chain,event);}
            else if (chain.contentToString().equals(".猜字谜") || chain.contentToString().contains(".猜 ")){
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
            }/*else if (chain.contentToString().contains(".每日一图")){
                APictureADay.getPic(chain,event);
            }*/


        });

        //listener.complete(); // 停止监听

        getLogger().info("Group Plugin loaded!");
    }
}