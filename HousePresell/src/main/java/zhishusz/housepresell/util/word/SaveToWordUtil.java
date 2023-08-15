package zhishusz.housepresell.util.word;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

import zhishusz.housepresell.util.DirectoryUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * 根据ftl模板和map集合生成word文档
 *
 */	
public class SaveToWordUtil
{	
	
	/**
	 * 
	 * @param 	dataMap 		word中需要展示的动态数据
	 * @param 	templateName 	模板文件名称
	 * @param 	fileName 		生成的word文件名称
	 * @param 	filePath 		生成的word文件路径
	 */
	public String createWord(HashMap<String, Object> dataMap, String templateName, String fileName)
	{
		
//		HashMap<String, Object> testMap = new HashMap<String,Object>();
//		testData(testMap);
		
		//创建配置实例 
		Configuration configuration = new Configuration();
		
		try {
			//设置编码
            configuration.setDefaultEncoding("UTF-8");
            //ftl模板文件统一放至 zhishusz.housepresell.template 包下面 (暂定)
            configuration.setClassForTemplateLoading(this.getClass(),"/zhishusz.housepresell/template/");
            //根据模板名称寻找对应存放的模板
			Template template = configuration.getTemplate(templateName + ".ftl");
			
			String outPath = "/zhishusz.housepresell/word";
			
			String filePath = outPath + File.separator + fileName + ".docx";
			//输出文件
			File outFile = new File(filePath);
			//如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()){
                outFile.getParentFile().mkdirs();
            }
            //将模板和数据模型合并生成文件 
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
            template.process(dataMap, writer);
			writer.close();
			return filePath;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (TemplateException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private void testData(HashMap<String, Object> dataMap) {  
		dataMap.put("name", "chenlei");
		dataMap.put("age", "23");
		
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < 10; i++) {  
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("xuehao", i);
			map.put("neirong", "内容"+i);
			list.add(map);
		}
		
		dataMap.put("list", list);
	}
}
