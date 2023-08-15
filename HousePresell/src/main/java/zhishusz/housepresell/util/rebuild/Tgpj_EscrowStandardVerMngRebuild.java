package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_IsNeedBackup;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：版本管理-托管标准
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpj_EscrowStandardVerMngRebuild extends RebuilderBase<Tgpj_EscrowStandardVerMng>
{

	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

	@Override
	public Properties getSimpleInfo(Tgpj_EscrowStandardVerMng object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		properties.put("theName", object.getTheName());
		properties.put("theVersion", object.getTheVersion());
		String theType = object.getTheType();
		if (S_EscrowStandardType.StandardAmount.equals(theType))
		{
			Double amount = object.getAmount();
			if (amount == null)
			{
				amount = 0.0;
			}
			properties.put("theType", "托管金额");
//			properties.put("theContent", MyString.getInstance().parse(amount.intValue())+"（元/m²）");
			properties.put("theContent", MyDouble.pointTOThousandths(amount) +"（元/m²）");
		}
		else
		{
			Double percentage = object.getPercentage();
			if (percentage == null)
			{
				percentage = 0.0;
			}
			properties.put("theType", "托管比例");
			properties.put("theContent", "物价备案均价*"+MyString.getInstance().parse(percentage.intValue())+"%");
		}

		properties.put("beginExpirationDate", MyDatetime.getInstance().dateToSimpleString(object.getBeginExpirationDate()));
		properties.put("endExpirationDate", MyDatetime.getInstance().dateToSimpleString(object.getEndExpirationDate()));
		properties.put("hasEnable", object.getHasEnable());

		return properties;
	}

	@Override
	public Properties getDetail(Tgpj_EscrowStandardVerMng object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		properties.put("eCode", object.geteCode());
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate == null)
		{
			userUpdate =  object.getUserStart();
		}
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
		Long operationTimeStamp = object.getLastUpdateTimeStamp();
		if (operationTimeStamp == null || operationTimeStamp < 1)
		{
			operationTimeStamp = object.getCreateTimeStamp();
		}

		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp", MyDatetime.getInstance().dateToString2(operationTimeStamp));
		properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
		properties.put("theName", object.getTheName());
		properties.put("theVersion", object.getTheVersion());
		String theType = object.getTheType();
		properties.put("theType", theType);
		if (S_EscrowStandardType.StandardAmount.equals(theType))
		{
			Double amount = object.getAmount();
			if (amount == null)
			{
				amount = 0.0;
			}
			properties.put("amount", MyString.getInstance().parse(object.getAmount()));
			properties.put("percentage", "0");
//			properties.put("theContent", MyString.getInstance().parse(amount.intValue())+"（元/m²）");
			properties.put("theContent", MyDouble.pointTOThousandths(amount) +"（元/m²）");
		}
		else
		{
			Double percentage = object.getPercentage();
			if (percentage == null)
			{
				percentage = 0.0;
			}
			properties.put("percentage", MyString.getInstance().parse(object.getPercentage()));
			properties.put("amount", "0");
			properties.put("theContent", "物价备案均价*"+MyString.getInstance().parse(percentage.intValue())+"%");
		}
		properties.put("extendParameter1", object.getExtendParameter1());
		properties.put("extendParameter2", object.getExtendParameter2());
		properties.put("beginExpirationDate", MyDatetime.getInstance().dateToSimpleString(object.getBeginExpirationDate()));
		properties.put("endExpirationDate", MyDatetime.getInstance().dateToSimpleString(object.getEndExpirationDate()));
		Boolean hasEnable = object.getHasEnable();
		if (hasEnable)
		{
			properties.put("hasEnable", "0");
		}
		else
		{
			properties.put("hasEnable", "1");
		}
		
		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties getDetailForApprovalProcess(Tgpj_EscrowStandardVerMng object)
	{
		if (object == null) return null;
		Properties properties = getDetail(object);
		//此处不需要从OSS上拿取审批流中字段，备注：审批流-变更字段.xlsx文件中托管标准版本管理无可变更字段

//		//查待提交的申请单
//		Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
//		sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
//		sm_ApprovalProcess_AFForm.setBusiState("待提交");
//		sm_ApprovalProcess_AFForm.setSourceId(object.getTableId());
//		sm_ApprovalProcess_AFForm.setBusiCode("06010101");
//		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//
//		if(sm_ApprovalProcess_AF == null)
//		{
//			sm_ApprovalProcess_AFForm.setBusiState("审核中");
//			sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//
//			if(sm_ApprovalProcess_AF == null)
//			{
//				return properties;
//			}
//		}
//
//		Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
//		Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = sm_ApprovalProcess_AF.getConfiguration();
//		Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao.findById(currentNode);
//		Boolean isNeedBackup = null;
//		if(sm_approvalProcess_workflow.getNextWorkFlow() == null)
//		{
//			if(S_IsNeedBackup.Yes.equals(sm_approvalProcess_cfg.getIsNeedBackup()))
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

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpj_EscrowStandardVerMng> tgpj_EscrowStandardVerMngList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpj_EscrowStandardVerMngList != null)
		{
			for(Tgpj_EscrowStandardVerMng object:tgpj_EscrowStandardVerMngList)
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
				properties.put("theVersion", object.getTheVersion());
				properties.put("theType", object.getTheType());
				properties.put("theContent", object.getTheContent());
				properties.put("amount", object.getAmount());
				properties.put("percentage", object.getPercentage());
				properties.put("extendParameter1", object.getExtendParameter1());
				properties.put("extendParameter2", object.getExtendParameter2());
				properties.put("beginExpirationDate", object.getBeginExpirationDate());
				properties.put("endExpirationDate", object.getEndExpirationDate());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	@Override
	public List<Properties> executeForSelectList(List<Tgpj_EscrowStandardVerMng> tgpj_EscrowStandardVerMngList) {
		List<Properties> list = new ArrayList<Properties>();
		if(tgpj_EscrowStandardVerMngList != null)
		{
			for(Tgpj_EscrowStandardVerMng object:tgpj_EscrowStandardVerMngList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				if(S_EscrowStandardType.StandardAmount.equals(object.getTheType()))
				{
					properties.put("theName", MyDouble.doubleTrans(object.getAmount())+"元");
				}
				else if(S_EscrowStandardType.StandardPercentage.equals(object.getTheType()))
				{
					properties.put("theName", MyDouble.doubleTrans(object.getPercentage())+"%");
				}

				list.add(properties);
			}
		}
		return list;
	}
}
