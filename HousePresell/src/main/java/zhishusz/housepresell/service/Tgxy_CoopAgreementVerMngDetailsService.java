package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：合作协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementVerMngDetailsService
{
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	public Properties execute(Tgxy_CoopAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();
		List<Sm_Attachment> smAttachmentList = new ArrayList<Sm_Attachment>();
		Long tgxy_CoopAgreementVerMngId = model.getTableId();
		//获取业务编号
		String busiCode = model.getBusiCode();
		Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng = (Tgxy_CoopAgreementVerMng)tgxy_CoopAgreementVerMngDao.findById(tgxy_CoopAgreementVerMngId);
		if(tgxy_CoopAgreementVerMng == null || S_TheState.Deleted.equals(tgxy_CoopAgreementVerMng.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgxy_CoopAgreementVerMng(Id:" + tgxy_CoopAgreementVerMngId + ")'不存在");
		}
		Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tgxy_CoopAgreementVerMngId);
		attachmentForm.setBusiType(busiCode);
		attachmentForm.setSourceId(sourceId);
		attachmentForm.setTheState(S_TheState.Normal);
		
		// 查询附件
		smAttachmentList  = smAttachmentDao
				.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

		if (null == smAttachmentList || smAttachmentList.size() == 0)
		{
			smAttachmentList = new ArrayList<Sm_Attachment>();
		}

		// 查询附件类型
		List<Sm_Attachment> smList = null;
		Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
		attachmentCfgForm.setTheState(S_TheState.Normal);
		attachmentCfgForm.setBusiType(busiCode);
		List<Sm_AttachmentCfg> smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), attachmentCfgForm));
		if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0)
		{
			smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}
		else
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				Long cfgTableId = sm_Attachment.getAttachmentCfg().getTableId();

				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList)
				{
					if (sm_AttachmentCfg.getTableId() == cfgTableId)
					{
						smList = sm_AttachmentCfg.getSmAttachmentList();
						if (null == smList || smList.size() == 0)
						{
							smList = new ArrayList<Sm_Attachment>();
						}
						smList.add(sm_Attachment);
						sm_AttachmentCfg.setSmAttachmentList(smList);
					}
				}
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgxy_CoopAgreementVerMng", tgxy_CoopAgreementVerMng);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);

		return properties;
	}
}
