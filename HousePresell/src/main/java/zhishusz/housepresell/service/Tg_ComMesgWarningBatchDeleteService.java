package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：未处理预警
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_ComMesgWarningBatchDeleteService
{
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	
	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_CommonMessageDtl sm_CommonMessageDtl = sm_CommonMessageDtlDao.findById(tableId);
			if(sm_CommonMessageDtl == null)
			{
				return MyBackInfo.fail(properties, "未查询到有效的预警信息！");
			}
			
			sm_CommonMessageDtl.setTheState(S_TheState.Deleted);

			sm_CommonMessageDtlDao.save(sm_CommonMessageDtl);
			
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
