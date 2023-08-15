package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_CoordinateState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：资金统筹 - 详情页面
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_FundOverallPlanDetailService
{
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;

	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_fundAppropriatedDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundOverallPlanForm model)
	{
		Properties properties = new MyProperties();

		//统筹单id
		Long fundOverallPlanId = model.getTableId();
		if(fundOverallPlanId == null || fundOverallPlanId < 1)
		{
			return MyBackInfo.fail(properties, "统筹单不能为空");
		}

		Tgpf_FundOverallPlan tgpf_FundOverallPlan = tgpf_FundOverallPlanDao.findById(fundOverallPlanId);
		if(tgpf_FundOverallPlan == null)
		{
			return MyBackInfo.fail(properties, "统筹单不能为空");
		}

		if(tgpf_FundOverallPlan.getFundAppropriated_AFList() == null)
		{
			return MyBackInfo.fail(properties, "'楼幢信息'不能为空");
		}
		if(tgpf_FundOverallPlan.getFundAppropriatedList() == null)
		{
			return MyBackInfo.fail(properties, "'拨付统筹信息'不能为空");
		}

		//用款申请主表信息
		List<Tgpf_FundAppropriated_AF> tgpf_fundAppropriated_AfList = tgpf_FundOverallPlan.getFundAppropriated_AFList();
		List<Tgpf_FundAppropriated_AFDtl> tgpf_fundAppropriated_afDtlList = new ArrayList<>();

		//用款申请明细表
		for(Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af : tgpf_fundAppropriated_AfList)
		{
			if(tgpf_fundAppropriated_af.getFundAppropriated_AFDtlList() != null && tgpf_fundAppropriated_af.getFundAppropriated_AFDtlList().size() > 0)
			{
				tgpf_fundAppropriated_afDtlList.addAll(tgpf_fundAppropriated_af.getFundAppropriated_AFDtlList());
			}
		}

		//资金拨付信息
		Tgpf_FundAppropriatedForm fundAppropriatedForm = new Tgpf_FundAppropriatedForm();
		fundAppropriatedForm.setTheState(S_TheState.Normal);
		fundAppropriatedForm.setFundOverallPlanId(fundOverallPlanId);
		fundAppropriatedForm.setOverallPlanPayoutAmount(0D);
		List<Tgpf_FundAppropriated> tgpf_FundAppropriatedList = tgpf_fundAppropriatedDao.findByPage(tgpf_fundAppropriatedDao.getQuery(tgpf_fundAppropriatedDao.getBasicHQL(), fundAppropriatedForm));

		properties.put("tgpf_FundOverallPlan",tgpf_FundOverallPlan); //资金统筹
		properties.put("tgpf_fundAppropriated_afDtlList",tgpf_fundAppropriated_afDtlList); // 用款申请明细表信息
		properties.put("tgpf_FundAppropriatedList",tgpf_FundAppropriatedList); // 拨付统筹信息
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
