package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_SerialNumberForm;
import zhishusz.housepresell.database.dao.Tgpf_SerialNumberDao;
import zhishusz.housepresell.database.po.Tgpf_SerialNumber;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：流水号
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_SerialNumberDetailService
{
	@Autowired
	private Tgpf_SerialNumberDao tgpf_SerialNumberDao;

	public Properties execute(Tgpf_SerialNumberForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_SerialNumberId = model.getTableId();
		Tgpf_SerialNumber tgpf_SerialNumber = (Tgpf_SerialNumber)tgpf_SerialNumberDao.findById(tgpf_SerialNumberId);
		if(tgpf_SerialNumber == null || S_TheState.Deleted.equals(tgpf_SerialNumber.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_SerialNumber(Id:" + tgpf_SerialNumberId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_SerialNumber", tgpf_SerialNumber);

		return properties;
	}
}
