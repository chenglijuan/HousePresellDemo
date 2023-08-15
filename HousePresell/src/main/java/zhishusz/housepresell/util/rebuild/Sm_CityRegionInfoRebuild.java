package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.state.S_RangeAuthType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
 * Rebuilder：基础数据-城市区域
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_CityRegionInfoRebuild extends RebuilderBase<Sm_CityRegionInfo>
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Override
	public Properties getSimpleInfo(Sm_CityRegionInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_CityRegionInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("theName", object.getTheName());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_CityRegionInfo> sm_CityRegionInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_CityRegionInfoList != null)
		{
			for(Sm_CityRegionInfo object:sm_CityRegionInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("theName", object.getTheName());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List executeForSelectList(List<Sm_CityRegionInfo> sm_CityRegionInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_CityRegionInfoList != null)
		{
			for(Sm_CityRegionInfo object:sm_CityRegionInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings({
		"rawtypes", "unchecked"
	})
	public List getDetailForRangeAuth(List<Sm_CityRegionInfo> sm_CityRegionInfoList, Sm_CityRegionInfoForm model)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_CityRegionInfoList != null)
		{
			Integer rangeAuthType = model.getRangeAuthType();
			
			if(S_RangeAuthType.Area.equals(rangeAuthType))
			{
				int city_level = 0;
				for(Sm_CityRegionInfo object : sm_CityRegionInfoList)
				{
					city_level+=1;
					
					Properties properties = new MyProperties();
					properties.put("id", city_level);
					properties.put("tableId", city_level);
					properties.put("pId", 0);
					properties.put("areaId", object.getTableId());
					properties.put("name", object.getTheName());
					properties.put("isParent", false);
					list.add(properties);
				}
			}
			else if(S_RangeAuthType.Project.equals(rangeAuthType))
			{
				Map<Long, Integer> cityIdAndIndexMap = new HashMap<Long,Integer>();
				Map<Long, Integer> cityIdAndProjectMaxIndex = new HashMap<Long,Integer>();
				Map<Long, Integer> projectIdAndIndexMap = new HashMap<Long,Integer>();
				
				//TODO 用缓存
				Empj_ProjectInfoForm empj_ProjectInfoForm = new Empj_ProjectInfoForm();
				empj_ProjectInfoForm.setTheState(S_TheState.Normal);
				List<Empj_ProjectInfo> empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getBasicHQL(), empj_ProjectInfoForm));
				
				int city_level = 0;
				for(Empj_ProjectInfo object : empj_ProjectInfoList)
				{
					Sm_CityRegionInfo city = object.getCityRegion();
					if(city == null) continue;
					
					Integer cityIndex = cityIdAndIndexMap.get(city.getTableId());
					if(cityIndex == null)
					{
						city_level += 1;
						cityIdAndIndexMap.put(city.getTableId(), city_level);
						
						Properties properties = new MyProperties();
						properties.put("id", city_level);
						properties.put("tableId", city_level);
						properties.put("pId", 0);
						properties.put("areaId", city.getTableId());
						properties.put("name", city.getTheName());
						properties.put("isParent", true);
						list.add(properties);
					}
					
					Integer projectIndex = projectIdAndIndexMap.get(object.getTableId());
					if(projectIndex == null) 
					{
						Integer projectMaxIndex = cityIdAndProjectMaxIndex.get(city.getTableId());
						if(projectMaxIndex == null)
						{
							projectMaxIndex = 1;
						}
						else
						{
							projectMaxIndex += 1;
						}
						cityIdAndProjectMaxIndex.put(city.getTableId(), projectMaxIndex);
						projectIdAndIndexMap.put(object.getTableId(), projectMaxIndex);
						
						String city_pId = cityIdAndIndexMap.get(city.getTableId()) + "";
						
						Properties properties = new MyProperties();
						properties.put("id", city_pId+"_"+projectMaxIndex);
						properties.put("tableId", city_pId+"_"+projectMaxIndex);
						properties.put("pId", city_pId);
						properties.put("projectId", object.getTableId());
						properties.put("name", object.getTheName());
						properties.put("isParent", false);
						list.add(properties);
					}
				}
			}
			else if(S_RangeAuthType.Building.equals(rangeAuthType))
			{
				Map<Long, Integer> cityIdAndIndexMap = new HashMap<Long,Integer>();
				Map<Long, Integer> cityIdAndProjectMaxIndex = new HashMap<Long,Integer>();
				Map<Long, Integer> projectIdAndIndexMap = new HashMap<Long,Integer>();
				Map<Long, Integer> projectIdAndBuildMaxIndex = new HashMap<Long,Integer>();
				Map<Long, Integer> buildIdAndIndexMap = new HashMap<Long,Integer>();
				
				//TODO 用缓存
				Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
				empj_BuildingInfoForm.setTheState(S_TheState.Normal);
				List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_BuildingInfoForm));
				
				Integer city_level = 0;
				for(Empj_BuildingInfo object : empj_BuildingInfoList)
				{
					Empj_ProjectInfo projectInfo = object.getProject();
					if(projectInfo == null) continue;
					Sm_CityRegionInfo cityRegionInfo = projectInfo.getCityRegion();
					if(cityRegionInfo == null) continue;
					//楼栋施工编号为空的数据不显示
					if(object.geteCodeFromConstruction() == null || object.geteCodeFromConstruction().length() == 0) continue;
					
					Integer cityIndex = cityIdAndIndexMap.get(cityRegionInfo.getTableId());
					if(cityIndex == null)
					{
						city_level += 1;
						cityIdAndIndexMap.put(cityRegionInfo.getTableId(), city_level);
						
						Properties properties = new MyProperties();
						properties.put("id", city_level);
						properties.put("tableId", city_level);
						properties.put("pId", 0);
						properties.put("areaId", cityRegionInfo.getTableId());
						properties.put("name", cityRegionInfo.getTheName());
						properties.put("isParent", true);
						list.add(properties);
					}
					
					Integer projectIndex = projectIdAndIndexMap.get(projectInfo.getTableId());
					if(projectIndex == null) 
					{
						Integer projectMaxIndex = cityIdAndProjectMaxIndex.get(cityRegionInfo.getTableId());
						if(projectMaxIndex == null)
						{
							projectMaxIndex = 1;
						}
						else
						{
							projectMaxIndex += 1;
						}
						cityIdAndProjectMaxIndex.put(cityRegionInfo.getTableId(), projectMaxIndex);
						projectIdAndIndexMap.put(projectInfo.getTableId(), projectMaxIndex);
						
						String city_pId = cityIdAndIndexMap.get(cityRegionInfo.getTableId()) + "";
						
						Properties properties = new MyProperties();
						properties.put("id", city_pId+"_"+projectMaxIndex);
						properties.put("tableId", city_pId+"_"+projectMaxIndex);
						properties.put("pId", city_pId);
						properties.put("projectId", projectInfo.getTableId());
						properties.put("name", projectInfo.getTheName());
						properties.put("isParent", true);
						list.add(properties);
					}
					
					Integer buildIndex = buildIdAndIndexMap.get(object.getTableId());
					if(buildIndex == null) 
					{
						Integer buildMaxIndex = projectIdAndBuildMaxIndex.get(projectInfo.getTableId());
						if(buildMaxIndex == null)
						{
							buildMaxIndex = 1;
						}
						else
						{
							buildMaxIndex += 1;
						}
						projectIdAndBuildMaxIndex.put(projectInfo.getTableId(), buildMaxIndex);
						buildIdAndIndexMap.put(object.getTableId(), buildMaxIndex);
						
						String city_pId = cityIdAndIndexMap.get(cityRegionInfo.getTableId()) + "";
						String project_pId = projectIdAndIndexMap.get(projectInfo.getTableId()) + "";
						Properties properties = new MyProperties();
						properties.put("id", city_pId+"_"+project_pId+"_"+buildMaxIndex);
						properties.put("tableId", city_pId+"_"+project_pId+"_"+buildMaxIndex);
						properties.put("pId", city_pId+"_"+project_pId);
						properties.put("buildId", object.getTableId());
						properties.put("name", object.geteCodeFromConstruction());
						properties.put("isParent", false);
						list.add(properties);
					}
				}
			}
		}
		return list;
	}
	
	
	public Properties getSelectForQuery(Sm_CityRegionInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());
		
		return properties;
	}
}
