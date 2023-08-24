package top.oupanyu.functions.bilibili.Exception;

public class NoSuchVideoVideoException extends Exception{
    public NoSuchVideoVideoException(){
        super("Bilibili haven't got this video");
    }
}
