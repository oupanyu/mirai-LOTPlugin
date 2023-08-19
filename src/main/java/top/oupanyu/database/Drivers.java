package top.oupanyu.database;

public enum Drivers {
    MYSQL("com.mysql.cj.jdbc.Driver"),SQLITE("org.sqlite.JDBC");


    private String driver;

    Drivers(String driver){
        this.driver = driver;
    }

    public boolean initDriver(){
        try {
            Class.forName(driver);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
