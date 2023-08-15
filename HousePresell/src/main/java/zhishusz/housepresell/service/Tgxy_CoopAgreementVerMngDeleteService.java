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
 * Service单个删除：合作协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementVerMngDeleteService
{
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;

	public Properties execute(Tgxy_CoopAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();

		Long tgxy_CoopAgreementVerMngId = model.getTableId();
		Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng = (Tgxy_CoopAgreementVerMng)tgxy_CoopAgreementVerMngDao.findById(tgxy_CoopAgreementVerMngId);
		if(tgxy_CoopAgreementVerMng == null)
		{
			return MyBackInfo.fail(properties, "'Tgxy_CoopAgreementVerMng(Id:" + tgxy_CoopAgreementVerMngId + ")'不存在");
		}
		
		tgxy_CoopAgreementVerMng.setTheState(S_TheState.Deleted);
		tgxy_CoopAgreementVerMngDao.save(tgxy_CoopAgreementVerMng);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
