package top.oupanyu.Functions.guesssong;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import top.oupanyu.helper.Command;
import top.oupanyu.helper.CommandHelper;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GuessSong {
    private static final String url = "jdbc:sqlite:data/lotplugin/AbstractSong.db";
    private static Connection conn;

    private static boolean isEnable = false;

    public static void configure(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            isEnable = true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void init(GroupMessageEvent event){
        if (isEnable) {
            String content = event.getMessage().contentToString();
            Command command = new Command(CommandHelper.parseStringToList(content));
            command.setFirstKey("/cxcsq");
            if (command.isFirstKeyEquals()){
                if (command.equals(1,"append")){
                    event.getGroup().sendMessage("It's not usable yet!");
                }else if (command.equals(1,"start")){
                    startGuessing(event);
                }else if (command.equals(1,"guess")){
                    guess(event,command);
                }
            }
        }
    }

    private static HashMap<Long,SongGuessing> gSongMap = new HashMap<>();
    public static void startGuessing(GroupMessageEvent event){
        try {
                Long gid = event.getGroup().getId();
                Group group = event.getGroup();
                Statement statement = conn.createStatement();
                String sql4count = "SELECT Count(*) FROM songsVC where available=1";
                int maxCount = statement.executeQuery(sql4count).getInt(1)+1;
                int sid = new Random().nextInt(1, maxCount);

                String sql = "SELECT question,answer,content2Chinese FROM songsVC where id=" + sid;
                ResultSet resultSet = statement.executeQuery(sql);
                SongGuessing sg = new SongGuessing(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                gSongMap.put(gid,sg);
                group.sendMessage("答题开始!问:\n" + sg.getQuestion());

        } catch (SQLException e) {
            e.printStackTrace();
            event.getGroup().sendMessage("答题系统出错!请联系管理员.");
        }
    }
    private static void guess(GroupMessageEvent event,Command command){
        Long gid = event.getGroup().getId();
        Group group = event.getGroup();
        String gansw = command.getContent(2);
        SongGuessing sg = gSongMap.get(gid);
        String zhCN = sg.getContent2Chinese();
        String oSName = sg.getAnswer();

        if (gansw.contains(zhCN) || gansw.contains(oSName)){
            String corretctt = "答题正确!\n歌曲名:"+oSName+"\n翻译:"+zhCN;
            MessageChainBuilder chain = new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append(corretctt);
            group.sendMessage(chain.build());
        } else {
            String wrongctt = "答错了！请再试一次！";
            MessageChainBuilder chain = new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append(wrongctt);

            group.sendMessage(chain.build());
        }

    }

    public static void appendVSong(SongGuessing sg) throws SQLException {
        String sql = "INSERT INTO songsVC (question, answer, content2Chinese) VALUES (?,?,?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1,sg.getQuestion());
        statement.setString(2,sg.getAnswer());
        statement.setString(3,sg.getContent2Chinese());
        statement.execute();
        statement.close();
    }
}
