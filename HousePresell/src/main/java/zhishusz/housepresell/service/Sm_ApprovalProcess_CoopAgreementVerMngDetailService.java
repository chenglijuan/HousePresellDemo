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
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
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
 */
@Service
@Transactional
public class Sm_ApprovalProcess_CoopAgreementVerMngDetailService {
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_CoopAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();		

		Long Tgxy_TripleAgreementVerMngId = model.getTableId();
		Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng = (Tgxy_CoopAgreementVerMng) tgxy_CoopAgreementVerMngDao
				.findById(Tgxy_TripleAgreementVerMngId);
		if (tgxy_CoopAgreementVerMng == null)
		{
			return MyBackInfo.fail(properties, "该合作协议管理信息已失效，请刷新后重试");
		}

		// 查询合作协议相关附件信息，设置附件model条件信息，进行附件条件查询 (先使用 A 方案 后期使用B方案)
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(model.getTableId()));
		sm_AttachmentForm.setBusiType("06110202");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 加载所有相关附件信息
		List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

		if (null == sm_AttachmentList || sm_AttachmentList.size() == 0)
		{
			sm_AttachmentList = new ArrayList<Sm_Attachment>();
		}

		// 查询同一附件类型下的所有附件信息（附件信息归类）
		List<Sm_Attachment> smList = null;

		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setBusiType("06110202");
		form.setTheState(S_TheState.Normal);

		List<Sm_AttachmentCfg> smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));

		if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0)
		{
			smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}
		else
		{
			for (Sm_Attachment sm_Attachment : sm_AttachmentList)
			{
				String sourceType = sm_Attachment.getSourceType();

				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList)
				{
					if (sm_AttachmentCfg.geteCode().equals(sourceType))
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

		Long afId = model.getAfId();
		
		// 查询审批流信息
		Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_approvalProcess_afDao.findById(afId);
		if (sm_approvalProcess_af == null || S_TheState.Deleted.equals(sm_approvalProcess_af.getTheState()))
		{
			return MyBackInfo.fail(properties, "'申请单'不存在");
		}

		String busiCode = sm_approvalProcess_af.getBusiCode(); //业务编码

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgxy_CoopAgreementVerMng", tgxy_CoopAgreementVerMng);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put("busiCode",busiCode);

		return properties;
	}
}
