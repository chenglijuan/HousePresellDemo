package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢-户室
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Empj_HouseInfoByEscListService
{
	@Autowired
	private SessionFactory sessionFactory;

	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * 查询所属指定楼幢下的所有户室信息
		 * 户室信息未签署三方协议
		 * 三方协议处于有效状态
		 */
		List<Empj_HouseInfo> empj_HouseInfoList = new ArrayList<Empj_HouseInfo>();
		String sql = "select * from empj_houseinfo where building = " + model.getBuildingId()
				+ " and theState='0' and tableId not in (select house from Tgxy_TripleAgreement where theState='0' and theStateOfTAE <> '3') ";

		empj_HouseInfoList = sessionFactory.getCurrentSession().createNativeQuery(sql, Empj_HouseInfo.class)
				.getResultList();

		properties.put("empj_HouseInfoList", empj_HouseInfoList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
