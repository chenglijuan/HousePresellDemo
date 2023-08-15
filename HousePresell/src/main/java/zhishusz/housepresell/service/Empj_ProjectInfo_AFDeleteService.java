package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_ProjectInfo_AFForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfo_AFDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：申请表-项目信息变更(审批)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_ProjectInfo_AFDeleteService
{
	@Autowired
	private Empj_ProjectInfo_AFDao empj_ProjectInfo_AFDao;

	public Properties execute(Empj_ProjectInfo_AFForm model)
	{
		Properties properties = new MyProperties();

		Long empj_ProjectInfo_AFId = model.getTableId();
		Empj_ProjectInfo_AF empj_ProjectInfo_AF = (Empj_ProjectInfo_AF)empj_ProjectInfo_AFDao.findById(empj_ProjectInfo_AFId);
		if(empj_ProjectInfo_AF == null)
		{
			return MyBackInfo.fail(properties, "'Empj_ProjectInfo_AF(Id:" + empj_ProjectInfo_AFId + ")'不存在");
		}
		
		empj_ProjectInfo_AF.setTheState(S_TheState.Deleted);
		empj_ProjectInfo_AFDao.save(empj_ProjectInfo_AF);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
