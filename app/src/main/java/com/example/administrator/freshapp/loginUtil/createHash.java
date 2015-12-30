package com.example.administrator.freshapp.loginUtil;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2015/4/26.
 */
public class createHash {
    private final static String[] hexArray = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" }; // 存储十六进制值的数组

    public static String createEncrypPassword(String string) {
        return encrypBySHA256(string);
    }

//    public static boolean verificationPassword(String password, String string) {
//        if (password.equals(encrypByMD5(string))) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    private static String encrypBySHA256(String originString) {
        if (originString != null) {
            try {
                // 创建具有MD5算法的信息摘要
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                // 将得到的字节数组变成字符串返回
                String resultString = byteArrayToHex(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static String byteArrayToHex(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHex(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHex(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexArray[d1] + hexArray[d2];
    }
}
