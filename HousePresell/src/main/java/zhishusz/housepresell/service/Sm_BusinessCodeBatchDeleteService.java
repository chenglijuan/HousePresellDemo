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
 * Service批量删除：业务编号
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_BusinessCodeBatchDeleteService
{
	@Autowired
	private Sm_BusinessCodeDao sm_BusinessCodeDao;

	public Properties execute(Sm_BusinessCodeForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_BusinessCode sm_BusinessCode = (Sm_BusinessCode)sm_BusinessCodeDao.findById(tableId);
			if(sm_BusinessCode == null)
			{
				return MyBackInfo.fail(properties, "'Sm_BusinessCode(Id:" + tableId + ")'不存在");
			}
		
			sm_BusinessCode.setTheState(S_TheState.Deleted);
			sm_BusinessCodeDao.save(sm_BusinessCode);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
