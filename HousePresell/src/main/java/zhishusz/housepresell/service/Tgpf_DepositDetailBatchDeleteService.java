package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：资金归集-明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_DepositDetailBatchDeleteService
{
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;

	public Properties execute(Tgpf_DepositDetailForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_DepositDetail tgpf_DepositDetail = (Tgpf_DepositDetail)tgpf_DepositDetailDao.findById(tableId);
			if(tgpf_DepositDetail == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_DepositDetail(Id:" + tableId + ")'不存在");
			}
		
			tgpf_DepositDetail.setTheState(S_TheState.Deleted);
			tgpf_DepositDetailDao.save(tgpf_DepositDetail);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
