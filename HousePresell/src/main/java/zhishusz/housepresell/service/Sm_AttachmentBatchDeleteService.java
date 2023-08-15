package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：附件
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentBatchDeleteService
{
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;

	public Properties execute(Sm_AttachmentForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_Attachment sm_Attachment = (Sm_Attachment)sm_AttachmentDao.findById(tableId);
			if(sm_Attachment == null)
			{
				return MyBackInfo.fail(properties, "'Sm_Attachment(Id:" + tableId + ")'不存在");
			}
		
			sm_Attachment.setTheState(S_TheState.Deleted);
			sm_AttachmentDao.save(sm_Attachment);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
