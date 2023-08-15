package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：支付保证 审批流
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Sm_ApprovalProcess_PaymentGuaranteeApplyService
{

	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;
	
	MyDouble myDouble = MyDouble.getInstance();
	
	MyDatetime myDatetime = MyDatetime.getInstance();


	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();

		Long empj_PaymentGuaranteetableId = model.getTableId();
		
		Long afId = model.getAfId();
		
		String applyDate = myDatetime.dateToSimpleString(System.currentTimeMillis());
		
		// 初始化输出参数
		Empj_PaymentGuarantee empj_PaymentGuarantee = new Empj_PaymentGuarantee();
		List<Empj_PaymentGuaranteeChild> empj_PaymentGuaranteeChildList = new ArrayList<Empj_PaymentGuaranteeChild>();
		List<Sm_Attachment> smAttachmentList = new ArrayList<Sm_Attachment>();
		
		if( empj_PaymentGuaranteetableId == null || empj_PaymentGuaranteetableId < 1)
		{
			
		}
		else
		{
			empj_PaymentGuarantee = (Empj_PaymentGuarantee)empj_PaymentGuaranteeDao.findById(empj_PaymentGuaranteetableId);
			if(empj_PaymentGuarantee == null || S_TheState.Deleted.equals(empj_PaymentGuarantee.getTheState()))
			{
				return MyBackInfo.fail(properties, "保证申请有误！");
			}
			
			Empj_PaymentGuaranteeChildForm empj_PaymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
			empj_PaymentGuaranteeChildForm.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
			empj_PaymentGuaranteeChildForm.setTheState(S_TheState.Normal);
			
			Integer childCount = empj_PaymentGuaranteeChildDao.findByPage_Size(empj_PaymentGuaranteeChildDao.getQuery_Size(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));

			Map<String,String> buildingId = new HashMap<String,String>();
			
			if(childCount > 0)
			{
				empj_PaymentGuaranteeChildList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));							
			}
			else
			{
				empj_PaymentGuaranteeChildList = new ArrayList<Empj_PaymentGuaranteeChild>();
			}
			
			Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();

			String sourceId = String.valueOf(empj_PaymentGuaranteetableId);
			attachmentForm.setBusiType(model.getBusiCode());
			attachmentForm.setSourceId(sourceId);
			attachmentForm.setTheState(S_TheState.Normal);
			
			// 查询附件
			smAttachmentList = smAttachmentDao
					.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

			if (null == smAttachmentList || smAttachmentList.size() == 0)
			{
				smAttachmentList = new ArrayList<Sm_Attachment>();
			}
	
		}	
		
		// 查询附件类型
		List<Sm_Attachment> smList = null;
		Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
		attachmentCfgForm.setTheState(S_TheState.Normal);
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
		properties.put("empj_PaymentGuaranteeChildList", empj_PaymentGuaranteeChildList);
		properties.put("empj_PaymentGuarantee", empj_PaymentGuarantee);	
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put("busiCode",busiCode);
		return properties;
	}
}
