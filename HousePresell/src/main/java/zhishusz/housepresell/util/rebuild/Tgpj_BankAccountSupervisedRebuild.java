package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：监管账户
 * Company：ZhiShuSZ
 */
@Service
public class Tgpj_BankAccountSupervisedRebuild extends RebuilderBase<Tgpj_BankAccountSupervised>
{
	@Override
	public Properties getSimpleInfo(Tgpj_BankAccountSupervised object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		return properties;
	}

	@Override
	public Properties getDetail(Tgpj_BankAccountSupervised object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		Sm_User userStart = object.getUserStart();
		if (userStart != null)
		{
			properties.put("userStartName", object.getUserStart().getTheName());
			properties.put("userStartId", object.getUserStart().getTableId());
		}

		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("lastUpdateTimeStampString",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
		Sm_User userRecord = object.getUserRecord();
		if (userRecord != null)
		{
			properties.put("userRecordId", userRecord.getTableId());
			properties.put("userRecordName", userRecord.getTheName());
		}

		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		Emmp_CompanyInfo developCompany = object.getDevelopCompany();
		if (developCompany != null)
		{
			properties.put("developCompanyId", developCompany.getTableId());
			properties.put("developCompanyName", developCompany.getTheName());
		}
		properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
		// properties.put("bank", object.getBank());
		// properties.put("bankId", object.getBank().getTableId());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("shortNameOfBank", object.getShortNameOfBank());
		Emmp_BankBranch bankBranch = object.getBankBranch();
		if (bankBranch != null)
		{
			properties.put("bankBranchId", bankBranch.getTableId());
			properties.put("bankBranchName", bankBranch.getTheName());
			properties.put("bankBranchShortName", bankBranch.getShortName());
		}
		properties.put("theName", object.getTheName());
		properties.put("theAccount", object.getTheAccount());
		properties.put("remark", object.getRemark());
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("isUsing", object.getIsUsing());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpj_BankAccountSupervised> tgpj_BankAccountSupervisedList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgpj_BankAccountSupervisedList != null)
		{
			for (Tgpj_BankAccountSupervised object : tgpj_BankAccountSupervisedList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				// properties.put("userStart", object.getUserStart());
				if (object.getUserStart() != null)
				{
					properties.put("userStartId", object.getUserStart().getTableId());
					properties.put("userStartName", object.getUserStart().getTheName());
				}
				// properties.put("createTimeStamp",
				// object.getCreateTimeStamp());
				// properties.put("lastUpdateTimeStamp",
				// object.getLastUpdateTimeStamp());
				// properties.put("userRecord", object.getUserRecord());
				// if(object.getUserRecord()!=null){
				// properties.put("userRecordId",
				// object.getUserRecord().getTableId());
				// }
				// properties.put("recordTimeStamp",
				// object.getRecordTimeStamp());
				// properties.put("developCompany", object.getDevelopCompany());
				if (object.getDevelopCompany() != null)
				{
					properties.put("developCompanyId", object.getDevelopCompany().getTableId());
					properties.put("developCompanyName", object.getDevelopCompany().getTheName());
				}
				properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
				Emmp_BankInfo bank = object.getBank();
				if (bank != null)
				{
					properties.put("bankId", bank.getTableId());
					properties.put("bankName", bank.getTheName());
				}

				properties.put("theNameOfBank", object.getTheNameOfBank());
				properties.put("shortNameOfBank", object.getShortNameOfBank());
				Emmp_BankBranch bankBranch = object.getBankBranch();
				if (bankBranch != null)
				{
					properties.put("bankBranchId", bankBranch.getTableId());
					properties.put("bankBranchName", bankBranch.getTheName());
					properties.put("bankBranchShortName", bankBranch.getShortName());
				}

				properties.put("theName", object.getTheName());
				properties.put("theAccount", object.getTheAccount());
				// properties.put("remark", object.getRemark());
				// properties.put("contactPerson", object.getContactPerson());
				// properties.put("contactPhone", object.getContactPhone());
				properties.put("isUsing", object.getIsUsing());

				list.add(properties);
			}
		}
		return list;
	}

	@Override
	public List<Properties> executeForSelectList(List<Tgpj_BankAccountSupervised> tgpj_bankAccountSuperviseds)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgpj_bankAccountSuperviseds != null)
		{
			for (Tgpj_BankAccountSupervised object : tgpj_bankAccountSuperviseds)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());
				list.add(properties);
			}
		}
		return list;
	}

	public List<Properties> executeForSelectList2(List<Tgpj_BankAccountSupervised> tgpj_bankAccountSuperviseds)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgpj_bankAccountSuperviseds != null)
		{
			for (Tgpj_BankAccountSupervised object : tgpj_bankAccountSuperviseds)
			{
				Properties properties = new MyProperties();

				String companyName = ":";
//				if (null == object.getBankBranch() || object.getBankBranch().getTheName().length() < 1)
//				{
//					companyName = "";
//					properties.put("theNameOfBank", companyName);
//				}
//				else
//				{
//					companyName += object.getBankBranch().getTheName();
//					
//					properties.put("theNameOfBank", object.getBankBranch().getTheName());
//				}
				
				properties.put("theNameOfBank", object.getTheNameOfBank());

//				properties.put("theBankAccount", object.getTheAccount() + companyName);
				properties.put("theBankAccount", object.getTheAccount());
				properties.put("theAccount", object.getTheAccount());
				
				list.add(properties);
			}
		}
		return list;
	}
}
