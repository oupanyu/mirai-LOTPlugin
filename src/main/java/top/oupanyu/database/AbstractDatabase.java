package top.oupanyu.database;



import java.sql.*;

public abstract class AbstractDatabase {
    protected String address = "";
    protected Connection connection;
    public AbstractDatabase(Drivers drivers){
        drivers.initDriver();
        this.address = address;
    }

    /**
     * 获取数据库Connection，必须实现。
     */
    protected abstract AbstractDatabase initConnection(String address,String username,String password);
    protected void setConnection(Connection connection){
        this.connection = connection;
    }
    public void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet execute(String sql){
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insert(PreparedStatement statement){
        try {
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public PreparedStatement getPreparedStatement(String sql){
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
