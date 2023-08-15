package zhishusz.housepresell.util.templateStatic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

//自定义标签-远程模板
public class IncludeXMacro implements TemplateDirectiveModel
{
	public static String GetTheName()
	{
		return "includeX";
	}
	
	private String template;
	
	public String getTemplate()
	{
		return template;
	}
	public void setTemplate(String template)
	{
		this.template = template;
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Override
	public void execute(Environment environment, Map params, TemplateModel[] templateModel,
			TemplateDirectiveBody directiveBody) throws TemplateException, IOException
	{
		try
		{
			BeanUtils.populate(this, params);
			
			if(this.getTemplate() != null)
			{
				RemoteTemplateLoader templateLoader = new RemoteTemplateLoader(this.getTemplate());
				
				environment.include(templateLoader.getTemplate(environment.getConfiguration()));
			}
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}
}