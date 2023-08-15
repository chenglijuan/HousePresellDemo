package zhishusz.housepresell.util.templateStatic;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

//自定义标签-普通标签
public class LabelDirective implements TemplateDirectiveModel
{
	public static String GetTheName()
	{
		return "label";
	}
	
	private Integer num;

	public Integer getNum()
	{
		return num;
	}
	public void setNum(Integer num)
	{
		this.num = num;
	}

	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	@Override
	public void execute(Environment environment, Map params,
			TemplateModel[] templateModel, TemplateDirectiveBody directiveBody)
			throws TemplateException, IOException
	{
		try
		{
			BeanUtils.populate(this, params);
			
			if(this.getNum() != null)
			{
				Writer out = environment.getOut();

				out.write("num=" + this.getNum());
				if (directiveBody != null)
				{
					directiveBody.render(out);
				}
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
