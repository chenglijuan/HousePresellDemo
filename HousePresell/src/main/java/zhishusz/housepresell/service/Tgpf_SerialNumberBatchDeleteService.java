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
 * Service批量删除：流水号
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_SerialNumberBatchDeleteService
{
	@Autowired
	private Tgpf_SerialNumberDao tgpf_SerialNumberDao;

	public Properties execute(Tgpf_SerialNumberForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_SerialNumber tgpf_SerialNumber = (Tgpf_SerialNumber)tgpf_SerialNumberDao.findById(tableId);
			if(tgpf_SerialNumber == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_SerialNumber(Id:" + tableId + ")'不存在");
			}
		
			tgpf_SerialNumber.setTheState(S_TheState.Deleted);
			tgpf_SerialNumberDao.save(tgpf_SerialNumber);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
