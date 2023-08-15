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
 * Service详情：留存权益(此表为留存权益计算时临时表)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RemainRight_BakDetailService
{
	@Autowired
	private Tgpf_RemainRight_BakDao tgpf_RemainRight_BakDao;

	public Properties execute(Tgpf_RemainRight_BakForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_RemainRight_BakId = model.getTableId();
		Tgpf_RemainRight_Bak tgpf_RemainRight_Bak = (Tgpf_RemainRight_Bak)tgpf_RemainRight_BakDao.findById(tgpf_RemainRight_BakId);
		if(tgpf_RemainRight_Bak == null || S_TheState.Deleted.equals(tgpf_RemainRight_Bak.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_RemainRight_Bak(Id:" + tgpf_RemainRight_BakId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_RemainRight_Bak", tgpf_RemainRight_Bak);

		return properties;
	}
}
