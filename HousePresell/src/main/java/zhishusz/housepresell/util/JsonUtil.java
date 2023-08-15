package zhishusz.housepresell.util;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

/*
* 主要功能：Json的处理工具
*  * JSON转换工具类
 * 提供了 对象到json，map到json ，数组到json，list到json的转换
 * dengm(dengm@unimassystem.com)
*/
public class JsonUtil {
	/*
	* 主要功能：判断Object类型的value数据是什么类型的值，并连接name字符串，供下面的函数调用
	* 输入参数：StringBuffer类型的对象，value-name键值对，level转换的层次（超过最大层级限制就不再转换）
	* 输出参数：StringBuffer类型的对象
	*/
    @SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public StringBuffer contact(StringBuffer sb, Object value, String name, int level)
    {
    	if(name != null && name.length() > 0)
    	{
    		if(name.indexOf("password") > -1) return sb;//过滤password用户密码
    		else if(name.indexOf("Password") > -1) return sb;
    		else name = "\"" + name + "\":";
    	}
    	
    	if (value == null){
    		sb.append(name).append("null,");
	    }
	    else if (value instanceof Boolean){
	        Boolean b = (Boolean)value;
	        sb.append(name).append(b.toString()).append(",");
	    }
	    else if (value instanceof Integer ||
	             value instanceof Long ||
	             value instanceof Float ||
	             value instanceof Double ||
	             value instanceof Short ||
	             value instanceof java.math.BigInteger ||
	             value instanceof java.math.BigDecimal){
	    	sb.append(name).append(value.toString()).append(",");
	    }
	    else if (value instanceof String){
	    	String v = (String)value;
	        if(name.indexOf("jsonObject") > -1)
	        {
	        	sb.append(name).append(v).append(",");
	        }
	        else
	        {
	        	v = v.replaceAll("\\\\", "\\\\\\\\");
	        	v = v.replaceAll("\n", "\\\\n");
	        	v = v.replaceAll("\r", "\\\\r");
	        	v = v.replaceAll("\"", "\\\\\"");
	        	//v = v.replaceAll("'", "\\\\\'");
	        	sb.append(name).append("\"").append(v).append("\",");
	        }
	    }
	    else if (value instanceof java.sql.Date){
	        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        java.sql.Date v = (java.sql.Date)value;
	        String s = df.format(new java.util.Date(v.getTime()));
	        sb.append(name).append("\"").append(s).append("\",");
	    }
	    else if (value instanceof java.util.Date){
	        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        java.util.Date v = (java.util.Date)value;
	        String s = df.format(v);
	        sb.append(name).append("\"").append(s).append("\",");
	    }
	    else if (value instanceof java.sql.Timestamp){
	        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        java.sql.Timestamp v = (java.sql.Timestamp)value;
	        String s = df.format(new java.util.Date(v.getTime()));
	        sb.append(name).append("\"").append(s).append("\",");
	    }
	    else if (value instanceof List){
	    	if(level > 0)
	    	{
		        String v = listToJson((List)value, level);
		        sb.append(name).append(v).append(",");
	    	}
	    }
	    else if (value instanceof Set)
	    {
	    	if(level > 0)
	    	{
	    		sb.append(name).append(setToJson((Set)value, level)).append(",");
	    	}
	    }
	    else if (value instanceof Map){
	    	if(level > 0)
	    	{
	    		sb.append(name).append(mapToJson((Map)value, level)).append(",");
	    	}
	    }
	    else if (value.getClass().getName().startsWith("java") == false){
	    	if(level > 0)
	    	{
		        Map<String,Object> attr = getAttributes(value);
		        sb.append(name).append(mapToJson(attr, level)).append(",");
	    	}
	    }
	    else{
	    	sb.append(name).append("\"").append(value.toString()).append("\",");
	    }
    	
    	return sb;
    }
    
    public String propertiesToJson(Properties properties)
    {
    	return propertiesToJson(properties, 5);
    }
    
	@SuppressWarnings("rawtypes")
	public String propertiesToJson(Properties properties,int level)
    {
    	if(properties.size() == 0)
    	{
    		return "{}";
    	}

    	if(level > 0){
			level--;
		}
    	else{
    		return "{}";
    	}
    	
    	StringBuffer sb = new StringBuffer();
    	
    	Enumeration enu = properties.propertyNames();
		while(enu.hasMoreElements())
		{
			String name = (String)enu.nextElement();
			Object value = properties.get(name);
			
    		sb = contact(sb,value,name,level);
		}
		
    	if (sb.length() == 0){
        	return "{}";
        }
        else{
            return "{"+sb.toString().substring(0,sb.toString().length()-1)+"}";
        }
    }
    
    public String mapToJson(Map<String,Object> map, int level){
    	if(map.size() == 0)
    	{
    		return "{}";
    	}

    	if(level > 0){
			level--;
		}
    	else{
    		return "{}";
    	}
    	
    	StringBuffer sb = new StringBuffer();
    	Set<Entry<String,Object>> set= map.entrySet();
    	
    	for(Map.Entry<String, Object> entry : set){
    		
    		String name = entry.getKey();
    		Object value = entry.getValue();
    		
    		sb = contact(sb,value,name,level);
    	}

        if (sb.toString().length() == 0){
        	return "{}";
        }
        else{
            return "{"+sb.toString().substring(0,sb.toString().length()-1)+"}";
        }
    }
    /*
	* 主要功能：将set集合转换为json格式数据
	* 输入参数：存储String类型的Set，level转换层级
	* 输出参数：String类型字符串
	*/
    public String setToJson(Set<String> set, int level)
    {
    	if(set.size() == 0)
		{
    		return "[]";
		}

    	if(level > 0){
			level--;
		}
    	else{
    		return "[]";
    	}
    	
    	StringBuffer sb = new StringBuffer();
    	for(Object obj: set){
    		sb = contact(sb,obj,"",level);
    	}
    	
    	if (sb.length() == 0){
    		return "[]";
        }
        else{
            return "["+sb.toString().substring(0,sb.toString().length()-1)+"]";
        }
    }
    /*
	* 主要功能：将数组转换为json格式数据
	* 输入参数：array数组数据，int类型的数字
	* 输出参数：String类型字符串
	*/
    public String arrayToJson(Object[] array, int level){
        if (array.length == 0){
            return "[]";
        }

		if(level > 0){
			level--;
		}
    	else{
    		return "[]";
    	}

		StringBuffer sb = new StringBuffer();
		for (Object obj : array) {
			sb = contact(sb, obj, "", level);
		}
		
		if(sb.length() == 0)
		{
			return "[]";
		}
		else
		{
			return "[" + sb.toString().substring(0, sb.toString().length() - 1) + "]";
		}
    }
    
	@SuppressWarnings("rawtypes")
	public String listToJson(List list, int level){
        return arrayToJson(list.toArray(), level);
    }
    
    /*
	* 主要功能：取得对象的属性名与值的键值对
	* 输入参数：Object类型的对象
	* 输出参数：Map类型的集合
	*/
    public Map<String,Object> getAttributes(Object obj){
        Class<?> c = obj.getClass();
       
        Map<String,Object> attr = new HashMap<String,Object>();
        
        //取得所有公共字段
        for(Field f: c.getFields()){
            try{
                Object value = f.get(obj);
                attr.put(f.getName(), value);
            }
            catch(Exception e){
            }
        }
        
        //取得所有类方法
        for(Method m: c.getDeclaredMethods()){
            String mname = m.getName();
            if (mname.equals("getClass")){
                continue;
            }
            else if (mname.startsWith("get")){
                String pname = mname.substring(3);
                if (pname.length()==1){
                    pname = pname.toLowerCase();
                }
                else{
                    pname = pname.substring(0,1).toLowerCase()+pname.substring(1);
                }
                
                try{
                    Object value = m.invoke(obj);
                    attr.put(pname, value);
                }
                catch(Exception e){}
            }
            else if (mname.startsWith("is")){
                String pname = mname.substring(2);
                if (pname.length()==1){
                    pname = pname.toLowerCase();
                }
                else{
                    pname = pname.substring(0,1).toLowerCase()+pname.substring(1);
                }
                
                try{
                    Object value = m.invoke(obj);
                    attr.put(pname, value);
                }
                catch(Exception e){}
            }
        }
        return attr;
    }
    
    public String getJsonString(String jsonFilePath)
	{
		BufferedReader reader = null;  
        String laststr = "";
        String jsonPath = jsonFilePath;
        try{  
            FileInputStream fileInputStream = new FileInputStream(jsonPath);  
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");  
            reader = new BufferedReader(inputStreamReader);  
            String tempString = null;  
            while((tempString = reader.readLine()) != null){  
                laststr += tempString;  
            }  
            reader.close();  
        }catch(IOException e){  
            e.printStackTrace();  
        }finally{  
            if(reader != null){  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }
        return laststr;
	}

	public String objToJson(Object object,int level){
		Map<String, Object> attributes = getAttributes(object);
		return mapToJson(attributes,level);
	}
}