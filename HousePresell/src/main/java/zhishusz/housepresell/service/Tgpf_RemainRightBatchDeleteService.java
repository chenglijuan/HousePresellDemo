package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：留存权益
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RemainRightBatchDeleteService
{
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;

	public Properties execute(Tgpf_RemainRightForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_RemainRight tgpf_RemainRight = (Tgpf_RemainRight)tgpf_RemainRightDao.findById(tableId);
			if(tgpf_RemainRight == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_RemainRight(Id:" + tableId + ")'不存在");
			}
		
			tgpf_RemainRight.setTheState(S_TheState.Deleted);
			tgpf_RemainRightDao.save(tgpf_RemainRight);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
