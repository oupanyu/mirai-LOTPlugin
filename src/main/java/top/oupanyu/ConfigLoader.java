package top.oupanyu;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class ConfigLoader {

    private String tiankey = "";

    private String pixiv_token,GPT3_host;

    private Boolean transmission,openai_enable;

    private String server_ip = "";

    private Integer server_port,GPT3_port;

    private Long groupnum,qqnum;

    public ConfigLoader() {
        try {
            String folder = "config/panyuou";
            String fileName = "config/panyuou/config.yml";
            File file = new File(fileName);
            File folderFile = new File(folder);
            if (!file.exists() || !folderFile.exists()) {
                String content = "#TianAPI Key\n" +
                        "tiankey: \"\"\n" +
                        "\n" +
                        "pkey : \"\"\n" +
                        "#OpenAI key\n" +
                        "openai_enable : false\n" +
                        "#openai_key : \"\"\n" +
                        "GPT3_host: \"127.0.0.1\"\n" +
                        "GPT3_port: 5000\n" +
                        "\n" +
                        "\n" +
                        "qqnum: 1234567890\n" +
                        "transmission: false\n" +
                        "\n" +
                        "groupnum: 123456789\n" +
                        "server_ip: \"127.0.0.1\"\n" +
                        "server_port: 25570";
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
            openai_enable = (Boolean) map.get("openai_enable");
            pixiv_token = (String)map.get("pkey");
            GPT3_host = (String) map.get("GPT3_host");
            GPT3_port = (Integer) map.get("GPT3_port");

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


    public Boolean getOpenai_enable() {
        return openai_enable;
    }

    public String getPixiv_token() {
        return pixiv_token;
    }

    public Integer getGPT3_port() {
        return GPT3_port;
    }

    public String getGPT3_host() {
        return GPT3_host;
    }
}
