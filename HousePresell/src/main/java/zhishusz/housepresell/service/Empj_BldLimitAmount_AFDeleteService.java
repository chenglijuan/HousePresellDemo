package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service单个删除：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldLimitAmount_AFDeleteService
{
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Empj_BldLimitAmount_AFForm model)
	{
		Properties properties = new MyProperties();

		Long empj_BldLimitAmount_AFId = model.getTableId();
		Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = (Empj_BldLimitAmount_AF)empj_BldLimitAmount_AFDao.findById(empj_BldLimitAmount_AFId);
		if(empj_BldLimitAmount_AF == null)
		{
			return MyBackInfo.fail(properties, "'Empj_BldLimitAmount_AF(Id:" + empj_BldLimitAmount_AFId + ")'不存在");
		}
		
		empj_BldLimitAmount_AF.setTheState(S_TheState.Deleted);
		empj_BldLimitAmount_AFDao.save(empj_BldLimitAmount_AF);
		//删除审批流
		deleteService.execute(empj_BldLimitAmount_AFId, S_BusiCode.busiCode_03030101);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
