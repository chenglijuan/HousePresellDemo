package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：申请-用款-明细
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriated_AFDtlDetailService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;

	public Properties execute(Tgpf_FundAppropriated_AFDtlForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_FundAppropriated_AFDtlId = model.getTableId();
		Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl = (Tgpf_FundAppropriated_AFDtl)tgpf_FundAppropriated_AFDtlDao.findById(tgpf_FundAppropriated_AFDtlId);
		if(tgpf_FundAppropriated_AFDtl == null || S_TheState.Deleted.equals(tgpf_FundAppropriated_AFDtl.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_FundAppropriated_AFDtl(Id:" + tgpf_FundAppropriated_AFDtlId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_FundAppropriated_AFDtl", tgpf_FundAppropriated_AFDtl);

		return properties;
	}
}
