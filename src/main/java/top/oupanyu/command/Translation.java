package top.oupanyu.command;

import com.google.gson.Gson;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import top.oupanyu.Functions.openai.Chat;
import top.oupanyu.Functions.translation.baidu.ResponseJson;
import top.oupanyu.Main;

import java.io.IOException;

public class Translation extends JRawCommand {
    public static Translation INSTANCE = new Translation();
    public Translation() {
        super(Main.INSTANCE, "tran");
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {

        String appid = Main.configloader.getBaidu_fanyi_appid();
        String salt = "qwsdfg";
        String key = Main.configloader.getBaidu_fanyi_key();
        String md5 = DigestUtils.md5Hex(appid+args.contentToString()+salt+key);
        OkHttpClient client = new OkHttpClient().newBuilder().build();


        Request request = new Request.Builder()
                .url(String.format("https://fanyi-api.baidu.com/api/trans/vip/translate?q=%s&from=auto&to=zh&appid=%s&salt=%s&sign=%s",args.contentToString(),appid,salt,md5))
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Gson gson = new Gson();
            ResponseJson responseJson = gson.fromJson(response.body().string(), ResponseJson.class);
            Main.logger.info(String.valueOf(response));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
