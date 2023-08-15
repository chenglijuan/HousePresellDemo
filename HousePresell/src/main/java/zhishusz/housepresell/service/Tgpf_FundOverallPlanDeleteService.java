package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_OverallPlanAccoutForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.dao.Tgpf_OverallPlanAccoutDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_OverallPlanAccout;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.database.dao.Tgpf_FundOverallPlanDao;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：资金统筹
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundOverallPlanDeleteService
{
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;
	@Autowired
	private Tgpf_OverallPlanAccoutDao tgpf_OverallPlanAccoutDao;
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_fundAppropriatedDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Tgpf_FundOverallPlanForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_FundOverallPlanId = model.getTableId();

		Tgpf_FundOverallPlan tgpf_FundOverallPlan = (Tgpf_FundOverallPlan)tgpf_FundOverallPlanDao.findById(tgpf_FundOverallPlanId);
		if(tgpf_FundOverallPlan == null)
		{
			return MyBackInfo.fail(properties, "'统筹单(Id:" + tgpf_FundOverallPlanId + ")'不存在");
		}

		//修改用款申请主表申请单状态为 3 : 已受理
		for (Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af : tgpf_FundOverallPlan.getFundAppropriated_AFList())
		{
			tgpf_fundAppropriated_af.setApplyState(S_ApplyState.Admissible);
		}

		//删除统筹账户
		Tgpf_OverallPlanAccoutForm overallPlanAccoutForm = new Tgpf_OverallPlanAccoutForm();
		overallPlanAccoutForm.setTheState(S_TheState.Normal);
		overallPlanAccoutForm.setFundOverallPlanId(tgpf_FundOverallPlanId);
		List<Tgpf_OverallPlanAccout> tgpf_OverallPlanAccoutList = tgpf_OverallPlanAccoutDao.findByPage(tgpf_OverallPlanAccoutDao.getQuery(tgpf_OverallPlanAccoutDao.getBasicHQL(), overallPlanAccoutForm));
		for (Tgpf_OverallPlanAccout tgpf_overallPlanAccout : tgpf_OverallPlanAccoutList)
		{
			tgpf_overallPlanAccout.setTheState(S_TheState.Deleted);
			tgpf_overallPlanAccout.setFundOverallPlan(null);
			tgpf_OverallPlanAccoutDao.save(tgpf_overallPlanAccout);
		}

		//删除资金拨付
		for (Tgpf_FundAppropriated tgpf_fundAppropriated : tgpf_FundOverallPlan.getFundAppropriatedList())
		{
			tgpf_fundAppropriated.setTheState(S_TheState.Deleted);
			tgpf_fundAppropriatedDao.save(tgpf_fundAppropriated);
		}
		tgpf_FundOverallPlan.getFundAppropriatedList().clear();

		//删除统筹单
		tgpf_FundOverallPlan.setTheState(S_TheState.Deleted);
		tgpf_FundOverallPlanDao.save(tgpf_FundOverallPlan);

		//删除审批流
		deleteService.execute(tgpf_FundOverallPlanId,S_BusiCode.busiCode8);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
