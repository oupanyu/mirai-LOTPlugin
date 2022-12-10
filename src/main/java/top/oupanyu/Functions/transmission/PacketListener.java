package top.oupanyu.Functions.transmission;

import net.mamoe.mirai.Bot;
import top.oupanyu.Main;

import java.io.DataInputStream;
import java.io.IOException;

public class PacketListener implements Runnable{
    @Override
    public void run() {
        String accpet = null;// 读取来自服务器的信息
        while (true) {
            try {
                DataInputStream in = new DataInputStream(Main.socket
                        .getInputStream());// 读取服务器端传过来信息的DataInputStream
                accpet = in.readUTF();
            } catch (IOException e) {
                continue;
            }
            //System.out.println(accpet);//输出来自服务器的信息
            Bot.getInstance(Main.configloader.getQQnum()).getGroup(Main.configloader.getGroupnum()).sendMessage(accpet);
        }

    }
}
