package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_RemainRight_BakForm;
import zhishusz.housepresell.database.dao.Tgpf_RemainRight_BakDao;
import zhishusz.housepresell.database.po.Tgpf_RemainRight_Bak;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：留存权益(此表为留存权益计算时临时表)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RemainRight_BakAddService
{
	@Autowired
	private Tgpf_RemainRight_BakDao tgpf_RemainRight_BakDao;
	
	public Properties execute(Tgpf_RemainRight_BakForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		String eCode = model.geteCode();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}

	
		Tgpf_RemainRight_Bak tgpf_RemainRight_Bak = new Tgpf_RemainRight_Bak();
		tgpf_RemainRight_Bak.setTheState(theState);
		tgpf_RemainRight_Bak.seteCode(eCode);
		tgpf_RemainRight_BakDao.save(tgpf_RemainRight_Bak);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
