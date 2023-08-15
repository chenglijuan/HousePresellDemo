package zhishusz.housepresell.service;

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
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;

/**
 * 
 * @ClassName: Empj_BuildingInfoByEscListService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年8月30日 下午8:02:17
 * @version V1.0
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Empj_BuildingInfoByEscListService
{
	@Autowired
	private SessionFactory sessionFactory;

	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();

		// 查询已备案且未签署托管合作协议的楼幢信息
//		String sql = "select * from Empj_BuildingInfo where project=" + model.getProjectId()
//				+ " and (busiState is null or busiState='0') and theState='0' and tableId not in ( select empj_buildinginfo from Rel_EscrowAgreement_Building )";
		
		String sql = "select sa.* from Empj_BuildingInfo sa,Empj_BuildingExtendInfo sb where sa.tableid=sb.buildinginfo and (sb.escrowstate is null or sb.escrowstate='"+S_EscrowState.UnEscrowState+"') and sa.project="+model.getProjectId()+" and (sa.busiState is null or sa.busiState='"+S_BusiState.HaveRecord+"') and sa.theState='0' and sa.tableId not in ( select empj_buildinginfo from Rel_EscrowAgreement_Building ) order by to_number(regexp_substr(sa.ecodefromconstruction,'[0-9]*[0-9]',1))";
		
		List<Empj_BuildingInfo> empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();

		empj_BuildingInfoList = sessionFactory.getCurrentSession().createNativeQuery(sql, Empj_BuildingInfo.class)
				.getResultList();

		properties.put("empj_BuildingInfoList", empj_BuildingInfoList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
