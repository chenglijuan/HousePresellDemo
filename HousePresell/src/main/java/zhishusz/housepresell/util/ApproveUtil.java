package zhishusz.housepresell.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.hutool.core.bean.BeanUtil;

public class ApproveUtil
{
    /**
     * 抽出需要审批的数据（Json格式）
     * @param obj
     * @param arrList
     * @return
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getJsonData(Object obj, List<String> arrList)
	{
		String peddingApprovalJson = null; //需要审批的json数据
		Map<String,Object> peddingApprovalMap = new HashMap<>();
		Map<String, Object> objMap = BeanUtil.beanToMap(obj);
		for (String key : arrList)
		{
			peddingApprovalMap.put(key,objMap.get(key));
		}
		
		Properties properties = new MyProperties();
		for(Map.Entry entry : peddingApprovalMap.entrySet()) 
		{
			properties.put(entry.getKey(), entry.getValue());
			if(entry.getValue() != null && entry.getValue().getClass().isArray())
			{
				List objectList = new ArrayList();
				for(Object eachObj : ((Object[])entry.getValue()))
				{
					objectList.add(eachObj);
				}
				properties.put(entry.getKey(), objectList);
			}
		}
		
		JsonUtil jsonUtil = new JsonUtil();
		
		peddingApprovalJson = jsonUtil.propertiesToJson(properties);
		
		return  peddingApprovalJson;
	}
}
