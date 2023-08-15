package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_DepositAgreementForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_DepositAgreementDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_DepositAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：协定存款协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_DepositAgreementBatchDeleteService
{
	@Autowired
	private Tgxy_DepositAgreementDao tgxy_DepositAgreementDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_UserDao sm_UserDao;

	public Properties execute(Tgxy_DepositAgreementForm model)
	{
		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		// 获取操作人Id
		Long userId = model.getUserId();
		// 查询操作人信息
		Sm_User sm_User = (Sm_User) sm_UserDao.findById(userId);
		if (sm_User == null)
		{
			return MyBackInfo.fail(properties, "查询操作人信息为空");
		}

		for (Long tableId : idArr)
		{
			Tgxy_DepositAgreement tgxy_DepositAgreement = (Tgxy_DepositAgreement) tgxy_DepositAgreementDao
					.findById(tableId);
			if (tgxy_DepositAgreement == null)
			{
				return MyBackInfo.fail(properties, "'Tgxy_DepositAgreement(Id:" + tableId + ")'不存在");
			}

			// 设置最后修改时间
			tgxy_DepositAgreement.setTheState(S_TheState.Deleted);
			tgxy_DepositAgreement.setUserUpdate(sm_User);
			tgxy_DepositAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgxy_DepositAgreementDao.save(tgxy_DepositAgreement);

			// 根据查询是否存在附件信息设置是否删除附件 (先使用 A 方案 后期使用B方案)
			// 设置附件model条件信息，进行附件条件查询
			Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(String.valueOf(tableId));
			sm_AttachmentForm.setBusiType("06110101");
			sm_AttachmentForm.setTheState(S_TheState.Normal);

			// 加载所有相关附件信息,for循环执行删除
			@SuppressWarnings("unchecked")
			List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

			if (null != sm_AttachmentList && sm_AttachmentList.size() > 0)
			{

				for (Sm_Attachment sm_Attachment : sm_AttachmentList)
				{
					sm_Attachment.setUserUpdate(sm_User);
					sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
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
