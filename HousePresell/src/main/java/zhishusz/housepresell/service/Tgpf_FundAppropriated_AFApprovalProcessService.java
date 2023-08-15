package zhishusz.housepresell.service;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.util.MQConnectionUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：用款申请-提交
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_FundAppropriated_AFApprovalProcessService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private MQConnectionUtil mqConnectionUtil;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_buildingAccountDao;//楼幢账户
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;//受限额度变更

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundAppropriated_AFForm model)
	{

		Properties properties = new MyProperties();

		//构建提交按钮
		String buttonType = S_ButtonType.Submit;
		String busiCode = S_BusiCode.busiCode7; //业务编码

		model.setButtonType(buttonType);
		model.setBusiCode(busiCode);

		//获取主键
		Long tableId = model.getTableId();
		if(null == tableId || tableId <= 0)
		{
			return MyBackInfo.fail(properties, "请选择提交单据");
		}

		Long loginUserId = model.getUserId();
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

		if(userStart.getCompany() == null)
		{
			return MyBackInfo.fail(properties, "'用户所属企业'不能为空");
		}

		//查询单据
		Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = tgpf_FundAppropriated_AFDao.findById(tableId);

		List<Tgpf_FundAppropriated_AFDtl> tgpf_FundAppropriated_AFDtls = tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList();

		if(properties.getProperty("info").equals("noApproval"))
		{
			//不需要走审批流程 变更字段直接保存到数据库中
			tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Admissible);	  //申请单状态：已受理
			tgpf_FundAppropriated_AF.setApprovalState(S_ApprovalState.Completed); //审批状态：已完结
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

			/**
			 * xsz by time 2019-10-8 10:07:26
			 * 校验申请金额与实际金额是否相等
			 */
			Double totalApplyAmount_af = tgpf_FundAppropriated_AF.getTotalApplyAmount();

			Double totalApplyAmount_afdtl = 0.00;
			Double totalApplyAmount_plan = 0.00;


			List<Tgpf_FundAppropriated_AFDtl> fundAppropriated_AFDtlList = tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList();
			for (Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl : fundAppropriated_AFDtlList) {
				totalApplyAmount_afdtl += null == tgpf_FundAppropriated_AFDtl.getAppliedAmount()?0.00:tgpf_FundAppropriated_AFDtl.getAppliedAmount();

				if(!tgpf_FundAppropriated_AF.getProject().getTableId().equals(tgpf_FundAppropriated_AFDtl.getBuilding().getProject().getTableId())){
					return MyBackInfo.fail(properties, "楼幢关联项目不正确，请修改后重新提交！");
				}

			}

			List<Tgpf_FundOverallPlanDetail> fundOverallPlanDetailList = tgpf_FundAppropriated_AF.getFundOverallPlanDetailList();
			for (Tgpf_FundOverallPlanDetail tgpf_FundOverallPlanDetail : fundOverallPlanDetailList) {
				totalApplyAmount_plan += tgpf_FundOverallPlanDetail.getAppliedAmount();
			}

			if(Math.abs(totalApplyAmount_af - totalApplyAmount_afdtl) >= 1.00)
			{
				return MyBackInfo.fail(properties, "本次申请总金额与实际金额不相等，请修改保存后重新提交！");
			}

			if(Math.abs(totalApplyAmount_af - totalApplyAmount_plan) >= 1.00)
			{
				return MyBackInfo.fail(properties, "本次申请总金额与实际金额不相等，请修改保存后重新提交！");
			}

			/**
			 * xsz by time 2019-7-15 10:07:43
			 * 提交前校验楼幢是否发起节点变更业务
			 */
			/*List<Tgpf_FundAppropriated_AFDtl> fundAppropriated_AFDtlList = tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList();
			if(!fundAppropriated_AFDtlList.isEmpty())
			{
				for (Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl : fundAppropriated_AFDtlList) {

					Empj_BuildingInfo building = tgpf_FundAppropriated_AFDtl.getBuilding();

					Empj_BldLimitAmount_AFForm AFModel = new Empj_BldLimitAmount_AFForm();
					AFModel.setTheState(S_TheState.Normal);
					AFModel.setBuilding(building);
					AFModel.setBuildingId(building.getTableId());
					List<Empj_BldLimitAmount_AF> afList = new ArrayList<>();
					afList = empj_BldLimitAmount_AFDao.findByPage(empj_BldLimitAmount_AFDao.getQuery(empj_BldLimitAmount_AFDao.getListHQL(), AFModel));

					if(!afList.isEmpty())
					{
						Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = afList.get(0);
						if(!S_ApprovalState.Completed.equals(empj_BldLimitAmount_AF.getApprovalState()))
						{
							return MyBackInfo.fail(properties, "该楼幢已发起节点变更流程，请待流程结束后重新提交！");
						}
					}

				}
			}*/

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			tgpf_FundAppropriated_AF.setApplyDate(sdf.format(new Date()));
			tgpf_FundAppropriated_AF.setUserUpdate(userStart);
			tgpf_FundAppropriated_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Application); // 申请单状态： 申请中

			/**
			 * xsz by time 2019-7-6 11:41:23
			 * 更换更新方式为调用存储过程更新
			 * ==========start================
			 */
			/*for(Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl : tgpf_FundAppropriated_AFDtls)
			{

				Empj_BuildingInfo building = tgpf_FundAppropriated_AFDtl.getBuilding();
				Tgpj_BuildingAccount tgpj_buildingAccount  = building.getBuildingAccount();

				Double appliedNoPayoutAmount = tgpj_buildingAccount.getAppliedNoPayoutAmount();// 已申请未拨付金额
				Double appropriateFrozenAmount = tgpj_buildingAccount.getAppropriateFrozenAmount(); //拨付冻结金额

				if(appliedNoPayoutAmount == null)
				{
					appliedNoPayoutAmount = 0.0;
				}
				if(appropriateFrozenAmount == null)
				{
					appropriateFrozenAmount = 0.0;
				}

				//本次划款申请金额
				Double appliedAmount = null == tgpf_FundAppropriated_AFDtl.getAppliedAmount()?0.00:tgpf_FundAppropriated_AFDtl.getAppliedAmount();
				// 当前可划拨金额
				Double allocableAmount = null == tgpj_buildingAccount.getAllocableAmount()?0.00:tgpj_buildingAccount.getAllocableAmount();

				allocableAmount -=appliedAmount; //减少可划拨金额
				appliedNoPayoutAmount += appliedAmount;//增加已申请未拨付金额
				appropriateFrozenAmount += appliedAmount;//增加拨付冻结金额

				tgpj_buildingAccount.setAllocableAmount(allocableAmount);//减少可划拨金额
				tgpj_buildingAccount.setAppliedNoPayoutAmount(appliedNoPayoutAmount);// 增加已申请未拨付金额
				tgpj_buildingAccount.setAppropriateFrozenAmount(appropriateFrozenAmount);//增加拨付冻结金额

				tgpj_buildingAccountDao.save(tgpj_buildingAccount);
			}*/

			Map<String, Object> map = tgpf_FundAppropriated_AFDao.update_FundAppropriated_Submit(tableId);
			if(null != map.get("sign")&&"fail".equals((String)map.get("sign")))
			{
				return MyBackInfo.fail(properties, (String)map.get("info"));
			}

			/**
			 * xsz by time 2019-7-6 11:41:23
			 * 更换更新方式为调用存储过程更新
			 * ==========end================
			 */

			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

			tgpf_FundAppropriated_AFDao.save(tgpf_FundAppropriated_AF);

			//审批操作
			sm_approvalProcessService.execute(tgpf_FundAppropriated_AF, model, sm_approvalProcess_cfg);
		}

		properties.put("tableId", tgpf_FundAppropriated_AF.getTableId());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

}
