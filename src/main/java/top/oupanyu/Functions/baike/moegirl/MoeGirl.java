package top.oupanyu.Functions.baike.moegirl;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import top.oupanyu.excuter.GroupMessageExecuter;
import top.oupanyu.request.Requests;
import top.oupanyu.tools.ListHelper;
import top.oupanyu.tools.MediaWikiHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class MoeGirl implements GroupMessageExecuter {
    public static void init(GroupMessageEvent event) {

    }
    public static void putText(GroupMessageEvent event) {
        String send = event.getMessage().contentToString();
        if (!send.split("")[0].equals(".")){
            return;
        }
        try {
            String post = URLEncoder.encode(send.replace(".moegirl ",""),"UTF-8");
            String content = Requests.doGet("https://zh.moegirl.org.cn/index.php?action=raw&title=" + post);
            sendImage(event, MediaWikiHelper.delNonUseKey(content));
        } catch (IOException e) {
            event.getGroup().sendMessage("出问题力！请先检查搜索内容是否正确");
            throw new RuntimeException(e);
        }

    }

    public static void sendImage(GroupMessageEvent event,String content) throws IOException {
        if (content == null){
            event.getGroup().sendMessage("没有"+event.getMessage().contentToString().replace(".moegirl ","")+"这个词条！");
            return;
        }
        int length = 35;
        List list = ListHelper.splitStingByCharLength(content,length);

        File file = new File(String.format("data/cache/moegirl/%s.png",event.getGroup().getId()));
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();

        int addHight = 0;
        for (int i = 0;i<list.size();i++){
            addHight+=50;
        }

        BufferedImage bufferedImage = new BufferedImage(1080,addHight,BufferedImage.TYPE_INT_BGR);
        writeImage(bufferedImage,list,file,1080,100+addHight);
        Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(),file);
        event.getGroup().sendMessage(image);


    }

    private static boolean writeImage(BufferedImage bi, List list, File file,int height,int width) {

        Graphics g = bi.getGraphics();
        g.fillRect(0,0,height,width);
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("微软雅黑",Font.PLAIN,30));

        for (int i = 0;i<list.size();i++){
            g.drawString((String) list.get(i),10 ,40 + 40*i);
        }

        //g.drawLine(0, 100, 100, 100);
        g.dispose();
        boolean val = false;
        try {
            val = ImageIO.write(bi, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return val;
    }

    @Override
    public void onRun(GroupMessageEvent event) {
        putText(event);
    }
}
