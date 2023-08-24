package top.oupanyu.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptUtils {
    public static String hmacSHA256(String data,String secret){
        try {
            Mac hmac256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("utf-8"), "HmacSHA256");
            hmac256.init(secretKey);
            byte[] hash = hmac256.doFinal(data.getBytes("utf-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
