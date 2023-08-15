package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckInfoForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckRatioConfigForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckBusiCodeSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckMonthSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckInfoDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckRatioConfigDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;
import zhishusz.housepresell.database.po.state.S_BaseParameter;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_YesNoStr;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.RiskCheckMessagePushUtil;

/*
 * Service更新操作：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckInfoBackService
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
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
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
		
		for(Tg_RiskRoutineCheckInfoForm tg_RiskRoutineCheckInfoForm : rishCheckList)
		{
			Tg_RiskRoutineCheckInfo riskCheckInfo = tg_RiskRoutineCheckInfoDao.findById(tg_RiskRoutineCheckInfoForm.getTableId());
			
			if(S_YesNoStr.Yes.equals(riskCheckInfo.getIsModify()))
			{
				riskCheckInfo.setIsModify(S_YesNoStr.No);
				
				Tg_RiskCheckBusiCodeSum busiCodeSummary = riskCheckInfo.getBusiCodeSummary();
				busiCodeSummary.setFeedbackCount(busiCodeSummary.getFeedbackCount()-1);
				busiCodeSummary.setPushCount(busiCodeSummary.getPushCount()+1);
				
				Tg_RiskCheckMonthSum monthSummary = riskCheckInfo.getMonthSummary();
				monthSummary.setFeedbackCount(monthSummary.getFeedbackCount()-1);
				monthSummary.setPushCount(monthSummary.getPushCount()+1);
				
				tg_RiskRoutineCheckInfoDao.save(riskCheckInfo);
				tg_RiskCheckBusiCodeSumDao.save(busiCodeSummary);
				tg_RiskCheckMonthSumDao.save(monthSummary);
				
				String theTitle = bigBusiParameter.getTheName()+"—"
						+smallBusiParameter.getTheName()+"，"
						+"单据号："+riskCheckInfo.geteCodeOfBill()+"。";
				String theContent = "整改不通过";

				riskCheckMessagePushUtil.pushMessage(sm_permission_roleUserList, theTitle, theContent, riskCheckInfo, model, S_BusiCode.busiCode_21020105);
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
