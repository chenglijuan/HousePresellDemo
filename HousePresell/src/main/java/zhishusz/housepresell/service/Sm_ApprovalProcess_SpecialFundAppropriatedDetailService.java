package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：特殊拨付-申请主表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Sm_ApprovalProcess_SpecialFundAppropriatedDetailService
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;// 审批节点信息
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;//用户角色
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;// 附件类型
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_SpecialFundAppropriated_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		if(null == user || user.getTableId() < 1)
		{
			return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");
		}

		Long tgpf_SpecialFundAppropriated_AFId = model.getTableId();
		if (null == tgpf_SpecialFundAppropriated_AFId || tgpf_SpecialFundAppropriated_AFId < 0)
		{
			return MyBackInfo.fail(properties, "请选择有效的申请单信息");
		}

		Tgpf_SpecialFundAppropriated_AF tgpf_SpecialFundAppropriated_AF = (Tgpf_SpecialFundAppropriated_AF) tgpf_SpecialFundAppropriated_AFDao
				.findById(tgpf_SpecialFundAppropriated_AFId);
		if (tgpf_SpecialFundAppropriated_AF == null
				|| S_TheState.Deleted.equals(tgpf_SpecialFundAppropriated_AF.getTheState()))
		{
			return MyBackInfo.fail(properties, "未查询到有效信息，请刷新后重试");
		}

		Long afId = model.getAfId();

		// 查询审批流信息
		Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_approvalProcess_afDao.findById(afId);
		if (sm_approvalProcess_af == null || S_TheState.Deleted.equals(sm_approvalProcess_af.getTheState()))
		{
			return MyBackInfo.fail(properties, "'申请单'不存在");
		}

		// 查询当前节点审批单信息
		Long currentWworkflowId = model.getCurrentWworkflowId();
		Sm_ApprovalProcess_Workflow workflow = sm_ApprovalProcess_WorkflowDao.findById(currentWworkflowId);
		if (null != workflow)
		{
			properties.put("workflowState", workflow.getBusiState());
		}
		else
		{
			properties.put("workflowState", "通过");
		}

		String busiCode = sm_approvalProcess_af.getBusiCode();
		
		/*
		 * xsz by time 2018-12-21 15:13:20
		 * 获取审批配置信息，用于区分角色
		 * 限制开发企业发起人不能看到拨付信息
		 */
		//根据登录用户查询角色信息
		Sm_Permission_RoleUserForm roleUserModel = new Sm_Permission_RoleUserForm();
		roleUserModel.setTheState(S_TheState.Normal);
		roleUserModel.setSm_UserId(user.getTableId());
		List<Sm_Permission_RoleUser> roleUserList = new ArrayList<>();
		roleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), roleUserModel));
		if(null==roleUserList||roleUserList.size()==0)
		{
			return MyBackInfo.fail(properties, "未查询到登录用户所属角色信息！");
		}
		
		Sm_ApprovalProcess_Cfg process_Cfg = sm_approvalProcess_af.getConfiguration();
		List<Sm_ApprovalProcess_Node> nodeList = process_Cfg.getNodeList();
		
		boolean isApprover = false;
		if(null != nodeList && nodeList.size()>0)
		{
			//遍历登录用户是否是审批角色
			for (Sm_ApprovalProcess_Node node : nodeList) {
				
				//获取第一个节点信息，与当前登录角色匹配，则不给它查看划拨信息
				if(null == node.getLastNode())
				{
					for (Sm_Permission_RoleUser roleUser : roleUserList) {
						//如果有角色匹配
						if(roleUser.getSm_Permission_Role().getTableId() == node.getRole().getTableId())
						{
							isApprover = true;
							break;
						}
						
					}
					
				}
				
				if(isApprover)
				{
					break;
				}
				
			}
		}
		
		/*
		 * xsz by time 2018-12-3 08:50:20
		 * 特殊拨付无附件信息
		 * ===================start===========================
		 */
		
		// 查询合作协议相关附件信息，设置附件model条件信息，进行附件条件查询 (先使用 A 方案 后期使用B方案)
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(model.getTableId()));
		sm_AttachmentForm.setBusiType("06120603");
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
		form.setBusiType("06120603");
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
		
		properties.put("isApprover", isApprover);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_SpecialFundAppropriated_AF", tgpf_SpecialFundAppropriated_AF);
		properties.put("busiCode", busiCode);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);

		return properties;
	}
}
