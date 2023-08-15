package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：支付保证申请
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_PaymentGuaranteeApplyRebuild extends RebuilderBase<Empj_PaymentGuarantee>
{
	
	MyDatetime myDatetime = MyDatetime.getInstance();

	@Override
	public Properties getSimpleInfo(Empj_PaymentGuarantee object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("createTimeStampString", myDatetime.dateToString2(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		properties.put("userRecord", object.getUserRecord());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("recordTimeStampString",myDatetime.dateToString2(object.getRecordTimeStamp()) );
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("lastUpdateTimeStampString",myDatetime.dateToString2(object.getLastUpdateTimeStamp()) );
		
		properties.put("applyDate", object.getApplyDate());
		properties.put("companyName", object.getCompanyName());
		properties.put("projectName", object.getProjectName());
		properties.put("company", object.getCompany());
		properties.put("project", object.getProject());
		properties.put("guaranteeNo", object.getGuaranteeNo());
		properties.put("guaranteeCompany",object.getGuaranteeCompany());
		properties.put("guaranteeType",object.getGuaranteeType());
		properties.put("alreadyActualAmount",object.getAlreadyActualAmount());
		properties.put("actualAmount",object.getActualAmount());
		properties.put("guaranteedAmount",object.getGuaranteedAmount());
		properties.put("remark",object.getRemark());
		return properties;
	}

	@Override
	public Properties getDetail(Empj_PaymentGuarantee object)
	{
		
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		Sm_User userRecord = object.getUserRecord();
		if(object.getUserRecord()!=null){
			properties.put("userRecord", userRecord.getTheName());
		}
		properties.put("recordTimeStamp",myDatetime.dateToString2(object.getRecordTimeStamp()));
		
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{					
			properties.put("userUpdate", userUpdate.getTheName());
		}
		properties.put("lastUpdateTimeStamp",myDatetime.dateToString2(object.getLastUpdateTimeStamp()) );
		
		properties.put("applyDate", object.getApplyDate());

		Emmp_CompanyInfo company = object.getCompany();
		if(object.getCompany()!=null){
			properties.put("companyId", company.getTableId());
			properties.put("companyName", company.getTheName());
		}
		
		Empj_ProjectInfo project = object.getProject();
		if(object.getProject()!=null){
			properties.put("projectId", project.getTableId());
		}

		properties.put("guaranteeNo", object.getGuaranteeNo());
		
		properties.put("guaranteeCompany", object.getGuaranteeCompany());
		
		properties.put("eCode", object.geteCode());
		properties.put("guaranteeType",object.getGuaranteeType());
		properties.put("alreadyActualAmount",object.getAlreadyActualAmount());
		properties.put("actualAmount",object.getActualAmount());
		properties.put("guaranteedAmount",object.getGuaranteedAmount());
		properties.put("remark",object.getRemark());
		properties.put("busiState",object.getBusiState());

		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_PaymentGuarantee> empj_PaymentGuaranteeList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_PaymentGuaranteeList != null)
		{
			for(Empj_PaymentGuarantee object:empj_PaymentGuaranteeList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				list.add(properties);
			}
		}
		return list;
	}

	public List<Properties> getApplyList(List<Empj_PaymentGuarantee> empj_PaymentGuaranteeList){
		List<Properties> list = new ArrayList<Properties>();
		if(empj_PaymentGuaranteeList != null)
		{
			for(Empj_PaymentGuarantee object:empj_PaymentGuaranteeList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("eCode", object.geteCode());

				properties.put("busiState", object.getBusiState());
				
				properties.put("applyDate", object.getApplyDate());
				properties.put("companyName", object.getCompanyName());
				properties.put("projectName", object.getProjectName());
//				properties.put("company", object.getCompany());
//				properties.put("project", object.getProject());
				properties.put("guaranteeNo", object.getGuaranteeNo());
				properties.put("alreadyActualAmount",object.getAlreadyActualAmount());
				properties.put("actualAmount",object.getActualAmount());
				properties.put("guaranteedAmount",object.getGuaranteedAmount());
				
				list.add(properties);
			}
		}
		return list;
	}
}
