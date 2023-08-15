package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 
 * @ClassName: Empj_BuildingInfoByListService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月7日 上午10:34:10
 * @version V1.0
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Empj_BuildingInfoByListService
{
	@Autowired
	private SessionFactory sessionFactory;

	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * 查询已托管且已签署托管合作协议的楼幢信息
		 * busiState=0 未托管状态
		 */
		// String sql = "select * from Empj_BuildingInfo where project=" +
		// model.getProjectId()
		//		+ " and busiState<>'0' and theState='0' and tableId in ( select empj_buildinginfo from Rel_EscrowAgreement_Building )";
		
		String sql = "select * from Empj_BuildingInfo where project=" + model.getProjectId()
		+ " and busiState<>'0' and theState='0'";

		
		List<Empj_BuildingInfo> empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();

		empj_BuildingInfoList = sessionFactory.getCurrentSession().createNativeQuery(sql, Empj_BuildingInfo.class)
				.getResultList();

		if (null == empj_BuildingInfoList || empj_BuildingInfoList.size() == 0)
		{
			return MyBackInfo.fail(properties, "项目ID:" + model.getProjectId() + "，未查询到有效的楼幢信息");
		}

		properties.put("empj_BuildingInfoList", empj_BuildingInfoList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
