package top.oupanyu.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends AbstractDatabase{
    private static final String url = "jdbc:sqlite:%s";
    public SQLite(Drivers drivers) {
        super(drivers);
    }

    @Override
    protected void initConnection(String address, String username, String password) {
        try {
            setConnection(DriverManager.getConnection(address));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void initConnection(String address){
        initConnection(String.format(url,address),"","");
    }
}
