package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldLimitAmountIsUniqueService
{
	@Autowired
	private Empj_BldLimitAmount_AFDao dao;

	public Properties execute(Empj_BldLimitAmount_AFForm model)
	{
		model.setTheState(S_TheState.Normal);
		Properties properties = new MyProperties();
		boolean uniqueBuilding = dao.isUniqueBuilding(model);

//		Long empj_BldLimitAmount_AFId = model.getTableId();
////		Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = (Empj_BldLimitAmount_AF)empj_BldLimitAmount_AFDao.findById(empj_BldLimitAmount_AFId);
//		List<Empj_BldLimitAmount_AF> byPage = dao.findByPage(dao.getQuery(dao.getBasicHQL(), model));
//		if(byPage.size()==0 || byPage.get(0)==null){
//			return MyBackInfo.fail(properties, "'Empj_BldLimitAmount_AF(Id:" + empj_BldLimitAmount_AFId + ")'不存在");
//		}
//		if(empj_BldLimitAmount_AF == null || S_TheState.Deleted.equals(empj_BldLimitAmount_AF.getTheState()))
//		{
//			return MyBackInfo.fail(properties, "'Empj_BldLimitAmount_AF(Id:" + empj_BldLimitAmount_AFId + ")'不存在");
//		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("isUnique",uniqueBuilding);

		return properties;
	}
}
