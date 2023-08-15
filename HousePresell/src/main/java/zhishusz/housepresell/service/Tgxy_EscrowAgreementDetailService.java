package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：托管合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_EscrowAgreementDetailService
{
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_ApprovalProcess_AFCommonDetailService sm_ApprovalProcess_AFservice;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_EscrowAgreementForm model)
	{
		Properties properties = new MyProperties();
		/*
		 * xsz by time 查看贷款托管合作协议详情
		 * 1.协议基本信息
		 * 2.协议附件信息
		 * 
		 */

		Long tgxy_EscrowAgreementId = model.getTableId();
		Tgxy_EscrowAgreement tgxy_EscrowAgreement = (Tgxy_EscrowAgreement) tgxy_EscrowAgreementDao
				.findById(tgxy_EscrowAgreementId);
		if (tgxy_EscrowAgreement == null)
		{
			return MyBackInfo.fail(properties, "该协议信息已失效，请刷新后重试");
		}

		// 查询合作协议相关附件信息，设置附件model条件信息，进行附件条件查询 (先使用 A 方案 后期使用B方案)
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(model.getTableId()));
		sm_AttachmentForm.setBusiType("06110201");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		String process_Workflow = "0";
		if(S_BusiState.HaveRecord.equals(tgxy_EscrowAgreement.getBusiState()))
		{
			process_Workflow = "1";
		}
		else
		{
			// 查询当前处于哪个审批节点
			process_Workflow = sm_ApprovalProcess_AFservice.getSm_ApprovalProcess_Workflow(tgxy_EscrowAgreementId,
					"06110201");
		}
		
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
		form.setBusiType("06110201");
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

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgxy_EscrowAgreement", tgxy_EscrowAgreement);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put("workflow", process_Workflow);

		return properties;
	}
}
