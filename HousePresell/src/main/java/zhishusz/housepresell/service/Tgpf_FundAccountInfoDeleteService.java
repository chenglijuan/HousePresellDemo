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
 * Service单个删除：推送给财务系统-设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAccountInfoDeleteService
{
	@Autowired
	private Tgpf_FundAccountInfoDao tgpf_FundAccountInfoDao;

	public Properties execute(Tgpf_FundAccountInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_FundAccountInfoId = model.getTableId();
		Tgpf_FundAccountInfo tgpf_FundAccountInfo = (Tgpf_FundAccountInfo)tgpf_FundAccountInfoDao.findById(tgpf_FundAccountInfoId);
		if(tgpf_FundAccountInfo == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_FundAccountInfo(Id:" + tgpf_FundAccountInfoId + ")'不存在");
		}
		
		tgpf_FundAccountInfo.setTheState(S_TheState.Deleted);
		tgpf_FundAccountInfoDao.save(tgpf_FundAccountInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
