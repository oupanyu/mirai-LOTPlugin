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
import top.oupanyu.Functions.*;
import top.oupanyu.Functions.Bilibili.GetBVideoInfo;
import top.oupanyu.Functions.Zimi.Zimi;
import top.oupanyu.Functions.baike.baidu.BaiduBaike;
import top.oupanyu.Functions.baike.moegirl.MoeGirl;
import top.oupanyu.Functions.cloudmusic.NeteaseCloudMusic;
import top.oupanyu.Functions.guesssong.AppendSong;
import top.oupanyu.Functions.guesssong.GuessSong;
import top.oupanyu.Functions.kugou.KugouAPI;
import top.oupanyu.Functions.openai.Chat;
import top.oupanyu.Functions.translation.baidu.BaiduTranslateCommand;
import top.oupanyu.Functions.transmission.PacketListener;
import top.oupanyu.Functions.transmission.PacketSender;
import top.oupanyu.command.ChatCommand;
import top.oupanyu.command.GroupChat;
import top.oupanyu.command.Reconnect;
import top.oupanyu.command.SendMessage2Server;
import top.oupanyu.Functions.translation.baidu.Translation;
import top.oupanyu.excuter.EventExecuter;

import java.io.File;
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
        CommandManager.INSTANCE.registerCommand(ChatCommand.INSTANCE,false);
        CommandManager.INSTANCE.registerCommand(Translation.INSTANCE,false);
        CommandManager.INSTANCE.registerCommand(GroupChat.INSTANCE,false);


        //Test.run();

        File[] dir = new File[4];
        dir[0] = new File("data/cache");
        dir[1] = new File("data/cache/pcache");
        dir[2] = new File("data/cache/baike");
        dir[3] = new File("data/cache/moegirl");
        for (int i = 0;i < dir.length;i++){
            if (!dir[i].exists()){
                dir[i].mkdirs();
            }
        }//create folders on boot up
        GuessSong.configure();//configure SongGuess when boot up

        EventExecuter.initGroup();

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

        if (configloader.getOpenai_enable()){
            /*GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
                String content = event.getMessage().contentToString();
                if (content.contains(".ai ")){
                    if (GPT3.onProcessing){
                        event.getSubject().sendMessage("AI还在处理呢！");
                    }else {
                        new GPT3().run(event);
                    }

                }else if (content.contains("$重置会话")){
                    GPT3.reset(event);
                }
            });*/

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
            String content = chain.contentToString();
            if (chain.contentToString().contains("&kugou")){
                KugouAPI.getMusic(chain,event);
            }else if (content.contains("&kid ")){
              KugouAPI.getMusicURL(event);
            } else if (chain.contentToString().contains("&kgmv")){
                KugouAPI.getMV(chain,event);
            }else if (chain.contentToString().contains(".ncm")) {
                NeteaseCloudMusic.getMusic(chain,event);
            } else if (content.contains(".nid ")) {
                NeteaseCloudMusic.getMusicURL(event);
            } else if (chain.contentToString().contains(".网易云mv ")){
                NeteaseCloudMusic.getMV(chain,event);
            }else if (chain.contentToString().contains("..每日一图")){
                APictureADay.getPic(chain,event);
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

        logger.info("Plugin load done!");
    }

    public void registerEvent(){
        EventExecuter.register(".B站",new GetBVideoInfo());
        EventExecuter.register(".moegirl",new MoeGirl());
        EventExecuter.register(".baike",new BaiduBaike());
        EventExecuter.register("..p站随机图片",new PixivPic());
    }


}