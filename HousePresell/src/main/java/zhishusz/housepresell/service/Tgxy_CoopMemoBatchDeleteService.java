package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopMemoForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopMemoDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopMemo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：合作备忘录
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_CoopMemoBatchDeleteService
{
	@Autowired
	private Tgxy_CoopMemoDao tgxy_CoopMemoDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_UserDao sm_UserDao;

	public Properties execute(Tgxy_CoopMemoForm model)
	{
		Properties properties = new MyProperties();

		// 获取操作人Id
		Long userId = model.getUserId();
		if (userId == null || userId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}

		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "请选择需要删除的合作备忘录信息");
		}

		// 查询操作人信息
		Sm_User sm_User = (Sm_User) sm_UserDao.findById(userId);
		if (sm_User == null)
		{
			return MyBackInfo.fail(properties, "操作人信息（ID:" + userId + "）查询为空");
		}

		// 遍历查询，再进行删除操作
		for (Long tableId : idArr)
		{
			Tgxy_CoopMemo tgxy_CoopMemo = (Tgxy_CoopMemo) tgxy_CoopMemoDao.findById(tableId);
			if (tgxy_CoopMemo == null)
			{
				return MyBackInfo.fail(properties, "'合作备忘录(Id:" + tableId + ")'不存在");
			}

			// 设置删除状态 为1 ，且设置最后修改时间为当前系统时间
			tgxy_CoopMemo.setTheState(S_TheState.Deleted);
			tgxy_CoopMemo.setUserUpdate(sm_User);
			tgxy_CoopMemo.setLastUpdateTimeStamp(System.currentTimeMillis());

			tgxy_CoopMemoDao.save(tgxy_CoopMemo);

			// 根据查询是否存在附件信息设置是否删除附件 (先使用 A 方案 后期使用B方案)
			// 设置附件model条件信息，进行附件条件查询
			Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(String.valueOf(tableId));
			sm_AttachmentForm.setBusiType("06110102");
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
