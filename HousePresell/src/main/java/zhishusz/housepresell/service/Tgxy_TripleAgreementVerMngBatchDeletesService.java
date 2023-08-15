package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：三方协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_TripleAgreementVerMngBatchDeletesService
{
	@Autowired
	private Tgxy_TripleAgreementVerMngDao tgxy_TripleAgreementVerMngDao;

	public Properties execute(Tgxy_TripleAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgxy_TripleAgreementVerMng tgxy_TripleAgreementVerMng = (Tgxy_TripleAgreementVerMng)tgxy_TripleAgreementVerMngDao.findById(tableId);
			if(tgxy_TripleAgreementVerMng == null)
			{
				return MyBackInfo.fail(properties, "'三方协议(Id:" + tableId + ")'不存在");
			}
		
			tgxy_TripleAgreementVerMng.setTheState(S_TheState.Deleted);
			tgxy_TripleAgreementVerMngDao.save(tgxy_TripleAgreementVerMng);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
