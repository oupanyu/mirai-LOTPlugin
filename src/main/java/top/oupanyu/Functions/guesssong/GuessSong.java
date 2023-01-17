package top.oupanyu.Functions.guesssong;

import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.sql.*;
import java.util.Random;

public class GuessSong {
    private static String url = "jdbc:sqlite::data:lotplugin/AbstractSong.db";
    private static Connection conn;

    private static boolean isEnable = false;

    public static void configure(){
        try {
            conn = DriverManager.getConnection(url);
            isEnable = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void init(GroupMessageEvent event){
        String content=event.getMessage().contentToString();
        if (content.equals("#cxcsq")){
            startGuessing(event);
        }
    }

    public static void startGuessing(GroupMessageEvent event){
        try {
            Statement statement = conn.createStatement();
            String sql4count = "SELECT Count(*) FROM songs";
            ResultSet rs = statement.executeQuery(sql4count);
            int maxCount = rs.getInt(0);
            int sid = new Random().nextInt(maxCount);
            System.out.println(sid);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
