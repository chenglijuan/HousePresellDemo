package zhishusz.housepresell.util.excel.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.state.S_RangeAuthType;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RangeAuthorizationTemplate implements IExportExcel<Sm_Permission_RangeAuthorization>
{
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
	
	@Getter @Setter @IFieldAnnotation(remark="机构类型")
	private String companyInfoType;
	
	@Getter @Setter @IFieldAnnotation(remark="机构名称")
	private String companyInfoName;
	
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
		
		map.put("companyInfoType", "机构类型");
		map.put("companyInfoName", "机构名称");
		map.put("rangeAuthType", "授权类别");
		map.put("rangeInfoListStr", "区域/项目名称/楼幢");
		map.put("authTimePeriod", "授权日期");
		
		return map;
	}
	
	@Override
	public void init(Sm_Permission_RangeAuthorization object)
	{
		Emmp_CompanyInfo companyInfo = object.getEmmp_CompanyInfo();
		if(companyInfo != null)
		{
			Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("8", companyInfo.getTheType());
			if(sm_BaseParameter != null)
			{
				this.setCompanyInfoType(myString.parseForExport(sm_BaseParameter.getTheName()));
			}
			else
			{
				this.setCompanyInfoType("");
			}
			this.setCompanyInfoName(myString.parseForExport(companyInfo.getTheName()));
		}
		else
		{
			this.setCompanyInfoType("");
			this.setCompanyInfoName("");
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