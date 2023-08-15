package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：特殊拨付-申请子表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_SpecialFundAppropriated_AFDtlBatchDeleteService
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;

	public Properties execute(Tgpf_SpecialFundAppropriated_AFDtlForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "登录失效，请重新登录");
		}

		Long[] idArr = model.getIdArr();
		if (idArr == null || idArr.length < 1)
		{
//			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for (Long tableId : idArr)
		{
			Tgpf_SpecialFundAppropriated_AFDtl tgpf_SpecialFundAppropriated_AFDtl = (Tgpf_SpecialFundAppropriated_AFDtl) tgpf_SpecialFundAppropriated_AFDtlDao
					.findById(tableId);
			if (null != tgpf_SpecialFundAppropriated_AFDtl)
			{
				tgpf_SpecialFundAppropriated_AFDtl.setTheState(S_TheState.Deleted);
				tgpf_SpecialFundAppropriated_AFDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
				tgpf_SpecialFundAppropriated_AFDtl.setUserUpdate(user);
				tgpf_SpecialFundAppropriated_AFDtlDao.save(tgpf_SpecialFundAppropriated_AFDtl);
			}

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
