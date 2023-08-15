package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tg_RiskLogInfoForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tg_RiskLogInfoDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_RiskLogInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：风险日志管理
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_RiskLogInfoBatchDeleteService
{
	// 项目风险日志
	private static final String BUSI_CODE = "21020303";// 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	@Autowired
	private Tg_RiskLogInfoDao tg_RiskLogInfoDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件

	public Properties execute(Tg_RiskLogInfoForm model)
	{
		Properties properties = new MyProperties();

		// 获取操作人
		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}

		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for (Long tableId : idArr)
		{
			Tg_RiskLogInfo tg_RiskLogInfo = (Tg_RiskLogInfo) tg_RiskLogInfoDao.findById(tableId);
			if (tg_RiskLogInfo == null)
			{
				return MyBackInfo.fail(properties, "'风险日志(Id:" + tableId + ")'不存在，请刷新后重试");
			}

			tg_RiskLogInfo.setUserUpdate(user);
			tg_RiskLogInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
			tg_RiskLogInfo.setTheState(S_TheState.Deleted);
			tg_RiskLogInfoDao.save(tg_RiskLogInfo);

			// 设置附件model条件信息，进行附件条件查询
			Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(String.valueOf(tableId));
			sm_AttachmentForm.setBusiType(BUSI_CODE);
			sm_AttachmentForm.setTheState(S_TheState.Normal);

			// 加载所有相关附件信息,for循环执行删除
			@SuppressWarnings("unchecked")
			List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

			if (null != sm_AttachmentList && sm_AttachmentList.size() > 0)
			{

				for (Sm_Attachment sm_Attachment : sm_AttachmentList)
				{
					sm_Attachment.setUserUpdate(user);
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
