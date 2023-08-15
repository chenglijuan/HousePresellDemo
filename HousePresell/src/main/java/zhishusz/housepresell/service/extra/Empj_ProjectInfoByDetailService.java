package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：项目信息
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_ProjectInfoByDetailService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();

		/*
		 *  通过传递的主键信息（tableId）匹配主键或者是外键关联字段(externalId)信息
		 *  
		 */
		Integer totalCount = empj_ProjectInfoDao
				.findByPage_Size(empj_ProjectInfoDao.getQuery_Size(empj_ProjectInfoDao.getDetailHql(), model));

		List<Empj_ProjectInfo> empj_ProjectInfoList;
		if (totalCount == 1)
		{
			// 查询到唯一信息时进行下一步操作
			empj_ProjectInfoList = empj_ProjectInfoDao
					.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getDetailHql(), model));
		}
		else
		{
			empj_ProjectInfoList = new ArrayList<Empj_ProjectInfo>();
			return MyBackInfo.fail(properties, "ID："+model.getTableId()+"，没有查询到相关项目信息");
		}

		Empj_ProjectInfo empj_ProjectInfo;
		if (null != empj_ProjectInfoList && empj_ProjectInfoList.size() > 0)
		{
			empj_ProjectInfo = empj_ProjectInfoList.get(0);
		}
		else
		{
			return MyBackInfo.fail(properties, "ID："+model.getTableId()+"，项目信息不存在");
		}
		

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_ProjectInfo", empj_ProjectInfo);

		return properties;
	}
}
