package zhishusz.housepresell.initialize;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import zhishusz.housepresell.util.MyString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:applicationContext.xml", "classpath*:spring-servlet.xml"
})
@WebAppConfiguration
public abstract class BaseJunitTest 
{
	public MockMvc mockMvc;
	@Autowired
	public WebApplicationContext webApplicationContext;
	public Integer DefaultInterfaceVersion = 19000101;
	//public String DefaultToken = "vSNuVD%2B2Gp9ruCf0bQYDvRoO6DnUsu%2BGX7U01sl7%2F5HmstQPLy4dUg%3D%3D";
	public Long DefaultUserId = 2l;
	
	
	@Before
	public void setUp() throws Exception
	{
		//AdminFilter adminFilter = new AdminFilter();
		//TerminaUserFilter terminaUserFilter = new TerminaUserFilter();
		//OpenSessionInViewFilter openSessionInViewFilter = new OpenSessionInViewFilter();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				//.addFilter(openSessionInViewFilter, "/*")
				//.addFilter(adminFilter, "*.ahtml,")
				//.addFilter(adminFilter, "*.sahtml")
				//.addFilter(terminaUserFilter, "*.tfhtml")
				//.addFilter(terminaUserFilter, "*.tdhtml")
				//.addFilter(terminaUserFilter, "*.tshtml")
				.build();
	}

	public abstract void execute() throws Exception;

	public static MultiValueMap<String, String> transBeanToMap(Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		try
		{
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors)
			{
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class"))
				{
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					map.set(key, MyString.getInstance().parse(value));
				}

			}
		}
		catch (Exception e)
		{
			System.out.println("transBean2Map Error " + e);
		}
		return map;
	}
}
