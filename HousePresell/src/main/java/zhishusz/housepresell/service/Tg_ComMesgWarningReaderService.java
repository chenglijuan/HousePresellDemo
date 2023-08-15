package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.state.S_IsReaderState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 预警更新操作：预警消息已读
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_ComMesgWarningReaderService
{
	
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;

	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要修改的信息");
		}

		for (Long tableId : idArr)
		{
			// 查询预警
			Sm_CommonMessageDtl sm_CommonMessageDtl = sm_CommonMessageDtlDao.findById(tableId);
			
			if(sm_CommonMessageDtl == null)
			{
				return MyBackInfo.fail(properties, "未查询到有效的预警信息！");
			}
			
			sm_CommonMessageDtl.setIsReader(S_IsReaderState.ReadMesg);//0 未读   1 已读
			
			sm_CommonMessageDtlDao.save(sm_CommonMessageDtl);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
