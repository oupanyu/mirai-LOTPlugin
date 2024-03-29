package top.oupanyu;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.MiraiLogger;
import top.oupanyu.config.Config;
import top.oupanyu.database.AbstractDatabase;
import top.oupanyu.functions.APictureADay;
import top.oupanyu.functions.bilibili.GetBVideoInfo;
import top.oupanyu.functions.Help;
import top.oupanyu.functions.PixivPic;
import top.oupanyu.functions.RandomPoem;
import top.oupanyu.functions.Zimi.Zimi;
import top.oupanyu.functions.baike.baidu.BaiduBaike;
import top.oupanyu.functions.baike.moegirl.MoeGirl;
import top.oupanyu.functions.cloudmusic.NeteaseCloudMusic;
import top.oupanyu.functions.guesssong.AppendSong;
import top.oupanyu.functions.guesssong.GuessSong;
import top.oupanyu.functions.kugou.KugouAPI;
import top.oupanyu.functions.openai.Chat;
import top.oupanyu.functions.rhythm.guessing.RhythmGuessing;
import top.oupanyu.functions.translation.baidu.BaiduTranslateCommand;
import top.oupanyu.functions.translation.baidu.Translation;
import top.oupanyu.functions.transmission.PacketListener;
import top.oupanyu.functions.transmission.PacketSender;
import top.oupanyu.command.ChatCommand;
import top.oupanyu.command.GroupChat;
import top.oupanyu.command.Reconnect;
import top.oupanyu.command.SendMessage2Server;
import top.oupanyu.excuter.EventExecuter;
import top.oupanyu.functions.xunfei.spark.XunfeiSpark;

import java.io.IOException;
import java.net.Socket;


public final class Main extends JavaPlugin {
    public static final Main INSTANCE = new Main();


    public static final MiraiLogger LOGGER = INSTANCE.getLogger();

    public static final Config configloader = ConfigLoaderNew.configLoaderNew();

    public static AbstractDatabase database = null;

    public static Socket socket;




    private Main() {
        super(new JvmPluginDescriptionBuilder("top.oupanyu.qqbot", "0.5.5")
                .name("LOT plugin")
                .author("panyuou")
                .info("GroupPlugin")
                .build());
    }

    @Override
    public void onEnable() {
        Preload.init();

        CommandManager.INSTANCE.registerCommand(ChatCommand.INSTANCE,false);
        CommandManager.INSTANCE.registerCommand(Translation.INSTANCE,false);
        CommandManager.INSTANCE.registerCommand(GroupChat.INSTANCE,false);


        //Test.run();


        GuessSong.configure();//configure SongGuess when boot up
        registerInit();

        EventExecuter.initGroup();

        //System.setProperty("file.encoding","UTF-8");
        if (Main.configloader.transmission.transmission) {

            CommandManager.INSTANCE.registerCommand(SendMessage2Server.INSTANCE,false);
            CommandManager.INSTANCE.registerCommand(Reconnect.INSTANCE,false);

            try {
                socket = new Socket(configloader.transmission.server_ip, configloader.transmission.server_port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(new PacketListener()).start();
            GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
                if (event.getGroup().getId() == Long.valueOf(configloader.transmission.groupnum) &&
                        !event.getMessage().contentToString().equals("!重连")) {
                    PacketSender.send(event);
                }else if (event.getMessage().contentToString().equals("!重连")){
                    try {
                        socket.close();
                        socket = new Socket(configloader.transmission.server_ip, configloader.transmission.server_port);
                        event.getSubject().sendMessage("重连成功！");
                    } catch (IOException e) {
                        event.getSubject().sendMessage("重连失败！");
                    }

                }

            });
        }

        if (configloader.openai.openai_enable){

            GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
                try {
                    String content = event.getMessage().contentToString();
                    if (content.contains(".chat ")){
                        if (Chat.onProccessing){
                            event.getSubject().sendMessage("AI还在处理呢！");
                        }else {
                            Chat.chatOrCreate(event);
                        }

                    }else if (content.contains("#重置会话")){
                        Chat.reset(event.getGroup().getId());
                    }
                }catch (Exception e){
                    Chat.onProccessing=false;
                    e.printStackTrace();
                }

            });
        }


        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
            String content = chain.contentToString();
            if (chain.contentToString().contains("&kugou")){
                KugouAPI.getMusic(chain,event);
            }else if (content.contains("&kid ")){
              KugouAPI.getMusicURL(event);
            } else if (chain.contentToString().contains("&kgmv")){
                KugouAPI.getMV(chain,event);
            }
            GuessSong.init(event);
            BaiduTranslateCommand.init(event);

        });

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, EventExecuter::execute);


        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
            if (chain.contentToString().contains(".帮助")){
                event.getSubject().sendMessage(Help.helpText);
            }

        });

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupTempMessageEvent.class, AppendSong::init);
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class,AppendSong::init);
        //listener.complete(); // 停止监听

        registerEvent();

        LOGGER.info("Plugin load done!");
    }

    public void registerEvent(){
        EventExecuter.register(".B站",new GetBVideoInfo());
        EventExecuter.register(".moegirl",new MoeGirl());
        EventExecuter.register(".baike",new BaiduBaike());
        EventExecuter.register("..p站随机图片",new PixivPic());
        Zimi.register();
        EventExecuter.register(".来首词",new RandomPoem());
        EventExecuter.register("..每日一图",new APictureADay());
        NeteaseCloudMusic.register();
        EventExecuter.register("/rhythm",new RhythmGuessing());
        EventExecuter.register(".spark",new XunfeiSpark());
    }

    public void registerInit(){
        RhythmGuessing.init();
    }
}