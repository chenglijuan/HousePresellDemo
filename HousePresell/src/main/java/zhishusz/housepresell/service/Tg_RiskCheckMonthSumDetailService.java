package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_RiskCheckBusiCodeSumForm;
import zhishusz.housepresell.controller.form.Tg_RiskCheckMonthSumForm;
import zhishusz.housepresell.database.dao.Tg_RiskCheckBusiCodeSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckMonthSumDao;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskCheckMonthSumDetailService
{
	@Autowired
	private Tg_RiskCheckMonthSumDao tg_RiskCheckMonthSumDao;
	@Autowired
	private Tg_RiskCheckBusiCodeSumDao tg_RiskCheckBusiCodeSumDao;

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
		
		Tg_RiskCheckBusiCodeSumForm tg_RiskCheckBusiCodeSumForm = new Tg_RiskCheckBusiCodeSumForm();
		tg_RiskCheckBusiCodeSumForm.setSpotTimeStamp(tg_RiskCheckMonthSum.getSpotTimeStamp());
		tg_RiskCheckBusiCodeSumForm.setTheState(model.getTheState());
		
		List<Tg_RiskCheckBusiCodeSum> tg_RiskCheckBusiCodeSumList = tg_RiskCheckBusiCodeSumDao.findByPage(tg_RiskCheckBusiCodeSumDao.getQuery(tg_RiskCheckBusiCodeSumDao.getBasicHQL(), tg_RiskCheckBusiCodeSumForm));
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tg_RiskCheckMonthSum", tg_RiskCheckMonthSum);
		properties.put("tg_RiskCheckBusiCodeSumList", tg_RiskCheckBusiCodeSumList);

		return properties;
	}
}
