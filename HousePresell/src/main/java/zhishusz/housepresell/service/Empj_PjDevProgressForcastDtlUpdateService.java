package zhishusz.housepresell.service;

import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.util.MyDatetime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastDtlForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：项目-工程进度预测 -明细表 
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PjDevProgressForcastDtlUpdateService
{
	@Autowired
	private Empj_PjDevProgressForcastDtlDao empj_PjDevProgressForcastDtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_PjDevProgressForcastDao empj_PjDevProgressForcastDao;
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_bldLimitAmountVer_afDtlDao;
	@Autowired
	private Empj_BuildingInfoDao buildingInfoDao;
	
	public Properties execute(Empj_PjDevProgressForcastDtlForm model)
	{
		Properties properties = new MyProperties();

		Long userUpdateId = model.getUserUpdateId();
		Long lastUpdateTimeStamp = model.getOperationTimeStamp(); //System.currentTimeMillis()
//		if (model.getOperationTimeStamp() != null && model.getOperationTimeStamp() > 1)
//		{
//			lastUpdateTimeStamp = model.getOperationTimeStamp();
//		}
//		Double predictedFigureProgress = model.getPredictedFigureProgress();
		Long bldLimitAmountVerAfDtlId = model.getBldLimitAmountVerAfDtlId();
		Integer progressJudgement = model.getProgressJudgement();
		String causeDescriptionForDelay = model.getCauseDescriptionForDelay();
		String remark = model.getRemark();

		String predictedFinishDatetimeStr = model.getPredictedFinishDatetime();
		String ogPredictedFinishDatetimeStr = model.getOgPredictedFinishDatetime();

		if(userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "操作人不存在");
		}

//		if(mainTableId == null || mainTableId < 1)
//		{
//			return MyBackInfo.fail(properties, "'mainTable'不能为空");
//		}
//		if(patrolTimestamp == null || patrolTimestamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'patrolTimestamp'不能为空");
//		}
//		if(currentProgressNode == null || currentProgressNode < 1)
//		{
//			return MyBackInfo.fail(properties, "'currentProgressNode'不能为空");
//		}
//		if(predictedFigureProgress == null || predictedFigureProgress < 0)
//		{
//			return MyBackInfo.fail(properties, "预测进度节点不能为空");
//		}
		if (bldLimitAmountVerAfDtlId == null || bldLimitAmountVerAfDtlId < 1)
		{
			return MyBackInfo.fail(properties, "预测进度节点不能为空");
		}
		if(predictedFinishDatetimeStr == null || "".equals(predictedFinishDatetimeStr))
		{
			return MyBackInfo.fail(properties, "预测完成日期不能为空");
		}
		if(progressJudgement == 1 && (causeDescriptionForDelay == null || causeDescriptionForDelay.length() < 1))
		{
			return MyBackInfo.fail(properties, "进度滞后原因不能为空");
		}

		Long predictedFinishDatetime = MyDatetime.getInstance().stringToLong(predictedFinishDatetimeStr);
		Long ogPredictedFinishDatetime = null;
		if (ogPredictedFinishDatetimeStr != null && !"".equals(ogPredictedFinishDatetimeStr))
		{
			ogPredictedFinishDatetime = MyDatetime.getInstance().stringToLong(ogPredictedFinishDatetimeStr);
		}
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "操作人不存在");
		}
		Tgpj_BldLimitAmountVer_AFDtl tgpjBldLimitAmountVerAfDtl= (Tgpj_BldLimitAmountVer_AFDtl)tgpj_bldLimitAmountVer_afDtlDao.findById(bldLimitAmountVerAfDtlId);
		if (tgpjBldLimitAmountVerAfDtl == null)
		{
			return MyBackInfo.fail(properties, "预测进度节点不存在");
		}

		Long empj_PjDevProgressForcastDtlId = model.getTableId();
		Empj_PjDevProgressForcastDtl empj_PjDevProgressForcastDtl = (Empj_PjDevProgressForcastDtl)empj_PjDevProgressForcastDtlDao.findById(empj_PjDevProgressForcastDtlId);
		if(empj_PjDevProgressForcastDtl == null)
		{
			return MyBackInfo.fail(properties, "当前进度信息不存在");
		}
		Tgpj_BldLimitAmountVer_AFDtl beforeTgpjBldLimitAmountVerAfDtl=
				(Tgpj_BldLimitAmountVer_AFDtl)tgpj_bldLimitAmountVer_afDtlDao.findById(model.getBeforeBldLimitAmountVerAfDtlId());
		Empj_BuildingInfo buildingInfo = (Empj_BuildingInfo) buildingInfoDao.findById(model.getBuildingId());

		empj_PjDevProgressForcastDtl.setUserUpdate(userUpdate);
		empj_PjDevProgressForcastDtl.setLastUpdateTimeStamp(lastUpdateTimeStamp);
//		empj_PjDevProgressForcastDtl.setMainTable(mainTable);
//		empj_PjDevProgressForcastDtl.setPatrolTimestamp(patrolTimestamp);
//		empj_PjDevProgressForcastDtl.setCurrentProgressNode(currentProgressNode);
//		empj_PjDevProgressForcastDtl.setPredictedFigureProgress(predictedFigureProgress);
		empj_PjDevProgressForcastDtl.setBeforeBldLimitAmountVerAfDtl(beforeTgpjBldLimitAmountVerAfDtl);
		empj_PjDevProgressForcastDtl.setBuildingInfo(buildingInfo);
		empj_PjDevProgressForcastDtl.setBldLimitAmountVerAfDtl(tgpjBldLimitAmountVerAfDtl);
		empj_PjDevProgressForcastDtl.setOgPredictedFinishDatetime(ogPredictedFinishDatetime);
		empj_PjDevProgressForcastDtl.setPredictedFinishDatetime(predictedFinishDatetime);
		empj_PjDevProgressForcastDtl.setProgressJudgement(progressJudgement);
		empj_PjDevProgressForcastDtl.setCauseDescriptionForDelay(causeDescriptionForDelay);
//		empj_PjDevProgressForcastDtl.setRemark(remark);
	
//		empj_PjDevProgressForcastDtlDao.save(empj_PjDevProgressForcastDtl);

		properties.put("empj_PjDevProgressForcastDtl",empj_PjDevProgressForcastDtl);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
