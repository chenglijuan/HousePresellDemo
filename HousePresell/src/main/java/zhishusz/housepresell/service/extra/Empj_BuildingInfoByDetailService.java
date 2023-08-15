package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼幢-基础信息
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_BuildingInfoByDetailService
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();

		model.setTheState(S_TheState.Normal);
		/*
		 * 通过传递的主键信息（tableId）匹配主键或者是外键关联字段(externalId)信息
		 * 
		 */
		Integer totalCount = empj_BuildingInfoDao
				.findByPage_Size(empj_BuildingInfoDao.getQuery_Size(empj_BuildingInfoDao.getDetailHql(), model));

		List<Empj_BuildingInfo> empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
		// if (totalCount == 1)
		// {
		// 查询到唯一信息时进行下一步操作
		empj_BuildingInfoList = empj_BuildingInfoDao
				.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getDetailHql(), model));
		// }
		// else
		// {
		// empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
		// return MyBackInfo.fail(properties, "ID：" + model.getTableId() +
		// "，没有查询到相关楼幢信息");
		// }

		Empj_BuildingInfo empj_BuildingInfo = new Empj_BuildingInfo();
		if (null != empj_BuildingInfoList && empj_BuildingInfoList.size() > 0)
		{
			empj_BuildingInfo = empj_BuildingInfoList.get(0);
		}
		else
		{
			return MyBackInfo.fail(properties, "ID：" + model.getTableId() + "，楼幢信息不存在");
		}
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_BuildingInfo", empj_BuildingInfo);

		return properties;
	}
}
