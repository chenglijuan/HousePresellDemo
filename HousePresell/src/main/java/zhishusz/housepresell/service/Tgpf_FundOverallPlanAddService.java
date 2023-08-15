package zhishusz.housepresell.service;

import java.util.*;

import javax.transaction.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.*;
import zhishusz.housepresell.util.MyDatetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service添加操作：资金统筹
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundOverallPlanAddService
{
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Tgpf_OverallPlanAccoutDao tgpf_overallPlanAccoutDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;

	private MyDatetime myDatetime = MyDatetime.getInstance();

	public Properties execute(Tgpf_FundOverallPlanForm model)
	{
		Properties properties = new MyProperties();

		Long userStartId = model.getUserId();
		String busiCode = S_BusiCode.busiCode8;

		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}

		Sm_User loginUser = model.getUser();
		if(loginUser == null)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}

		// 发起人校验
		//1 如果该业务没有配置审批流程，直接保存
		//2 如果该业务配置了审批流程 ，判断用户能否与对应模块下的审批流程的发起人角色匹配
		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		if(! properties.getProperty("info").equals("noApproval") && properties.getProperty("result").equals("fail"))
		{
			return properties;
		}

		Long[] idArr = model.getIdArr();
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要统筹的用款申请");
		}

		List<Tgpf_FundAppropriated_AF>  tgpf_FundAppropriated_AFList  = new ArrayList<>();
		List<Tgpf_FundOverallPlanDetail>  tgpf_FundOverallPlanDetailList  = new ArrayList<>();

		//用款申请单id
		String applyType = "0";

		for(int i = 0; i < idArr.length; i++ ){
			Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = tgpf_FundAppropriated_AFDao.findById(idArr[i]);

			if(tgpf_FundAppropriated_AF == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_FundAppropriated_AF(Id:" + idArr[i] + ")'不存在");
			}

			if(i == 0){
				applyType = StrUtil.isBlank(tgpf_FundAppropriated_AF.getApplyType()) ? "0" : tgpf_FundAppropriated_AF.getApplyType();
			}else{
				if(!applyType.equals(StrUtil.isBlank(tgpf_FundAppropriated_AF.getApplyType()) ? "0" : tgpf_FundAppropriated_AF.getApplyType())){
					return MyBackInfo.fail(properties, "不同申请类型的申请单需要分别统筹！");
				}
			}

			if(tgpf_FundAppropriated_AF.getFundOverallPlanDetailList()!=null)
			{
				tgpf_FundOverallPlanDetailList.addAll(tgpf_FundAppropriated_AF.getFundOverallPlanDetailList());
			}
			tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Overallplanning); // 用款申请单状态 ： 统筹中
			tgpf_FundAppropriated_AFList.add(tgpf_FundAppropriated_AF);
		}


		Tgpf_FundOverallPlan tgpf_FundOverallPlan = new Tgpf_FundOverallPlan();
		tgpf_FundOverallPlan.setTheState(S_TheState.Normal);
		tgpf_FundOverallPlan.seteCode(sm_BusinessCodeGetService.execute(busiCode));
		tgpf_FundOverallPlan.setUserStart(loginUser);
		tgpf_FundOverallPlan.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_FundOverallPlan.setFundAppropriated_AFList(tgpf_FundAppropriated_AFList); //用款申请列表
		tgpf_FundOverallPlan.setFundOverallPlanDetailList(tgpf_FundOverallPlanDetailList);//用款申请汇总信息
		tgpf_FundOverallPlan.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_FundOverallPlan.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpf_FundOverallPlan.setUserStart(loginUser);
		tgpf_FundOverallPlan.setUserUpdate(loginUser);
		tgpf_FundOverallPlan.setApplyType(applyType);
		tgpf_FundOverallPlan.setFundOverallPlanDate(myDatetime.dateToSimpleString(System.currentTimeMillis()));
		tgpf_FundOverallPlan.setBusiState(S_CoordinateState.Overallplanning); //统筹状态 ： 统筹中
		tgpf_FundOverallPlan.setApprovalState(S_ApprovalState.WaitSubmit);//流程状态： 待提交
		tgpf_FundOverallPlanDao.save(tgpf_FundOverallPlan);

		//新建统筹账户
		Tgxy_BankAccountEscrowedForm bankAccountEscrowedForm = new Tgxy_BankAccountEscrowedForm();
		bankAccountEscrowedForm.setTheState(S_TheState.Normal);
		bankAccountEscrowedForm.setIsUsing(0);
		List<Tgxy_BankAccountEscrowed> tgxy_bankAccountEscrowedList =
				tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));

		for(Tgxy_BankAccountEscrowed tgxy_bankAccountEscrowed :tgxy_bankAccountEscrowedList )
		{
			Tgpf_OverallPlanAccout tgpf_overallPlanAccout = new Tgpf_OverallPlanAccout();
			tgpf_overallPlanAccout.setTheState(S_TheState.Normal);
			tgpf_overallPlanAccout.setUserStart(loginUser);
			tgpf_overallPlanAccout.setCreateTimeStamp(System.currentTimeMillis());
			tgpf_overallPlanAccout.setFundOverallPlan(tgpf_FundOverallPlan);
			tgpf_overallPlanAccout.seteCodeOfFundOverallPlan(tgpf_FundOverallPlan.geteCode());
			tgpf_overallPlanAccout.setBankAccountEscrowed(tgxy_bankAccountEscrowed);
			tgpf_overallPlanAccout.setOverallPlanAmount(0D);

			tgpf_overallPlanAccoutDao.save(tgpf_overallPlanAccout);
		}

		if(!properties.getProperty("info").equals("noApproval"))
		{
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

			//审批操作
			sm_approvalProcessService.execute(tgpf_FundOverallPlan, model, sm_approvalProcess_cfg);
		}

		Long  tableId = tgpf_FundOverallPlan.getTableId();// 获取统筹id

		properties.put("tableId",tableId);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
