package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompleted_DtlDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：申请表-项目托管终止（审批）-明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldEscrowCompleted_DtlBatchDeleteService
{
	@Autowired
	private Empj_BldEscrowCompleted_DtlDao empj_BldEscrowCompleted_DtlDao;

	public Properties execute(Empj_BldEscrowCompleted_DtlForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_BldEscrowCompleted_Dtl empj_BldEscrowCompleted_Dtl = (Empj_BldEscrowCompleted_Dtl)empj_BldEscrowCompleted_DtlDao.findById(tableId);
			if(empj_BldEscrowCompleted_Dtl == null)
			{
				return MyBackInfo.fail(properties, "'Empj_BldEscrowCompleted_Dtl(Id:" + tableId + ")'不存在");
			}
		
			empj_BldEscrowCompleted_Dtl.setTheState(S_TheState.Deleted);
			empj_BldEscrowCompleted_DtlDao.save(empj_BldEscrowCompleted_Dtl);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
