package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：业务对账-日终结算
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_DayEndBalancingDetailService
{
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao;

	public Properties execute(Tgpf_DayEndBalancingForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_DayEndBalancingId = model.getTableId();
		Tgpf_DayEndBalancing tgpf_DayEndBalancing = (Tgpf_DayEndBalancing)tgpf_DayEndBalancingDao.findById(tgpf_DayEndBalancingId);
		if(tgpf_DayEndBalancing == null || S_TheState.Deleted.equals(tgpf_DayEndBalancing.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_DayEndBalancing(Id:" + tgpf_DayEndBalancingId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_DayEndBalancing", tgpf_DayEndBalancing);

		return properties;
	}
}
