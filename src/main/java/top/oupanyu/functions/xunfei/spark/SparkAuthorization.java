package top.oupanyu.functions.xunfei.spark;

import okhttp3.HttpUrl;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import top.oupanyu.Main;
import top.oupanyu.utils.EncryptUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.rowset.spi.SyncResolver;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class SparkAuthorization {
    private static String hostURL = "https://spark-api.xf-yun.com/%s/chat";
    private static String host = "spark-api.xf-yun.com";
    private static String authStr = "host: %s" +
            "date: %s" +
            "GET /%s/chat HTTP/1.1";
    private static String authorization_origin ="api_key=\"%s\", " +
            "algorithm=\"hmac-sha256\", " +
            "headers=\"host date request-line\", " +
            "signature=\"%s\"";

    private static String wsAddress = "wss://%s/%s/chat?authorization=%s&date=%s&host=%s";

    private static String apiKey = Main.configloader.xunfeiSpark.apiKey;
    private static String apiSecret = Main.configloader.xunfeiSpark.apiSecret;
    public static String getAuthedURL1(Type aiType){
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());
            String formattedHostURL = String.format(hostURL,aiType.getVersion());
            String preAuth = String.format(authStr, URLEncoder.encode(formattedHostURL,"utf-8"), date, aiType.getVersion());
            String encryptedCode = String.format(authorization_origin,
                    apiKey
                    , EncryptUtils.hmacSHA256(preAuth, apiSecret));
            String authorization = Base64.getEncoder().encodeToString(encryptedCode.getBytes());
            String ret = String.format(wsAddress,host,aiType.getVersion(),
                    authorization,URLEncoder.encode(date,"utf-8"),
                    URLEncoder.encode(formattedHostURL,"utf-8"));
            Main.LOGGER.info(ret);
            return ret;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getAuthUrl(){
        try {
            URL url = new URL("https://spark-api.xf-yun.com/v2.1/chat");
            // 时间
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());
            // 拼接
            String preStr = "host: " + url.getHost() + "\n" +
                    "date: " + date + "\n" +
                    "GET " + url.getPath() + " HTTP/1.1";
            // System.err.println(preStr);
            // SHA256加密
            Mac mac = Mac.getInstance("hmacsha256");
            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
            mac.init(spec);

            byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
            // Base64加密
            String sha = Base64.getEncoder().encodeToString(hexDigits);
            // System.err.println(sha);
            // 拼接
            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
            // 拼接地址
            HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                    addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                    addQueryParameter("date", date).//
                    addQueryParameter("host", url.getHost()).//
                    build();

            // System.err.println(httpUrl.toString());
            return httpUrl.toString();
        }catch (Exception ignored){
            ignored.printStackTrace();
            return null;
        }

    }


    public enum Type{

        V1p5("v1.1"),V2("v2.1");
        private String version;
        Type(String version){
            this.version = version;
        }

        public String getVersion() {
            return version;
        }
    }
}
