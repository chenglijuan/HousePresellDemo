package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：合作协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementVerMngDetailService
{
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;

	public Properties execute(Tgxy_CoopAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();

		Long tgxy_CoopAgreementVerMngId = model.getTableId();
		Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng = (Tgxy_CoopAgreementVerMng)tgxy_CoopAgreementVerMngDao.findById(tgxy_CoopAgreementVerMngId);
		if(tgxy_CoopAgreementVerMng == null || S_TheState.Deleted.equals(tgxy_CoopAgreementVerMng.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgxy_CoopAgreementVerMng(Id:" + tgxy_CoopAgreementVerMngId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgxy_CoopAgreementVerMng", tgxy_CoopAgreementVerMng);

		return properties;
	}
}
