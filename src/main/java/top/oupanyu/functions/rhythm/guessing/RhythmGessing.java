package top.oupanyu.functions.rhythm.guessing;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.functions.guesssong.SongGuessing;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Random;

public class RhythmGessing implements GroupMessageExecuter {

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
        if (isInit){

        }
    }

    private Rhythm getRandomSongFromDatabase(){
        try {
            Statement statement = conn.createStatement();
            String sql4count = "SELECT Count(*) FROM Rhythm where enable=1";
            int maxCount = statement.executeQuery(sql4count).getInt(1) + 1;
            int sid = new Random().nextInt(1, maxCount);

            String sql = "SELECT songname FROM Rhythm where id=" + sid;
            ResultSet resultSet = statement.executeQuery(sql);
            SongGuessing sg = new SongGuessing(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));

        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
        return null;
    }





    private void start(){

    }
}
