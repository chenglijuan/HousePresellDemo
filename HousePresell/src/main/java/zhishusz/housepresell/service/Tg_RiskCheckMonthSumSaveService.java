package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_RiskCheckMonthSumForm;
import zhishusz.housepresell.database.dao.Tg_RiskCheckBusiCodeSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckMonthSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckInfoDao;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskCheckMonthSumSaveService
{
	@Autowired
	private Tg_RiskCheckMonthSumDao tg_RiskCheckMonthSumDao;
	@Autowired
	private Tg_RiskCheckBusiCodeSumDao tg_RiskCheckBusiCodeSumDao;
	@Autowired
	private Tg_RiskRoutineCheckInfoDao tg_RiskRoutineCheckInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskCheckMonthSumForm model)
	{
		Properties properties = new MyProperties();
		MyDatetime myDatetime = MyDatetime.getInstance();
		
		model.setSpotTimeStamp(myDatetime.stringToLong(model.getSpotTimeStr()+"-01"));
		
		Tg_RiskCheckMonthSum tg_RiskCheckMonthSum = tg_RiskCheckMonthSumDao.findOneByQuery_T(tg_RiskCheckMonthSumDao.getQuery(tg_RiskCheckMonthSumDao.getBasicHQL(), model));
		if(tg_RiskCheckMonthSum == null)
		{
			return MyBackInfo.fail(properties, "该抽查月的抽查业务不存在");
		}
		
		tg_RiskCheckMonthSum.setTheState(S_TheState.Normal);
		tg_RiskCheckMonthSumDao.save(tg_RiskCheckMonthSum);
		
		List<Tg_RiskCheckBusiCodeSum> tg_RiskCheckBusiCodeSumList = tg_RiskCheckBusiCodeSumDao.findByPage(tg_RiskCheckBusiCodeSumDao.getQuery(tg_RiskCheckBusiCodeSumDao.getBasicHQL(), model));
		for(Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum : tg_RiskCheckBusiCodeSumList)
		{
			tg_RiskCheckBusiCodeSum.setTheState(S_TheState.Normal);
			tg_RiskCheckBusiCodeSumDao.save(tg_RiskCheckBusiCodeSum);
		}
		
		List<Tg_RiskRoutineCheckInfo> tg_RiskRoutineCheckInfoList = tg_RiskRoutineCheckInfoDao.findByPage(tg_RiskRoutineCheckInfoDao.getQuery(tg_RiskRoutineCheckInfoDao.getBasicHQL(), model));
		for(Tg_RiskRoutineCheckInfo tg_RiskRoutineCheckInfo : tg_RiskRoutineCheckInfoList)
		{
			tg_RiskRoutineCheckInfo.setTheState(S_TheState.Normal);
			tg_RiskRoutineCheckInfoDao.save(tg_RiskRoutineCheckInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tg_RiskCheckMonthSum", tg_RiskCheckMonthSum);
		properties.put("tg_RiskCheckBusiCodeSumList", tg_RiskCheckBusiCodeSumList);

		return properties;
	}
}
