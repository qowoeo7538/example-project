package org.lucas.example.foundation.jvm.demo.load.support;

/**
 * Created by joy on 17-2-6.
 */
public class OfficeBetter {
    public static void load(String args) throws Exception {
        Class c = Class.forName(args);
        OfficeAble officeAble = (OfficeAble) c.newInstance();
        officeAble.start();
    }
}
