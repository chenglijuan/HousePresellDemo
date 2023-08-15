package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.controller.form.extra.Tgxy_JumpForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：从楼盘表楼幢信息，直接跳转签署合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_BuildJumpEscrowService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;// 楼幢
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;// 户室
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;// 三方协议
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;

	public Properties execute(Tgxy_JumpForm model)
	{
		Properties properties = new MyProperties();

		// return MyBackInfo.fail(properties, "该合同已签署有效的三方协议信息");

		Tgxy_JumpForm vo = new Tgxy_JumpForm();

		/*
		 * xsz by time 2018-9-29 11:32:45
		 * 根据楼盘楼幢跳转签署合作协议
		 * 1.根据楼幢id查询楼幢信息
		 * 校验楼幢是否处于正常态（有效），且未签署过有效合作协议
		 * 2.根据楼幢查询所属项目
		 * 3.根据项目加载项目下有效且未签署合作协议的楼幢列表
		 * 校验：楼幢未签署有效合作协议，楼幢处于有效状态
		 * 
		 * 带出数据：
		 * 项目名称、开发企业名称、项目所属区域
		 */
		String buildingid = model.getBuildingid();

		if (null == buildingid || buildingid.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择有效的楼幢信息");
		}

		Empj_BuildingInfo info = empj_BuildingInfoDao.findById(new Long(buildingid));

		if (null == info)
		{
			return MyBackInfo.fail(properties, "未查询到有效的楼幢信息");
		}

		// 校验该楼幢是否已签署有效的合作协议
		// String sql = "select * from Empj_BuildingInfo where tableId=" +
		// info.getTableId()
		// + " and busiState='0' and theState='0' and tableId in ( select
		// empj_buildinginfo from Rel_EscrowAgreement_Building )";

		String sql = "select * from Empj_BuildingInfo where tableId=" + info.getTableId()
				+ " and theState='0' and tableId in ( select empj_buildinginfo from Rel_EscrowAgreement_Building )";

		List<Empj_BuildingInfo> empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();

		empj_BuildingInfoList = sessionFactory.getCurrentSession().createNativeQuery(sql, Empj_BuildingInfo.class)
				.getResultList();

		if (null != empj_BuildingInfoList && empj_BuildingInfoList.size() > 0)
		{
			return MyBackInfo.fail(properties, "该楼幢已签署有效的合作协议信息");
		}

		// 设置选中状态楼幢
		vo.setBuildingInfoId(info.getTableId());

		// 查询该楼幢下的项目信息
		Empj_ProjectInfo project = info.getProject();
		if (null == project)
		{
			return MyBackInfo.fail(properties, "未查询到楼幢所属的项目信息");
		}

		// 获取项目所属信息 项目名称、开发企业名称、项目所属区域
		Long projectId = project.getTableId();
		String projectName = project.getTheName();
		// 关联开发企业
		Emmp_CompanyInfo company = project.getDevelopCompany();

		// 所属区域
		Sm_CityRegionInfo cityRegion = project.getCityRegion();

		vo.setProjectId(projectId);
		vo.setProjectName(projectName);

		if (null == company)
		{
			vo.setDevelopCompanyName(" ");
		}
		else
		{
			vo.setDevelopCompanyName(company.getTheName());
		}

		if (null == cityRegion)
		{
			vo.setCityRegionName(" ");
		}
		else
		{
			vo.setCityRegionName(cityRegion.getTheName());
		}

		// 查询指定项目下的有效楼幢列表
		// 查询已备案且未签署托管合作协议的楼幢信息
		// String sql1 = "select * from Empj_BuildingInfo where project=" +
		// model.getProjectId()
		// + " and busiState='0' and theState='0' and tableId not in ( select
		// empj_buildinginfo from Rel_EscrowAgreement_Building )";

		String sql1 = "select * from Empj_BuildingInfo where project=" + projectId
				+ " and theState='0' and tableId not in ( select empj_buildinginfo from Rel_EscrowAgreement_Building )";

		List<Empj_BuildingInfo> empj_BuildingInfoList1 = new ArrayList<Empj_BuildingInfo>();

		empj_BuildingInfoList1 = sessionFactory.getCurrentSession().createNativeQuery(sql1, Empj_BuildingInfo.class)
				.getResultList();

		properties.put("tgxy_EscrowAgreement", vo);
		properties.put("empj_BuildingInfoList", empj_BuildingInfoList1);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
