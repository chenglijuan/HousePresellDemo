package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情:支付保证撤销 - 审批流
 */
@Service
@Transactional
public class Sm_ApprovalProcess_CancelPayDetailService
{
	@Autowired
	private  Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();
		
		Long empj_PaymentGuaranteeId = model.getTableId();
		
		Long afId = model.getAfId();
		
		Empj_PaymentGuarantee empj_PaymentGuarantee = (Empj_PaymentGuarantee)empj_PaymentGuaranteeDao.findById(empj_PaymentGuaranteeId);
		if(empj_PaymentGuarantee == null || S_TheState.Deleted.equals(empj_PaymentGuarantee.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_PaymentGuarantee(Id:"+empj_PaymentGuaranteeId+")'不存在");
		}
		
		Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();
		
		String sourceId = String.valueOf(empj_PaymentGuaranteeId);
		attachmentForm.setBusiType(model.getBusiCode());
		attachmentForm.setSourceId(sourceId);
		attachmentForm.setTheState(S_TheState.Normal);
		
		//查询附件
		List<Sm_Attachment> smAttachmentList = smAttachmentDao
				.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

		if (null == smAttachmentList || smAttachmentList.size() == 0)
		{
			smAttachmentList = new ArrayList<Sm_Attachment>();
		}
		
		//查询附件类型
		List<Sm_Attachment> smList = null;
		Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
		attachmentCfgForm.setTheState(0);
		attachmentCfgForm.setBusiType(model.getBusiCode());
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
		
		//查询审批流信息
		Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_approvalProcess_afDao.findById(afId);
		if(sm_approvalProcess_af == null || S_TheState.Deleted.equals(sm_approvalProcess_af.getTheState()))
		{
			return MyBackInfo.fail(properties, "'申请单'不存在");
		}
		
		String busiCode = sm_approvalProcess_af.getBusiCode();
				
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_PaymentGuarantee", empj_PaymentGuarantee);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put("busiCode",busiCode);
		return properties;
		
	}
}

















