package zhishusz.housepresell.util.rebuild;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：支付保证申请撤销
 * 
 * @author li
 */

@Service
public class Empj_PaymentGuaranteeListRebuild extends RebuilderBase<Empj_PaymentGuarantee>
{

	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfo;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired	
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	
	@Override
	public Properties getSimpleInfo(Empj_PaymentGuarantee object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();
				
		properties.put("tableId", object.getTableId());
		properties.put("revokeNo", object.getRevokeNo());
		properties.put("applyDate",object.getApplyDate());
		properties.put("eCode", object.geteCode());
		
		if (object.getCompanyName() != null)
		{
			properties.put("companyName", object.getCompanyName());
		}
		if (object.getProject() != null)
		{
			properties.put("projectName", object.getProjectName());
		}

		properties.put("guaranteeCompany", object.getGuaranteeCompany());
			
		if(object.getGuaranteeType() != null)
		{ 
			properties.put("guaranteeType", object.getGuaranteeType());
		}
		properties.put("guaranteeNo", object.getGuaranteeNo());
		properties.put("alreadyActualAmount", object.getAlreadyActualAmount());
		properties.put("actualAmount", object.getActualAmount());
		properties.put("guaranteedAmount", object.getGuaranteedAmount());
//		properties.put("remark", object.getRemark());
		properties.put("remark", object.getRemark2());
		properties.put("busiState", object.getBusiState());
		
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{					
			properties.put("userUpdate", userUpdate.getTheName());
			properties.put("lastUpdateTimeStamp",MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));//操作日期
		}
	
		
		Sm_User userRecord = object.getUserRecord();
		if (userRecord != null)
		{					
			properties.put("userRecord", userRecord.getTheName());//审核人
			properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));//审核日期
		}		
		return properties;
	}
	
	@Override
	public Properties getDetail(Empj_PaymentGuarantee object)
	{
		if (object == null)
			return null;

		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		properties.put("eCode", object.geteCode());
		properties.put("revokeNo", object.getRevokeNo());
		properties.put("applyDate", object.getApplyDate());
		properties.put("busiState", object.getBusiState());
		if (object.getCompanyName() != null)
		{
			properties.put("companyName", object.getCompanyName());
		}
		if (object.getProjectName() != null)
		{
			properties.put("projectName", object.getProjectName());
		}
		if(object.getUserUpdate() != null)
		{
			
		}
		properties.put("guaranteeCompany", object.getGuaranteeCompany());
		
		properties.put("guaranteeNo", object.getGuaranteeNo());
		properties.put("guaranteeType", object.getGuaranteeType());
		properties.put("alreadyActualAmount", object.getAlreadyActualAmount());
		properties.put("actualAmount", object.getActualAmount());
		properties.put("guaranteedAmount", object.getGuaranteedAmount());
		properties.put("remark", object.getRemark2());
		
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{					
			properties.put("userUpdate", userUpdate.getTheName());
			properties.put("lastUpdateTimeStamp",MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));//操作日期
		}
	
		
		Sm_User userRecord = object.getUserRecord();
		if (userRecord != null)
		{					
			properties.put("userRecord", userRecord.getTheName());//审核人
			properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));//审核日期
		}

		
		return properties;
	}

}
