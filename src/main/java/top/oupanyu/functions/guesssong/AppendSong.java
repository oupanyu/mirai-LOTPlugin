package top.oupanyu.functions.guesssong;

import net.mamoe.mirai.event.events.MessageEvent;
import top.oupanyu.helper.Command;

import java.sql.SQLException;
import java.util.HashMap;

public class AppendSong {

    private static HashMap<Long,SongGuessing> songAppendMap = new HashMap<>();


    public static void init(MessageEvent event){
        Long qid = event.getSender().getId();
        Command command = new Command(event.getMessage().contentToString())
                .setFirstKey("/cxcsq");
        if (command.equals(1,"append")){
            songAppendMap.put(qid,new SongGuessing());
            event.getSender().sendMessage("请输入要猜歌曲的抽象化表达，可为emoji");
            return;
        }else if (songAppendMap.containsKey(qid)){
            SongGuessing sg = songAppendMap.get(qid);
            if (sg.getQuestion() == null){
                sg.setQuestion(command.getOriginContent());
                event.getSender().sendMessage("请输入歌曲答案（最好为原语言）：");
            }else if (sg.getAnswer() == null){
                sg.setAnswer(command.getOriginContent());
                event.getSender().sendMessage("请输入歌曲中文译名：");
            }else if (sg.getContent2Chinese() == null){
                sg.setContent2Chinese(command.getOriginContent());
                try {
                    sg.appendToDatabase();
                } catch (SQLException e) {
                    event.getSender().sendMessage("出错了！请查看输入内容是否不符合标准！");
                    throw new RuntimeException(e);
                }
                event.getSender().sendMessage("添加成功！感谢支持！");
            }
            return;
        }
    }
}
