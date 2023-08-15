package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：特殊拨付-申请主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_SpecialFundAppropriated_AFDeleteService
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;

	public Properties execute(Tgpf_SpecialFundAppropriated_AFForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_SpecialFundAppropriated_AFId = model.getTableId();
		Tgpf_SpecialFundAppropriated_AF tgpf_SpecialFundAppropriated_AF = (Tgpf_SpecialFundAppropriated_AF)tgpf_SpecialFundAppropriated_AFDao.findById(tgpf_SpecialFundAppropriated_AFId);
		if(tgpf_SpecialFundAppropriated_AF == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_SpecialFundAppropriated_AF(Id:" + tgpf_SpecialFundAppropriated_AFId + ")'不存在");
		}
		
		tgpf_SpecialFundAppropriated_AF.setTheState(S_TheState.Deleted);
		tgpf_SpecialFundAppropriated_AFDao.save(tgpf_SpecialFundAppropriated_AF);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
