package top.oupanyu;

import org.yaml.snakeyaml.Yaml;
import top.oupanyu.config.Config;

import java.io.*;


public class ConfigLoaderNew {


    public static Config configLoaderNew() {
        try {
            String folder = "config/panyuou";
            String fileName = "config/panyuou/config.yaml";
            File file = new File(fileName);
            File folderFile = new File(folder);
            if (!folderFile.exists()){
                folderFile.mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();
                Config config = new Config();
                Yaml yaml = new Yaml();
                new FileWriter(fileName)
                        .write(yaml.dump(config));
                Main.logger.error("[Mirai-LOTPlugin] Please do some configs at first!Config file is in "+fileName);
                System.exit(1);
            }
            FileInputStream inputStream = new FileInputStream(file);
            Yaml yaml = new Yaml();
            Config config = yaml.loadAs(inputStream,Config.class);
            return config;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
