package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Empj_PjDevProgressForcastDtl;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：项目-工程进度预测 -明细表 
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_PjDevProgressForcastDtlRebuild extends RebuilderBase<Empj_PjDevProgressForcastDtl>
{
	@Override
	public Properties getSimpleInfo(Empj_PjDevProgressForcastDtl object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		Sm_User sm_user = object.getUserStart();
		if (sm_user == null)
		{
			sm_user = object.getUserUpdate();
		}
		if(sm_user != null)
		{
			properties.put("userUpdateId", sm_user.getTableId());
			properties.put("userName", sm_user.getTheName());
		}
		Long operationTimeStamp = object.getLastUpdateTimeStamp();
		if (operationTimeStamp == null || operationTimeStamp < 1)
		{
			operationTimeStamp = object.getCreateTimeStamp();
		}
		properties.put("operationDateTime",  MyDatetime.getInstance().dateToString2(operationTimeStamp));

		Tgpj_BldLimitAmountVer_AFDtl tgpjBldLimitAmountVerAfDtl= object.getBldLimitAmountVerAfDtl();
		if (tgpjBldLimitAmountVerAfDtl != null)
		{
			properties.put("bldLimitAmountVerAfDtlId",  tgpjBldLimitAmountVerAfDtl.getTableId());
			properties.put("stageName",  tgpjBldLimitAmountVerAfDtl.getStageName());
			properties.put("limitedAmount",  tgpjBldLimitAmountVerAfDtl.getLimitedAmount());
		}

		properties.put("predictedFinishDatetime",
				MyDatetime.getInstance().dateToSimpleString(object.getPredictedFinishDatetime()));
		properties.put("ogPredictedFinishDatetime", MyDatetime.getInstance().dateToSimpleString(object.getOgPredictedFinishDatetime()));
		properties.put("progressJudgement", object.getProgressJudgement());
		properties.put("causeDescriptionForDelay", object.getCauseDescriptionForDelay());

		return properties;
	}

	@Override
	public Properties getDetail(Empj_PjDevProgressForcastDtl object)
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
		properties.put("mainTable", object.getMainTable());
		properties.put("mainTableId", object.getMainTable().getTableId());
		properties.put("patrolTimestamp", object.getPatrolTimestamp());
		properties.put("currentProgressNode", object.getCurrentProgressNode());
		properties.put("predictedFigureProgress", object.getPredictedFigureProgress());
		properties.put("predictedFinishDatetime", object.getPredictedFinishDatetime());
		properties.put("progressJudgement", object.getProgressJudgement());
		properties.put("causeDescriptionForDelay", object.getCauseDescriptionForDelay());
		properties.put("remark", object.getRemark());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_PjDevProgressForcastDtl> empj_PjDevProgressForcastDtlList)
	{
		List<HashMap> list = new ArrayList<HashMap>();
		if(empj_PjDevProgressForcastDtlList != null)
		{
			for(Empj_PjDevProgressForcastDtl object:empj_PjDevProgressForcastDtlList)
			{
				HashMap properties = new HashMap<String,Object>();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
//				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("userStartName", object.getUserStart().getTheName());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				if ( object.getUserRecord() != null) {
//					properties.put("userRecordId", object.getUserRecord().getTableId());
//				}
				properties.put("tableId", object.getTableId());
				Sm_User sm_user = object.getUserStart();
				if (sm_user == null)
				{
					sm_user = object.getUserUpdate();
				}
				if(sm_user != null)
				{
					properties.put("userUpdateId", sm_user.getTableId());
					properties.put("userName", sm_user.getTheName());
				}
				Long operationTimeStamp = object.getLastUpdateTimeStamp();
				if (operationTimeStamp == null || operationTimeStamp < 1)
				{
					operationTimeStamp = object.getCreateTimeStamp();
				}
				properties.put("operationDateTime",  MyDatetime.getInstance().dateToSimpleString(operationTimeStamp));

				Tgpj_BldLimitAmountVer_AFDtl tgpjBldLimitAmountVerAfDtl= object.getBldLimitAmountVerAfDtl();
				if (tgpjBldLimitAmountVerAfDtl != null)
				{
					properties.put("bldLimitAmountVerAfDtlId",  tgpjBldLimitAmountVerAfDtl.getTableId());
					properties.put("stageName",  tgpjBldLimitAmountVerAfDtl.getStageName());
					properties.put("limitedAmount",  tgpjBldLimitAmountVerAfDtl.getLimitedAmount());
				}

				properties.put("predictedFinishDatetime",
						MyDatetime.getInstance().dateToSimpleString(object.getPredictedFinishDatetime()));
				properties.put("ogPredictedFinishDatetime", MyDatetime.getInstance().dateToSimpleString(object.getOgPredictedFinishDatetime()));
				properties.put("progressJudgement", object.getProgressJudgement());
				properties.put("causeDescriptionForDelay", object.getCauseDescriptionForDelay());


				properties.put("recordTimeStamp", object.getRecordTimeStamp());
//				properties.put("mainTable", object.getMainTable());
				properties.put("mainTableId", object.getMainTable().getTableId());
				properties.put("patrolTimestamp", object.getPatrolTimestamp());
				properties.put("currentProgressNode", object.getCurrentProgressNode());
				properties.put("predictedFigureProgress", object.getPredictedFigureProgress());
//				properties.put("predictedFinishDatetime", object.getPredictedFinishDatetime());
//				properties.put("progressJudgement", object.getProgressJudgement());
//				properties.put("causeDescriptionForDelay", object.getCauseDescriptionForDelay());
				properties.put("remark", object.getRemark());
				
				list.add(properties);
			}
		}
		return list;
	}
}
