package zhishusz.housepresell.util.templateStatic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import zhishusz.housepresell.util.DirectoryUtil;
import com.google.gson.JsonObject;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

//用FreeMarker实现模板静态化工具类
public class FreemarkerStaticUtil
{
	private static FreemarkerStaticUtil instance;
	private static final String Font = "UTF-8";
	private JsonUtil jsonUtil;
	private String filePath;
	private Version version;
	private String oldRelativeOutPutFilePath;
	
	public String getOldRelativeOutPutFilePath()
	{
		return oldRelativeOutPutFilePath;
	}
	public void setOldRelativeOutPutFilePath(String oldRelativeOutPutFilePath)
	{
		this.oldRelativeOutPutFilePath = oldRelativeOutPutFilePath;
	}
	
	public static FreemarkerStaticUtil getInstance()
	{
		if(instance == null) instance = new FreemarkerStaticUtil();
		
		return instance;
	}
	public static FreemarkerStaticUtil getInstance(String relativeOutPutFilePath)
	{
		//实例为空 或者 实例不为空，但是地址不相等
		if(instance == null || ((relativeOutPutFilePath != null && !relativeOutPutFilePath.equals(instance.getOldRelativeOutPutFilePath())) || 
				instance.getOldRelativeOutPutFilePath() != null && instance.getOldRelativeOutPutFilePath().equals(relativeOutPutFilePath)))
		{
			//创建实例
			instance = new FreemarkerStaticUtil(relativeOutPutFilePath);
		}
		return instance;
	}
	
	public FreemarkerStaticUtil()
	{
		this.jsonUtil = JsonUtil.getInstance();
		
		//默认静态化文件的根目录
		DirectoryUtil directoryUtil = new DirectoryUtil();
		filePath = directoryUtil.getProjectRoot()+"/tempStaticFile/";
		directoryUtil.mkdir(filePath);
		
		version = new Version("2.3.23");
	}
	
	public void setFileRootPath(String filePath)
	{
		this.filePath = filePath;
	}

	/**
	 * @param relativeOutPutFilePath 输出文件相对路径：admin/superAdmin/
	 * 		为空则用默认路径：根路径/tempStaticFile/
	 */
	public FreemarkerStaticUtil(String relativeOutPutFilePath)
	{
		this.jsonUtil = JsonUtil.getInstance();
		
		DirectoryUtil directoryUtil = new DirectoryUtil();
		oldRelativeOutPutFilePath = relativeOutPutFilePath;
		if(relativeOutPutFilePath == null)
		{
			filePath = directoryUtil.getProjectRoot()+"/tempStaticFile/";
		}
		else
		{
			filePath = directoryUtil.getProjectRoot()+"/"+relativeOutPutFilePath;
		}
		//filePath = "E:/TemplateStatic/Test20171021/";//本地 Junit 测试时用“指定根路径” 
		directoryUtil.mkdir(filePath);
		
		version = new Version("2.3.23");
	}
	
	/**
	 * @param templatePath 模板所在路径(注意全部用“/”分割)
	 * @param fileOutputPath 文件输出路径(注意全部用“/”分割)
	 * @param dataJsonObj 渲染模板所需要的数据
	 * @param orgInputMap 最原始的完整的输入参数
	 * @param currentInputMap 当前输入参数组合
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void createFile(String templatePath, String fileOutputPath,
			JsonObject dataJsonObj, Map orgInputMap, Map currentInputMap)
	{
		try
		{
			Configuration configuration = new Configuration(version);
			String templateName = "";
			if(templatePath != null && templatePath.length() > 0 && !templatePath.startsWith("http://") && !templatePath.startsWith("https://"))
			{
				//跨平台：文件路径分隔符 File.separator
				Integer lastIndex = templatePath.lastIndexOf("/");
				//模板文件夹路径：templateFilePath
				String templateFilePath = templatePath.substring(0, lastIndex);
				
				//根据物理路径，获取模板内容
				configuration.setDirectoryForTemplateLoading(new File(templateFilePath));
				//模板名称
				templateName = templatePath.substring(lastIndex+1);
			}
			else
			{
				//根据网络路径，获取模板内容
				configuration.setTemplateLoader(new RemoteTemplateLoader(templatePath));
			}
			
			configuration.setSharedVariable(LabelDirective.GetTheName(), new LabelDirective());
			configuration.setSharedVariable(IncludeXMacro.GetTheName(), new IncludeXMacro());

			Template template = configuration.getTemplate(templateName, Font);
			//指定模板如何检索数据模型
			configuration.setObjectWrapper(new DefaultObjectWrapper(version));
			//这个一定要设置，不然在生成的页面中 会乱码
			configuration.setDefaultEncoding(Font);
			
			Map parameterMap = new HashMap<String, Object>();
			//需要模板渲染的数据
			parameterMap.put("dataJsonStr", dataJsonObj);
			if(orgInputMap != null)
			{
				//列表页面，key-JsonArray
				parameterMap.put("orgInputMapStr", jsonUtil.mapToJson(orgInputMap, 2));
			}
			if(currentInputMap != null)
			{
				//当前页面的输入参数
				parameterMap.put("paramListStr", jsonUtil.mapToJson(currentInputMap, 2));
			}
			
			String fileName = fileOutputPath.substring(fileOutputPath.lastIndexOf("/") + 1, fileOutputPath.length());
			
			//执行渲染，并输出到指定路径
			Writer writer = new OutputStreamWriter(new FileOutputStream(filePath+fileName), "UTF-8");
			template.process(parameterMap, writer);
			writer.close();
			
//			//TODO 将生成的文件上传到第三方
//			
//			// 删除本地文件
//			File file = new File(filePath+fileName);
//		    // 路径为文件且不为空则进行删除
//		    if (file.isFile() && file.exists()) {
//		        file.delete();
//		    }
		}
		catch (TemplateNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (TemplateException e)
		{
			e.printStackTrace();
		}
		catch (MalformedTemplateNameException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
