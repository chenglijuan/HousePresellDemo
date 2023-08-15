package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_CoopAgreementBatchDeleteService
{
	@Autowired
	private Tgxy_CoopAgreementDao tgxy_CoopAgreementDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_UserDao sm_UserDao;

	public Properties execute(Tgxy_CoopAgreementForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * 删除合作协议（逻辑删除）
		 * 1.theState 状态置为删除态：1
		 * 2.查询合作协议是否有相关附件信息 如果存在 置为删除态 1
		 */
		Long userUpdateId = model.getUserId();
		if (userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}

		// 查询关联修改人信息
		Sm_User userUpdate = (Sm_User) sm_UserDao.findById(userUpdateId);
		if (userUpdate == null)
		{
			return MyBackInfo.fail(properties, "查询登录人信息为空");
		}
		// 删除合作协议基本信息
		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "请选择需要删除的信息");
		}

		for (Long tableId : idArr)
		{
			Tgxy_CoopAgreement tgxy_CoopAgreement = (Tgxy_CoopAgreement) tgxy_CoopAgreementDao.findById(tableId);
			if (tgxy_CoopAgreement == null)
			{
				return MyBackInfo.fail(properties, "'合作协议(Id:" + tableId + ")'未查询到相关信息");
			}

			tgxy_CoopAgreement.setUserUpdate(userUpdate);
			tgxy_CoopAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgxy_CoopAgreement.setTheState(S_TheState.Deleted);
			tgxy_CoopAgreementDao.save(tgxy_CoopAgreement);

			// 根据查询是否存在附件信息设置是否删除附件 (先使用 A 方案 后期使用B方案)
			// 设置附件model条件信息，进行附件条件查询
			Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(String.valueOf(tableId));
			sm_AttachmentForm.setBusiType("06110103");
			sm_AttachmentForm.setTheState(S_TheState.Normal);

			// 加载所有相关附件信息,for循环执行删除
			@SuppressWarnings("unchecked")
			List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

			if (null != sm_AttachmentList && sm_AttachmentList.size() > 0)
			{

				for (Sm_Attachment sm_Attachment : sm_AttachmentList)
				{
					sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
					sm_Attachment.setUserUpdate(userUpdate);
					sm_Attachment.setTheState(S_TheState.Deleted);
					sm_AttachmentDao.save(sm_Attachment);
				}

			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
