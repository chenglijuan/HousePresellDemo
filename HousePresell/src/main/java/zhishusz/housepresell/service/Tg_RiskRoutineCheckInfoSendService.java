package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckInfoForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckRatioConfigForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckBusiCodeSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckMonthSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckInfoDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckRatioConfigDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;
import zhishusz.housepresell.database.po.state.S_BaseParameter;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_CommonMessageType;
import zhishusz.housepresell.database.po.state.S_EntryState;
import zhishusz.housepresell.database.po.state.S_IsQualified;
import zhishusz.housepresell.database.po.state.S_IsReaderState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_RectificationState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_YesNoStr;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.RiskCheckMessagePushUtil;

/*
 * Service更新操作：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckInfoSendService
{
	@Autowired
	private Tg_RiskRoutineCheckInfoDao tg_RiskRoutineCheckInfoDao;
	@Autowired
	private Tg_RiskCheckBusiCodeSumDao tg_RiskCheckBusiCodeSumDao;
	@Autowired
	private Tg_RiskCheckMonthSumDao tg_RiskCheckMonthSumDao;
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigDao tg_RiskRoutineCheckRatioConfigDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	@Autowired
	private Sm_PushletService sm_PushletService;
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
	@Autowired
	private RiskCheckMessagePushUtil riskCheckMessagePushUtil;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskRoutineCheckInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Tg_RiskRoutineCheckInfoForm[] rishCheckList = model.getRishCheckList();

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSumObject = tg_RiskCheckBusiCodeSumDao.findById(model.getBusiCodeSummaryId());
		if(tg_RiskCheckBusiCodeSumObject == null)
		{
			return MyBackInfo.fail(properties, "该小类抽查不存在");
		}
		
		Sm_BaseParameter bigBusiParameter = sm_BaseParameterGetService.getParameter(S_BaseParameter.BusinessCode, tg_RiskCheckBusiCodeSumObject.getBigBusiType());
		Sm_BaseParameter smallBusiParameter = sm_BaseParameterGetService.getParameter(S_BaseParameter.BusinessCode, tg_RiskCheckBusiCodeSumObject.getSmallBusiType());
		
		//抽查配置
		Tg_RiskRoutineCheckRatioConfigForm tg_RiskRoutineCheckRatioConfigForm = new Tg_RiskRoutineCheckRatioConfigForm();
		tg_RiskRoutineCheckRatioConfigForm.setLargeBusinessValue(tg_RiskCheckBusiCodeSumObject.getBigBusiType());
		tg_RiskRoutineCheckRatioConfigForm.setSubBusinessValue(tg_RiskCheckBusiCodeSumObject.getSmallBusiType());
		tg_RiskRoutineCheckRatioConfigForm.setTheState(S_TheState.Normal);
		
		Tg_RiskRoutineCheckRatioConfig tg_RiskRoutineCheckRatioConfig = tg_RiskRoutineCheckRatioConfigDao.findOneByQuery_T(tg_RiskRoutineCheckRatioConfigDao.getQuery(tg_RiskRoutineCheckRatioConfigDao.getBasicHQL(), tg_RiskRoutineCheckRatioConfigForm));
		
		List<Sm_Permission_RoleUser> sm_permission_roleUserList = new ArrayList<Sm_Permission_RoleUser>();
		
		if(tg_RiskRoutineCheckRatioConfig.getRole() != null)
		{
			Sm_Permission_RoleUserForm roleUserForm = new Sm_Permission_RoleUserForm();
			roleUserForm.setTheState(S_TheState.Normal);
			roleUserForm.setSm_Permission_RoleId(tg_RiskRoutineCheckRatioConfig.getRole().getTableId());
			sm_permission_roleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), roleUserForm));
		}
		
		Integer pushCount = 0;
		Integer unqualifiedCount = 0;

		for(Tg_RiskRoutineCheckInfoForm tg_RiskRoutineCheckInfoForm : rishCheckList)
		{
			Tg_RiskRoutineCheckInfo riskCheckInfo = tg_RiskRoutineCheckInfoDao.findById(tg_RiskRoutineCheckInfoForm.getTableId());
			
			//已经录入过的
			if(S_EntryState.Completed.equals(riskCheckInfo.getEntryState()))
			{
				if(S_IsQualified.Yes.equals(tg_RiskRoutineCheckInfoForm.getCheckResult()))
				{
					//选择的是合格
					//原来是合格不做数量上的变更
					//原来是不合格要做数量上的变更
					if(S_IsQualified.No.equals(riskCheckInfo.getCheckResult()))
					{
						Tg_RiskCheckBusiCodeSum busiCodeSummary = riskCheckInfo.getBusiCodeSummary();
						busiCodeSummary.setQualifiedCount(busiCodeSummary.getQualifiedCount()+1);
						busiCodeSummary.setUnqualifiedCount(busiCodeSummary.getUnqualifiedCount()-1);
						
						Boolean flag1 = (busiCodeSummary.getSumCheckCount() == (busiCodeSummary.getQualifiedCount() + busiCodeSummary.getUnqualifiedCount()));
						
						if(busiCodeSummary.getUnqualifiedCount().equals(busiCodeSummary.getHandleCount()) && flag1)
						{
							busiCodeSummary.setRectificationState(S_RectificationState.Completed);
						}
						else
						{
							busiCodeSummary.setRectificationState(S_RectificationState.Doing);
						}
						
						Tg_RiskCheckMonthSum monthSummary = riskCheckInfo.getMonthSummary();
						monthSummary.setQualifiedCount(monthSummary.getQualifiedCount()+1);
						monthSummary.setUnqualifiedCount(monthSummary.getUnqualifiedCount()-1);
						
						Boolean flag2 = (monthSummary.getSumCheckCount() == (monthSummary.getQualifiedCount() + monthSummary.getUnqualifiedCount()));
						
						if(monthSummary.getUnqualifiedCount().equals(monthSummary.getHandleCount()) && flag2)
						{
							monthSummary.setRectificationState(S_RectificationState.Completed);
						}
						else
						{
							monthSummary.setRectificationState(S_RectificationState.Doing);
						}
						
						tg_RiskCheckBusiCodeSumDao.save(busiCodeSummary);
						tg_RiskCheckMonthSumDao.save(monthSummary);
						
						riskCheckInfo.setUnqualifiedReasons(null);
					}
				}
				if(S_IsQualified.No.equals(tg_RiskRoutineCheckInfoForm.getCheckResult()))
				{
					if(S_IsQualified.Yes.equals(riskCheckInfo.getCheckResult()))
					{
						Tg_RiskCheckBusiCodeSum busiCodeSummary = riskCheckInfo.getBusiCodeSummary();
						busiCodeSummary.setUnqualifiedCount(busiCodeSummary.getUnqualifiedCount()+1);
						busiCodeSummary.setQualifiedCount(busiCodeSummary.getQualifiedCount()-1);
						
						Boolean flag1 = (busiCodeSummary.getSumCheckCount() == (busiCodeSummary.getQualifiedCount() + busiCodeSummary.getUnqualifiedCount()));
						
						if(busiCodeSummary.getUnqualifiedCount().equals(busiCodeSummary.getHandleCount()) && flag1)
						{
							busiCodeSummary.setRectificationState(S_RectificationState.Completed);
						}
						else
						{
							busiCodeSummary.setRectificationState(S_RectificationState.Doing);
						}
						
						Tg_RiskCheckMonthSum monthSummary = riskCheckInfo.getMonthSummary();
						monthSummary.setUnqualifiedCount(monthSummary.getUnqualifiedCount()+1);
						monthSummary.setQualifiedCount(monthSummary.getQualifiedCount()-1);
						
						Boolean flag2 = (monthSummary.getSumCheckCount() == (monthSummary.getQualifiedCount() + monthSummary.getUnqualifiedCount()));
						
						if(monthSummary.getUnqualifiedCount().equals(monthSummary.getHandleCount()) && flag2)
						{
							monthSummary.setRectificationState(S_RectificationState.Completed);
						}
						else
						{
							monthSummary.setRectificationState(S_RectificationState.Doing);
						}
						
						tg_RiskCheckBusiCodeSumDao.save(busiCodeSummary);
						tg_RiskCheckMonthSumDao.save(monthSummary);
						
					}
					
					riskCheckInfo.setUnqualifiedReasons(tg_RiskRoutineCheckInfoForm.getUnqualifiedReasons());
				}
			}
			else//未录入过的
			{
				if(S_IsQualified.Yes.equals(tg_RiskRoutineCheckInfoForm.getCheckResult()))
				{
					Tg_RiskCheckBusiCodeSum busiCodeSummary = riskCheckInfo.getBusiCodeSummary();
					busiCodeSummary.setQualifiedCount(busiCodeSummary.getQualifiedCount()+1);
					
					if(busiCodeSummary.getSumCheckCount().equals(busiCodeSummary.getQualifiedCount()))
					{
						busiCodeSummary.setRectificationState(S_RectificationState.Completed);
					}
					else
					{
						busiCodeSummary.setRectificationState(S_RectificationState.Doing);
					}
					
					Tg_RiskCheckMonthSum monthSummary = riskCheckInfo.getMonthSummary();
					monthSummary.setQualifiedCount(monthSummary.getQualifiedCount()+1);
					
					if(monthSummary.getSumCheckCount().equals(monthSummary.getQualifiedCount()))
					{
						monthSummary.setRectificationState(S_RectificationState.Completed);
					}
					else
					{
						monthSummary.setRectificationState(S_RectificationState.Doing);
					}
					
					tg_RiskCheckBusiCodeSumDao.save(busiCodeSummary);
					tg_RiskCheckMonthSumDao.save(monthSummary);
				}
				if(S_IsQualified.No.equals(tg_RiskRoutineCheckInfoForm.getCheckResult()))
				{
					Tg_RiskCheckBusiCodeSum busiCodeSummary = riskCheckInfo.getBusiCodeSummary();
					busiCodeSummary.setUnqualifiedCount(busiCodeSummary.getUnqualifiedCount()+1);
					
					Tg_RiskCheckMonthSum monthSummary = riskCheckInfo.getMonthSummary();
					monthSummary.setUnqualifiedCount(monthSummary.getUnqualifiedCount()+1);
					
					tg_RiskCheckBusiCodeSumDao.save(busiCodeSummary);
					tg_RiskCheckMonthSumDao.save(monthSummary);
					
					riskCheckInfo.setUnqualifiedReasons(tg_RiskRoutineCheckInfoForm.getUnqualifiedReasons());
				}
			}
			
			if(tg_RiskRoutineCheckInfoForm.getCheckResult() != null && tg_RiskRoutineCheckInfoForm.getCheckResult().length() > 0)
			{
				unqualifiedCount++;
				riskCheckInfo.setEntryState(S_EntryState.Completed);
			}
			
			riskCheckInfo.setCheckResult(tg_RiskRoutineCheckInfoForm.getCheckResult());
			riskCheckInfo.setIsChoosePush(tg_RiskRoutineCheckInfoForm.getIsChoosePush());
			
			tg_RiskRoutineCheckInfoDao.save(riskCheckInfo);
			
			if(S_IsQualified.No.equals(tg_RiskRoutineCheckInfoForm.getCheckResult()))
			{
				if(S_YesNoStr.No.equals(riskCheckInfo.getIsDoPush()) && S_YesNoStr.Yes.equals(riskCheckInfo.getIsChoosePush()))
				{
					pushCount++;
					riskCheckInfo.setIsDoPush(S_YesNoStr.Yes);
					tg_RiskRoutineCheckInfoDao.save(riskCheckInfo);
					
					Tg_RiskCheckBusiCodeSum busiCodeSummary = riskCheckInfo.getBusiCodeSummary();
					busiCodeSummary.setPushCount(busiCodeSummary.getPushCount()+1);
					
					Tg_RiskCheckMonthSum monthSummary = riskCheckInfo.getMonthSummary();
					monthSummary.setPushCount(monthSummary.getPushCount()+1);
					
					tg_RiskCheckBusiCodeSumDao.save(busiCodeSummary);
					tg_RiskCheckMonthSumDao.save(monthSummary);
					
					String theTitle = bigBusiParameter.getTheName()+"—"
										+smallBusiParameter.getTheName()+"，"
										+"抽查业务数量"+tg_RiskCheckBusiCodeSumObject.getSumCheckCount()+"，"
										+"合格数"+tg_RiskCheckBusiCodeSumObject.getQualifiedCount()+"，"
										+"不合格数"+tg_RiskCheckBusiCodeSumObject.getUnqualifiedCount()+"。";
					String theContent =	"单据号："+riskCheckInfo.geteCodeOfBill()
											+"不合格原因："+riskCheckInfo.getUnqualifiedReasons()+"。";
					
					riskCheckMessagePushUtil.pushMessage(sm_permission_roleUserList, theTitle, theContent, riskCheckInfo, model, S_BusiCode.busiCode_21020102);
				}
			}
		}
		
		if(unqualifiedCount.equals(rishCheckList.length))
		{
			tg_RiskCheckBusiCodeSumObject.setEntryState(S_EntryState.Completed);
			tg_RiskCheckBusiCodeSumDao.save(tg_RiskCheckBusiCodeSumObject);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
