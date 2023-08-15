package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_OverallPlanAccoutForm;
import zhishusz.housepresell.database.dao.Tgpf_OverallPlanAccoutDao;
import zhishusz.housepresell.database.po.Tgpf_OverallPlanAccout;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：统筹-账户状况信息保存表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_OverallPlanAccoutDetailService
{
	@Autowired
	private Tgpf_OverallPlanAccoutDao tgpf_OverallPlanAccoutDao;

	public Properties execute(Tgpf_OverallPlanAccoutForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_OverallPlanAccoutId = model.getTableId();
		Tgpf_OverallPlanAccout tgpf_OverallPlanAccout = (Tgpf_OverallPlanAccout)tgpf_OverallPlanAccoutDao.findById(tgpf_OverallPlanAccoutId);
		if(tgpf_OverallPlanAccout == null || S_TheState.Deleted.equals(tgpf_OverallPlanAccout.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_OverallPlanAccout(Id:" + tgpf_OverallPlanAccoutId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_OverallPlanAccout", tgpf_OverallPlanAccout);

		return properties;
	}
}
