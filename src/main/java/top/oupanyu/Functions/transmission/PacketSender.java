package top.oupanyu.Functions.transmission;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.oupanyu.Main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketSender {
    public static void send(GroupMessageEvent event) {
        try {
            InetAddress address = InetAddress.getByName(Main.configloader.getServer_ip());

            int port = Main.configloader.getServer_port();//定义端口类型
            String sendinfo = "来自QQ: " + event.getSenderName() + ":" + event.getMessage().contentToString();
            byte[] data = sendinfo.getBytes();//将接收到的数据变成字节数组
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);//2.创建数据报，包含发送的数据信息
            DatagramSocket socket = new DatagramSocket(); // 3.创建DatagramSocket对象

            socket.send(packet);// 4.向服务器端发送数据报
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
