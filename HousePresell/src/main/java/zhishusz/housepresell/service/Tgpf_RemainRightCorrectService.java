package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.controller.form.Tgpf_RemainRight_DtlForm;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Properties;
	
/*
 * Service 数据修正
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RemainRightCorrectService
{
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;
	
	public Properties execute(Tgpf_RemainRightForm model)
	{
		Properties properties = new MyProperties();

		if(model.getTgpf_RemainRight_DtlTab() == null || model.getTgpf_RemainRight_DtlTab().length < 1)
		{
			return MyBackInfo.fail(properties, "户留存权益比对明细不能为空");
		}
		
		for(Tgpf_RemainRight_DtlForm tgpf_RemainRight_DtlForm : model.getTgpf_RemainRight_DtlTab())
		{
			if (!tgpf_RemainRight_DtlForm.getHasUploadData()) {
				continue;
			}

			Tgpf_RemainRight tgpf_RemainRight = tgpf_RemainRightDao.findById(tgpf_RemainRight_DtlForm.getTableId());
			
			if(tgpf_RemainRight == null)
			{
				return MyBackInfo.fail(properties, "户留存权益不存在");
			}

			tgpf_RemainRight.setTheAmount(tgpf_RemainRight_DtlForm.getTheAmount_Upload());
			tgpf_RemainRight.setLimitedRetainRight(tgpf_RemainRight_DtlForm.getLimitedRetainRight_Upload());
			tgpf_RemainRight.setWithdrawableRetainRight(tgpf_RemainRight_DtlForm.getWithdrawableRetainRight_Upload());
			
			tgpf_RemainRightDao.save(tgpf_RemainRight);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
