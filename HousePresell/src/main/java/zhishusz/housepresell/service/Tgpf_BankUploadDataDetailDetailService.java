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
 * Service详情：银行对账单数据
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BankUploadDataDetailDetailService
{
	@Autowired
	private Tgpf_BankUploadDataDetailDao tgpf_BankUploadDataDetailDao;

	public Properties execute(Tgpf_BankUploadDataDetailForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_BankUploadDataDetailId = model.getTableId();
		Tgpf_BankUploadDataDetail tgpf_BankUploadDataDetail = (Tgpf_BankUploadDataDetail)tgpf_BankUploadDataDetailDao.findById(tgpf_BankUploadDataDetailId);
		if(tgpf_BankUploadDataDetail == null || S_TheState.Deleted.equals(tgpf_BankUploadDataDetail.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_BankUploadDataDetail(Id:" + tgpf_BankUploadDataDetailId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_BankUploadDataDetail", tgpf_BankUploadDataDetail);

		return properties;
	}
}
