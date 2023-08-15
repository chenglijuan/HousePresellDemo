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
 * Service详情：项目-工程进度预测 -明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PjDevProgressForcastDtlDetailService
{
	@Autowired
	private Empj_PjDevProgressForcastDtlDao empj_PjDevProgressForcastDtlDao;

	public Properties execute(Empj_PjDevProgressForcastDtlForm model)
	{
		Properties properties = new MyProperties();

		Long empj_PjDevProgressForcastDtlId = model.getTableId();
		Empj_PjDevProgressForcastDtl empj_PjDevProgressForcastDtl = (Empj_PjDevProgressForcastDtl)empj_PjDevProgressForcastDtlDao.findById(empj_PjDevProgressForcastDtlId);
		if(empj_PjDevProgressForcastDtl == null || S_TheState.Deleted.equals(empj_PjDevProgressForcastDtl.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_PjDevProgressForcastDtl(Id:" + empj_PjDevProgressForcastDtlId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_PjDevProgressForcastDtl", empj_PjDevProgressForcastDtl);

		return properties;
	}
}
