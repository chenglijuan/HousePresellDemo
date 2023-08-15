package zhishusz.housepresell.util.picture;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.xiaominfo.oss.sdk.ReceiveMessage;

import cn.hutool.core.util.StrUtil;
import jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Code128;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.fileupload.OssServerUtil;

/**
 * 生成图片  一维码   二维码
 * @author ZS004
 *
 */
@SuppressWarnings("deprecation")
public class MatrixUtil
{
    
    @Autowired
    private OssServerUtil ossUtil;
    
	/* 背景色,默认黑色 */
	private static int foreColor = 0xFF000000;

	/* 前景色，默认白色 */
	private static int backColor = 0xFFFFFFFF;

	private String PIC_TYPE = "jpg";
	
	/**
	 * 生成一维码图片
	 * @param content  
	 * 					一维码内容
	  * @param outFilePath
	 * 					输出路径
	 */
	public static void inserPIC(String content, String outFilePath){
		String picPath = outFilePath + "a.png";
		JBarcodeBean jBarcodeBean = new JBarcodeBean();
		// 条形码类型
		jBarcodeBean.setCodeType(new Code128());
		// 在条形码下面显示文字
		jBarcodeBean.setLabelPosition(JBarcodeBean.LABEL_BOTTOM);
		OutputStream out;
		try {
			out = new FileOutputStream(picPath);
			jBarcodeBean.setCode(content);
			BufferedImage image = new BufferedImage(110, 55,
					BufferedImage.TYPE_INT_RGB);
			image = jBarcodeBean.draw(image);
			
			ImageIO.write(image, "PNG", out);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成二维码图片，保存数据库
	 * @param content  
	 * 					二维码内容
	 * @param outFilePath
	 * 					输出路径
	 */
	public static void inserPIC2(String content, String outFilePath) {
		String picPath = outFilePath + "b.png";
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        //内容所使用编码  
        hints.put(EncodeHintType.CHARACTER_SET, "gb2312"); 
        hints.put(EncodeHintType.MARGIN, 1);
        try {
			BitMatrix bitMatrix = multiFormatWriter.encode(content,BarcodeFormat.QR_CODE, 180, 180, hints);
			File outputFile = new File(picPath);
			//生成二维码  
            BufferedImage image = toBufferedImage(bitMatrix);
            ImageIO.write(image, "PNG", outputFile);
		} catch (WriterException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置二维码前景色和背景色
	 * @param matrix
	 * @return
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix) {  
        int width = matrix.getWidth();  
        int height = matrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y, matrix.get(x, y) ? foreColor : backColor);  
            }  
        }  
        foreColor = 0xFF000000;  
        backColor = 0xFFFFFFFF;  
        return image;  
    }
	
	/**
	 * 给图片添加水印
	 * 
	 * @param pressimg
	 *            水印的路径
	 * @param targerImg
	 *            原始图片的路径
	 * @param location
	 *            位置： 1 左上角 2 右上角 3 左下角 4 右下角 5 正中间
	 * @param alpha
	 *            透明度
	 * @param targetPath
	 *            保存路径
	 */
	public void pressImage(String pressimg, String targerImg, int location, Float alpha, String targetPath)
	{
		try
		{
			File img = new File(targerImg);

			Image src = ImageIO.read(img);

			int width = src.getWidth(null);

			int height = src.getHeight(null);

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// 创建一个指定 BufferedImage 的 Graphics2D 对象
			Graphics2D g = image.createGraphics();

			g.drawImage(src, 0, 0, width, height, null);

			Image src_biao = ImageIO.read(new File(pressimg));

			int width_biao = src_biao.getWidth(null);

			int height_biao = src_biao.getHeight(null);

			int new_width_biao = width_biao;

			int new_height_biao = height_biao;

			if (width_biao > width)
			{
				new_width_biao = width;

				new_height_biao = (int) ((double) new_width_biao / width_biao * height);
			}

			if (new_height_biao > height)
			{
				new_height_biao = height;

				new_width_biao = (int) ((double) new_height_biao / height_biao * new_width_biao);
			}

			int x = 0;
			int y = 0;

			switch (location)
			{
			case 1:

				break;

			case 2:

				x = width - new_width_biao;

				break;

			case 3:

				y = height - new_height_biao;

				break;

			case 4:

				x = width - new_width_biao - 10;

				y = height - new_height_biao - 10;

				break;

			case 5:

				x = (width - new_width_biao) / 2;

				y = (height - new_height_biao) / 2;

				break;

			default:
				break;
			}

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			g.drawImage(src_biao, x, y, new_width_biao, new_height_biao, null);

			g.dispose();

			ImageIO.write(image, PIC_TYPE, new File(targetPath));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

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
	public void saveMinPhoto(String srcURL, String deskURL, Integer weightBase, Integer heightBase)
	{

		try
		{
			File srcFile = new File(srcURL);

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

	/**
	 * 图片保存
	 * 
	 * @param theLink
	 *            oss 图片路径
	 * @param watermarkPic
	 *            水印图片位置
	 * @param weightBase
	 *            基础宽度
	 * @param heightBase
	 *            基础高度
	 * @return
	 */
	public String compressionPic(String theLink, String watermarkPic, String weightBase, String heightBase)
	{
		OssServerUtil ossUtil = new OssServerUtil();

		// 初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();

		String localPath = directoryUtil.getProjectRoot();// 项目路径

		if (localPath.contains("%20"))
		{
			localPath = localPath.replace("%20", " ");
		}
		if (localPath.contains("file:/"))
		{
			localPath = localPath.replace("file:/", "");
		}

		String impPath = localPath + UUID.randomUUID().toString().split("-")[0] + "." + PIC_TYPE;

		ossUtil.method(theLink, impPath);

		String targetPath = localPath + UUID.randomUUID().toString().split("-")[1] + "." + PIC_TYPE;

		pressImage(watermarkPic, impPath, 4, 0.8f, targetPath);

		// 删除原始图片
		deleteFile(impPath);

		String deskURL = localPath + UUID.randomUUID().toString().split("-")[2] + "." + PIC_TYPE;

		saveMinPhoto(targetPath, deskURL, Integer.parseInt(weightBase.trim()), Integer.parseInt(heightBase.trim()));

		// 删除原始图片
		deleteFile(targetPath);

		return deskURL;
	}
	
	

	
	/**
	 * 图片加水印
	 * @param theLink
	 * @param watermarkPic
	 * @param weightBase
	 * @param heightBase
	 * @return
	 */
    public String addWaterMark(String theLink, String watermarkPic, String weightBase, String heightBase)
    {
        /*
         * 从文件服务器获取图片到服务器进行处理
         */
        OssServerUtil ossUtil = new OssServerUtil();
        DirectoryUtil directoryUtil = new DirectoryUtil();
        String localPath = directoryUtil.getProjectRoot();
        if (localPath.contains("%20"))
        {
            localPath = localPath.replace("%20", " ");
        }
        if (localPath.contains("file:/"))
        {
            localPath = localPath.replace("file:/", "");
        }

        //下载图片到服务器的指定位置
        String impPath = localPath + UUID.randomUUID().toString().split("-")[0] + "." + PIC_TYPE;
        ossUtil.method(theLink, impPath);

        //处理图片后的服务器保存位置
        String targetPath = localPath + UUID.randomUUID().toString().split("-")[1] + "." + PIC_TYPE;
        pressImage(watermarkPic, impPath, 4, 0.8f, targetPath);

        // 删除原始图片
        deleteFile(impPath);
        
        //重新上传到文件服务器
        ReceiveMessage upload = ossUtil.upload(targetPath);
        // 删除服务器加水印图片
        deleteFile(targetPath);
        if(null != upload && null != upload.getData() && !upload.getData().isEmpty() && StrUtil.isNotBlank(upload.getData().get(0).getUrl())){
            return upload.getData().get(0).getUrl();
        }else{
            return "";
        }

    }
    
    /**
     * 压缩后再上上传
     * @param theLink
     * @param watermarkPic
     * @param weightBase
     * @param heightBase
     * @return
     */
    public String[] addWaterMarkAndCompress(String theLink, String watermarkPic, String weightBase, String heightBase)
    {
        
        String[] url = new String[2];
        /*
         * 从文件服务器获取图片到服务器进行处理
         */
        OssServerUtil ossUtil = new OssServerUtil();
        DirectoryUtil directoryUtil = new DirectoryUtil();
        String localPath = directoryUtil.getProjectRoot();
        if (localPath.contains("%20"))
        {
            localPath = localPath.replace("%20", " ");
        }
        if (localPath.contains("file:/"))
        {
            localPath = localPath.replace("file:/", "");
        }

        //下载图片到服务器的指定位置
        String impPath = localPath + UUID.randomUUID().toString().split("-")[0] + "." + PIC_TYPE;
        ossUtil.method(theLink, impPath);

        //处理图片后的服务器保存位置
        String targetPath = localPath + UUID.randomUUID().toString().split("-")[1] + "." + PIC_TYPE;
        pressImage(watermarkPic, impPath, 4, 0.8f, targetPath);
        // 删除原始图片
        deleteFile(impPath);
        
        url[0] = targetPath;
        //重新上传到文件服务器
        /*ReceiveMessage upload = ossUtil.upload(targetPath);
        if(null != upload && null != upload.getData() && !upload.getData().isEmpty() && StrUtil.isNotBlank(upload.getData().get(0).getUrl())){
            url[0] = (String)upload.getData().get(0).getUrl();
        }else{
            url[0] = "";
        }*/

        //压缩后的服务器保存位置
        String deskURL = localPath + UUID.randomUUID().toString().split("-")[2] + "." + PIC_TYPE;
        saveMinPhoto(targetPath, deskURL, Integer.parseInt(weightBase.trim()), Integer.parseInt(heightBase.trim()));
        
        
        url[1] = deskURL;
        // 删除加水印后的图片
//        deleteFile(targetPath);
        //重新上传到文件服务器
        /*upload = ossUtil.upload(deskURL);
        if(null != upload && null != upload.getData() && !upload.getData().isEmpty() && StrUtil.isNotBlank(upload.getData().get(0).getUrl())){
            url[1] = (String)upload.getData().get(0).getUrl();
        }else{
            url[1] = "";
        }*/
        // 删除加水印后压缩的图片
//        deleteFile(deskURL);

        return url;
    }

	// 删除文件
	public static void deleteFile(String localPath)
	{
		File file = new File(localPath);

		if (file.isFile() && file.exists())
		{
			file.delete();
		}
	}
}
