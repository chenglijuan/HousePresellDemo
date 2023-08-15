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
import zhishusz.housepresell.database.po.state.S_EntryState;
import zhishusz.housepresell.database.po.state.S_IsQualified;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_RectificationState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckInfoUpdateService
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
		
		Integer count = 0;//录入次数记录
		
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
					
					if(monthSummary.getSumCheckCount().equals(monthSummary.getQualifiedCount()))//合格数和总数一样
					{
						monthSummary.setRectificationState(S_RectificationState.Completed);
					}
					//合格数和总数不一样但是已确认数和不合格数一样并且合格数加不合格数等于总数
					else if(monthSummary.getUnqualifiedCount().equals(monthSummary.getHandleCount()) 
							&& 
							((monthSummary.getUnqualifiedCount() + monthSummary.getQualifiedCount()) == monthSummary.getSumCheckCount()))
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
				count++;
				riskCheckInfo.setEntryState(S_EntryState.Completed);
			}
			
			riskCheckInfo.setCheckResult(tg_RiskRoutineCheckInfoForm.getCheckResult());
			riskCheckInfo.setIsChoosePush(tg_RiskRoutineCheckInfoForm.getIsChoosePush());
			
			tg_RiskRoutineCheckInfoDao.save(riskCheckInfo);
		}
		
		//表示所有该小类下的抽查全部录入完毕
		if(count.equals(rishCheckList.length))
		{
			tg_RiskCheckBusiCodeSumObject.setEntryState(S_EntryState.Completed);
			tg_RiskCheckBusiCodeSumDao.save(tg_RiskCheckBusiCodeSumObject);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
