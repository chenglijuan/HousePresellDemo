package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.po.Empj_PjDevProgressForcastDtl;
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
 * Service详情：项目-工程进度预测-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PjDevProgressForcastDetailService
{
	@Autowired
	private Empj_PjDevProgressForcastDao empj_PjDevProgressForcastDao;

	public Properties execute(Empj_PjDevProgressForcastForm model)
	{
		Properties properties = new MyProperties();

		Long empj_PjDevProgressForcastId = model.getTableId();
		Empj_PjDevProgressForcast empj_PjDevProgressForcast = (Empj_PjDevProgressForcast)empj_PjDevProgressForcastDao.findById(empj_PjDevProgressForcastId);
		if(empj_PjDevProgressForcast == null || S_TheState.Deleted.equals(empj_PjDevProgressForcast.getTheState()))
		{
			return MyBackInfo.fail(properties, "工程进度预测信息不存在");
		}

		List<Empj_PjDevProgressForcastDtl> empj_pjDevProgressForcastDtlList;
		if (empj_PjDevProgressForcast.getDetailList() != null)
		{
			empj_pjDevProgressForcastDtlList = empj_PjDevProgressForcast.getDetailList();
		}
		else
		{
			empj_pjDevProgressForcastDtlList = new ArrayList<Empj_PjDevProgressForcastDtl>();
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_PjDevProgressForcast", empj_PjDevProgressForcast);
		properties.put("empj_pjDevProgressForcastDtlList", empj_pjDevProgressForcastDtlList);


		return properties;
	}
}
