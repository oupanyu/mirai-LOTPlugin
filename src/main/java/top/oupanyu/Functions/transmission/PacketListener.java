package top.oupanyu.Functions.transmission;

import net.mamoe.mirai.Bot;
import top.oupanyu.Main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketListener implements Runnable{
    @Override
    public void run() {
        InetAddress address = null; // 1.定义服务器的地址、端口号、数据
        try {
            address = InetAddress.getByName(Main.configloader.getServer_ip());
            Bot.getInstance(Main.configloader.getQQnum()).getGroup(Main.configloader.getGroupnum()).sendMessage("与服务端连接连接已建立！");//向QQ发送信息
            int port = Main.configloader.getServer_port();//定义端口类型
            while(true) {//通过循环不同的向客户端发送和接受数据

                byte[] data = "&hpkt".getBytes();//将接收到的数据变成字节数组
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);//2.创建数据报，包含发送的数据信息
                DatagramSocket socket = new DatagramSocket(); // 3.创建DatagramSocket对象
                socket.send(packet);// 4.向服务器端发送数据报

                /*
                 * 接收服务器端响应的数据
                 */
                byte[] data2 = new byte[1024];//创建字节数组
                DatagramPacket packet2 = new DatagramPacket(data2, data2.length);// 1.创建数据报，用于接收服务器端响应的数据
                socket.receive(packet2);// 2.接收服务器响应的数据
                //3.读取数据
                String reply = new String(data2, 0, packet2.getLength());//创建字符串对象
                System.out.println("我是客户端，服务器说：" + reply);//输出提示信息
                Bot.getInstance(Main.configloader.getQQnum()).getGroup(Main.configloader.getGroupnum()).sendMessage("来自MC内信息：\n" + reply);//向QQ发送信息
                socket.close();//4.关闭资源
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
