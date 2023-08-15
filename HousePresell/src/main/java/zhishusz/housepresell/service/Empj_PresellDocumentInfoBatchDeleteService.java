package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PresellDocumentInfoForm;
import zhishusz.housepresell.database.dao.Empj_PresellDocumentInfoDao;
import zhishusz.housepresell.database.po.Empj_PresellDocumentInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：预售证信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PresellDocumentInfoBatchDeleteService
{
	@Autowired
	private Empj_PresellDocumentInfoDao empj_PresellDocumentInfoDao;

	public Properties execute(Empj_PresellDocumentInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_PresellDocumentInfo empj_PresellDocumentInfo = (Empj_PresellDocumentInfo)empj_PresellDocumentInfoDao.findById(tableId);
			if(empj_PresellDocumentInfo == null)
			{
				return MyBackInfo.fail(properties, "'Empj_PresellDocumentInfo(Id:" + tableId + ")'不存在");
			}
		
			empj_PresellDocumentInfo.setTheState(S_TheState.Deleted);
			empj_PresellDocumentInfoDao.save(empj_PresellDocumentInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
