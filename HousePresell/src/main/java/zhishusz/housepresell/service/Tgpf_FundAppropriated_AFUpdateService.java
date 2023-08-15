package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_FundOverPlanDetaillForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.util.MQConnectionUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：申请-用款-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriated_AFUpdateService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDtlAddService tgpf_FundAppropriated_AFDtlAddService;
	@Autowired
	private Tgpf_FundOverPlanDetailAddService tgpf_fundOverPlanDetailAddService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired 
	private MQConnectionUtil mqConnectionUtil;
	
	public Properties execute(Tgpf_FundAppropriated_AFForm model)
	{
		Properties properties = new MyProperties();

		String busiCode = S_BusiCode.busiCode7; //业务编码
		Long loginUserId = model.getUserId();
		String buttonType = model.getButtonType();
		Long projectId = model.getProjectId();
		String applyDate = model.getApplyDate();
		Double totalApplyAmount = model.getTotalApplyAmount(); //本次申请总金额
		Long tableId  = model.getTableId(); //用款申请主表id
		Boolean isNeedApproval;

		if (loginUserId == null || loginUserId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}
		Sm_User userStart = model.getUser();
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}

		// 发起人校验
		//1 如果该业务没有配置审批流程，直接保存
		//2 如果该业务配置了审批流程 ，判断用户能否与对应模块下的审批流程的发起人角色匹配
		properties = sm_ApprovalProcessGetService.execute(busiCode, loginUserId);
		if(! properties.getProperty("info").equals("noApproval") && properties.getProperty("result").equals("fail"))
		{
			return properties;
		}
		else
		{
			isNeedApproval = true;
		}

		if (tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "'用款基本信息'不能为空");
		}
		if (projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "'项目'不能为空");
		}

		Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = tgpf_FundAppropriated_AFDao.findById(tableId);
		if (tgpf_FundAppropriated_AF == null)
		{
			return MyBackInfo.fail(properties, "'用款基本信息'不能为空");
		}

		Empj_ProjectInfo project = (Empj_ProjectInfo) empj_ProjectInfoDao.findById(projectId);
		if (project == null)
		{
			return MyBackInfo.fail(properties, "'项目'不能为空");
		}

		//用款申请明细
		/**
		 * 1.先修改删除的明细信息 状态为1（删除），再解除主表和删除的明细表之间的关联关系
		 */
		if(tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList() != null && tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList().size()>0)
		{
			for(Tgpf_FundAppropriated_AFDtl tgpf_fundAppropriated_afDtl :  tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList())
			{
				tgpf_fundAppropriated_afDtl.setTheState(S_TheState.Deleted);
			}
			tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList().clear();
		}

		/**
		 * 2.重新新增明细表信息，并且设置主表与明细表之间一对多关联关系
		 */
		List<Tgpf_FundAppropriated_AFDtl> tgpf_FundAppropriated_AFDtls = new ArrayList<Tgpf_FundAppropriated_AFDtl>();

		if(model.getTgpf_FundAppropriated_AFAddtab() == null || model.getTgpf_FundAppropriated_AFAddtab().length == 0)
		{
			return MyBackInfo.fail(properties, "请选择'用款申请楼幢信息'");
		}
		for (Tgpf_FundAppropriated_AFDtlForm tgpf_FundAppropriated_AFDtlForm : model.getTgpf_FundAppropriated_AFAddtab())
		{
			Properties propertiesSaveDetail = tgpf_FundAppropriated_AFDtlAddService.execute(tgpf_FundAppropriated_AFDtlForm,buttonType,userStart,applyDate,isNeedApproval);
			if (S_NormalFlag.success.equals(propertiesSaveDetail.getProperty(S_NormalFlag.result)))
			{
				tgpf_FundAppropriated_AFDtls.add((Tgpf_FundAppropriated_AFDtl) propertiesSaveDetail.get("tgpf_FundAppropriated_AFDtl"));
			}
			else
			{
				return propertiesSaveDetail;
			}
		}

		//用款申请汇总信息
		/**
		 * 3.先修改删除的汇总信息 状态为1（删除），再解除主表和删除的用款申请汇总表之间的关联关系
		 */
		if(tgpf_FundAppropriated_AF.getFundOverallPlanDetailList()!=null && tgpf_FundAppropriated_AF.getFundOverallPlanDetailList().size() > 0)
		{
			for(Tgpf_FundOverallPlanDetail tgpf_fundOverallPlanDetail : tgpf_FundAppropriated_AF.getFundOverallPlanDetailList())
			{
				tgpf_fundOverallPlanDetail.setTheState(S_TheState.Deleted);
			}
			tgpf_FundAppropriated_AF.getFundOverallPlanDetailList().clear();
		}

		/**
		 * 4.重新新增明细表信息，并且设置主表与用款申请汇总表之间一对多关联关系
		 */
		List<Tgpf_FundOverallPlanDetail> tgpf_FundOverallPlanDetails = new ArrayList<Tgpf_FundOverallPlanDetail>();
		if(model.getTgpf_FundOverPlanDetailltab() == null || model.getTgpf_FundOverPlanDetailltab().length == 0)
		{
			return MyBackInfo.fail(properties, "'用款申请汇总信息'不能为空");
		}
		for (Tgpf_FundOverPlanDetaillForm tgpf_FundOverallPlanDetailForm : model.getTgpf_FundOverPlanDetailltab())
		{
			Properties propertiesSaveDetail = tgpf_fundOverPlanDetailAddService.execute(tgpf_FundOverallPlanDetailForm);
			if (S_NormalFlag.success.equals(propertiesSaveDetail.getProperty(S_NormalFlag.result)))
			{
				tgpf_FundOverallPlanDetails.add((Tgpf_FundOverallPlanDetail) propertiesSaveDetail.get("tgpf_FundOverallPlanDetail"));
			}
			else
			{
				return propertiesSaveDetail;
			}
		}

		//用款申请主表
		tgpf_FundAppropriated_AF.setTotalApplyAmount(totalApplyAmount);
		tgpf_FundAppropriated_AF.setUserUpdate(userStart);
		tgpf_FundAppropriated_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpf_FundAppropriated_AF.setFundAppropriated_AFDtlList(tgpf_FundAppropriated_AFDtls);
		tgpf_FundAppropriated_AF.setFundOverallPlanDetailList(tgpf_FundOverallPlanDetails);

		//判断是否配置了审批流程
		if(properties.getProperty("info").equals("noApproval"))
		{
			//不需要走审批流程 变更字段直接保存到数据库中
			tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Admissible);	      // 申请单状态：已受理
			tgpf_FundAppropriated_AF.setApprovalState(S_ApprovalState.Completed); 	  // 审批状态 ： 已完结
			tgpf_FundAppropriated_AFDao.save(tgpf_FundAppropriated_AF);
			
			for(Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl : tgpf_FundAppropriated_AFDtls)
			{
				//反写到Tgpf_RetainedRightsView拨付留存权益
				Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
				tgpf_BuildingRemainRightLogForm.setBuildingId(tgpf_FundAppropriated_AFDtl.getBuilding().getTableId());
				tgpf_BuildingRemainRightLogForm.setSrcBusiType("业务触发");
				tgpf_BuildingRemainRightLogForm.setBillTimeStamp(MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));

				//生成楼幢留存权益，同时生成每个楼幢下的户留存权益
				mqConnectionUtil.sendMessage(MQKey_EventType.APPROPRIATION_RETAINED, MQKey_OrgType.SYSTEM, tgpf_BuildingRemainRightLogForm);
			}
		}
		else
		{
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

			tgpf_FundAppropriated_AFDao.save(tgpf_FundAppropriated_AF);
			//审批操作
			sm_approvalProcessService.execute(tgpf_FundAppropriated_AF, model, sm_approvalProcess_cfg);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", tableId);

		return properties;
	}
}
