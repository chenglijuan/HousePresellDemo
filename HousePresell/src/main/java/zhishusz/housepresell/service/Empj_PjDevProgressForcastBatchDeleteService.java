package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDao;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：项目-工程进度预测-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PjDevProgressForcastBatchDeleteService
{
	@Autowired
	private Empj_PjDevProgressForcastDao empj_PjDevProgressForcastDao;

	public Properties execute(Empj_PjDevProgressForcastForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_PjDevProgressForcast empj_PjDevProgressForcast = (Empj_PjDevProgressForcast)empj_PjDevProgressForcastDao.findById(tableId);
			if(empj_PjDevProgressForcast == null)
			{
				return MyBackInfo.fail(properties, "'Empj_PjDevProgressForcast(Id:" + tableId + ")'不存在");
			}
		
			empj_PjDevProgressForcast.setTheState(S_TheState.Deleted);
			empj_PjDevProgressForcastDao.save(empj_PjDevProgressForcast);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
