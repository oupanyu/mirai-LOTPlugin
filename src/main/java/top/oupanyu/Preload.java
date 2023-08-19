package top.oupanyu;

import top.oupanyu.database.Drivers;
import top.oupanyu.database.MySQL;
import top.oupanyu.database.SQLite;

import java.io.File;

import static top.oupanyu.Main.configloader;

public class Preload {
    public static void init(){
        File[] dir = new File[4];
        dir[0] = new File("data/cache");
        dir[1] = new File("data/cache/pcache");
        dir[2] = new File("data/cache/baike");
        dir[3] = new File("data/cache/moegirl");
        for (int i = 0;i < dir.length;i++){
            if (!dir[i].exists()){
                dir[i].mkdirs();
            }
        }//create folders on boot up

        preloadDatabase();
    }

    public static void preloadDatabase(){
        switch (configloader.database.db){
            case "mysql" : configloader.setDB(Drivers.MYSQL);
                            Main.database = new MySQL().initConnection(configloader.database.address,
                                    configloader.database.username,
                                    configloader.database.password);
                            break;
            case "sqlite" :
            default:
                configloader.setDB(Drivers.SQLITE);
                Main.database = new SQLite().initConnection(configloader.database.address);
                break;
        }


    }

}
