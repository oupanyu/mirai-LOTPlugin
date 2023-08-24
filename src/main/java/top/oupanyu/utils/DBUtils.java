package top.oupanyu.utils;

import top.oupanyu.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    public static void executeCommand(String sql){
        try {
            Connection connection = Main.database.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
