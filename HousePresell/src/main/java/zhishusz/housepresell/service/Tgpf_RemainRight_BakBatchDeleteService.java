package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_RemainRight_BakForm;
import zhishusz.housepresell.database.dao.Tgpf_RemainRight_BakDao;
import zhishusz.housepresell.database.po.Tgpf_RemainRight_Bak;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：留存权益(此表为留存权益计算时临时表)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RemainRight_BakBatchDeleteService
{
	@Autowired
	private Tgpf_RemainRight_BakDao tgpf_RemainRight_BakDao;

	public Properties execute(Tgpf_RemainRight_BakForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_RemainRight_Bak tgpf_RemainRight_Bak = (Tgpf_RemainRight_Bak)tgpf_RemainRight_BakDao.findById(tableId);
			if(tgpf_RemainRight_Bak == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_RemainRight_Bak(Id:" + tableId + ")'不存在");
			}
		
			tgpf_RemainRight_Bak.setTheState(S_TheState.Deleted);
			tgpf_RemainRight_BakDao.save(tgpf_RemainRight_Bak);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
