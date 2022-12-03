package top.oupanyu.Functions.transmission;

import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.io.DataOutputStream;
import java.io.IOException;

import static top.oupanyu.Main.socket;

public class PacketSender {
    public static void send(GroupMessageEvent event) {
        /*
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
        }*/

            try {


                DataOutputStream out = new DataOutputStream(socket
                        .getOutputStream());// 向服务器端发送信息的DataOutputStream

                //Scanner scanner = new Scanner(System.in);// 装饰标准输入流，用于从控制台输入

                String send = String.format("%s:%s", event.getSenderName(), event.getMessage().contentToString());//读取控制台输入的内容
                //System.out.println("Client:：" + send);//输出键盘输出内容提示 ，也就是客户端向服务器端发送的消息
                // 把从控制台得到的信息传送给服务器
                out.writeUTF(send);//将客户端的信息传递给服务器
                //String accpet = in.readUTF();// 读取来自服务器的信息
                //System.out.println(accpet);//输出来自服务器的信息


            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}
