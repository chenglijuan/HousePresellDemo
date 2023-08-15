package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：三方协议
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_TripleAgreementDeleteService
{
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;

	public Properties execute(Tgxy_TripleAgreementForm model)
	{
		Properties properties = new MyProperties();

		Long tgxy_TripleAgreementId = model.getTableId();
		Tgxy_TripleAgreement tgxy_TripleAgreement = (Tgxy_TripleAgreement)tgxy_TripleAgreementDao.findById(tgxy_TripleAgreementId);
		if(tgxy_TripleAgreement == null)
		{
			return MyBackInfo.fail(properties, "'Tgxy_TripleAgreement(Id:" + tgxy_TripleAgreementId + ")'不存在");
		}
		// 状态设置为删除状态 1,同时更新最后操作时间
		tgxy_TripleAgreement.setTheState(1);
		tgxy_TripleAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
