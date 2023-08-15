package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckInfoForm;
import zhishusz.housepresell.database.dao.Tg_RiskCheckBusiCodeSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckMonthSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckInfoDao;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_YesNoStr;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckInfoRetractService
{
	@Autowired
	private Tg_RiskRoutineCheckInfoDao tg_RiskRoutineCheckInfoDao;
	@Autowired
	private Tg_RiskCheckBusiCodeSumDao tg_RiskCheckBusiCodeSumDao;
	@Autowired
	private Tg_RiskCheckMonthSumDao tg_RiskCheckMonthSumDao;
	
	public Properties execute(Tg_RiskRoutineCheckInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Tg_RiskRoutineCheckInfoForm[] rishCheckList = model.getRishCheckList();

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSumObject = tg_RiskCheckBusiCodeSumDao.findById(model.getBusiCodeSummaryId());
		if(tg_RiskCheckBusiCodeSumObject == null)
		{
			return MyBackInfo.fail(properties, "该小类抽查不存在");
		}
		
		for(Tg_RiskRoutineCheckInfoForm tg_RiskRoutineCheckInfoForm : rishCheckList)
		{
			Tg_RiskRoutineCheckInfo riskCheckInfo = tg_RiskRoutineCheckInfoDao.findById(tg_RiskRoutineCheckInfoForm.getTableId());
			
			//如果已反馈或者已处理了不可撤回
			if(S_YesNoStr.Yes.equals(riskCheckInfo.getIsModify()) || S_YesNoStr.Yes.equals(riskCheckInfo.getIsHandle()))
			{
				return MyBackInfo.fail(properties, "单据号:"+riskCheckInfo.geteCodeOfBill()+"已反馈或者已处理不可撤回");
			}
			
			riskCheckInfo.setIsDoPush(S_YesNoStr.No);
			
			Tg_RiskCheckBusiCodeSum busiCodeSummary = riskCheckInfo.getBusiCodeSummary();
			busiCodeSummary.setPushCount(busiCodeSummary.getPushCount()-1);
			
			Tg_RiskCheckMonthSum monthSummary = riskCheckInfo.getMonthSummary();
			monthSummary.setPushCount(monthSummary.getPushCount()-1);
			
			tg_RiskRoutineCheckInfoDao.save(riskCheckInfo);
			tg_RiskCheckBusiCodeSumDao.save(busiCodeSummary);
			tg_RiskCheckMonthSumDao.save(monthSummary);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
