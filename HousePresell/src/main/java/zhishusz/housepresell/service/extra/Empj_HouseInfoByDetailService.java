package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼幢-户室
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_HouseInfoByDetailService
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();

		Integer totalCount = empj_HouseInfoDao
				.findByPage_Size(empj_HouseInfoDao.getQuery_Size(empj_HouseInfoDao.getDetailHQL(), model));

		List<Empj_HouseInfo> empj_HouseInfoList;
		if (totalCount > 0)
		{
			empj_HouseInfoList = empj_HouseInfoDao
					.findByPage(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getDetailHQL(), model));
		}
		else
		{
			empj_HouseInfoList = new ArrayList<Empj_HouseInfo>();
			return MyBackInfo.fail(properties, "ID：" + model.getTableId() + "，没有查询到相关户室信息");
		}

		Empj_HouseInfo empj_HouseInfo = new Empj_HouseInfo();
		if (null != empj_HouseInfoList && empj_HouseInfoList.size() > 0)
		{
			empj_HouseInfo = empj_HouseInfoList.get(0);
		}
		else
		{
			return MyBackInfo.fail(properties, "ID："+model.getTableId()+"，户室信息不存在");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_HouseInfo", empj_HouseInfo);

		return properties;
	}
}
