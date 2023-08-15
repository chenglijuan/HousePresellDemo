package zhishusz.housepresell.util.tianyin;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String md5(byte[] buffer) {
        String string = null;
        char[] hexDigist = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] datas = md.digest();
            char[] str = new char[32];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte b = datas[i];
                str[k++] = hexDigist[b >>> 4 & 0xF];
                str[k++] = hexDigist[b & 0xF];
            }
            string = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return string;
    }
}
