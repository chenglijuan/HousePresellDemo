package zhishusz.housepresell.util.excel.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_RangeAuthType;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Sm_Permission_RangeAuthorizationForZTTemplate implements IExportExcel<Sm_Permission_RangeAuthorization>
{
	@Getter @Setter @IFieldAnnotation(remark="正泰用户")
	private String userInfoName;
	
	@Getter @Setter @IFieldAnnotation(remark="授权类别")
	private String rangeAuthType;
	
	@Getter @Setter @IFieldAnnotation(remark="区域/项目名称/楼幢")
	private String rangeInfoListStr;

	@Getter @Setter @IFieldAnnotation(remark="授权日期")
	private String authTimePeriod;
	
	private MyString  myString = MyString.getInstance();
	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("userInfoName", "机构名称");
		map.put("rangeAuthType", "授权类别");
		map.put("rangeInfoListStr", "区域/项目名称/楼幢");
		map.put("authTimePeriod", "授权日期");
		
		return map;
	}
	
	@Override
	public void init(Sm_Permission_RangeAuthorization object)
	{
		Sm_User userInfo = object.getUserInfo();
		if(userInfo != null)
		{
			this.setUserInfoName(myString.parseForExport(userInfo.getTheName()));
		}
		else
		{
			this.setUserInfoName("");
		}
		
		this.setRangeAuthType(myString.parseForExport(S_RangeAuthType.IntKeyToVal.get(object.getRangeAuthType())));
		
		//区域/项目名称/楼幢
		List<String> rangeInfoList_Str = new ArrayList<String>();
		List<Sm_Permission_Range> rangeInfoList = object.getRangeInfoList();
		if(rangeInfoList != null)
		{
			for(Sm_Permission_Range rangeInfo : rangeInfoList)
			{
				if(S_RangeAuthType.Area.equals(object.getRangeAuthType()))
				{
					Sm_CityRegionInfo cityRegionInfo = rangeInfo.getCityRegionInfo();
					rangeInfoList_Str.add(cityRegionInfo.getTheName());
				}
				else if(S_RangeAuthType.Project.equals(object.getRangeAuthType()))
				{
					Empj_ProjectInfo projectInfo = rangeInfo.getProjectInfo();
					rangeInfoList_Str.add(projectInfo.getTheName());
				}
				else if(S_RangeAuthType.Building.equals(object.getRangeAuthType()))
				{
					Empj_BuildingInfo buildingInfo = rangeInfo.getBuildingInfo();
					rangeInfoList_Str.add(buildingInfo.geteCodeFromConstruction());
				}
			}
		}
		String rangeInfoListStr = StringUtils.join(rangeInfoList_Str, "、");
		this.setRangeInfoListStr(myString.parseForExport(rangeInfoListStr));
		this.setAuthTimePeriod(myString.parseForExport(myDatetime.dateToString(object.getAuthStartTimeStamp(),"yyyy-MM-dd") +" - "+ myDatetime.dateToString(object.getAuthEndTimeStamp(),"yyyy-MM-dd")));
	}
}