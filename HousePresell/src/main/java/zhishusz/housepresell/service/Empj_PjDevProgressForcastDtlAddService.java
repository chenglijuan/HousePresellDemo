package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastDtlForm;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcastDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
//import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.database.dao.Sm_UserDao;
	
/*
 * Service添加操作：项目-工程进度预测 -明细表 
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PjDevProgressForcastDtlAddService
{
//	private static final String BUSI_CODE = "03030201";//具体业务编码参看SVN文

	@Autowired
	private Empj_PjDevProgressForcastDtlDao empj_PjDevProgressForcastDtlDao;
//	@Autowired
//	private Empj_PjDevProgressForcastDao empj_PjDevProgressForcastDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_bldLimitAmountVer_afDtlDao;
	@Autowired
	private Empj_BuildingInfoDao buildingInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	public Properties execute(Empj_PjDevProgressForcastDtlForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = S_TheState.Normal;
		String busiState = S_BusiState.HaveRecord; //model.getBusiState()
		String eCode = sm_BusinessCodeGetService.execute(S_BusiCode.busiCode_03030201); //自动编号：TGZZ+YY+MM+DD+四位流水号（按年度流水）
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getOperationTimeStamp(); //System.currentTimeMillis()
//		Long mainTableId = model.getMainTableId();
//		Long patrolTimestamp = model.getPatrolTimestamp();
//		Double currentProgressNode = model.getCurrentProgressNode();
//		Double predictedFigureProgress = model.getPredictedFigureProgress();
		Long bldLimitAmountVerAfDtlId = model.getBldLimitAmountVerAfDtlId();
		Integer progressJudgement = model.getProgressJudgement();
		String causeDescriptionForDelay = model.getCauseDescriptionForDelay();
		String remark = model.getRemark();

		String predictedFinishDatetimeStr = model.getPredictedFinishDatetime();
		String ogPredictedFinishDatetimeStr = model.getOgPredictedFinishDatetime();

		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "操作人不存在");
		}
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
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "操作人不存在");
		}
		Tgpj_BldLimitAmountVer_AFDtl tgpjBldLimitAmountVerAfDtl= (Tgpj_BldLimitAmountVer_AFDtl)tgpj_bldLimitAmountVer_afDtlDao.findById(bldLimitAmountVerAfDtlId);
		if (tgpjBldLimitAmountVerAfDtl == null)
		{
			return MyBackInfo.fail(properties, "预测进度节点不存在");
		}
		Tgpj_BldLimitAmountVer_AFDtl beforeTgpjBldLimitAmountVerAfDtl=
				(Tgpj_BldLimitAmountVer_AFDtl)tgpj_bldLimitAmountVer_afDtlDao.findById(model.getBeforeBldLimitAmountVerAfDtlId());
		Empj_BuildingInfo buildingInfo = (Empj_BuildingInfo) buildingInfoDao.findById(model.getBuildingId());

//		Empj_PjDevProgressForcast mainTable = (Empj_PjDevProgressForcast)empj_PjDevProgressForcastDao.findById(mainTableId);
//		if(mainTable == null)
//		{
//			return MyBackInfo.fail(properties, "'mainTable'不能为空");
//		}

		Empj_PjDevProgressForcastDtl empj_PjDevProgressForcastDtl = new Empj_PjDevProgressForcastDtl();
		empj_PjDevProgressForcastDtl.setTheState(theState);
		empj_PjDevProgressForcastDtl.setBusiState(busiState);
		empj_PjDevProgressForcastDtl.seteCode(eCode);
		empj_PjDevProgressForcastDtl.setUserStart(userStart);
		empj_PjDevProgressForcastDtl.setCreateTimeStamp(createTimeStamp);
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

		properties.put("tableId", empj_PjDevProgressForcastDtl.getTableId());
		properties.put("empj_PjDevProgressForcastDtl",empj_PjDevProgressForcastDtl);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
