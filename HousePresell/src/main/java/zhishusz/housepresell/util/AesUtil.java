package zhishusz.housepresell.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AesUtil {
	private static byte[] key;
	private static AES aes;

	private static AesUtil instance;
	private AesUtil()
	{
		
	}
	
	public static AesUtil getInstance()
	{
		if(instance == null)
		{
			instance = new AesUtil();
			key = toBytes("D446AC016FC462C735A663AD565C6E97");//SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();// 随机生成密钥
			aes = SecureUtil.aes(key);// 构建
		}
		
		return instance;
	}
	
	public String encrypt(String str)
	{
		if(str == null) return null;
		return aes.encryptHex(str);
	}
	
	public String decrypt(String str)
	{
		if(str == null) return null;
		return aes.decryptStr(str, CharsetUtil.CHARSET_UTF_8);
	}
	
	/**
	 * 将16进制字符串转byte数组
	 * @param str
	 * @return
	 */
	public static byte[] toBytes(String str) 
	{
		str = str.replace(" ", "");
		
		if(str == null || str.trim().equals("")) 
		{
			return new byte[0];
		}
		
		byte[] bytes = new byte[str.length() / 2];
		
		for(int i = 0; i < str.length() / 2; i++) 
		{
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}
		
		return bytes;
	}

	private static String PIC_TYPE = "jpg";

	/**
	 * 等比例压缩算法：
	 * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
	 *
	 * @param srcURL
	 *            原图地址
	 * @param deskURL
	 *            缩略图地址
	 * @param comBase
	 *            宽度压缩基数
	 * @param heightBase
	 *            高度压缩基数
	 * @throws Exception
	 * @author shenbin
	 * @createTime 2014-12-16
	 * @lastModifyTime 2014-12-16
	 */
	public static void saveMinPhoto(String srcURL, String deskURL, Integer weightBase, Integer heightBase)
	{

		try
		{
			weightBase= 690;
			heightBase = 920;
			deskURL = "F:\\tp\\2e48dddc43ac49d7b349bf5f70cec06c.jpeg";
			File srcFile = new File("F:\\tp\\2e48dddc43ac49d7b349bf5f70cec06c.jpeg");


			Image src = ImageIO.read(srcFile);

			int srcHeight = src.getHeight(null);

			int srcWidth = src.getWidth(null);

			int deskHeight = 0;// 缩略图高

			int deskWidth = 0;// 缩略图宽

			double srcScale = 0.00;

			/** 缩略图宽高算法 */

			if (srcWidth > weightBase)
			{
				srcScale = (double) weightBase / srcWidth;

				deskWidth = (int) (srcWidth * srcScale);

				deskHeight = (int) (srcHeight * srcScale);
			}
			else
			{
				deskHeight = srcHeight;

				deskWidth = srcWidth;
			}

			if (deskHeight > heightBase)
			{
				srcScale = (double) heightBase / deskHeight;

				deskWidth = (int) (srcWidth * srcScale);

				deskHeight = (int) (srcHeight * srcScale);
			}

			BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);

			tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); // 绘制缩小后的图

			tag.flush();

			ImageIO.write(tag, PIC_TYPE, new File(deskURL));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {

//		saveMinPhoto("","",0,0);
		System.out.println(AesUtil.getInstance().decrypt("e6305f69e178effe68152784eb59a8b4"));
//		System.out.println(AesUtil.getInstance().decrypt("0d8871104e719644ceb0cd82e5609ab5"));
//		ef4cfc844cbfdd134dff7e66e6b7c3ae
//		System.out.println(AesUtil.getInstance().encrypt("000000"));

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		long time = new Date().getTime() + 24 * 3600 * 1000l * 90;
//		System.out.println(sdf.format(new Date(time)));


//		String[] idd = UUID.randomUUID().toString().split("-");

		// 合同eCode
//		String eCode = idd[0] + idd[1];


//		System.out.println("eCode="+eCode);


	}

}
