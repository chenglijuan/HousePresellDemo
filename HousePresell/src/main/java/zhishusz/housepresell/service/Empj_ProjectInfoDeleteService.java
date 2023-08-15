package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_ProjectInfoDeleteService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();

		Long empj_ProjectInfoId = model.getTableId();

		Empj_ProjectInfo empj_ProjectInfo = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(empj_ProjectInfoId);
		if(empj_ProjectInfo == null)
		{
			return MyBackInfo.fail(properties, "删除的项目信息不存在");
		}
		
		empj_ProjectInfo.setTheState(S_TheState.Deleted);
		empj_ProjectInfoDao.save(empj_ProjectInfo);

		//删除审批流
		deleteService.execute(empj_ProjectInfoId,S_BusiCode.busiCode_03010101);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
