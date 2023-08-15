package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;

/**
 * Service修改操作：附件
 * 
 * @author ZS_XSZ
 * @date 2018-11-22 14:16:50
 *       修改审批过程中的附件材料
 */
@Service
@Transactional
public class Sm_AttachmentCommonUpdateService
{
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;

	@SuppressWarnings("unchecked")
	public Properties execute(BaseForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "未获取到相关操作人信息");
		}

		String busiCode = model.getBusiType();// 业务编码
		Long tableId = model.getPicTableId();// 单据主键

		if (null == busiCode || busiCode.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "业务编码不能为空");
		}

		if (null == tableId || tableId < 0)
		{
			return MyBackInfo.fail(properties, "所选单据不能为空");
		}

		// 判断是否有必传
		Sm_AttachmentCfgForm sm_AttachmentCfgForm = new Sm_AttachmentCfgForm();
		sm_AttachmentCfgForm.setBusiType(busiCode);
		sm_AttachmentCfgForm.setTheState(S_TheState.Normal);
		List<Sm_AttachmentCfg> sm_AttachmentCfgList = smAttachmentCfgDao
				.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), sm_AttachmentCfgForm));

		// 先判断是否有附件传递
		List<Sm_Attachment> attachmentList;
		String listPic = model.getSm_AttachmentList();
		if (null != listPic && !listPic.trim().isEmpty())
		{
			attachmentList = JSON.parseArray(listPic.toString(), Sm_Attachment.class);
		}
		else
		{
			attachmentList = new ArrayList<Sm_Attachment>();
		}

		if (null != sm_AttachmentCfgList && sm_AttachmentCfgList.size() > 0)
		{

			for (Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList)
			{
				// 根据业务判断是否有必传的附件配置
				if (sm_AttachmentCfg.getIsNeeded())
				{
					Boolean isExistAttachment = false;

					if (attachmentList.size() > 0)
					{

						for (Sm_Attachment sm_Attachment : attachmentList)
						{
							if (sm_AttachmentCfg.geteCode().equals(sm_Attachment.getSourceType()))
							{
								isExistAttachment = true;
								break;
							}
						}

					}

					if (!isExistAttachment)
					{
						return MyBackInfo.fail(properties, sm_AttachmentCfg.getTheName() + "未上传,此附件为必须上传附件");
					}

				}
			}
		}

		/*
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		String smAttachmentJson = null;
		// 查询原本附件信息
		Sm_AttachmentForm from = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tableId);
		from.setTheState(S_TheState.Normal);
		from.setBusiType(busiCode);
		from.setSourceId(sourceId);

		// 查询附件
		List<Sm_Attachment> smAttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), from));

		// 删除附件
		if (null != smAttachmentList && smAttachmentList.size() > 0)
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				sm_Attachment.setUserUpdate(user);// 操作人
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());// 操作时间
				sm_Attachment.setTheState(S_TheState.Deleted);
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		// 重新保存附件
		smAttachmentJson = model.getSm_AttachmentList().toString();
		if (null != smAttachmentJson && !smAttachmentJson.trim().isEmpty())
		{
			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);
			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);

				sm_Attachment.setSourceId(String.valueOf(tableId));
				sm_Attachment.setUserStart(user);// 创建人
				sm_Attachment.setUserUpdate(user);// 操作人
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_Attachment.setBusiType(busiCode);
				sm_AttachmentDao.save(sm_Attachment);
			}

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
