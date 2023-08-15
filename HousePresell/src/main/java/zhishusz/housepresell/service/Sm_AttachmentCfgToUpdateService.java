package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：附件配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentCfgToUpdateService
{	
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;	
	
	public Properties execute(Sm_AttachmentCfgForm model)
	{
		Properties properties = new MyProperties();
		
		//获取单条信息	
		Sm_AttachmentCfg sm_AttachmentCfg=sm_AttachmentCfgDao.findById(model.getTableId());	
		properties.put("Sm_AttachmentCfg",sm_AttachmentCfg);	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}
}