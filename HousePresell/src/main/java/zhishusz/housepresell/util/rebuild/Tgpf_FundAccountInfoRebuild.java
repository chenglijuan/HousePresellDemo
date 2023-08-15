package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_FundAccountInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：推送给财务系统-设置
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_FundAccountInfoRebuild extends RebuilderBase<Tgpf_FundAccountInfo>
{
	@Override
	public Properties getSimpleInfo(Tgpf_FundAccountInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_FundAccountInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
//		properties.put("busiState", object.getBusiState());
//		properties.put("eCode", object.geteCode());
//		properties.put("userStart", object.getUserStart());
//		properties.put("userStartId", object.getUserStart().getTableId());
//		properties.put("createTimeStamp", object.getCreateTimeStamp());
//		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//		properties.put("userRecord", object.getUserRecord());
//		properties.put("userRecordId", object.getUserRecord().getTableId());
//		properties.put("recordTimeStamp", object.getRecordTimeStamp());
//		properties.put("companyInfo", object.getCompanyInfo());
//		properties.put("companyInfoId", object.getCompanyInfo().getTableId());
		properties.put("theNameOfCompany", object.getTheNameOfCompany());
		properties.put("eCodeOfCompany", object.geteCodeOfCompany());
		properties.put("fullNameOfCompanyFromFinanceSystem", object.getFullNameOfCompanyFromFinanceSystem());
		properties.put("shortNameOfCompanyFromFinanceSystem", object.getShortNameOfCompanyFromFinanceSystem());
//		properties.put("project", object.getProject());
//		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
//		properties.put("eCodeOfProject", object.geteCodeOfProject());
		properties.put("fullNameOfProjectFromFinanceSystem", object.getFullNameOfProjectFromFinanceSystem());
		properties.put("shortNameOfProjectFromFinanceSystem", object.getShortNameOfProjectFromFinanceSystem());
//		properties.put("building", object.getBuilding());
//		properties.put("buildingId", object.getBuilding().getTableId());
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
		properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
		properties.put("fullNameOfBuildingFromFinanceSystem", object.getFullNameOfBuildingFromFinanceSystem());
		properties.put("shortNameOfBuildingFromFinanceSystem", object.getShortNameOfBuildingFromFinanceSystem());
		properties.put("operateType", object.getOperateType());
		properties.put("depositRemark", object.getDepositRemark());
		properties.put("financeRemark", object.getFinanceRemark());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_FundAccountInfo> tgpf_FundAccountInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_FundAccountInfoList != null)
		{
			for(Tgpf_FundAccountInfo object:tgpf_FundAccountInfoList)
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
				properties.put("companyInfo", object.getCompanyInfo());
				properties.put("companyInfoId", object.getCompanyInfo().getTableId());
				properties.put("theNameOfCompany", object.getTheNameOfCompany());
				properties.put("eCodeOfCompany", object.geteCodeOfCompany());
				properties.put("fullNameOfCompanyFromFinanceSystem", object.getFullNameOfCompanyFromFinanceSystem());
				properties.put("shortNameOfCompanyFromFinanceSystem", object.getShortNameOfCompanyFromFinanceSystem());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("eCodeOfProject", object.geteCodeOfProject());
				properties.put("fullNameOfProjectFromFinanceSystem", object.getFullNameOfProjectFromFinanceSystem());
				properties.put("shortNameOfProjectFromFinanceSystem", object.getShortNameOfProjectFromFinanceSystem());
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("fullNameOfBuildingFromFinanceSystem", object.getFullNameOfBuildingFromFinanceSystem());
				properties.put("shortNameOfBuildingFromFinanceSystem", object.getShortNameOfBuildingFromFinanceSystem());
				properties.put("operateType", object.getOperateType());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	
	@SuppressWarnings("rawtypes")
	public List getFundAccountList(List<Tgpf_FundAccountInfo> tgpf_FundAccountInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_FundAccountInfoList != null)
		{
			for(Tgpf_FundAccountInfo object:tgpf_FundAccountInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
//				properties.put("busiState", object.getBusiState());
//				properties.put("eCode", object.geteCode());
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				properties.put("userRecordId", object.getUserRecord().getTableId());
//				properties.put("recordTimeStamp", object.getRecordTimeStamp());
//				properties.put("companyInfo", object.getCompanyInfo());
//				properties.put("companyInfoId", object.getCompanyInfo().getTableId());
				properties.put("theNameOfCompany", object.getTheNameOfCompany());
//				properties.put("eCodeOfCompany", object.geteCodeOfCompany());
				properties.put("fullNameOfCompanyFromFinanceSystem", object.getFullNameOfCompanyFromFinanceSystem());
				properties.put("shortNameOfCompanyFromFinanceSystem", object.getShortNameOfCompanyFromFinanceSystem());
//				properties.put("project", object.getProject());
//				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
//				properties.put("eCodeOfProject", object.geteCodeOfProject());
				properties.put("fullNameOfProjectFromFinanceSystem", object.getFullNameOfProjectFromFinanceSystem());
				properties.put("shortNameOfProjectFromFinanceSystem", object.getShortNameOfProjectFromFinanceSystem());
//				properties.put("building", object.getBuilding());
//				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("fullNameOfBuildingFromFinanceSystem", object.getFullNameOfBuildingFromFinanceSystem());
				properties.put("shortNameOfBuildingFromFinanceSystem", object.getShortNameOfBuildingFromFinanceSystem());
//				properties.put("operateType", object.getOperateType());
//				properties.put("configureUser", object.getConfigureUser());
//				properties.put("configureTime", object.getConfigureTime());
				
				list.add(properties);
			}
		}
		return list;
	}
}
