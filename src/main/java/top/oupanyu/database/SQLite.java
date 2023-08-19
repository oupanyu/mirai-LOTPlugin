package top.oupanyu.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends AbstractDatabase{
    private static final String url = "jdbc:sqlite:%s";
    public SQLite() {
        super(Drivers.SQLITE);
    }

    @Override
    protected AbstractDatabase initConnection(String address, String username, String password) {
        try {
            setConnection(DriverManager.getConnection(address));
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public AbstractDatabase initConnection(String address){
        return initConnection(String.format(url,address),"","");
    }
}
