package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BankUploadDataDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_BankUploadDataDetailDao;
import zhishusz.housepresell.database.po.Tgpf_BankUploadDataDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：银行对账单数据
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BankUploadDataDetailBatchDeleteService
{
	@Autowired
	private Tgpf_BankUploadDataDetailDao tgpf_BankUploadDataDetailDao;

	public Properties execute(Tgpf_BankUploadDataDetailForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_BankUploadDataDetail tgpf_BankUploadDataDetail = (Tgpf_BankUploadDataDetail)tgpf_BankUploadDataDetailDao.findById(tableId);
			if(tgpf_BankUploadDataDetail == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_BankUploadDataDetail(Id:" + tableId + ")'不存在");
			}
		
			tgpf_BankUploadDataDetail.setTheState(S_TheState.Deleted);
			tgpf_BankUploadDataDetailDao.save(tgpf_BankUploadDataDetail);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
