package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDtlDao;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：三方协议结算-子表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementSettleDtlDeleteService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;

	public Properties execute(Tgxy_CoopAgreementSettleDtlForm model)
	{
		Properties properties = new MyProperties();

		Long tgxy_CoopAgreementSettleDtlId = model.getTableId();
		Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl = (Tgxy_CoopAgreementSettleDtl)tgxy_CoopAgreementSettleDtlDao.findById(tgxy_CoopAgreementSettleDtlId);
		if(tgxy_CoopAgreementSettleDtl == null)
		{
			return MyBackInfo.fail(properties, "'Tgxy_CoopAgreementSettleDtl(Id:" + tgxy_CoopAgreementSettleDtlId + ")'不存在");
		}
		
		tgxy_CoopAgreementSettleDtl.setTheState(S_TheState.Deleted);
		tgxy_CoopAgreementSettleDtlDao.save(tgxy_CoopAgreementSettleDtl);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
