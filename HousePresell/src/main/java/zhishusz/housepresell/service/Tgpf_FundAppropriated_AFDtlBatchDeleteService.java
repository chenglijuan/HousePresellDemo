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
 * Service批量删除：申请-用款-明细
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriated_AFDtlBatchDeleteService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;

	public Properties execute(Tgpf_FundAppropriated_AFDtlForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl = (Tgpf_FundAppropriated_AFDtl)tgpf_FundAppropriated_AFDtlDao.findById(tableId);
			if(tgpf_FundAppropriated_AFDtl == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_FundAppropriated_AFDtl(Id:" + tableId + ")'不存在");
			}
		
			tgpf_FundAppropriated_AFDtl.setTheState(S_TheState.Deleted);
			tgpf_FundAppropriated_AFDtlDao.save(tgpf_FundAppropriated_AFDtl);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
