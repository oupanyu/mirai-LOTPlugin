package top.oupanyu.Functions.pixiv;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.FileMessage;
import net.mamoe.mirai.message.data.Image;
import top.oupanyu.Main;
import top.oupanyu.request.Request;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class Pixiv {

    public static HashMap<Long,JSONArray> groupPixivPicCache = new HashMap<Long,JSONArray>();


    public static void getArray(GroupMessageEvent event) throws UnsupportedEncodingException {
        String tag = URLEncoder.encode(event.getMessage().contentToString().replace("&pixiv ",""),"UTF-8");
        String rtn = Request.get("https://api.moedog.org/pixiv/v2/?type=search&order=popular_desc&word=" + tag);
        JSONArray jArray = JSONObject.parseObject(rtn).getJSONArray("illusts");
        StringBuilder result = new StringBuilder("获取到的结果为：\n");
        JSONArray jsonArray = new JSONArray();//获取JSON数组
        try {
            for (int i =0,an=0;an<6;i++) {

                JSONArray arrayFilter = jArray.getJSONObject(i).getJSONArray("tags");

                int isR18 = 0;
                for (int j = 0; j < arrayFilter.size(); j++) {
                    String filterKwd = arrayFilter.getJSONObject(j).getString("name");
                    if (filterKwd.equals("R18") || filterKwd.equals("r18") || filterKwd.equals("r-18") || filterKwd.equals("R-18")) {
                        isR18 = 1;
                        break;

                    }
                }
                if (isR18 == 1) {

                    continue;
                }

                jsonArray.add(an, jArray.getJSONObject(i).getJSONObject("image_urls").getString("large"));
                String temp = String.format("%s:%s (%s)\n", an,
                        jArray.getJSONObject(i).getString("title"),
                        jArray.getJSONObject(i).getJSONArray("tags").getJSONObject(0).getString("name"));
                result.append(temp);
                //循环获取Pid、简介
                an++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            groupPixivPicCache.put(event.getGroup().getId(), jsonArray);
            event.getGroup().sendMessage(String.valueOf(result) + "获取图片请输入&pnum 数字代号");
        }

    }

    public static void searchArray(GroupMessageEvent event){
        Long groupID = event.getGroup().getId();
        try {
            Integer number = Integer.valueOf(event.getMessage().contentToString().replace("&pnum ",""));
            JSONArray jsonArray = groupPixivPicCache.get(event.getGroup().getId());

                String url = String.format(jsonArray.getString(number),"UTF-8").replace("i.pximg.net","i.pixiv.re");
                Request.downloadFile(url,
                        "data/cache/pcache",
                        String.format("%s.webp", event.getGroup().getId()));//下载图片
                String command = String.format("ffmpeg -i data/cache/pcache/%s.webp data/cache/pcache/%s.jpg",event.getGroup().getId(),event.getGroup().getId());
                Process p = Runtime.getRuntime().exec(command);
                Thread.sleep(2000);
                File file = new File(
                        String.format("data/cache/pcache/%s.jpg",
                                event.getGroup().getId()));//获取图片对象
                Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(), file);

                event.getSubject().sendMessage(image);
                File webp = new File(String.format("data/cache/pcache/%s.webp",
                        event.getGroup().getId()));
                file.delete();
                webp.delete();

        } catch (NumberFormatException ignored) {
            event.getGroup().sendMessage("出现错误！只能使用整型参数搜索！");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init(GroupMessageEvent event){
        if (event.getMessage().contentToString().contains("&pixiv ")){
            try {
                getArray(event);
            } catch (UnsupportedEncodingException e) {
                event.getGroup().sendMessage("出现错误！\n"+e.getMessage());
                throw new RuntimeException(e);
            }
        }else if (event.getMessage().contentToString().contains("&pnum")){
            searchArray(event);
        }

    }

    public static String downloadFile(String fileUrl,String saveUrl,String filename) {
        HttpURLConnection httpUrl = null;
        byte[] buf = new byte[1024];
        int size = 0;
        try {
            //下载的地址
            URL url = new URL(fileUrl);
            //支持http特定功能
            httpUrl = (HttpURLConnection) url.openConnection();
            //httpUrl.setRequestProperty("token",Main.configloader.getPixiv_token());
            //HttpURLConnection.setDefaultRequestProperty("token", Main.configloader.getPixiv_token());
            httpUrl.connect();
            //缓存输入流,提供了一个缓存数组,每次调用read的时候会先尝试从缓存区读取数据
            BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());
            File file = new File(saveUrl);
            //判断文件夹是否存在
            if(!file.exists()){
                file.mkdir();//如果不存在就创建一个文件夹
            }
            //讲http上面的地址拆分成数组,
            String arrUrl[] = fileUrl.split("/");
            //输出流,输出到新的地址上面
            FileOutputStream fos = new FileOutputStream(saveUrl+"/"+filename);
            while ((size = bis.read(buf)) != -1){
                fos.write(buf, 0, size);
            }
            //记得及时释放资源
            fos.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpUrl.disconnect();
        return null;
    }
}
