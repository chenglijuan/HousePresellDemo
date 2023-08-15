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
 * Service批量删除：推送给财务系统-拨付凭证-项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundProjectInfoBatchDeleteService
{
	@Autowired
	private Tgpf_FundProjectInfoDao tgpf_FundProjectInfoDao;

	public Properties execute(Tgpf_FundProjectInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_FundProjectInfo tgpf_FundProjectInfo = (Tgpf_FundProjectInfo)tgpf_FundProjectInfoDao.findById(tableId);
			if(tgpf_FundProjectInfo == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_FundProjectInfo(Id:" + tableId + ")'不存在");
			}
		
			tgpf_FundProjectInfo.setTheState(S_TheState.Deleted);
			tgpf_FundProjectInfoDao.save(tgpf_FundProjectInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
