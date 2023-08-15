package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckRatioConfigForm;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckRatioConfigDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_RiskCheckBusiCodeSumRebuild extends RebuilderBase<Tg_RiskCheckBusiCodeSum>
{
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigDao tg_RiskRoutineCheckRatioConfigDao;
	
	@Override
	public Properties getSimpleInfo(Tg_RiskCheckBusiCodeSum object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		
		Sm_BaseParameter bigBusi = sm_BaseParameterGetService.getParameter("1", object.getBigBusiType());
		Sm_BaseParameter smallBusi = sm_BaseParameterGetService.getParameter("1", object.getSmallBusiType());
		if(bigBusi != null)
		{
			properties.put("bigBusiName", bigBusi.getTheName());//业务大类
			properties.put("bigBusiValue", bigBusi.getTheValue());
		}
		if(smallBusi != null)
		{
			
			
			if("存单存入管理".equals(smallBusi.getTheName()))
			{
				properties.put("smallBusiName", "存单管理");//业务小类
			}
			else
			{
				properties.put("smallBusiName", smallBusi.getTheName());//业务小类
			}
			
			properties.put("smallBusiValue", smallBusi.getTheValue());
		}
		
		properties.put("sumCheckCount", object.getSumCheckCount());//业务
		properties.put("qualifiedCount", object.getQualifiedCount());//合格
		properties.put("unqualifiedCount", object.getUnqualifiedCount());//不合格
		properties.put("pushCount", object.getPushCount());//推送整改
		properties.put("feedbackCount", object.getFeedbackCount());//整改反馈
		properties.put("handleCount", object.getHandleCount());//完成整改
		properties.put("entryState", object.getEntryState());//录入状态
		properties.put("rectificationState", object.getRectificationState());//录入状态
		properties.put("spotTimeStamp", MyDatetime.getInstance().dateToString(object.getSpotTimeStamp(), "yyyy-MM"));//抽查所在月
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_RiskCheckBusiCodeSum object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		Sm_BaseParameter bigBusi = sm_BaseParameterGetService.getParameter("1", object.getBigBusiType());
		Sm_BaseParameter smallBusi = sm_BaseParameterGetService.getParameter("1", object.getSmallBusiType());
		if(bigBusi != null)
		{
			properties.put("bigBusiName", bigBusi.getTheName());//业务大类
		}
		if(smallBusi != null)
		{
			properties.put("smallBusiName", smallBusi.getTheName());//业务小类
		}
		
		properties.put("sumCheckCount", object.getSumCheckCount());//业务
		properties.put("qualifiedCount", object.getQualifiedCount());//合格
		properties.put("unqualifiedCount", object.getUnqualifiedCount());//不合格
		properties.put("pushCount", object.getPushCount());//推送整改
		properties.put("feedbackCount", object.getFeedbackCount());//整改反馈
		properties.put("handleCount", object.getHandleCount());//完成整改
		properties.put("entryState", object.getEntryState());//录入状态
		
		//抽查配置
		Tg_RiskRoutineCheckRatioConfigForm tg_RiskRoutineCheckRatioConfigForm = new Tg_RiskRoutineCheckRatioConfigForm();
		tg_RiskRoutineCheckRatioConfigForm.setLargeBusinessValue(object.getBigBusiType());
		tg_RiskRoutineCheckRatioConfigForm.setSubBusinessValue(object.getSmallBusiType());
		tg_RiskRoutineCheckRatioConfigForm.setTheState(S_TheState.Normal);
		
		Tg_RiskRoutineCheckRatioConfig tg_RiskRoutineCheckRatioConfig = tg_RiskRoutineCheckRatioConfigDao.findOneByQuery_T(tg_RiskRoutineCheckRatioConfigDao.getQuery(tg_RiskRoutineCheckRatioConfigDao.getBasicHQL(), tg_RiskRoutineCheckRatioConfigForm));
		if(tg_RiskRoutineCheckRatioConfig != null && tg_RiskRoutineCheckRatioConfig.getRole() != null)
		{
			properties.put("roleName", tg_RiskRoutineCheckRatioConfig.getRole().getTheName());//发送角色
		}
		if(object.getUserStart() != null)
		{
			properties.put("checkUserName", object.getUserStart().getTheName());//抽查人
		}
		
		properties.put("spotTimeStamp", MyDatetime.getInstance().dateToString(object.getSpotTimeStamp(), "yyyy-MM"));//抽查所在月
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_RiskCheckBusiCodeSum> Tg_RiskCheckBusiCodeSumList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(Tg_RiskCheckBusiCodeSumList != null)
		{
			for(Tg_RiskCheckBusiCodeSum object:Tg_RiskCheckBusiCodeSumList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				
				list.add(properties);
			}
		}
		return list;
	}

	public List<Properties> executeRiskRoutineMonthSumList(List<Tg_RiskCheckBusiCodeSum> oldList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(oldList != null)
		{
			List<Tg_RiskCheckBusiCodeSum> oldTempList = new ArrayList<Tg_RiskCheckBusiCodeSum>();
			for(Tg_RiskCheckBusiCodeSum element:oldList)
			{
				Tg_RiskCheckBusiCodeSum tg_riskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
				tg_riskCheckBusiCodeSum.setBigBusiType(element.getBigBusiType());
				tg_riskCheckBusiCodeSum.setSumCheckCount(element.getSumCheckCount());
				tg_riskCheckBusiCodeSum.setQualifiedCount(element.getQualifiedCount());
				tg_riskCheckBusiCodeSum.setUnqualifiedCount(element.getUnqualifiedCount());
				tg_riskCheckBusiCodeSum.setPushCount(element.getPushCount());
				tg_riskCheckBusiCodeSum.setFeedbackCount(element.getFeedbackCount());
				tg_riskCheckBusiCodeSum.setHandleCount(element.getHandleCount());
				oldTempList.add(tg_riskCheckBusiCodeSum);
			}
			for (int i = 0; i < oldTempList.size() - 1; i++)
			{
				Tg_RiskCheckBusiCodeSum tg_riskCheckBusiCodeSumI = oldTempList.get(i);
				for (int j = i + 1; j < oldTempList.size(); j++) {
					Tg_RiskCheckBusiCodeSum tg_riskCheckBusiCodeSumJ = oldTempList.get(j);
					if (tg_riskCheckBusiCodeSumI.getBigBusiType().equals(tg_riskCheckBusiCodeSumJ.getBigBusiType())) {
						tg_riskCheckBusiCodeSumI.setSumCheckCount(tg_riskCheckBusiCodeSumI.getSumCheckCount() + tg_riskCheckBusiCodeSumJ.getSumCheckCount());
						tg_riskCheckBusiCodeSumI.setQualifiedCount(tg_riskCheckBusiCodeSumI.getQualifiedCount() + tg_riskCheckBusiCodeSumJ.getQualifiedCount());
						tg_riskCheckBusiCodeSumI.setUnqualifiedCount(tg_riskCheckBusiCodeSumI.getUnqualifiedCount() + tg_riskCheckBusiCodeSumJ.getUnqualifiedCount());
						tg_riskCheckBusiCodeSumI.setPushCount(tg_riskCheckBusiCodeSumI.getPushCount() + tg_riskCheckBusiCodeSumJ.getPushCount());
						tg_riskCheckBusiCodeSumI.setFeedbackCount(tg_riskCheckBusiCodeSumI.getFeedbackCount() + tg_riskCheckBusiCodeSumJ.getFeedbackCount());
						tg_riskCheckBusiCodeSumI.setHandleCount(tg_riskCheckBusiCodeSumI.getHandleCount() + tg_riskCheckBusiCodeSumJ.getHandleCount());
						oldTempList.remove(j);
						j--;
					}
				}
			}
			for(Tg_RiskCheckBusiCodeSum element:oldTempList)
			{
				list.add(getSimpleInfoRiskRoutineMonthSum(element));
			}
		}
		return list;
	}

	public Properties executeRiskRoutineMonthSumDetail(List<Tg_RiskCheckBusiCodeSum> oldList)
	{
		Properties properties = null;
		if(oldList != null)
		{
			List<Tg_RiskCheckBusiCodeSum> oldTempList = new ArrayList<Tg_RiskCheckBusiCodeSum>();
			for(Tg_RiskCheckBusiCodeSum element:oldList)
			{
				Tg_RiskCheckBusiCodeSum tg_riskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
				tg_riskCheckBusiCodeSum.setBigBusiType(element.getBigBusiType());
				tg_riskCheckBusiCodeSum.setSumCheckCount(element.getSumCheckCount());
				tg_riskCheckBusiCodeSum.setQualifiedCount(element.getQualifiedCount());
				tg_riskCheckBusiCodeSum.setUnqualifiedCount(element.getUnqualifiedCount());
				tg_riskCheckBusiCodeSum.setPushCount(element.getPushCount());
				tg_riskCheckBusiCodeSum.setFeedbackCount(element.getFeedbackCount());
				tg_riskCheckBusiCodeSum.setHandleCount(element.getHandleCount());
				oldTempList.add(tg_riskCheckBusiCodeSum);
			}
			for (int i = 0; i < oldTempList.size() - 1; i++)
			{
				Tg_RiskCheckBusiCodeSum tg_riskCheckBusiCodeSumI = oldTempList.get(i);
				for (int j = i + 1; j < oldTempList.size(); j++) {
					Tg_RiskCheckBusiCodeSum tg_riskCheckBusiCodeSumJ = oldTempList.get(j);
					tg_riskCheckBusiCodeSumI.setSumCheckCount(tg_riskCheckBusiCodeSumI.getSumCheckCount() + tg_riskCheckBusiCodeSumJ.getSumCheckCount());
					tg_riskCheckBusiCodeSumI.setQualifiedCount(tg_riskCheckBusiCodeSumI.getQualifiedCount() + tg_riskCheckBusiCodeSumJ.getQualifiedCount());
					tg_riskCheckBusiCodeSumI.setUnqualifiedCount(tg_riskCheckBusiCodeSumI.getUnqualifiedCount() + tg_riskCheckBusiCodeSumJ.getUnqualifiedCount());
					tg_riskCheckBusiCodeSumI.setPushCount(tg_riskCheckBusiCodeSumI.getPushCount() + tg_riskCheckBusiCodeSumJ.getPushCount());
					tg_riskCheckBusiCodeSumI.setFeedbackCount(tg_riskCheckBusiCodeSumI.getFeedbackCount() + tg_riskCheckBusiCodeSumJ.getFeedbackCount());
					tg_riskCheckBusiCodeSumI.setHandleCount(tg_riskCheckBusiCodeSumI.getHandleCount() + tg_riskCheckBusiCodeSumJ.getHandleCount());
					oldTempList.remove(j);
					j--;
				}
			}
			if (!oldTempList.isEmpty()) {
				properties = getSimpleInfoRiskRoutineMonthSum(oldTempList.get(0));
			}
		}
		return properties;
	}

	private Properties getSimpleInfoRiskRoutineMonthSum(Tg_RiskCheckBusiCodeSum object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		Sm_BaseParameter bigBusi = sm_BaseParameterGetService.getParameter("1", object.getBigBusiType());
		Sm_BaseParameter smallBusi = sm_BaseParameterGetService.getParameter("1", object.getSmallBusiType());
		if(bigBusi != null)
		{
			properties.put("bigBusiName", bigBusi.getTheName());//业务大类
			properties.put("bigBusiValue", bigBusi.getTheValue());
		}
		if(smallBusi != null)
		{
			properties.put("smallBusiName", smallBusi.getTheName());//业务小类
			properties.put("smallBusiValue", smallBusi.getTheValue());
		}

		properties.put("sumCheckCount", object.getSumCheckCount());//业务
		properties.put("qualifiedCount", object.getQualifiedCount());//合格
		properties.put("unqualifiedCount", object.getUnqualifiedCount());//不合格
		properties.put("pushCount", object.getPushCount());//推送整改
		properties.put("feedbackCount", object.getFeedbackCount());//整改反馈
		properties.put("handleCount", object.getHandleCount());//完成整改
		properties.put("spotTimeStamp", MyDatetime.getInstance().dateToString(object.getSpotTimeStamp(), "yyyy-MM"));//抽查所在月

		return properties;
	}
}
