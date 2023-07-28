package top.oupanyu.functions.rhythm.guessing;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.functions.guesssong.SongGuessing;
import top.oupanyu.helper.Command;
import top.oupanyu.utils.StringUtils;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RhythmGuessing implements GroupMessageExecuter {

    private HashMap<Long,Rhythm> guessingMap = new HashMap<>();


    private static Connection conn;
    private static final String url = "jdbc:sqlite:data/lotplugin/Rhythm.db";
    private static final File file = new File("data/lotplugin/Rhythm.db");
    private static final File folder = new File("data/lotplugin/");
    public static boolean isInit = false;

    public static void init(){
        try {
            if (!folder.exists()){
                folder.mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();
            }

                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(url);
                isInit = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onRun(GroupMessageEvent event) {
        Command command = Command.getInstance(event.getMessage().contentToString());
        long gid = event.getGroup().getId();
        if (!isInit) {init();}
        if (isInit){
            if (!command.getContent(1).equals("guess")){
                return;
            }

            String fun = command.getContent(2);
            MessageChainBuilder messages = new MessageChainBuilder();
            messages.add(new QuoteReply(event.getMessage()));
            switch (fun) {
                case "start":
                    if (guessingMap.containsKey(gid)) {
                        messages.add("当前有未完成的猜曲任务，请先完成或输入/rhythm guess abandon放弃哦");
                    }else {
                        start(gid,event);
                    }

                    break;
                case "guess":
                    guess(gid,event,command);
                    break;
                case "abandon":
                    int correct = guessingMap.get(gid).getCorrect();
                    guessingMap.remove(gid);
                    messages.add(
                            String.format("本次猜曲结束!您总共答对了%s题",correct));
                    break;
                case "open":
                    open(gid,event,command);
                    break;
                default:
                    return;
            }
            event.getSubject().sendMessage(messages.build());


        }
    }

    private Rhythm getRandomSongFromDatabase(){
        try {
            Statement statement = conn.createStatement();
            String sql4count = "SELECT Count(*) FROM Rhythm where enable=1";
            int maxCount = statement.executeQuery(sql4count).getInt(1) + 1;
            Rhythm rhythm = new Rhythm();
            for (int i =0;i<5;i++){
                int sid = new Random().nextInt(1, maxCount);
                String sql = "SELECT songname FROM Rhythm where id=" + sid;
                ResultSet resultSet = statement.executeQuery(sql);
                rhythm.append(resultSet.getString(2));
            }

        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
        return null;
    }





    private void start(long gid,GroupMessageEvent event){
        Rhythm rhythm = getRandomSongFromDatabase();
        guessingMap.put(gid,rhythm);
        MessageChainBuilder messages = new MessageChainBuilder();
        messages.add("猜曲开始！请看题：");
        assert rhythm != null;
        List<String> guessing = rhythm.getGuessing();
        for (int i=0;i<guessing.size();i++){
            messages.add(String.format("%d,%s",i+1,guessing.get(i)));
        }
        messages.add("开字请用/rhythm guess open");
        event.getSubject().sendMessage(messages.build());
    }

    private void open(long gid,GroupMessageEvent event,Command command){
        MessageChainBuilder messages = new MessageChainBuilder();
        char[] word = command.getContent(2).toCharArray();
        if (word.length != 1){
            messages.add(new QuoteReply(event.getMessage()));
            messages.add("请输入单个字符！");
        }else {
            Rhythm rhythm = guessingMap.get(gid);
            List<String> origin = rhythm.getOrigin();
            List<String> guessing = rhythm.getGuessing();
            for (int i =0;i<origin.size();i++){
                String openContent = StringUtils.OpenWordFromAsterisk(guessing.get(i),
                        origin.get(i),word[0]);
                rhythm.change(i,openContent);
            }
            guessing = rhythm.getGuessing();
            messages.add("当前开字符：" + word[0]);
            messages.add("\n歌曲列表:\n");
            for (int i =0;i<guessing.size();i++){
                messages.add(String.format("%d. %s",i,guessing.get(i)));
            }
        }
        event.getSubject().sendMessage(messages.build());
    }

    private void guess(long gid,GroupMessageEvent event,Command command){
        MessageChainBuilder messages = new MessageChainBuilder();
        String answer = command.getContent(2);
        Rhythm rhythm = guessingMap.get(gid);
        List<String> origin = rhythm.getOrigin();
        List<String> guessing = rhythm.getGuessing();
        for (int i=0;i<origin.size();i++){
            boolean isSimilar = StringUtils.isStringSimilar(answer,origin.get(i));
            if (isSimilar){
                messages.add("开出了"+origin.get(i));
                rhythm.change(i, origin.get(i));
                rhythm.correct(1);
                for (int j =0;j<guessing.size();j++){
                    messages.add(String.format("%d. %s",i,guessing.get(i)));
                }
                break;
            }
        }



        event.getSubject().sendMessage(messages.build());
    }
}
