package zhishusz.housepresell.util.templateStatic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class RemoteTemplateLoader extends URLTemplateLoader
{
	private String remoteUrl;

	public RemoteTemplateLoader(String remoteUrl)
	{
		this.remoteUrl = remoteUrl;
	}

	@Override
	protected URL getURL(String name)
	{
		return getURL();
	}
	public URL getURL()
	{
		URL url = null;
		try
		{
			url = new URL(remoteUrl);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		return url;
	}
	
	public Template getTemplate(Configuration cft)
	{
		Template template = null;
		try
		{
			template = new Template("", getReader(findTemplateSource(remoteUrl), "UTF-8"), cft);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return template;
	}
}
