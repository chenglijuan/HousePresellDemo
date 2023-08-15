package zhishusz.housepresell.util.pdf;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import zhishusz.housepresell.util.ZipUtil;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.hutool.core.io.FileUtil;

/**
 * Word模板转为PDF，支持参数替换和图片替换
 */
public class WordTemplate2PDF
{
	private String templatePath;
	private String desPath;
	private String openOfficePath;
	private HashMap<String, String> paramMap;
	private HashMap<Integer, String> imgMap;
	private String generalFileName;
	private String desFoldPath;
	private String outputDocxPath;
	private String outputTempFoldPath;
	private String tempDocPath;

	/**
	 * @param templatePath   Word模板的路径，例如：（/.../..../..../*****.docx），注意！！必须是docx格式！！
	 * @param desPath        输出模板路径，例如：（/.../..../..../*****.pdf）
	 * @param openOfficePath OpenOffice程序的安装路径，例如：C:/Program Files (x86)/OpenOffice 4/program/soffice.exe
	 * @param paramMap       用于替换的参数的Map
	 * @param imgMap         用于替换图片的Map（注意！！图片的顺序是模板后解压的，从image1开始）
	 */
	public WordTemplate2PDF(String templatePath, String desPath, String openOfficePath,
			HashMap<String, String> paramMap, HashMap<Integer, String> imgMap)
	{
		this.templatePath = templatePath;
		this.desPath = desPath;
		this.openOfficePath = openOfficePath;
		this.paramMap = paramMap;
		this.imgMap = imgMap;
		desPath = desPath.replace("\\", "/");
		int lastIndexOfDot = desPath.lastIndexOf(".");
		int lastIndexOfSplash = desPath.lastIndexOf("/");
		generalFileName = desPath.substring(lastIndexOfSplash + 1, lastIndexOfDot);
		desFoldPath = desPath.substring(0, lastIndexOfSplash + 1);
		outputTempFoldPath=desFoldPath+generalFileName+"_"+"upzip";
	}

	/**
	 * 根据指定的参数值、模板，生成 word 文档
	 */
	private XWPFDocument generateWord()
	{
		XWPFDocument doc = null;
		try
		{
			OPCPackage pack = POIXMLDocument.openPackage(templatePath);
			doc = new XWPFDocument(pack);
			if (paramMap != null && paramMap.size() > 0)
			{
				//处理段落
				List<XWPFParagraph> paragraphList = doc.getParagraphs();
				processParagraphs(paragraphList, paramMap, doc);
				List<XWPFHeader> headerList = doc.getHeaderList();
				for (XWPFHeader xwpfHeader : headerList)
				{
					List<XWPFParagraph> paragraphs = xwpfHeader.getParagraphs();
					processParagraphs(paragraphs, paramMap, doc);
				}

				List<XWPFFooter> footerList = doc.getFooterList();
				for (XWPFFooter xwpfFooter : footerList)
				{
					List<XWPFParagraph> paragraphs = xwpfFooter.getParagraphs();
					processParagraphs(paragraphs, paramMap, doc);
				}
				//处理表格
				Iterator<XWPFTable> it = doc.getTablesIterator();
				while (it.hasNext())
				{
					XWPFTable table = it.next();
					List<XWPFTableRow> rows = table.getRows();
					for (XWPFTableRow row : rows)
					{
						List<XWPFTableCell> cells = row.getTableCells();
						for (XWPFTableCell cell : cells)
						{
							List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
							processParagraphs(paragraphListTable, paramMap, doc);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 处理段落
	 *
	 * @param paragraphList
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException
	 */
	private void processParagraphs(List<XWPFParagraph> paragraphList, Map<String, String> param, XWPFDocument doc)
	{
		if (paragraphList != null && paragraphList.size() > 0)
		{
			for (XWPFParagraph paragraph : paragraphList)
			{
				List<XWPFRun> runs = paragraph.getRuns();
				for (XWPFRun run : runs)
				{
					String text = run.getText(0);
					//					System.out.println("text==" + text);
					if (text != null)
					{
						boolean isSetText = false;
						for (Entry<String, String> entry : param.entrySet())
						{
							String key = entry.getKey();
							if (text.indexOf(key) != -1)
							{
								isSetText = true;
								Object value = entry.getValue();
								if (value instanceof String)
								{//文本替换
									//									System.out.println("key==" + key);
									text = text.replace(key, value.toString());
								}
							}
						}
						if (isSetText)
						{
							run.setText(text, 0);
						}
					}
				}
			}
		}
	}

	/**
	 * 将docx文件解压
	 */
	public void upzipWord()
	{
		XWPFDocument xwpfDocument = generateWord();
		FileOutputStream fileOutputStream = null;
		try
		{
			outputDocxPath = desFoldPath + generalFileName + ".docx";
			File file = new File(desFoldPath);
			if (!file.exists())
			{
				file.mkdirs();
			}
			fileOutputStream = new FileOutputStream(outputDocxPath);
			xwpfDocument.write(fileOutputStream);
			fileOutputStream.close();
			ZipUtil.unzip(outputDocxPath, outputTempFoldPath, "");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ZipException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 覆盖image文件夹中的图片文件
	 */
	private void overwritePic()
	{
		String picFold = outputTempFoldPath + "word/media/image";
		for (Entry<Integer, String> entry : imgMap.entrySet())
		{
			File jpegFile = new File(picFold + entry.getKey() + ".jpeg");
			//			System.out.println("jpegFile path is " + jpegFile.getAbsolutePath());
			if (jpegFile.exists())
			{
				//				System.out.println("replace value is " + entry.getValue() + "  key is " + entry.getKey() + ".jpeg");
				FileUtil.copy(entry.getValue(), picFold + entry.getKey() + ".jpeg", true);
				break;
			}
			File pngFile = new File(picFold + entry.getKey() + ".png");
			//			System.out.println("pngFile path is " + pngFile.getAbsolutePath());
			if (pngFile.exists())
			{
				FileUtil.copy(entry.getValue(), picFold + entry.getKey() + ".png", true);
				break;
			}
		}

	}

	/**
	 * 把解压后的文件再次压缩打包成zip文件
	 */
	private void zipDocx()
	{
		ZipFile zipFile = null;
		try
		{
			tempDocPath = desFoldPath + "$" + generalFileName + ".docx";
			zipFile = new ZipFile(tempDocPath);
			File file = new File(outputTempFoldPath);
			ArrayList<File> fileList = new ArrayList<>();
			for (File file1 : file.listFiles())
			{
				fileList.add(file1);
			}
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			zipFile.addFiles(fileList, parameters);
			for (File srcFile : fileList)
			{
				if (srcFile.isDirectory())
				{
					File[] subFiles = srcFile.listFiles();
					ArrayList<File> temp = new ArrayList<File>();
					Collections.addAll(temp, subFiles);
					zipFile.addFiles(temp, parameters);
					zipFile.addFolder(srcFile, parameters);
				}
				else
				{
					zipFile.addFile(srcFile, parameters);
				}
			}
		}
		catch (ZipException e)
		{
			e.printStackTrace();
		}

	}

	// 将word格式的文件转换为pdf格式
	private void wordToPDF()
	{
		try
		{
			// 源文件目录
			File inputFile = new File(tempDocPath);
			if (!inputFile.exists())
			{
				System.out.println("源文件不存在！");
				return;
			}
			// 输出文件目录
			File outputFile = new File(desPath);
			if (!outputFile.getParentFile().exists())
			{
				outputFile.getParentFile().exists();
			}
			String command = openOfficePath + " -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
			Process p = Runtime.getRuntime().exec(command);
			// 连接openoffice服务
			OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
			connection.connect();
			// 转换
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(inputFile, outputFile);
			// 关闭连接
			connection.disconnect();
			// 关闭进程
			p.destroy();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 删除临时文件
	 */
	public void deleteFile()
	{
		FileUtil.del(outputDocxPath);
		FileUtil.del(desFoldPath + "unzip");
		FileUtil.del(tempDocPath);
	}

	/**
	 * 输出PDF文件
	 */
	public void output()
	{
		upzipWord();
		overwritePic();
		zipDocx();
		wordToPDF();
		deleteFile();
	}

}

