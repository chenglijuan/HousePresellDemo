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
 * Service批量删除：业务对账-日终结算
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_DayEndBalancingBatchDeleteService
{
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao;

	public Properties execute(Tgpf_DayEndBalancingForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_DayEndBalancing tgpf_DayEndBalancing = (Tgpf_DayEndBalancing)tgpf_DayEndBalancingDao.findById(tableId);
			if(tgpf_DayEndBalancing == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_DayEndBalancing(Id:" + tableId + ")'不存在");
			}
		
			tgpf_DayEndBalancing.setTheState(S_TheState.Deleted);
			tgpf_DayEndBalancingDao.save(tgpf_DayEndBalancing);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
