package top.oupanyu;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class ConfigLoader {

    private String tiankey = "";

    private String chatGPT_key,pixiv_token;

    private Boolean transmission,openai_enable;

    private String server_ip = "";

    private Integer server_port;

    private Long groupnum,qqnum;

    public ConfigLoader() {
        try {
            String folder = "config/panyuou";
            String fileName = "config/panyuou/config.yml";
            File file = new File(fileName);
            File folderFile = new File(folder);
            if (!file.exists() || !folderFile.exists()) {
                String content = "#TianAPI Key\n" +
                        "tiankey: \"abcdefg\"\n" +
                        "\n" +
                        "\n" +
                        "ptoken : \"\""+
                        "#OpenAI functions\n" +
                        "openai_enable : true\n" +
                        "#ChatGPT cookies\n" +
                        "chatGPT_key : \" \"\n" +
                        "#Transmission with Minecraft Server\n" +
                        "transmission: false\n" +
                        "qqnum: 123456789\n" +
                        "\n" +
                        "groupnum: 12345678901\n" +
                        "server_ip: \"127.0.0.1\"\n" +
                        "server_port: 25565";
                folderFile.mkdirs();
                file.createNewFile();
                FileWriter fileWritter = new FileWriter(fileName, true);
                fileWritter.write(content);
                fileWritter.close();
                Main.logger.warning("配置已生成！请先调整配置后再打开mirai");
                System.exit(0);
            }
            Yaml yaml = new Yaml();
            Object obj = yaml.load(Files.newInputStream(file.toPath()));
            Map map = (Map) obj;
            tiankey = (String) map.get("tiankey");
            transmission = (Boolean)map.get("transmission");
            qqnum = Long.valueOf(String.valueOf(map.get("qqnum")));
            server_ip = (String) map.get("server_ip");
            server_port = (Integer) map.get("server_port");
            groupnum = Long.valueOf(String.valueOf(map.get("groupnum")));
            chatGPT_key = (String) map.get("chatGPT_key");
            openai_enable = (Boolean) map.get("openai_enable");
            pixiv_token = (String)map.get("pkey");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTiankey() {
        return tiankey;
    }

    public Boolean getTransmission() {
        return transmission;
    }

    public Integer getServer_port() {
        return server_port;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public Long getGroupnum() {
        return groupnum;
    }

    public Long getQQnum() {
        return qqnum;
    }

    public String getChatGPT_key() {
        return chatGPT_key;
    }

    public Boolean getOpenai_enable() {
        return openai_enable;
    }

    public String getPixiv_token() {
        return pixiv_token;
    }
}
