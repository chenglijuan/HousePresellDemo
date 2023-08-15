package zhishusz.housepresell.approvalprocess;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Emmp_OrgMemberDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.service.Emmp_OrgMemberAddService;
import zhishusz.housepresell.service.Sm_AttachmentBatchAddService;
import zhishusz.housepresell.service.Sm_BusiState_LogAddService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.convert.OrgMemberConverter;
import zhishusz.housepresell.util.objectdiffer.model.Emmp_CompanyInfoTemplate;
import zhishusz.housepresell.util.project.CompanyLogUitl;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 开发企业变更审批：
 * 最后一个结点审核通过，更新数据到数据库中
 */
@Transactional
public class ApprovalProcessCallBack_020109 implements IApprovalProcessCallback
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_companyInfoDao;
	@Autowired
	private Gson gson;
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	//机构成员
	@Autowired
	private Emmp_OrgMemberAddService emmp_OrgMemberAddService;
	@Autowired
	private Emmp_OrgMemberDao emmp_OrgMemberDao;
	@Autowired
	private CompanyLogUitl companyLogUitl;
	@Autowired
	private Sm_BusiState_LogAddService logAddService;

	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm model)
	{
		Properties properties = new Properties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的开发企业
			Long developConpamyId = sm_ApprovalProcess_AF.getSourceId();
			if(developConpamyId == null || developConpamyId < 1)
			{
				return MyBackInfo.fail(properties, "审批的代理机构不存在");
			}
			Emmp_CompanyInfo emmp_companyInfo =  emmp_companyInfoDao.findById(developConpamyId);
			Emmp_OrgMemberForm theModel = new Emmp_OrgMemberForm();
			theModel.setCompanyId(emmp_companyInfo.getTableId());
			theModel.setTheState(S_TheState.Normal);
			List<Emmp_OrgMember> emmp_OrgMemberList = emmp_OrgMemberDao.findByPage(emmp_OrgMemberDao.getQuery(emmp_OrgMemberDao.getBasicHQL(), theModel), null, null);
			Emmp_CompanyInfoTemplate companyOldTemplate = companyLogUitl.getCopyTemplate(emmp_companyInfo, emmp_OrgMemberList, S_CompanyType.Cooperation);

			if(emmp_companyInfo == null)
			{
				return MyBackInfo.fail(properties, "审批的代理机构不存在");
			}
			switch (approvalProcessWork)
			{
			case "020109001_ZS":
				if(S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();

					if (jsonStr != null && jsonStr.length() > 0)
					{
						Emmp_CompanyInfoForm emmp_companyInfoForm = gson.fromJson(jsonStr, Emmp_CompanyInfoForm.class);
						emmp_companyInfo.setTheName(emmp_companyInfoForm.getTheName());
						emmp_companyInfo.setAddress(emmp_companyInfoForm.getAddress());
						emmp_companyInfo.setLegalPerson(emmp_companyInfoForm.getLegalPerson());
						emmp_companyInfo.setProjectLeader(emmp_companyInfoForm.getProjectLeader());

						if (emmp_companyInfoForm.getRegisteredDateStr() != null && emmp_companyInfoForm.getRegisteredDateStr().length() > 0)
						{
							emmp_companyInfo.setRegisteredDate(MyDatetime.getInstance().stringToLong(emmp_companyInfoForm.getRegisteredDateStr()));
						}

						emmp_companyInfo.setContactPerson(emmp_companyInfoForm.getContactPerson());
						emmp_companyInfo.setContactPhone(emmp_companyInfoForm.getContactPhone());
						emmp_companyInfo.setBusiState("已备案");
						emmp_companyInfo.setApprovalState("已完结");

						emmp_companyInfo.setUserRecord(approvalProcessWorkflow.getUserUpdate());
						emmp_companyInfo.setRecordTimeStamp(System.currentTimeMillis());

						//更新成员信息
						saveOrgmemberList(emmp_companyInfoForm, emmp_companyInfo.getTableId());
						ArrayList<Emmp_OrgMember> emmp_orgMemberListNew = OrgMemberConverter.orgMemberFormList2OrgMemberList(emmp_companyInfoForm.getOrgMemberList());
						Emmp_CompanyInfoTemplate companyNewTemplate = companyLogUitl.getCopyTemplate(emmp_companyInfo, emmp_orgMemberListNew, S_CompanyType.Cooperation);
						emmp_companyInfoForm.setUser(sm_ApprovalProcess_AF.getUserStart());
						logAddService.addLog(emmp_companyInfoForm,emmp_companyInfo.getTableId(),companyOldTemplate,companyNewTemplate);

						//更新附件信息
						emmp_companyInfoForm.setBusiType("020108");
						emmp_companyInfoForm.setUserId(sm_ApprovalProcess_AF.getUserStart().getTableId());
						sm_AttachmentBatchAddService.execute(emmp_companyInfoForm, developConpamyId);

						emmp_companyInfoDao.save(emmp_companyInfo);
					}
				}break;
			default:
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
			}
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
			
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
			
		}
		return properties;
	}

	public void saveOrgmemberList(Emmp_CompanyInfoForm model, Long tableId)
	{
		Emmp_OrgMemberForm theModel = new Emmp_OrgMemberForm();
		theModel.setCompanyId(tableId);
		theModel.setTheState(S_TheState.Normal);

		List<Emmp_OrgMember> emmp_OrgMemberList = emmp_OrgMemberDao.findByPage(emmp_OrgMemberDao.getQuery(emmp_OrgMemberDao.getBasicHQL(), theModel), null, null);
		for (Emmp_OrgMember emmp_OrgMember : emmp_OrgMemberList)
		{
			emmp_OrgMember.setTheState(S_TheState.Deleted);
			emmp_OrgMemberDao.save(emmp_OrgMember);
		}

		//去除已删除的机构成员
		Emmp_OrgMemberForm[] orgMemberList = Arrays.stream(model.getOrgMemberList()).filter(emmp_orgMemberForm->
				!"del".equals(emmp_orgMemberForm.getChangeState())
		).toArray(Emmp_OrgMemberForm[]::new);

		if (orgMemberList != null && orgMemberList.length > 0)
		{
			for (Emmp_OrgMemberForm orgMemberForm : orgMemberList)
			{
				orgMemberForm.setCompanyId(tableId);
				emmp_OrgMemberAddService.execute(orgMemberForm);
			}
		}
		model.setOrgMemberList(orgMemberList);

	}
}
