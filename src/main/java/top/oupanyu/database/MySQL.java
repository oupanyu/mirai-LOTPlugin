package top.oupanyu.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends AbstractDatabase{

    String uri = "jdbc:mysql://%s?useSSL=false&allowPublicKeyRetrieval=true";

    public MySQL(Drivers drivers) {
        super(drivers);
    }

    @Override
    public void initConnection(String address, String username, String password) {
        try {
            address = String.format(uri,address);
            Connection connection1 = DriverManager.getConnection(address,username,password);
            setConnection(connection1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
