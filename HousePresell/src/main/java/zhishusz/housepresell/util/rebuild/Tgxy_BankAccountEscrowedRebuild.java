package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：托管账户
 * Company：ZhiShuSZ
 */
@Service
public class Tgxy_BankAccountEscrowedRebuild extends RebuilderBase<Tgxy_BankAccountEscrowed>
{
	@Autowired
	private Tgxy_BankAccountEscrowedDao  tgxy_BankAccountEscrowedDao;
	
	@Override
	public Properties getSimpleInfo(Tgxy_BankAccountEscrowed object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
		properties.put("theAccount", object.getTheAccount());
		properties.put("currentBalance", object.getCurrentBalance());
		properties.put("largeRatio", object.getLargeRatio());
		properties.put("totalFundsRatio", object.getTotalFundsRatio());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("shortNameOfBank", object.getShortNameOfBank());

		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_BankAccountEscrowed object)
	{
		MyDatetime myDateTime = MyDatetime.getInstance();
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		// properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
//		properties.put("userStart", object.getUserStart());
		if(object.getUserStart()!=null){
			properties.put("userStartId", object.getUserStart().getTableId());
			properties.put("userStartName", object.getUserStart().getTheName());
		}
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("createTimeStampString", myDateTime.dateToSimpleString(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
		properties.put("userRecord", object.getUserRecord());
		if(object.getUserRecord()!=null){
			properties.put("userRecordId", object.getUserRecord().getTableId());
		}
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
//		properties.put("company", object.getCompany());
		if (null != object.getCompany() && null != object.getCompany().getTableId())
		{
			properties.put("companyId", object.getCompany().getTableId());
			properties.put("companyName", object.getCompany().getTheName());
		}
		else
		{
			properties.put("companyId", null);
		}
		// properties.put("project", object.getProject());
		// properties.put("projectId", object.getProject().getTableId());
//		properties.put("bank", object.getBank());
		if (null != object.getBank() && null != object.getBank().getTableId())
		{
			properties.put("bankId", object.getBank().getTableId());
			properties.put("bankName", object.getBank().getTheName());
			properties.put("bankShortName", object.getBank().getShortName());
		}
		else
		{
			properties.put("bankId", null);
		}
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("shortNameOfBank", object.getShortNameOfBank());
//		properties.put("bankBranch", object.getBankBranch());
		if (null != object.getBankBranch() && null != object.getBankBranch().getTableId())
		{
			properties.put("bankBranchId", object.getBankBranch().getTableId());
			properties.put("bankBranchName", object.getBankBranch().getTheName());
			properties.put("bankBranchShortName", object.getBankBranch().getShortName());
		}
		else
		{
			properties.put("bankBranchId", null);
		}
		properties.put("theName", object.getTheName());
		properties.put("theAccount", object.getTheAccount());
		properties.put("remark", object.getRemark());
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("updatedStamp", object.getUpdatedStamp());
		properties.put("isUsing", object.getIsUsing());
		properties.put("hasClosing", null==object.getHasClosing()?0:object.getHasClosing());
		MyDatetime myDatetime = MyDatetime.getInstance();
		properties.put("closingTime",myDatetime.dateToSimpleString(object.getClosingTime()));
		properties.put("closingPerson",null==object.getClosingPerson()?"":object.getClosingPerson().getTheName());
		properties.put("transferOutAmount",object.getTransferOutAmount());
		properties.put("transferInAmount",object.getTransferInAmount());
		
		if(1 == object.getHasClosing()){
			Tgxy_BankAccountEscrowedForm model1 = new Tgxy_BankAccountEscrowedForm();
			String eCode=object.getToECode();
			model1.seteCode(eCode);
			model1.setTheState(S_TheState.Normal);
			List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), model1));
			if(!tgxy_BankAccountEscrowedList.isEmpty()){
				properties.put("transferInAccount",tgxy_BankAccountEscrowedList.get(0).getTheAccount());
				properties.put("transferInBank",tgxy_BankAccountEscrowedList.get(0).getShortNameOfBank());
			}else{
				properties.put("transferInAccount","");
				properties.put("transferInBank","");
			}
			
		}else{
			properties.put("transferInAccount","");
			properties.put("transferInBank","");
		}
		
		
//		properties.put("income", object.getIncome());
//		properties.put("payout", object.getPayout());
//		properties.put("certOfDeposit", object.getCertOfDeposit());
//		properties.put("structuredDeposit", object.getStructuredDeposit());
//		properties.put("breakEvenFinancial", object.getBreakEvenFinancial());
//		properties.put("currentBalance", object.getCurrentBalance());
//		properties.put("largeRatio", object.getLargeRatio());
//		properties.put("largeAndCurrentRatio", object.getLargeAndCurrentRatio());
//		properties.put("financialRatio", object.getFinancialRatio());
//		properties.put("totalFundsRatio", object.getTotalFundsRatio());
//		properties.put("userUpdate", object.getUserUpdate());
//		properties.put("userUpdate", object.getCanPayAmount());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList)
	{
		List<Properties> list = new ArrayList<Properties>();
		MyDatetime myDatetime = MyDatetime.getInstance();
		if (tgxy_BankAccountEscrowedList != null)
		{
			for (Tgxy_BankAccountEscrowed object : tgxy_BankAccountEscrowedList)
			{
				Properties properties = new MyProperties();

				// properties.put("theState", object.getTheState());
				properties.put("tableId", object.getTableId());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
//				properties.put("userStart", object.getUserStart());
				if(object.getUserStart()!=null){
					properties.put("userStartId", object.getUserStart().getTableId());
					properties.put("userStartName", object.getUserStart().getTheName());
				}
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("createTimeStampString", myDatetime.dateToSimpleString(object.getCreateTimeStamp()));
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				Sm_User userRecord = object.getUserRecord();
				if(userRecord!=null){
					properties.put("userRecordId", object.getUserRecord().getTableId());
				}
//				properties.put("userRecord", );

				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				// properties.put("company", object.getCompany());
				// properties.put("companyId",
				// object.getCompany().getTableId());
				// properties.put("project", object.getProject());
				// properties.put("projectId",
				// object.getProject().getTableId());
//				properties.put("bank", object.getBank());
				if(object.getBank()!=null){
					properties.put("bankId", object.getBank().getTableId());
				}
				properties.put("theNameOfBank", object.getTheNameOfBank());
				properties.put("shortNameOfBank", object.getShortNameOfBank());
//				properties.put("bankBranch", object.getBankBranch());
				if (null != object.getBankBranch() && null != object.getBankBranch().getTableId())
				{
					properties.put("bankBranchId", object.getBankBranch().getTableId());
					properties.put("bankBranchName", object.getBankBranch().getTheName());
					properties.put("bankBranchShortName", object.getBankBranch().getShortName());
					properties.put("theNameOfBankBranch", object.getBankBranch().getTheName());
				}
				else
				{
					properties.put("bankBranchId", null);
					properties.put("theNameOfBankBranch", "");
				}
				properties.put("theName", object.getTheName());
				properties.put("theAccount", object.getTheAccount());
				// properties.put("remark", object.getRemark());
				// properties.put("contactPerson", object.getContactPerson());
				// properties.put("contactPhone", object.getContactPhone());
				properties.put("updatedStamp", object.getUpdatedStamp());
				properties.put("isUsing", object.getIsUsing());
				// properties.put("income", object.getIncome());
				// properties.put("payout", object.getPayout());
				// properties.put("certOfDeposit", object.getCertOfDeposit());
				// properties.put("structuredDeposit",
				// object.getStructuredDeposit());
				// properties.put("breakEvenFinancial",
				// object.getBreakEvenFinancial());
				//活期余额
				properties.put("currentBalance", null==object.getCurrentBalance()?0.00:object.getCurrentBalance());
				//托管可拨付金额
				properties.put("canPayAmount", null==object.getCanPayAmount()?0.00:object.getCanPayAmount());
				// properties.put("largeRatio", object.getLargeRatio());
				// properties.put("largeAndCurrentRatio",
				// object.getLargeAndCurrentRatio());
				// properties.put("financialRatio", object.getFinancialRatio());
				// properties.put("totalFundsRatio",
				// object.getTotalFundsRatio());
				
				properties.put("theAccountAndBankName", object.getTheAccount() + " : " + object.getShortNameOfBank());
				properties.put("hasClosing",null==object.getHasClosing()?0:object.getHasClosing());
				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List executeForSelectList(List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgxy_BankAccountEscrowedList != null)
		{
			for(Tgxy_BankAccountEscrowed object:tgxy_BankAccountEscrowedList)
			{
				Properties properties = new MyProperties();

				properties.put("theName", object.getTheAccount());
				properties.put("tableId", object.getTableId());
				Integer hasClosing =object.getHasClosing()==null?0:object.getHasClosing();
				if(hasClosing.equals(0)){
					list.add(properties);
				}
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List executeForViewSelectList(List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgxy_BankAccountEscrowedList != null)
		{
			for(Tgxy_BankAccountEscrowed object:tgxy_BankAccountEscrowedList)
			{
				Properties properties = new MyProperties();

				properties.put("theName", object.getShortNameOfBank());
				properties.put("tableId", object.getTheAccount());

				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List loadingListExecute(List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgxy_BankAccountEscrowedList != null)
		{
			for(Tgxy_BankAccountEscrowed object:tgxy_BankAccountEscrowedList)
			{
				Properties properties = new MyProperties();

				properties.put("theName", object.getTheName());
				properties.put("theAccount", object.getTheAccount());
				properties.put("toTableId",object.getTableId());
				properties.put("toECode",object.geteCode());
				list.add(properties);
			}
		}
		return list;
	}
}
