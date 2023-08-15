package test.normalTest;

import zhishusz.housepresell.util.AesUtil;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

public class AesTest {

	public static void main(String[] args) {
		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
		System.out.println(bytes2HexString(key));
		System.out.println(AesUtil.getInstance().encrypt("000000"));
		String s = "d501fdbe97f7bb663cea1d01a1863960";
		System.out.println(AesUtil.getInstance().decrypt(s));
	}

	/**
	 * 将字节数组转成16进制字符串显示
	 * @param b 数组
	 * @return
	 */
	public static String bytes2HexString(byte[] b) {
        String r = "";
        
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }
        
        return r;
    }
}
