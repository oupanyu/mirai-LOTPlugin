package top.oupanyu;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigLoader {

    private String tiankey = "";

    public ConfigLoader() {
        try {
            String folder = "config/panyuou";
            String fileName = "config/panyuou/config.yml";
            File file = new File(fileName);
            File folderFile = new File(folder);
            if (!file.exists() || !folderFile.exists()) {
                String content = "#TianAPI Key\n" +
                        "tiankey: \"1a2b3c4d5e6f7g\"";
                folderFile.mkdirs();
                file.createNewFile();
                FileWriter fileWritter = new FileWriter(fileName, true);
                fileWritter.write(content);
                fileWritter.close();
                Main.logger.warning("配置已生成！请先调整配置后再打开后端");
                System.exit(0);
            }
            Yaml yaml = new Yaml();
            Object obj = yaml.load(Files.newInputStream(file.toPath()));
            Map map = (Map) obj;
            tiankey = (String) map.get("tiankey");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTiankey() {
        return tiankey;
    }
}
