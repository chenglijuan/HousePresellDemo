package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundAccountInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAccountInfoDao;
import zhishusz.housepresell.database.po.Tgpf_FundAccountInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：推送给财务系统-设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAccountInfoBatchDeleteService
{
	@Autowired
	private Tgpf_FundAccountInfoDao tgpf_FundAccountInfoDao;

	public Properties execute(Tgpf_FundAccountInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_FundAccountInfo tgpf_FundAccountInfo = (Tgpf_FundAccountInfo)tgpf_FundAccountInfoDao.findById(tableId);
			if(tgpf_FundAccountInfo == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_FundAccountInfo(Id:" + tableId + ")'不存在");
			}
		
			tgpf_FundAccountInfo.setTheState(S_TheState.Deleted);
			tgpf_FundAccountInfoDao.save(tgpf_FundAccountInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
