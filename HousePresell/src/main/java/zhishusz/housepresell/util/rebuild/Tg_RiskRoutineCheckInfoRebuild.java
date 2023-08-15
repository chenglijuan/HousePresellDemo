package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckRatioConfigDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.state.S_IsQualified;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_RiskRoutineCheckInfoRebuild extends RebuilderBase<Tg_RiskRoutineCheckInfo>
{
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigDao tg_RiskRoutineCheckRatioConfigDao;
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;

	@Override
	public Properties getSimpleInfo(Tg_RiskRoutineCheckInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("eCodeOfBill", object.geteCodeOfBill());
		
		if(object.getCheckResult() != null)
		{
			properties.put("checkResult", object.getCheckResult());
		}
		else
		{
			properties.put("checkResult", S_IsQualified.Yes);
		}
		
		if(object.getUnqualifiedReasons() != null)
		{
			properties.put("unqualifiedReasons", object.getUnqualifiedReasons());
		}
		else
		{
			properties.put("unqualifiedReasons", "");
		}
		properties.put("modifyFeedback", object.getModifyFeedback());
		properties.put("forensicConfirmation", object.getForensicConfirmation());
		properties.put("isChoosePush", object.getIsChoosePush());
		properties.put("isDoPush", object.getIsDoPush());
		properties.put("lastUpdateTimeStamp", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
		properties.put("isHandle", object.getIsHandle());
		properties.put("rectificationState", object.getRectificationState());
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
		
		//抽查配置
//		Tg_RiskRoutineCheckRatioConfigForm tg_RiskRoutineCheckRatioConfigForm = new Tg_RiskRoutineCheckRatioConfigForm();
//		tg_RiskRoutineCheckRatioConfigForm.setLargeBusinessValue(object.getBigBusiType());
//		tg_RiskRoutineCheckRatioConfigForm.setSubBusinessValue(object.getSmallBusiType());
//		tg_RiskRoutineCheckRatioConfigForm.setTheState(S_TheState.Normal);
//		
//		Tg_RiskRoutineCheckRatioConfig tg_RiskRoutineCheckRatioConfig = tg_RiskRoutineCheckRatioConfigDao.findOneByQuery_T(tg_RiskRoutineCheckRatioConfigDao.getQuery(tg_RiskRoutineCheckRatioConfigDao.getBasicHQL(), tg_RiskRoutineCheckRatioConfigForm));
//		if(tg_RiskRoutineCheckRatioConfig != null && tg_RiskRoutineCheckRatioConfig.getRole() != null)
//		{
//			properties.put("roleName", tg_RiskRoutineCheckRatioConfig.getRole().getTheName());//发送角色
//		}


		return properties;
	}

	
	@SuppressWarnings("rawtypes")
	public List executeForList(List<Tg_RiskRoutineCheckInfo> tg_RiskRoutineCheckInfoList, String roleName)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_RiskRoutineCheckInfoList != null)
		{
			for(Tg_RiskRoutineCheckInfo object:tg_RiskRoutineCheckInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("eCodeOfBill", object.geteCodeOfBill());
				
				if(object.getCheckResult() != null)
				{
					properties.put("checkResult", object.getCheckResult());
				}
				else
				{
					properties.put("checkResult", S_IsQualified.Yes);
				}
				
				if(object.getUnqualifiedReasons() != null)
				{
					properties.put("unqualifiedReasons", object.getUnqualifiedReasons());
				}
				else
				{
					properties.put("unqualifiedReasons", "");
				}
				properties.put("modifyFeedback", object.getModifyFeedback());
				properties.put("forensicConfirmation", object.getForensicConfirmation());
				properties.put("isChoosePush", object.getIsChoosePush());
				properties.put("isDoPush", object.getIsDoPush());
				properties.put("lastUpdateTimeStamp", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
				properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
				properties.put("isHandle", object.getIsHandle());
				properties.put("rectificationState", object.getRectificationState());
				properties.put("roleName", roleName);
				properties.put("relatedTableId", object.getRelatedTableId());
				properties.put("smallBusiType", object.getSmallBusiType());
				
				list.add(properties);
			}
		}
		return list;
	}

	@Override
	public Properties getDetail(Tg_RiskRoutineCheckInfo object)
	{
		if(object == null) return null;
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
		properties.put("spotTimeStamp", object.getSpotTimeStamp());
		properties.put("bigBusiType", object.getBigBusiType());
		properties.put("smallBusiType", object.getSmallBusiType());
		properties.put("eCodeOfBill", object.geteCodeOfBill());
		properties.put("checkResult", object.getCheckResult());
		properties.put("unqualifiedReasons", object.getUnqualifiedReasons());
		properties.put("modifyFeedback", object.getModifyFeedback());
		properties.put("forensicConfirmation", object.getForensicConfirmation());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_RiskRoutineCheckInfo> tg_RiskRoutineCheckInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_RiskRoutineCheckInfoList != null)
		{
			for(Tg_RiskRoutineCheckInfo object:tg_RiskRoutineCheckInfoList)
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
				properties.put("spotTimeStamp", object.getSpotTimeStamp());
				properties.put("bigBusiType", object.getBigBusiType());
				properties.put("smallBusiType", object.getSmallBusiType());
				properties.put("eCodeOfBill", object.geteCodeOfBill());
				properties.put("checkResult", object.getCheckResult());
				properties.put("unqualifiedReasons", object.getUnqualifiedReasons());
				properties.put("modifyFeedback", object.getModifyFeedback());
				properties.put("forensicConfirmation", object.getForensicConfirmation());
				
				list.add(properties);
			}
		}
		return list;
	}
}
