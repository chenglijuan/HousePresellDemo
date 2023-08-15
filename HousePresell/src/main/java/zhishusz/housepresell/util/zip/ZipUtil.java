package zhishusz.housepresell.util.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Util工具类：文件ZIP压缩下载
 * @author yu.yang
 * @since 2018-09-07 10:54
 */
public class ZipUtil
{
	static final int BUFFER = 8192;

	private File zipFile;

	public ZipUtil(String pathName)
	{
		zipFile = new File(pathName);
	}

	/**
	 * 压缩文件
	 * @param srcPathName  源文件地址
	 */
	public void compress(String srcPathName)
	{
		File file = new File(srcPathName);
		if (!file.exists())
			throw new RuntimeException(srcPathName + "不存在！");
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			String basedir = "";
			compress(file, out, basedir);
			out.close();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 判断文件类型
	 * @param file  文件流
	 * @param out   保存压缩包地址
	 * @param basedir  临时存储字段名称
	 */
	private void compress(File file, ZipOutputStream out, String basedir)
	{
		/* 判断是目录还是文件 */
		if (file.isDirectory())
		{
			this.compressDirectory(file, out, basedir);
		}
		else
		{
			this.compressFile(file, out, basedir);
		}
	}

	/**
	 * 压缩一个目录
	 * @param file  文件流
	 * @param out   保存压缩包地址
	 * @param basedir  临时存储字段名称
	 */
	private void compressDirectory(File dir, ZipOutputStream out, String basedir)
	{
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
		dir.delete();
	}

	/**
	 * 压缩一个文件
	 * @param file  文件流
	 * @param out   保存压缩包地址
	 * @param basedir  临时存储字段名称
	 */
	private void compressFile(File file, ZipOutputStream out, String basedir)
	{
		if (!file.exists())
		{
			return;
		}
		try
		{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1)
			{
				out.write(data, 0, count);
			}
			bis.close();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		file.delete();
	}
}
