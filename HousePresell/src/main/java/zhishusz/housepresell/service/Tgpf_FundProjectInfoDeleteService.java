package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundProjectInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_FundProjectInfoDao;
import zhishusz.housepresell.database.po.Tgpf_FundProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：推送给财务系统-拨付凭证-项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundProjectInfoDeleteService
{
	@Autowired
	private Tgpf_FundProjectInfoDao tgpf_FundProjectInfoDao;

	public Properties execute(Tgpf_FundProjectInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_FundProjectInfoId = model.getTableId();
		Tgpf_FundProjectInfo tgpf_FundProjectInfo = (Tgpf_FundProjectInfo)tgpf_FundProjectInfoDao.findById(tgpf_FundProjectInfoId);
		if(tgpf_FundProjectInfo == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_FundProjectInfo(Id:" + tgpf_FundProjectInfoId + ")'不存在");
		}
		
		tgpf_FundProjectInfo.setTheState(S_TheState.Deleted);
		tgpf_FundProjectInfoDao.save(tgpf_FundProjectInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
