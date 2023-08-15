package zhishusz.housepresell.util.pdf;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Created by Dechert on 2018-09-10.
 * Company: zhishusz
 */

public class WordXml2PDF
{
	private String templateXmlPath;
	private String outputPDFPath;
	private HashMap<String, Object> contentMap;
	private HashMap<String, Object> headMap;
	private HashMap<String, Object> footMap;
	private HashMap<Integer, String> imgMap;
	private String wordRootPath = "";
	private File templateFile;
	private String templateDirectory;
	private String tempDocPath;
	private String outputPDFDirectory;
	private String openOfficePath;

	/**
	 * @param templateDirectory 模板document.xml/document_template.xml所在的目录，一般是docx解压后得到，
	 *                          其中document_template.xml需要手动复制document.xml后改名，然后添加template的变量，
	 *                          例：E:\temp\word\DemoTemplate2\word\
	 * @param outputPDFPath     输出pdf的路径，注意是完整路径！例：E:\temp\word\MyTest.pdf
	 * @param openOfficePath    OpenOffice程序的安装路径，例：C:/Program Files (x86)/OpenOffice 4/program/soffice.exe
	 * @param contentMap        document_template.xml中的参数map，也就是正文的参数Map
	 * @param headMap           header1_template.xml中的参数map，也就是页眉的参数Map
	 * @param footMap           footer1_template.xml中的参数map，也就是页脚的参数Map
	 * @param imgMap            用于替换图片的Map（注意！！图片的顺序是模板后解压的，从image1开始）
	 */
	public WordXml2PDF(String templateDirectory, String outputPDFPath, String openOfficePath,
			HashMap<String, Object> contentMap, HashMap<String, Object> headMap, HashMap<String, Object> footMap,
			HashMap<Integer, String> imgMap)
	{
		this.templateDirectory = templateDirectory;
		templateDirectory = templateDirectory.replace("\\", "/");
		if (!templateDirectory.endsWith("/"))
		{
			templateDirectory += "/";
		}
		templateXmlPath = templateDirectory + "document_template.xml";
		this.outputPDFPath = outputPDFPath;
		this.outputPDFPath = outputPDFPath.replace("\\", "/");
		this.openOfficePath = openOfficePath;
		this.contentMap = contentMap;
		this.headMap = headMap;
		this.footMap = footMap;
		this.imgMap = imgMap;
		System.out.println(templateXmlPath);
		int wordSlash = templateXmlPath.lastIndexOf("/");
		wordRootPath = templateXmlPath.substring(0, wordSlash);
		int rootSlash = wordRootPath.lastIndexOf("/");
		wordRootPath = wordRootPath.substring(0, rootSlash + 1);
		System.out.println("rootPath is " + wordRootPath);
		templateFile = new File(templateXmlPath);
	}

	private boolean readXml()
	{
		try
		{
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("UTF-8");
			configuration.setDirectoryForTemplateLoading(new File(templateDirectory));
			generateXml(configuration, "document", contentMap);
			generateXml(configuration, "header1", headMap);
			generateXml(configuration, "footer1", footMap);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private void generateXml(Configuration configuration, String templateType, HashMap<String, Object> paraMap)
	{
		try
		{
			if (paraMap != null)
			{
				File file = new File(templateDirectory + templateType + "_template.xml");
				if (!file.exists())
				{
					throw new FileNotFoundException(templateType + "_template.xml没有找到！！");
				}
				else
				{
					//					deleteTabsAndLine(file);
					Template template = configuration.getTemplate(file.getName());
					template.process(paraMap, new FileWriter(templateDirectory + templateType + ".xml"));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 覆盖image文件夹中的图片文件
	 */
	private void overwritePic()
	{
		String picFold = wordRootPath + "word/media/image";
		for (Map.Entry<Integer, String> entry : imgMap.entrySet())
		{
			File jpegFile = new File(picFold + entry.getKey() + ".jpeg");
			if (jpegFile.exists())
			{
				FileUtil.copy(entry.getValue(), picFold + entry.getKey() + ".jpeg", true);
				break;
			}
			File pngFile = new File(picFold + entry.getKey() + ".png");
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
		try
		{
			int slash = outputPDFPath.lastIndexOf("/");
			outputPDFDirectory = outputPDFPath.substring(0, slash + 1);
			File pdfFile = new File(outputPDFPath);
			File pdfPath = new File(outputPDFDirectory);
			if (!pdfPath.exists())
			{
				pdfPath.mkdirs();
			}
			if (!pdfFile.exists())
			{
				pdfFile.createNewFile();
			}
			int dotIndex = pdfFile.getName().lastIndexOf(".");
			String fileName = pdfFile.getName().substring(0, dotIndex);
			tempDocPath = outputPDFDirectory + "$" + fileName + ".docx";
			ZipFile zipFile = new ZipFile(tempDocPath);
			File file = new File(wordRootPath);
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 将word格式的文件转换为pdf格式
	 */
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
			File outputFile = new File(outputPDFPath);
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

	private void deleteTabsAndLine(File templateFile)
	{
		String fileString = FileUtil.readString(templateFile, "UTF-8");
		fileString = fileString.replace("\n", "");
		fileString = fileString.replace("\t", "");
		fileString = fileString.replace("\r", "");
		FileUtil.writeString(fileString, templateFile, "UTF-8");
	}

	private void deleteTempFile(){
		FileUtil.del(new File(tempDocPath));
	}

	public void generatePdf()
	{
		readXml();
		overwritePic();
		zipDocx();
		wordToPDF();
		deleteTempFile();
	}
}
