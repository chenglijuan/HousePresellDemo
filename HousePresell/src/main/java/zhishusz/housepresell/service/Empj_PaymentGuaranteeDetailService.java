package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情:支付保证撤销
 */
@Service
@Transactional
public class Empj_PaymentGuaranteeDetailService
{
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;
	
	private static final String BUSI_CODE = "06120403";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();
		//获取业务编号
		String busiCode = model.getBusiCode();
		
		List<Sm_Attachment> smAttachmentList = new ArrayList<Sm_Attachment>();
		
		List<Empj_PaymentGuaranteeChild> empj_PaymentGuaranteeChildList = new ArrayList<Empj_PaymentGuaranteeChild>();
		
		// 支付申请撤销基本信息
		Long empj_PaymentGuaranteeId = model.getTableId();
		Empj_PaymentGuarantee empj_PaymentGuarantee = (Empj_PaymentGuarantee) empj_PaymentGuaranteeDao
				.findById(empj_PaymentGuaranteeId);
		if (empj_PaymentGuarantee == null || S_TheState.Deleted.equals(empj_PaymentGuarantee.getTheState()))
		{
			return MyBackInfo.fail(properties, "支付申请撤销单据不存在");
		}
		
		Empj_PaymentGuaranteeChildForm empj_PaymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
		empj_PaymentGuaranteeChildForm.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
		empj_PaymentGuaranteeChildForm.setTheState(S_TheState.Normal);
		
		Integer childCount = empj_PaymentGuaranteeChildDao.findByPage_Size(empj_PaymentGuaranteeChildDao.getQuery_Size(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));
		
		if(childCount > 0)
		{
			empj_PaymentGuaranteeChildList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));
			
			for(Empj_PaymentGuaranteeChild empj_PaymentGuaranteeChild : empj_PaymentGuaranteeChildList)
			{
				Empj_BuildingInfo building = empj_PaymentGuaranteeChild.getEmpj_BuildingInfo();
				
				Tgpf_FundAppropriated_AFDtlForm tgpf_FundAppropriated_AFDtlForm = new Tgpf_FundAppropriated_AFDtlForm();
				tgpf_FundAppropriated_AFDtlForm.setBuilding(building);
				tgpf_FundAppropriated_AFDtlForm.setPayoutStatement("(1,2,3,4,5)");
				tgpf_FundAppropriated_AFDtlForm.setTheState(S_TheState.Normal);
							
				Integer payBuildingCount = tgpf_FundAppropriated_AFDtlDao.findByPage_Size(tgpf_FundAppropriated_AFDtlDao.getQuery_Size(tgpf_FundAppropriated_AFDtlDao.getSpecialHQL(), tgpf_FundAppropriated_AFDtlForm));

				if( payBuildingCount > 0 )
				{
					return MyBackInfo.fail(properties, "正在进行用款申请统筹的流程不允许进行支付保证撤销！");
				}	
			}								
		}
		else
		{
			empj_PaymentGuaranteeChildList = new ArrayList<Empj_PaymentGuaranteeChild>();
		}

		if( null == empj_PaymentGuarantee.getRevokeNo() ||  empj_PaymentGuarantee.getRevokeNo().length() < 1)
		{
			empj_PaymentGuarantee.setRevokeNo(sm_BusinessCodeGetService.execute(BUSI_CODE));
		}
		
		Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();

		String sourceId = String.valueOf(empj_PaymentGuaranteeId);
		attachmentForm.setBusiType(busiCode);
		attachmentForm.setSourceId(sourceId);
		attachmentForm.setTheState(S_TheState.Normal);
		
		// 查询附件
		smAttachmentList = smAttachmentDao
				.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

		if (null == smAttachmentList || smAttachmentList.size() == 0)
		{
			smAttachmentList = new ArrayList<Sm_Attachment>();
		}

		// 查询同一附件类型下的所有附件信息（附件信息归类）
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
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put("empj_PaymentGuarantee", empj_PaymentGuarantee);
		return properties;

	}
}
