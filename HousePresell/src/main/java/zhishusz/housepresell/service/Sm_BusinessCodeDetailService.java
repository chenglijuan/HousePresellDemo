package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BusinessCodeForm;
import zhishusz.housepresell.database.dao.Sm_BusinessCodeDao;
import zhishusz.housepresell.database.po.Sm_BusinessCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：业务编号
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_BusinessCodeDetailService
{
	@Autowired
	private Sm_BusinessCodeDao sm_BusinessCodeDao;

	public Properties execute(Sm_BusinessCodeForm model)
	{
		Properties properties = new MyProperties();

		Long sm_BusinessCodeId = model.getTableId();
		Sm_BusinessCode sm_BusinessCode = (Sm_BusinessCode)sm_BusinessCodeDao.findById(sm_BusinessCodeId);
		if(sm_BusinessCode == null || S_TheState.Deleted.equals(sm_BusinessCode.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_BusinessCode(Id:" + sm_BusinessCodeId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_BusinessCode", sm_BusinessCode);

		return properties;
	}
}
