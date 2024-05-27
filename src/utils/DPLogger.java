package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DPLogger {
    private static String getTimeStr(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern(" yyyy-MM-dd HH:mm:ss ");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        return sdf.format(date);
    }

    public static void success(String content) {
        System.out.println("\u001B[32m[DrawPanel]\u001B[0m" + getTimeStr() + content);
    }

    public static void error(String content) {
        System.out.println("\u001B[31m[DrawPanel]\u001B[0m" + getTimeStr() + content);
    }

    public static void warning(String content) {
        System.out.println("\u001B[33m[DrawPanel]\u001B[0m" + getTimeStr() + content);
    }
}
