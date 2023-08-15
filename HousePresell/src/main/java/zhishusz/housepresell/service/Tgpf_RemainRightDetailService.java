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
 * Service详情：留存权益
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RemainRightDetailService
{
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;

	public Properties execute(Tgpf_RemainRightForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_RemainRightId = model.getTableId();
		Tgpf_RemainRight tgpf_RemainRight = (Tgpf_RemainRight)tgpf_RemainRightDao.findById(tgpf_RemainRightId);
		if(tgpf_RemainRight == null || S_TheState.Deleted.equals(tgpf_RemainRight.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_RemainRight(Id:" + tgpf_RemainRightId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_RemainRight", tgpf_RemainRight);

		return properties;
	}
}
