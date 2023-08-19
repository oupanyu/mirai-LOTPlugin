package top.oupanyu.functions.rhythm.guessing;

import top.oupanyu.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Rhythm {

    private List<String> origin;
    private List<String> guessing;
    private int correct;
    public Rhythm(){
        origin = new ArrayList<>();
        guessing = new ArrayList<>();
    }

    public void append(String songName){
        if (origin.contains(songName)){
            return;
        }
        String guessingSong = StringUtils.changeString2Asterisk(songName);
        origin.add(songName);
        guessing.add(guessingSong);
    }
    public void change(int index,String content){
        if (guessing.size() < index){return;}
        guessing.remove(index);
        guessing.add(index,content);
//        List<String> newList = new ArrayList<>();
//        for (int i=0;i<guessing.size();i++){
//            if (i == index){
//                newList.add(content);
//            }else {
//                newList.add()
//            }
//        }
    }

    public List<String> getGuessing() {
        return guessing;
    }

    public List<String> getOrigin() {
        return origin;
    }

    public int getCorrect() {
        return correct;
    }
    public void correct(int i){
        correct += i;
    }
}
