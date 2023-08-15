package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Emmp_ComAccount;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：机构-财务账号信息
 * Company：ZhiShuSZ
 * */
@Service
public class Emmp_ComAccountRebuild extends RebuilderBase<Emmp_ComAccount>
{
	@Override
	public Properties getSimpleInfo(Emmp_ComAccount object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Emmp_ComAccount object)
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
		properties.put("officeAddress", object.getOfficeAddress());
		properties.put("officePhone", object.getOfficePhone());
		properties.put("bankBranch", object.getBankBranch());
		properties.put("bankBranchId", object.getBankBranch().getTableId());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("bankAccount", object.getBankAccount());
		properties.put("theNameOfBankAccount", object.getTheNameOfBankAccount());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Emmp_ComAccount> emmp_ComAccountList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(emmp_ComAccountList != null)
		{
			for(Emmp_ComAccount object:emmp_ComAccountList)
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
				properties.put("officeAddress", object.getOfficeAddress());
				properties.put("officePhone", object.getOfficePhone());
				properties.put("bankBranch", object.getBankBranch());
				properties.put("bankBranchId", object.getBankBranch().getTableId());
				properties.put("theNameOfBank", object.getTheNameOfBank());
				properties.put("bankAccount", object.getBankAccount());
				properties.put("theNameOfBankAccount", object.getTheNameOfBankAccount());
				
				list.add(properties);
			}
		}
		return list;
	}
}
