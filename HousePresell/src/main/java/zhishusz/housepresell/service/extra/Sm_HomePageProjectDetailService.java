package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/**
 * 首页展示加载
 * 
 * @ClassName: Sm_HomePageListService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月12日 下午2:14:11
 * @version V1.0
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Sm_HomePageProjectDetailService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;// 楼幢

	@SuppressWarnings("unchecked")
	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tableId = model.getTableId();

		if (null == tableId || tableId <= 0)
		{
			return MyBackInfo.fail(properties, "请选择正确的项目信息");
		}

		Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(tableId);

		/*
		 * 查询出项目信息后，根据项目信息查询项目下楼幢的托管总面积
		 */
		Empj_BuildingInfoForm buildModel = new Empj_BuildingInfoForm();
		buildModel.setProjectId(tableId);

		List<Empj_BuildingInfo> empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
		empj_BuildingInfoList = empj_BuildingInfoDao
				.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), buildModel));

		empj_ProjectInfo.setLandArea(0.00);
		// 遍历查询出的楼幢信息，累加托管总面积(landArea)
		if (null != empj_BuildingInfoList && empj_BuildingInfoList.size() > 0)
		{
			MyDouble dp = MyDouble.getInstance();
			for (Empj_BuildingInfo build : empj_BuildingInfoList)
			{
				empj_ProjectInfo.setLandArea(dp.doubleAddDouble(empj_ProjectInfo.getLandArea(), build.getEscrowArea()));
			}

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_ProjectInfo", empj_ProjectInfo);

		return properties;
	}
}
