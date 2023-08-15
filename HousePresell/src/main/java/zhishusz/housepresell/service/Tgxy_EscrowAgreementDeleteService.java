package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：托管合作协议
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_EscrowAgreementDeleteService
{
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;

	public Properties execute(Tgxy_EscrowAgreementForm model)
	{
		Properties properties = new MyProperties();
		// 先根据传递的id进行查询，再进行删除操作
		Long tgxy_EscrowAgreementId = model.getTableId();
		Tgxy_EscrowAgreement tgxy_EscrowAgreement = (Tgxy_EscrowAgreement)tgxy_EscrowAgreementDao.findById(tgxy_EscrowAgreementId);
		if(tgxy_EscrowAgreement == null)
		{
			return MyBackInfo.fail(properties, "'协议(Id:" + tgxy_EscrowAgreementId + ")'不存在");
		}
		// 状态设置为删除状态 1,同时更新最后操作时间
		tgxy_EscrowAgreement.setTheState(S_TheState.Deleted);
		tgxy_EscrowAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgxy_EscrowAgreementDao.save(tgxy_EscrowAgreement);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
