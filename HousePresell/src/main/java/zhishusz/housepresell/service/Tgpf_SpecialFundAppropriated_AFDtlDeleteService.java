package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：特殊拨付-申请子表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_SpecialFundAppropriated_AFDtlDeleteService
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;

	public Properties execute(Tgpf_SpecialFundAppropriated_AFDtlForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_SpecialFundAppropriated_AFDtlId = model.getTableId();
		Tgpf_SpecialFundAppropriated_AFDtl tgpf_SpecialFundAppropriated_AFDtl = (Tgpf_SpecialFundAppropriated_AFDtl)tgpf_SpecialFundAppropriated_AFDtlDao.findById(tgpf_SpecialFundAppropriated_AFDtlId);
		if(tgpf_SpecialFundAppropriated_AFDtl == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_SpecialFundAppropriated_AFDtl(Id:" + tgpf_SpecialFundAppropriated_AFDtlId + ")'不存在");
		}
		
		tgpf_SpecialFundAppropriated_AFDtl.setTheState(S_TheState.Deleted);
		tgpf_SpecialFundAppropriated_AFDtlDao.save(tgpf_SpecialFundAppropriated_AFDtl);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
