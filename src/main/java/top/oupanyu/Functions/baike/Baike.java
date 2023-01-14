package top.oupanyu.Functions.baike;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import org.nd4j.shade.jackson.annotation.JsonIgnoreProperties;
import org.nd4j.shade.jackson.annotation.JsonProperty;
import top.oupanyu.tools.ListHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Baike {
    @JsonProperty("abstract")
    private String content;
    private String imageURL;

    public String getContent() {
        return content;
    }

    public String getImageURL() {
        return imageURL;
    }
    public void sendImage(GroupMessageEvent event) throws IOException{
            if (content == null){
                event.getGroup().sendMessage("没有"+event.getMessage().contentToString().replace(".baike ","")+"这个词条呢！");
                return;
            }
            int length = 35;
            List list = ListHelper.splitStingByCharLength(content,length);

            File file = new File(String.format("data/cache/baike/%s.png",event.getGroup().getId()));
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();

            int addHight = 100;
            for (int i = 0;i<list.size();i++){
                addHight+=50;
            }

            BufferedImage bufferedImage = new BufferedImage(1080,100+addHight,BufferedImage.TYPE_INT_BGR);
            writeImage(bufferedImage,list,file,1080,100+addHight);
            Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(),file);
            event.getGroup().sendMessage(image);


    }

    private boolean writeImage(BufferedImage bi, List list, File file,int height,int width) {

        Graphics g = bi.getGraphics();
        g.fillRect(0,0,height,width);
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("微软雅黑",Font.PLAIN,30));

        for (int i = 0;i<list.size();i++){
            g.drawString((String) list.get(i),20 ,40 + 40*i);
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
}
