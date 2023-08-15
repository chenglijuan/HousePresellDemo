package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_CommonMessageNoticeDetailService
{
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();
		
		Long sm_CommonMessageId = model.getTableId();
			
		Sm_CommonMessage sm_CommonMessage = (Sm_CommonMessage)sm_CommonMessageDao.findById(sm_CommonMessageId);
		if(sm_CommonMessage == null || S_TheState.Deleted.equals(sm_CommonMessage.getTheState()))
		{
			return MyBackInfo.fail(properties, "通告信息不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_CommonMessage", sm_CommonMessage);
		
		return properties;
	}
}
