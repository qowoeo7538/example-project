package org.shaw.reflect.loader.dynamic;

/**
 * Created by joy on 17-2-6.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        //new是对象的静态加载，编译时就会加载;
        OfficeBetter officeBetter = new OfficeBetter();
        OfficeBetter.load("com.myweb.reflect.dynamicLoad.Word");
    }
}
