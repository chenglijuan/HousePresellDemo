package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastDtlForm;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDtlDao;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcastDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：项目-工程进度预测 -明细表 
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PjDevProgressForcastDtlBatchDeleteService
{
	@Autowired
	private Empj_PjDevProgressForcastDtlDao empj_PjDevProgressForcastDtlDao;

	public Properties execute(Empj_PjDevProgressForcastDtlForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_PjDevProgressForcastDtl empj_PjDevProgressForcastDtl = (Empj_PjDevProgressForcastDtl)empj_PjDevProgressForcastDtlDao.findById(tableId);
			if(empj_PjDevProgressForcastDtl == null)
			{
				return MyBackInfo.fail(properties, "进度详情信息不存在");
			}
		
			empj_PjDevProgressForcastDtl.setTheState(S_TheState.Deleted);
			empj_PjDevProgressForcastDtlDao.save(empj_PjDevProgressForcastDtl);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
