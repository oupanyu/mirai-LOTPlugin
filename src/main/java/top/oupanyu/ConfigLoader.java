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

    private String openai_key="";
    private String openai_url="";

    private Integer server_port,GPT3_port;

    private Long groupnum,qqnum;

    private String baidu_fanyi_key,baidu_fanyi_appid;

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
                        "openai_key : \"\"\n" +
                        "openai_host: \"api.openai.com\"\n" +
                        "GPT3_host: \"127.0.0.1\"\n" +
                        "GPT3_port: 5000\n" +
                        "\n" +
                        "baidu_fanyi_appid: \"\"\n" +
                        "baidu_fanyi_key: \"\"\n" +
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
            openai_key = (String)map.get("openai_key");
            openai_url = (String)map.get("openai_host");
            pixiv_token = (String)map.get("pkey");
            baidu_fanyi_key=(String)map.get("baidu_fanyi_key");
            baidu_fanyi_appid = (String)map.get("baidu_fanyi_appid");

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

    public String getOpenai_key() {
        return openai_key;
    }

    public String getOpenai_url() {
        return openai_url;
    }

    public String getBaidu_fanyi_appid() {
        return baidu_fanyi_appid;
    }

    public String getBaidu_fanyi_key() {
        return baidu_fanyi_key;
    }
}
