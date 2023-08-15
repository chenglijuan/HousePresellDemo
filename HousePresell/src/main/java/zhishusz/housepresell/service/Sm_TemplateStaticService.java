package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.TemplateStaticModel;
import zhishusz.housepresell.util.templateStatic.FreemarkerStaticUtil;
import zhishusz.housepresell.util.templateStatic.JsonUtil;
import com.google.gson.JsonObject;

/*
 * Service：模板静态化业务
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_TemplateStaticService
{
	@SuppressWarnings("rawtypes")
	public void execute(TemplateStaticModel model)
	{
		//执行静态化，公共参数
		JsonUtil jsonUtil = JsonUtil.getInstance();
		
		String relativeOutPutFilePath = model.getRelativeOutPutFilePath();
		if(relativeOutPutFilePath == null || relativeOutPutFilePath.length() == 0)
		{
			relativeOutPutFilePath = "admin/";
		}
		
		FreemarkerStaticUtil freemarkerStaticUtil = FreemarkerStaticUtil.getInstance(relativeOutPutFilePath);
				
		JsonObject dataJsonObj = jsonUtil.jsonStrToJsonObj(model.getDataJsonStr());
		String templatePath = model.getTemplatePath(); 
		String fileOutputPath = model.getFileOutputPath();
		Map orgInputMap = model.getOrgInputMap();
		Map currentInputMap = model.getCurrentInputMap();
		
		if(orgInputMap == null)
		{
			orgInputMap = new HashMap();
		}
		if(currentInputMap == null)
		{
			currentInputMap = new HashMap();
		}
		
		//执行静态化
		freemarkerStaticUtil.createFile(templatePath, fileOutputPath,
				dataJsonObj, orgInputMap, currentInputMap);
	}
}
