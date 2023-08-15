package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckInfoForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckBusiCodeSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckMonthSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckInfoDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.state.S_BaseParameter;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_YesNoStr;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.RiskCheckMessagePushUtil;

/*
 * Service保存抽查反馈
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckInfoSaveService
{
	@Autowired
	private Tg_RiskRoutineCheckInfoDao tg_RiskRoutineCheckInfoDao;
	@Autowired
	private Tg_RiskCheckBusiCodeSumDao tg_RiskCheckBusiCodeSumDao;
	@Autowired
	private Tg_RiskCheckMonthSumDao tg_RiskCheckMonthSumDao;
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
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
		
		Sm_Permission_RoleForm sm_Permission_RoleForm = new Sm_Permission_RoleForm();
		sm_Permission_RoleForm.setTheState(S_TheState.Normal);
		sm_Permission_RoleForm.setTheName("法务%");
		
		List<Sm_Permission_Role> sm_Permission_RoleList = sm_Permission_RoleDao.findByPage(sm_Permission_RoleDao.getQuery(sm_Permission_RoleDao.getLawWorks(), sm_Permission_RoleForm));
		
		List<Sm_Permission_RoleUser> sm_permission_roleUserList = new ArrayList<Sm_Permission_RoleUser>();
		for(Sm_Permission_Role sm_Permission_Role : sm_Permission_RoleList)
		{
			Sm_Permission_RoleUserForm roleUserForm = new Sm_Permission_RoleUserForm();
			roleUserForm.setTheState(S_TheState.Normal);
			roleUserForm.setSm_Permission_RoleId(sm_Permission_Role.getTableId());
			
			List<Sm_Permission_RoleUser> roleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), roleUserForm));
			if(!roleUserList.isEmpty())
			{
				sm_permission_roleUserList.addAll(roleUserList);
			}
		}
		
		for(Tg_RiskRoutineCheckInfoForm tg_RiskRoutineCheckInfoForm : rishCheckList)
		{
			Tg_RiskRoutineCheckInfo riskCheckInfo = tg_RiskRoutineCheckInfoDao.findById(tg_RiskRoutineCheckInfoForm.getTableId());
			
			if(S_YesNoStr.No.equals(riskCheckInfo.getIsModify()))
			{
				riskCheckInfo.setIsModify(S_YesNoStr.Yes);
				riskCheckInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
				riskCheckInfo.setRecordTimeStamp(System.currentTimeMillis());

				Tg_RiskCheckBusiCodeSum busiCodeSummary = riskCheckInfo.getBusiCodeSummary();
				busiCodeSummary.setFeedbackCount(busiCodeSummary.getFeedbackCount()+1);//反馈数量加一
				busiCodeSummary.setPushCount(busiCodeSummary.getPushCount()-1);
				
				Tg_RiskCheckMonthSum monthSummary = riskCheckInfo.getMonthSummary();
				monthSummary.setFeedbackCount(monthSummary.getFeedbackCount()+1);
				monthSummary.setPushCount(monthSummary.getPushCount()-1);
				
				tg_RiskCheckBusiCodeSumDao.save(busiCodeSummary);
				tg_RiskCheckMonthSumDao.save(monthSummary);
				
				String theTitle = bigBusiParameter.getTheName()+"—"
									+smallBusiParameter.getTheName()+"，整改反馈";
				String theContent = "单据号："+riskCheckInfo.geteCodeOfBill()
										+"不合格原因："+riskCheckInfo.getUnqualifiedReasons()
										+"反馈信息："+tg_RiskRoutineCheckInfoForm.getModifyFeedback()+"。";
				
				riskCheckMessagePushUtil.pushMessage(sm_permission_roleUserList, theTitle, theContent, riskCheckInfo, model, S_BusiCode.busiCode_21020103);
			}
			
			riskCheckInfo.setModifyFeedback(tg_RiskRoutineCheckInfoForm.getModifyFeedback());
			riskCheckInfo.setForensicConfirmation(tg_RiskRoutineCheckInfoForm.getForensicConfirmation());
			tg_RiskRoutineCheckInfoDao.save(riskCheckInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
