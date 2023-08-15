package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_AccVoucherForm;
import zhishusz.housepresell.database.dao.Tgpf_AccVoucherDao;
import zhishusz.housepresell.database.po.Tgpf_AccVoucher;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：推送给财务系统-凭证
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_AccVoucherBatchDeleteService
{
	@Autowired
	private Tgpf_AccVoucherDao tgpf_AccVoucherDao;

	public Properties execute(Tgpf_AccVoucherForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_AccVoucher tgpf_AccVoucher = (Tgpf_AccVoucher)tgpf_AccVoucherDao.findById(tableId);
			if(tgpf_AccVoucher == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_AccVoucher(Id:" + tableId + ")'不存在");
			}
		
			tgpf_AccVoucher.setTheState(S_TheState.Deleted);
			tgpf_AccVoucherDao.save(tgpf_AccVoucher);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
