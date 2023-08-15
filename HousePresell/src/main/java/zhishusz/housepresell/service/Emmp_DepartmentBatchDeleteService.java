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
 * Service批量删除：部门
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_DepartmentBatchDeleteService
{
	@Autowired
	private Emmp_DepartmentDao emmp_DepartmentDao;

	public Properties execute(Emmp_DepartmentForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Emmp_Department emmp_Department = (Emmp_Department)emmp_DepartmentDao.findById(tableId);
			if(emmp_Department == null)
			{
				return MyBackInfo.fail(properties, "'Emmp_Department(Id:" + tableId + ")'不存在");
			}
		
			emmp_Department.setTheState(S_TheState.Deleted);
			emmp_DepartmentDao.save(emmp_Department);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
