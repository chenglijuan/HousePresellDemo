package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_CoopAgreementDeleteService
{
	@Autowired
	private Tgxy_CoopAgreementDao tgxy_CoopAgreementDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;

	public Properties execute(Tgxy_CoopAgreementForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * 删除合作协议（逻辑删除）
		 * 1.theState 状态置为删除态：1
		 * 2.查询合作协议是否有相关附件信息 如果存在 置为删除态 1
		 */

		// 删除合作协议基本信息

		Long tgxy_CoopAgreementId = model.getTableId();
		Tgxy_CoopAgreement tgxy_CoopAgreement = (Tgxy_CoopAgreement) tgxy_CoopAgreementDao
				.findById(tgxy_CoopAgreementId);
		if (tgxy_CoopAgreement == null)
		{
			return MyBackInfo.fail(properties, "'Tgxy_CoopAgreement(Id:" + tgxy_CoopAgreementId + ")'不存在");
		}

		tgxy_CoopAgreement.setTheState(S_TheState.Deleted);
		tgxy_CoopAgreementDao.save(tgxy_CoopAgreement);

		// 根据查询是否存在附件信息设置是否删除附件 (先使用 A 方案 后期使用B方案)
		// 设置附件model条件信息，进行附件条件查询
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(model.getTableId()));

		// 加载所有相关附件信息,for循环执行删除
		List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm), 0, 0);

		if (null != sm_AttachmentList && sm_AttachmentList.size() > 0)
		{

			for (Sm_Attachment sm_Attachment : sm_AttachmentList)
			{
				sm_Attachment.setTheState(S_TheState.Deleted);
				sm_AttachmentDao.save(sm_Attachment);
			}

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
