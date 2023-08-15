package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_RiskCheckBusiCodeSumForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckInfoForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckRatioConfigForm;
import zhishusz.housepresell.database.dao.Tg_RiskCheckBusiCodeSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckInfoDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckRatioConfigDao;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskCheckBusiCodeSumDetailService
{
	@Autowired
	private Tg_RiskCheckBusiCodeSumDao tg_RiskCheckBusiCodeSumDao;
	@Autowired
	private Tg_RiskRoutineCheckInfoDao tg_RiskRoutineCheckInfoDao;
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigDao tg_RiskRoutineCheckRatioConfigDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskCheckBusiCodeSumForm model)
	{
		Properties properties = new MyProperties();
		MyDatetime myDatetime = MyDatetime.getInstance();
		
		model.setSpotTimeStamp(myDatetime.stringToLong(model.getSpotTimeStr()+"-01"));
		
		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = tg_RiskCheckBusiCodeSumDao.findOneByQuery_T(tg_RiskCheckBusiCodeSumDao.getQuery(tg_RiskCheckBusiCodeSumDao.getBasicHQL(), model));
		
		Tg_RiskRoutineCheckRatioConfigForm tg_RiskRoutineCheckRatioConfigForm = new Tg_RiskRoutineCheckRatioConfigForm();
		tg_RiskRoutineCheckRatioConfigForm.setLargeBusinessValue(tg_RiskCheckBusiCodeSum.getBigBusiType());
		tg_RiskRoutineCheckRatioConfigForm.setSubBusinessValue(tg_RiskCheckBusiCodeSum.getSmallBusiType());
		tg_RiskRoutineCheckRatioConfigForm.setTheState(S_TheState.Normal);
		
		Tg_RiskRoutineCheckRatioConfig tg_RiskRoutineCheckRatioConfig = tg_RiskRoutineCheckRatioConfigDao.findOneByQuery_T(tg_RiskRoutineCheckRatioConfigDao.getQuery(tg_RiskRoutineCheckRatioConfigDao.getBasicHQL(), tg_RiskRoutineCheckRatioConfigForm));
		if(tg_RiskRoutineCheckRatioConfig != null && tg_RiskRoutineCheckRatioConfig.getRole() != null)
		{
			properties.put("roleName", tg_RiskRoutineCheckRatioConfig.getRole().getTheName());//发送角色
		}
		
		Tg_RiskRoutineCheckInfoForm checkInfoModel = new Tg_RiskRoutineCheckInfoForm();
		checkInfoModel.setTheState(S_TheState.Normal);
		checkInfoModel.setBigBusiType(model.getBigBusiType());
		checkInfoModel.setSmallBusiType(model.getSmallBusiType());
//		checkInfoModel.setIsDoPush(model.getIsDoPush());
		if(null != model.getIsCheckResult()&&"1".equals( model.getIsCheckResult()))
		{
			checkInfoModel.setCheckResult("0");//不合格
		}
		checkInfoModel.setSpotTimeStamp(myDatetime.stringToLong(model.getSpotTimeStr()+"-01"));
		
		List<Tg_RiskRoutineCheckInfo> tg_RiskRoutineCheckInfoList = tg_RiskRoutineCheckInfoDao.findByPage(tg_RiskRoutineCheckInfoDao.getQuery(tg_RiskRoutineCheckInfoDao.getBasicHQL(), checkInfoModel));
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tg_RiskCheckBusiCodeSum", tg_RiskCheckBusiCodeSum);
		properties.put("tg_RiskRoutineCheckInfoList", tg_RiskRoutineCheckInfoList);

		return properties;
	}
}
