package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：附件配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentCfgDetailService
{
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	public Properties execute(Sm_AttachmentCfgForm model)
	{
		Properties properties = new MyProperties();

		Long sm_AttachmentCfgId = model.getTableId();
		Sm_AttachmentCfg sm_AttachmentCfg = (Sm_AttachmentCfg)sm_AttachmentCfgDao.findById(sm_AttachmentCfgId);
		if(sm_AttachmentCfg == null || S_TheState.Deleted.equals(sm_AttachmentCfg.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_AttachmentCfg(Id:" + sm_AttachmentCfgId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_AttachmentCfg", sm_AttachmentCfg);

		return properties;
	}
}
