package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_DepartmentForm;
import zhishusz.housepresell.database.dao.Emmp_DepartmentDao;
import zhishusz.housepresell.database.po.Emmp_Department;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：部门
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_DepartmentDeleteService
{
	@Autowired
	private Emmp_DepartmentDao emmp_DepartmentDao;

	public Properties execute(Emmp_DepartmentForm model)
	{
		Properties properties = new MyProperties();

		Long emmp_DepartmentId = model.getTableId();
		Emmp_Department emmp_Department = (Emmp_Department)emmp_DepartmentDao.findById(emmp_DepartmentId);
		if(emmp_Department == null)
		{
			return MyBackInfo.fail(properties, "'Emmp_Department(Id:" + emmp_DepartmentId + ")'不存在");
		}
		
		emmp_Department.setTheState(S_TheState.Deleted);
		emmp_DepartmentDao.save(emmp_Department);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
