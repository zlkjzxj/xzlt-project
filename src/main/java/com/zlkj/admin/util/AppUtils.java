package com.zlkj.admin.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 接口工具类
 * @Author sunny
 * @Date 2019-05-16 15:06
 */
public class AppUtils {
    public static boolean checkParam(String receiveStr, String timestamp) {
        //把 ID,SECRET,TOKEN MD5加密，判断传来的值对不对
        String str = Constant.APPID + Constant.APPSECRET + Constant.APP_TOKEN + timestamp;
        String encodeStr = MD5Utils.MD5Encode(str, "utf-8");
        if (receiveStr.equals(encodeStr.toUpperCase())) {
            return true;
        }
        return false;
    }

    public static boolean checkTimestamp(String timestamp) {
        Long s = (System.currentTimeMillis() - Long.parseLong(timestamp)) / (1000 * 60);
        return s >= 1L;
    }

    public static void main(String[] args) {
        //        //把 ID,SECRET,TOKEN MD5加密，判断传来的值对不对
        //        String str = Constant.APPID + Constant.APPSECRET + Constant.APP_TOKEN;
        //        String encodeStr = MD5Utils.MD5Encode(str, "utf-8");
        //        System.out.println(encodeStr);
        Long s = (System.currentTimeMillis() - Long.parseLong("1557990028268")) / (1000 * 60);
        System.out.println(s);
    }

    public static boolean checkCreateTime(String createTime) {
        Long s = (System.currentTimeMillis() - Long.parseLong(createTime)) / (1000 * 60 * 60 * 24);
        return s >= Constant.QRCODE_EXPIRE_TIME;
    }

    /**
     * 生成照片名字：：日期时间+6位随机数
     */
    public static String createZImgPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(new Date());
        int a = (int) ((Math.random() * 9 + 1) * 100000);
        return time + a + ".jpg";
    }
}
