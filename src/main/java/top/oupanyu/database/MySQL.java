package top.oupanyu.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends AbstractDatabase{

    String uri = "jdbc:mysql://%s?useSSL=false&allowPublicKeyRetrieval=true";

    public MySQL() {
        super(Drivers.MYSQL);
    }

    @Override
    public AbstractDatabase initConnection(String address, String username, String password) {
        try {
            address = String.format(uri,address);
            Connection connection1 = DriverManager.getConnection(address,username,password);
            setConnection(connection1);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
