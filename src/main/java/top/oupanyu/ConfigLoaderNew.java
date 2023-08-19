package top.oupanyu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import top.oupanyu.config.Config;
import top.oupanyu.config.ConfigDefault;

import java.io.*;


public class ConfigLoaderNew {


    public static Config configLoaderNew() {
        try {
            String folder = "config/panyuou";
            String fileName = "config/panyuou/config.json";
            File file = new File(fileName);
            File folderFile = new File(folder);
            if (!folderFile.exists()){
                folderFile.mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();
                ConfigDefault config = new ConfigDefault();
                Gson yaml = new GsonBuilder().setPrettyPrinting().create();
                System.out.println(yaml.toJson(config));
                try (FileWriter writer = new FileWriter(fileName)){
                    yaml.toJson(config,writer);
                }
                Main.logger.error("[Mirai-LOTPlugin] Please do some configs at first!Config file is in "+fileName);
                System.exit(1);
            }
            FileReader fileReader = new FileReader(file);
            Gson gson = new Gson();
            Config config = gson.fromJson(fileReader,Config.class);
            return config;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
