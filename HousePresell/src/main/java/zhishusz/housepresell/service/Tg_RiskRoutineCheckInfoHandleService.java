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
import zhishusz.housepresell.database.po.state.S_RectificationState;
import zhishusz.housepresell.database.po.state.S_YesNoStr;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service风控抽查处理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckInfoHandleService
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
			
			if(!S_YesNoStr.Yes.equals(riskCheckInfo.getIsModify()))
			{
				return MyBackInfo.fail(properties, "单据号:"+riskCheckInfo.geteCodeOfBill()+"还未反馈不可确认");
			}
			
			if(S_YesNoStr.No.equals(riskCheckInfo.getIsHandle()))
			{
				riskCheckInfo.setIsHandle(S_YesNoStr.Yes);
				riskCheckInfo.setRectificationState(S_RectificationState.Completed);
				riskCheckInfo.setRecordTimeStamp(System.currentTimeMillis());

				Tg_RiskCheckBusiCodeSum busiCodeSummary = riskCheckInfo.getBusiCodeSummary();
				busiCodeSummary.setHandleCount(busiCodeSummary.getHandleCount()+1);
				busiCodeSummary.setFeedbackCount(busiCodeSummary.getFeedbackCount()-1);
				
				Boolean flag1 = (busiCodeSummary.getSumCheckCount() == (busiCodeSummary.getQualifiedCount() + busiCodeSummary.getUnqualifiedCount()));
				
				if(busiCodeSummary.getHandleCount().equals(busiCodeSummary.getUnqualifiedCount()) && flag1)
				{
					busiCodeSummary.setRectificationState(S_RectificationState.Completed);
				}
				else
				{
					busiCodeSummary.setRectificationState(S_RectificationState.Doing);
				}
				
				Tg_RiskCheckMonthSum monthSummary = riskCheckInfo.getMonthSummary();
				monthSummary.setHandleCount(monthSummary.getHandleCount()+1);
				monthSummary.setFeedbackCount(monthSummary.getFeedbackCount()-1);
				
				Boolean flag2 = (monthSummary.getSumCheckCount() == (monthSummary.getQualifiedCount() + monthSummary.getUnqualifiedCount()));
				
				if(monthSummary.getHandleCount().equals(monthSummary.getUnqualifiedCount()) && flag2)
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
			
			riskCheckInfo.setForensicConfirmation(tg_RiskRoutineCheckInfoForm.getForensicConfirmation());
			tg_RiskRoutineCheckInfoDao.save(riskCheckInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
