package top.oupanyu;

import com.github.plexpt.chatgpt.Chatbot;
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
import top.oupanyu.Functions.chatgpt.ChatGPT;
import top.oupanyu.Functions.pixiv.Pixiv;
import top.oupanyu.Functions.transmission.PacketListener;
import top.oupanyu.Functions.transmission.PacketSender;
import top.oupanyu.command.Reconnect;
import top.oupanyu.command.SendMessage2Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;


public final class Main extends JavaPlugin {
    public static final Main INSTANCE = new Main();

    public static final MiraiLogger logger = INSTANCE.getLogger();

    public static final ConfigLoader configloader = new ConfigLoader();

    public static Socket socket;

    public static Chatbot chatbot ;




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
                if (event.getGroup().getId() == Main.configloader.getGroupnum() && !event.getMessage().contentToString().equals("!??????")) {
                    PacketSender.send(event);
                }else if (event.getMessage().contentToString().equals("!??????")){
                    try {
                        socket.close();
                        socket = new Socket(configloader.getServer_ip(), configloader.getServer_port());
                        event.getSubject().sendMessage("???????????????");
                    } catch (IOException e) {
                        event.getSubject().sendMessage("???????????????");
                    }

                }

            });
        }

        if (configloader.getOpenai_enable()){
            chatbot = new Chatbot(configloader.getChatGPT_key());
            GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
                if (event.getMessage().contentToString().equals(".??????ai??????") && !ChatGPT.onProcessing){
                    chatbot.resetChat();
                    event.getGroup().sendMessage("???????????????");
                }

                if (event.getMessage().contentToString().contains(".ai") && !ChatGPT.onProcessing) {
                    new ChatGPT().run(event, chatbot);
                }else if (event.getMessage().contentToString().contains(".ai") && ChatGPT.onProcessing){
                    event.getGroup().sendMessage("AI??????????????????");
                }
            });

        }

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // ???????????????????????????, ???????????? `GroupMessageEvent`
            if (chain.contentToString().equals(".?????????")){
                RandomPoem.getRandomPoem(chain,event);
            } else if (chain.contentToString().equals(".?????????") ||
                    chain.contentToString().contains(".??? ") ||
                    chain.contentToString().equals(".??????")){
                Zimi zimi = new Zimi();
                zimi.start(chain,event);
            }

        });

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // ???????????????????????????, ???????????? `GroupMessageEvent`
            String content = chain.contentToString();
            if (chain.contentToString().contains("&kugou")){
                KugouAPI.getMusic(chain,event);
            }else if (content.contains("&kid ")){
              KugouAPI.getMusicURL(event);
            } else if (chain.contentToString().contains(".??????mv")){
                KugouAPI.getMV(chain,event);
            }else if (chain.contentToString().contains(".???????????????")) {
                NeteaseCloudMusic.getMusic(chain,event);
            }else if (chain.contentToString().contains(".?????????mv")){
                NeteaseCloudMusic.getMV(chain,event);
            }else if (chain.contentToString().contains("..????????????")){
                APictureADay.getPic(chain,event);
            }else if (chain.contentToString().contains("..p???????????????")) {
                PixivPic.getPic(chain,event);
            }

            Pixiv.init(event);


        });

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // ???????????????????????????, ???????????? `GroupMessageEvent`
            if (chain.contentToString().contains(".B???")){
                try {
                    GetBVideoInfo.getbyAID(chain,event);
                }catch (NumberFormatException ignored){
                    GetBVideoInfo.getByBID(chain,event);
                }


            }

        });


        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event->{
            MessageChain chain=event.getMessage(); // ???????????????????????????, ???????????? `GroupMessageEvent`
            if (chain.contentToString().contains(".??????")){
                event.getSubject().sendMessage(Help.helpText);
            }

        });
        

        //listener.complete(); // ????????????

        logger.info("Plugin load done!");
    }


}