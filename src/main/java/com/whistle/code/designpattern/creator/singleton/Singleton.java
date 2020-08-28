package com.whistle.code.designpattern.creator.singleton;

import com.whistle.code.designpattern.creator.Game;
import com.whistle.code.designpattern.creator.LOL;
import com.whistle.code.designpattern.creator.XYD;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.designpattern.creator.singleton.Singleton
 */
public class Singleton {
    private static volatile Game instance;
    //缺点，线程不安全
    public static Game getLazyInstance(){
        return null==instance?instance = new LOL():instance;
    }

    private static Game xyd = new XYD();
    //缺点，占用资源，在加载类的时候就会初始化一个类对象
    public static Game getHungryInstance(){
        return xyd;
    }
    //缺点，资源耗费大
    public static Game getDoubleCheckInstance(){
        if(instance==null){
            synchronized (Singleton.class){
                if(instance==null){
                    instance = new LOL();
                }
            }
        }
        return instance;
    }
    //静态内部类方式
    private static class Inner{
        private static final Game INSTANCE = new LOL();
    }

    public static Game getInnerInstance(){
        return Inner.INSTANCE;
    }


    public enum SingletonEnum {
        LOL(new LOL()),XYD(new XYD());
        private Game instance;
        SingletonEnum(Game lol) {
            this.instance = lol;
        }
        public Game getInstance(){
            return instance;
        }
    }


}
