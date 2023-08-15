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
 * Service单个删除：合作备忘录
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_CoopMemoDeleteService
{
	@Autowired
	private Tgxy_CoopMemoDao tgxy_CoopMemoDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_UserDao sm_UserDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_CoopMemoForm model)
	{
		Properties properties = new MyProperties();

		// 获取当前操作人信息
		Long userUpdateId = model.getUserId();
		if (userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}

		Sm_User userUpdate = (Sm_User) sm_UserDao.findById(userUpdateId);
		if (userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'修改人(Id:" + userUpdateId + ")'不存在");
		}

		// 先根据传递的id进行查询，再进行删除操作
		Long tgxy_CoopMemoId = model.getTableId();
		Tgxy_CoopMemo tgxy_CoopMemo = (Tgxy_CoopMemo) tgxy_CoopMemoDao.findById(tgxy_CoopMemoId);
		if (tgxy_CoopMemo == null)
		{
			return MyBackInfo.fail(properties, "'Tgxy_CoopMemo(Id:" + tgxy_CoopMemoId + ")'不存在");
		}

		// 状态设置为删除状态 1,同时更新最后操作时间
		tgxy_CoopMemo.setTheState(S_TheState.Deleted);
		tgxy_CoopMemo.setUserUpdate(userUpdate);
		tgxy_CoopMemo.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgxy_CoopMemoDao.save(tgxy_CoopMemo);

		// 根据查询是否存在附件信息设置是否删除附件 (先使用 A 方案 后期使用B方案)
		// 设置附件model条件信息，进行附件条件查询
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(model.getTableId()));
		sm_AttachmentForm.setBusiType("Tgxy_CoopMemo");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 加载所有相关附件信息,for循环执行删除
		List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

		if (null != sm_AttachmentList && sm_AttachmentList.size() > 0)
		{

			for (Sm_Attachment sm_Attachment : sm_AttachmentList)
			{
				sm_Attachment.setTheState(S_TheState.Deleted);
				sm_Attachment.setUserUpdate(userUpdate);// 操作人
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());// 操作时间
				sm_AttachmentDao.save(sm_Attachment);
			}

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
