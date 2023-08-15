package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;

/*
 * Service更新操作：三方协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_TripleAgreementUpdateService
{
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;

	public Properties execute(Tgxy_TripleAgreementForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "请先登录");
		}

		/*
		 * xsz by time 2018-9-2 12:56:20
		 * 修改三方协议(大致流程同新增三方协议)
		 * 
		 * 1.三方协议附件信息修改
		 * 2.更新三方协议上传状态
		 *
		 */
		Long tableId = model.getTableId();// 主键
		if (null == tableId || tableId < 0)
		{
			return MyBackInfo.fail(properties, "请选择有效的三方协议");
		}	
		Tgxy_TripleAgreement findById = tgxy_TripleAgreementDao.findById(tableId);
		String printMethod = model.getPrintMethod();//打印方式
		if(null != printMethod && !printMethod.trim().isEmpty())
		{
			findById.setPrintMethod(printMethod);
			tgxy_TripleAgreementDao.save(findById);
		}
		
		/*
		 * xsz by time 2018-11-7 09:14:12
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		String smAttachmentJson = null;
		if (null != model.getSmAttachmentList())
		{
			// 查询原本附件信息
			Sm_AttachmentForm from = new Sm_AttachmentForm();

			String sourceId = String.valueOf(tableId);
			from.setTheState(S_TheState.Normal);
			from.setBusiType("06110301");
			from.setSourceId(sourceId);

			// 查询附件
			@SuppressWarnings("unchecked")
			List<Sm_Attachment> smAttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), from));
			// 删除附件
			if (null != smAttachmentList && smAttachmentList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : smAttachmentList)
				{
					sm_Attachment.setUserUpdate(user);
					sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
					sm_Attachment.setTheState(S_TheState.Deleted);
					sm_AttachmentDao.save(sm_Attachment);
				}
			}

			// 重新保存附件
			if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
			{
				smAttachmentJson = model.getSmAttachmentList().toString();

				List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);

				for (Sm_Attachment sm_Attachment : gasList)
				{
					// 查询附件配置表
					Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
					form.seteCode(sm_Attachment.getSourceType());
					Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
							.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

					sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);

					sm_Attachment.setUserStart(user);
					sm_Attachment.setUserUpdate(user);
					sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
					sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
					sm_Attachment.setSourceId(tableId.toString());// 关联Id
					sm_Attachment.setTheState(S_TheState.Normal);
					sm_AttachmentDao.save(sm_Attachment);
					
					//附件类型未三方协议的，更新三方协议状态为2：已上传
					
					if("06110301".equals(sm_Attachment.getBusiType())) {
						Tgxy_TripleAgreement tgxy_TripleAgreement = tgxy_TripleAgreementDao.findById(tableId);
						tgxy_TripleAgreement.setTheStateOfTripleAgreement("2");
						tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);						
					}
				}
			}
		}

		properties.put("tableId", tableId);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
