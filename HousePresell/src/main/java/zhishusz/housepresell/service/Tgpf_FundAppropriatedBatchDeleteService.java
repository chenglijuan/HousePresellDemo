package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：资金拨付
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriatedBatchDeleteService
{
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_FundAppropriatedDao;

	public Properties execute(Tgpf_FundAppropriatedForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_FundAppropriated tgpf_FundAppropriated = (Tgpf_FundAppropriated)tgpf_FundAppropriatedDao.findById(tableId);
			if(tgpf_FundAppropriated == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_FundAppropriated(Id:" + tableId + ")'不存在");
			}
		
			tgpf_FundAppropriated.setTheState(S_TheState.Deleted);
			tgpf_FundAppropriatedDao.save(tgpf_FundAppropriated);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
