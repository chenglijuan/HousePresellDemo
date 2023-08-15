package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_DepositState;
import zhishusz.housepresell.database.po.state.S_IsNeedBackup;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import com.google.gson.Gson;

/*
 * Rebuilder：存单管理
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_DepositManagementRebuild extends RebuilderBase<Tg_DepositManagement>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
	@Autowired
	private Gson gson;

	@Override
	public Properties getSimpleInfo(Tg_DepositManagement object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("depositState", object.getDepositState());
		properties.put("depositProperty", object.getDepositProperty());
		if (object.getBank() != null)
		{
			properties.put("bankId", object.getBank().getTableId());
			properties.put("bankName", object.getBank().getTheName());
		}
		if (object.getBankOfDeposit() != null)
		{
			properties.put("bankOfDepositId", object.getBankOfDeposit().getTableId());
			properties.put("bankOfDepositName", object.getBankOfDeposit().getTheName());
		}
		if (object.getEscrowAcount() != null)
		{
			properties.put("escrowAcountId", object.getEscrowAcount().getTableId());
			properties.put("escrowAcountName", object.getEscrowAcount().getTheName());
			properties.put("escrowAcountAcount", object.getEscrowAcount().getTheAccount());
		}
		properties.put("principalAmount", object.getPrincipalAmount());	
		properties.put("startDateStr", myDatetime.dateToSimpleString(object.getStartDate()));	
		properties.put("stopDateStr", myDatetime.dateToSimpleString(object.getStopDate()));
		properties.put("storagePeriod", object.getStoragePeriod());	
		properties.put("storagePeriodCompany", object.getStoragePeriodCompany());
		properties.put("annualRate", object.getAnnualRate());
		properties.put("remarkIn", object.getRemarkIn());
		properties.put("remarkOut", object.getRemarkOut());
		properties.put("remark", object.getRemarkIn());
		
		properties.put("stateIn", null == object.getStateIn()?"0":object.getStateIn());
		properties.put("stateOut", null == object.getStateOut()?"0":object.getStateOut());

		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());

		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_DepositManagement object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
//		properties.put("userStart", object.getUserStart());
//		properties.put("userStartId", object.getUserStart().getTableId());
//		properties.put("createTimeStamp", object.getCreateTimeStamp());
		if (object.getUserUpdate() != null)
		{
			properties.put("userUpdateName", object.getUserUpdate().getTheName());
		}
		properties.put("lastUpdateTimeStampStr", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
//		properties.put("userRecord", object.getUserRecord());
//		properties.put("userRecordId", object.getUserRecord().getTableId());
//		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("depositState", object.getDepositState());
		properties.put("depositProperty", object.getDepositProperty());

		if (object.getBank() != null)
		{
			properties.put("bankId", object.getBank().getTableId());
			properties.put("bankName", object.getBank().getTheName());
		}
		if (object.getBankOfDeposit() != null)
		{
			properties.put("bankOfDepositId", object.getBankOfDeposit().getTableId());
			properties.put("bankOfDepositName", object.getBankOfDeposit().getTheName());
		}
		if (object.getEscrowAcount() != null)
		{
			properties.put("escrowAcountId", object.getEscrowAcount().getTableId());
			properties.put("escrowAcountName", object.getEscrowAcount().getTheName());
			properties.put("escrowAcountAcount", object.getEscrowAcount().getTheAccount());
		}

//		properties.put("escrowAcountShortName", object.getEscrowAcountShortName());
		properties.put("agent", object.getAgent());
		properties.put("principalAmount", object.getPrincipalAmount());
		properties.put("storagePeriod", object.getStoragePeriod());
		properties.put("storagePeriodCompany", object.getStoragePeriodCompany());
		properties.put("annualRate", object.getAnnualRate());
		properties.put("startDate", object.getStartDate());
		properties.put("stopDate", object.getStopDate());
		properties.put("startDateStr", myDatetime.dateToSimpleString(object.getStartDate()));	
		properties.put("stopDateStr", myDatetime.dateToSimpleString(object.getStopDate()));
		properties.put("openAccountCertificate", object.getOpenAccountCertificate());
		properties.put("expectedInterest", object.getExpectedInterest());
		properties.put("floatAnnualRate", object.getFloatAnnualRate());

		properties.put("extractDateStr", myDatetime.dateToSimpleString(object.getExtractDate()));
		properties.put("realInterest", object.getRealInterest());
		properties.put("realInterestRate", object.getRealInterestRate());

		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		
		properties.put("remarkIn", object.getRemarkIn());
		properties.put("remarkOut", object.getRemarkOut());
		properties.put("remark", object.getRemarkIn());
		
		properties.put("stateIn", null == object.getStateIn()?"0":object.getStateIn());
		properties.put("stateOut", null == object.getStateOut()?"0":object.getStateOut());
		
		return properties;
	}

	public Properties getDetailForApproval(Tg_DepositManagement object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		if (object.getUserUpdate() != null)
		{
			properties.put("userUpdateName", object.getUserUpdate().getTheName());
		}
		properties.put("lastUpdateTimeStampStr", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		properties.put("depositState", object.getDepositState());
		properties.put("depositProperty", object.getDepositProperty());

		if (object.getBank() != null)
		{
			properties.put("bankId", object.getBank().getTableId());
			properties.put("bankName", object.getBank().getTheName());
		}
		if (object.getBankOfDeposit() != null)
		{
			properties.put("bankOfDepositId", object.getBankOfDeposit().getTableId());
			properties.put("bankOfDepositName", object.getBankOfDeposit().getTheName());
		}
		if (object.getEscrowAcount() != null)
		{
			properties.put("escrowAcountId", object.getEscrowAcount().getTableId());
			properties.put("escrowAcountName", object.getEscrowAcount().getTheName());
			properties.put("escrowAcountAcount", object.getEscrowAcount().getTheAccount());
		}

		//		properties.put("escrowAcountShortName", object.getEscrowAcountShortName());
		properties.put("agent", object.getAgent());
		properties.put("principalAmount", object.getPrincipalAmount());
		properties.put("storagePeriod", object.getStoragePeriod());
		properties.put("storagePeriodCompany", object.getStoragePeriodCompany());
		properties.put("annualRate", object.getAnnualRate());
		properties.put("startDate", object.getStartDate());
		properties.put("stopDate", object.getStopDate());
		properties.put("startDateStr", myDatetime.dateToSimpleString(object.getStartDate()));
		properties.put("stopDateStr", myDatetime.dateToSimpleString(object.getStopDate()));
		properties.put("openAccountCertificate", object.getOpenAccountCertificate());
		properties.put("expectedInterest", object.getExpectedInterest());
		properties.put("floatAnnualRate", object.getFloatAnnualRate());

		properties.put("extractDateStr", myDatetime.dateToSimpleString(object.getExtractDate()));
		properties.put("realInterest", object.getRealInterest());
		properties.put("realInterestRate", object.getRealInterestRate());

		//计算规则
		properties.put("calculationRule", null == object.getCalculationRule()?"360":object.getCalculationRule());
		
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());

		properties.put("remarkIn", object.getRemarkIn());
		properties.put("remarkOut", object.getRemarkOut());
		
		properties.put("remark", object.getRemarkIn());
		
		properties.put("stateIn", null == object.getStateIn()?"0":object.getStateIn());
		properties.put("stateOut", null == object.getStateOut()?"0":object.getStateOut());
		
		// 如果不是 已备案的存单 就不必要去查OSS
		if (!S_BusiState.HaveRecord.equals(object.getBusiState()))
		{
			return properties;
		}

		//审核的申请单
		Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
		sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
		sm_ApprovalProcess_AFForm.setBusiState("待提交");
		sm_ApprovalProcess_AFForm.setSourceId(object.getTableId());
		sm_ApprovalProcess_AFForm.setBusiCode("210105");
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));

		if (sm_ApprovalProcess_AF == null)
		{
			sm_ApprovalProcess_AFForm.setBusiState("审核中");
			sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));

			if (sm_ApprovalProcess_AF == null)
			{
				return properties;
			}
		}

//		Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
//		Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao.findById(currentNode);
//		Boolean isNeedBackup = null;
//		if(sm_approvalProcess_workflow.getNextWorkFlow() == null)
//		{
//			if(S_IsNeedBackup.Yes.equals(sm_ApprovalProcess_AF.getIsNeedBackup()))
//			{
//				isNeedBackup = true;
//			}
//		}
//		else
//		{
//			isNeedBackup = false;
//		}
//
//		properties.put("isNeedBackup", isNeedBackup);//是否显示备案按钮

		String expectObj = sm_ApprovalProcess_AF.getExpectObjJson();
		if (expectObj != null && expectObj.length() > 0 )
		{
			Tg_DepositManagementForm tg_DepositManagementForm = gson.fromJson(expectObj, Tg_DepositManagementForm.class);

			properties.put("extractDateStr", tg_DepositManagementForm.getExtractDateStr());
			properties.put("realInterest", tg_DepositManagementForm.getRealInterest());
			properties.put("realInterestRate", tg_DepositManagementForm.getRealInterestRate());

		}

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_DepositManagement> tg_DepositManagementList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_DepositManagementList != null)
		{
			for(Tg_DepositManagement object:tg_DepositManagementList)
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
				properties.put("depositProperty", object.getDepositProperty());

				if (object.getBank() != null)
				{
					properties.put("bankId", object.getBank().getTableId());
					properties.put("bankName", object.getBank().getTheName());
				}
				if (object.getBankOfDeposit() != null)
				{
					properties.put("bankOfDepositId", object.getBankOfDeposit().getTableId());
					properties.put("bankOfDepositName", object.getBankOfDeposit().getTheName());
				}
				if (object.getEscrowAcount() != null)
				{
					properties.put("escrowAcountId", object.getEscrowAcount().getTableId());
					properties.put("escrowAcountName", object.getEscrowAcount().getTheName());
				}

				properties.put("escrowAcountShortName", object.getEscrowAcountShortName());
				properties.put("agent", object.getAgent());
				properties.put("principalAmount", object.getPrincipalAmount());
				properties.put("storagePeriod", object.getStoragePeriod());
				properties.put("storagePeriodCompany", object.getStoragePeriodCompany());
				properties.put("annualRate", object.getAnnualRate());
				properties.put("startDate", object.getStartDate());
				properties.put("stopDate", object.getStopDate());
				properties.put("openAccountCertificate", object.getOpenAccountCertificate());
				properties.put("expectedInterest", object.getExpectedInterest());
				properties.put("floatAnnualRate", object.getFloatAnnualRate());
				properties.put("extractDate", object.getExtractDate());
				properties.put("realInterest", object.getRealInterest());
				properties.put("realInterestRate", object.getRealInterestRate());
				
				properties.put("remarkIn", object.getRemarkIn());
				properties.put("remarkOut", object.getRemarkOut());
				properties.put("remark", object.getRemarkIn());
				
				properties.put("stateIn", null == object.getStateIn()?"0":object.getStateIn());
				properties.put("stateOut", null == object.getStateOut()?"0":object.getStateOut());

				properties.put("busiState", object.getBusiState());
				properties.put("approvalState", object.getApprovalState());
				
				list.add(properties);
			}
		}
		return list;
	}
}
