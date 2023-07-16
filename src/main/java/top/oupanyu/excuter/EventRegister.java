package top.oupanyu.excuter;

import java.util.HashMap;

public class EventRegister<T extends GroupMessageExecuter> {
    protected HashMap<String,T> methodMap = new HashMap<>();

    protected EventRegister<T> getInstance(){
        return new EventRegister<>();
    }

    protected HashMap<String,T> getMethodMap(){
        return methodMap;
    }

    public boolean register(String key,T t){
        if (methodMap.containsKey(key)){
            return false;
        }
        methodMap.put(key,t);
        return true;
    }


}
