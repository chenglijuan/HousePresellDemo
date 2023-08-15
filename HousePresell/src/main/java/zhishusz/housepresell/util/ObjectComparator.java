package zhishusz.housepresell.util;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;

/*
 * 工具类：对象对比工具，获取两个对象之间属性不同的字段
 * Company：ZhiShuSZ
 */
public class ObjectComparator<T>
{
	/**
	 * 
	 * @param orgObj
	 * @param newObj
	 * @return
	 * 		对比完后的新数据
	 */
	@SuppressWarnings("null")
	public Map<String, Object> execute(T orgObj, T newObj)
	{
		Map<String, Object> m1 = BeanUtil.beanToMap(orgObj);
		Map<String, Object> m2 = BeanUtil.beanToMap(newObj);

		for (Map.Entry<String, Object> entry2 : m2.entrySet())
		{
			Object m2value = entry2.getValue();
			Object m1value = m1.get(entry2.getKey());
			if(m1value ==null)
			{
				if(m2value !=null)
				{
				   m1.put(entry2.getKey(), m2value);
				}
			}
			else
			{
				if (m2value!=null&&!m1value.equals(m2value))
				{
					m1.put(entry2.getKey(), m2value);
				}
			}

		}
		  return m1;
	}

	public Map<String, Object> execute2(T orgObj, T newObj)
	{
		Map<String ,Object> result = new HashMap<>();
		Map<String, Object> m1 = BeanUtil.beanToMap(orgObj);
		Map<String, Object> m2 = BeanUtil.beanToMap(newObj);

		for (Map.Entry<String, Object> entry2 : m2.entrySet())
		{
			Object m2value = entry2.getValue();
			Object m1value = m1.get(entry2.getKey());
			if(m1value ==null)
			{
				if(m2value !=null)
				{
					m1.put(entry2.getKey(), m2value);
				}
			}
			else
			{
				if (m2value!=null&&!m1value.equals(m2value))
				{
					m1.put(entry2.getKey(), m2value);
					m2.put(entry2.getKey(),m1value);
				}
			}

		}
		result.put("newObjMap",m1);
		result.put("ossJsonMap",m2);
		return result;
	}
}
