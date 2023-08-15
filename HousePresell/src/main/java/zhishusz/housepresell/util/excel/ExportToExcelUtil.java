package zhishusz.housepresell.util.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.model.IExportExcel;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import sun.reflect.misc.ReflectUtil;

public class ExportToExcelUtil
{
	public Properties execute(List fromList, Class toClass, String fileNameHead)
	{
		Properties properties = new MyProperties();

		//初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String relativeDir = directoryUtil.createRelativePathWithDate("exportExcel");//文件在项目中的相对路径
		String localPath = directoryUtil.getProjectRoot();//项目路径
		
		String saveDirectory = localPath + relativeDir;//文件在服务器文件系统中的完整路径
		
		directoryUtil.mkdir(saveDirectory);

		String strNewFileName = fileNameHead + "-"
			+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(),"yyyyMMddHHmmss")
			+ ".xlsx";

		try
		{
			Object obj = ReflectUtil.newInstance(toClass);
			IExportExcel iExportExcel = (IExportExcel)obj;
			
			// 通过工具类创建writer
			ExcelWriter writer = ExcelUtil.getWriter(saveDirectory + strNewFileName);
			
			//自定义标题别名
			writer.setHeaderAlias(iExportExcel.GetExcelHead());

			// 一次性写出内容，使用默认样式
			writer.write(getTemplateList(fromList, toClass));
			
			// 关闭writer，释放内存
			writer.flush();
			writer.close();

			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			
			properties.put("fileName", strNewFileName);
			properties.put("fileRelativePath", relativeDir + strNewFileName);
			properties.put("fileLocalFullPath", saveDirectory + strNewFileName);
			
			return properties;
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.fail);
		properties.put(S_NormalFlag.info, "文件导出异常");
		return properties;
	}
	
	@SuppressWarnings({
		"unchecked", "rawtypes",
	})
	public List getTemplateList(List fromList, Class toClass)
	{
		List list = new ArrayList();
		
		for(Object fromObj : fromList)
		{
			try
			{
				IExportExcel iExportExcel = (IExportExcel)toClass.newInstance();
				iExportExcel.init(fromObj);
				
				list.add(iExportExcel);
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		
		return list;
	}
}
