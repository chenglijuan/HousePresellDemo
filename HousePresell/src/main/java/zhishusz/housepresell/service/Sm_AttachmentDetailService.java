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
 * Service详情：附件
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentDetailService
{
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;

	public Properties execute(Sm_AttachmentForm model)
	{
		Properties properties = new MyProperties();

		Long sm_AttachmentId = model.getTableId();
		Sm_Attachment sm_Attachment = (Sm_Attachment)sm_AttachmentDao.findById(sm_AttachmentId);
		if(sm_Attachment == null || S_TheState.Deleted.equals(sm_Attachment.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_Attachment(Id:" + sm_AttachmentId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_Attachment", sm_Attachment);

		return properties;
	}
}
