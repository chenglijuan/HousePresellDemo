package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_buildingForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_buildingDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.Tb_b_building;
import zhishusz.housepresell.database.po.extra.Tb_b_buildingVO;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_ApprovalProcessGetService;
import zhishusz.housepresell.service.Sm_ApprovalProcessService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 中间库-楼幢取数
 * 
 * @ClassName: Tb_b_buildingService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月26日 上午9:48:35
 * @version V1.0
 * 
 * @author: xushizhong
 * @date: 2018年11月28日14:23:24
 * @version V2.0
 *          楼幢导入时采用调用存储过程导入数据
 * 
 *          修改方法save()
 *
 */
@Service
@Transactional
public class Tb_b_buildingProcedureService
{
	private static final String BUSI_CODE = "03010201";
	private static final Integer BUILDING_MAX = 100;// 最大楼幢导入数量

	@Autowired
	private Tb_b_buildingDao dao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;

	public Properties execute(Tb_b_buildingForm model)
	{
		Properties properties = new MyProperties();
		try
		{
			// 获取请求类型
			String type = model.getType();

			switch (type)
			{

			case "detail":

				// 根据楼幢rowguid查询项目详情
				String rowguid = model.getROWGUID();

				if (null == rowguid || rowguid.trim().isEmpty())
				{
					return MyBackInfo.fail(properties, "请选择楼幢");
				}

				Tb_b_building vo = new Tb_b_building();

				vo = dao.getBuildingDetail(rowguid);

				properties.put("buildingDetail", vo);

				break;

			case "list":

				// 根据项目rowguid和施工编号查询楼幢列表
				String projectid1 = model.getPROJECTID();

				if (null == projectid1 || projectid1.trim().isEmpty())
				{
					return MyBackInfo.fail(properties, "请选择正确的项目信息");
				}

				List<Tb_b_buildingVO> list1 = new ArrayList<Tb_b_buildingVO>();

				String buildingid = model.getBUILDINGNO();
				if (null == buildingid || buildingid.trim().isEmpty())
				{
					list1 = dao.getBuildingListByProjectId(projectid1);
				}
				else
				{
					list1 = dao.getBuildingListByProjectId(projectid1, buildingid);
				}

				// 查询本系统中从预售导入的楼幢信息
				List<Empj_BuildingInfo> infoList = new ArrayList<Empj_BuildingInfo>();
				Empj_BuildingInfo info;

				// 查询到有效的预售楼幢信息
				if (null != list1 && list1.size() > 0)
				{

					List<Empj_BuildingInfo> buildingInfoList = new ArrayList<Empj_BuildingInfo>();

					String sql = "select * from empj_buildinginfo eb where eb.thestate='0' and eb.externalid is not null";

					buildingInfoList = sessionFactory.getCurrentSession()
							.createNativeQuery(sql, Empj_BuildingInfo.class).getResultList();

					if (null != buildingInfoList && buildingInfoList.size() > 0)
					{

						boolean isAdd = true;
						for (Tb_b_buildingVO vo1 : list1)
						{

							for (Empj_BuildingInfo empj_BuildingInfo : buildingInfoList)
							{

								if (vo1.getExternalId().equals(empj_BuildingInfo.getExternalId()))
								{
									isAdd = false;
									break;
								}
								else
								{
									isAdd = true;
								}
							}

							if (isAdd)
							{
								info = new Empj_BuildingInfo();
								info = init(vo1);
								infoList.add(info);

							}

						}

					}
					else
					{
						for (Tb_b_buildingVO vo1 : list1)
						{
							info = new Empj_BuildingInfo();
							info = init(vo1);

							infoList.add(info);
						}
					}

				}
				else
				{
					return MyBackInfo.fail(properties, "未查询到有效的楼幢信息");
				}

				properties.put("buildingList", infoList);

				break;

			case "save":

				Sm_User user = model.getUser();
				if (null == user)
				{
					return MyBackInfo.fail(properties, "请先进行登录");
				}

				// 获取需要保存的楼幢信息
				String saveBuildingList = model.getSaveBuildingList();
				if (null == saveBuildingList || saveBuildingList.trim().isEmpty())
				{
					return MyBackInfo.fail(properties, "请选择需要导入的楼幢信息");
				}

				// 判断是否有对应的审批流程配置
				properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
				if (!"noApproval".equals(properties.getProperty("info"))
						&& "fail".equals(properties.getProperty("result")))
				{
					return properties;
				}

				// 根据选择的本地项目主键查询项目信息
				String projectLocalId = model.getProjectLocalId();
				if (null == projectLocalId || projectLocalId.trim().isEmpty())
				{
					return MyBackInfo.fail(properties, "请选择本地项目信息信息");
				}

				Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(Long.parseLong(projectLocalId));
				if (null == projectInfo || projectInfo.getTheName().trim().isEmpty())
				{
					return MyBackInfo.fail(properties, "未查询到有效的项目信息");
				}

				// 获取前端传递导入楼幢的信息
				List<Empj_BuildingInfo> gasList = JSON.parseArray(saveBuildingList, Empj_BuildingInfo.class);
				/*
				 * xsz by time 2018-11-28 14:27:05
				 * 此处需要判断导入楼幢大小：
				 * 调用存储过程的传入参数最多只有4000，
				 * 需要对导入的楼幢数量做限制
				 * 
				 * 现阶段最多导入100幢楼
				 * 
				 */
				if (gasList.size() > BUILDING_MAX)
				{
					return MyBackInfo.fail(properties, "导入楼幢数量超出限制，最多导入：" + BUILDING_MAX + "幢楼");
				}

				// 对需要导入的楼幢进行中间库buildingid的拼接
				StringBuffer idbf = new StringBuffer();
				for (Empj_BuildingInfo buildingInfo : gasList)
				{
					idbf.append("," + buildingInfo.getExternalId());
				}

				// 获取最终拼接好的需要导入的楼幢信息
//				String externalIdStr = idbf.toString().substring(1, idbf.toString().length());
				String externalIdStr = idbf.substring(1).toString();

				// 调用存储过程(返回拼接好的导入楼幢tableId拼接)
				String buildingImportId = empj_BuildingInfoDao.buildingImport(projectInfo.getTableId(), externalIdStr,
						model.getUserId());
				if (null == buildingImportId || buildingImportId.trim().isEmpty())
				{
					return MyBackInfo.fail(properties, "未接收到返回的楼幢信息");
				}

				// 对返回的信息进行判断（如果包含;，说明调用存储过程发生异常，将异常信息抛出）
				if (buildingImportId.contains(";"))
				{
//					return MyBackInfo.fail(properties, buildingImportId.substring(1, idbf.toString().length()));
				    return MyBackInfo.fail(properties, "导入楼幢信息异常！");
				}

				if (buildingImportId.contains(","))
				{
					// 多个楼幢信息
					String[] strings = buildingImportId.split(",");
					Empj_BuildingInfo empj_BuildingInfo;
					for (String tableId : strings)
					{
						// 循环遍历楼幢进行审批流提交
						empj_BuildingInfo = empj_BuildingInfoDao.findById(Long.parseLong(tableId));
						if (null == empj_BuildingInfo)
						{
							return MyBackInfo.fail(properties, "未查询到楼幢信息：" + tableId);
						}

						// 提交审批流
						commitBuildingApproval(model, properties, empj_BuildingInfo);
					}
				}
				else
				{
					// 一条楼幢信息
					Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao
							.findById(Long.parseLong(buildingImportId));
					if (null == empj_BuildingInfo)
					{
						return MyBackInfo.fail(properties, "未查询到楼幢信息：" + buildingImportId);
					}

					// 提交审批流
					commitBuildingApproval(model, properties, empj_BuildingInfo);
				}

				break;

			default:

				return MyBackInfo.fail(properties, "请选择正确的请求类型");

			}
		}
		catch (Exception e)
		{
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
			properties.put("log", e.getMessage());
			properties.put("logger", e);

			return properties;
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * xsz by time 楼幢导入提交审批流程
	 * 
	 * @param model
	 * @param properties
	 * @param po
	 */
	private void commitBuildingApproval(Tb_b_buildingForm model, Properties properties, Empj_BuildingInfo po)
	{
		if (!"noApproval".equals(properties.getProperty(S_NormalFlag.info)))
		{
			po.setBusiState(S_BusiState.NoRecord);
			empj_BuildingInfoDao.save(po);
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
					.get("sm_approvalProcess_cfg");

			/*
			 * 创建提交审批的条件
			 * 如果需要审批流程，则创建模拟新建楼幢的model,以便发起审批
			 * 需要注意的是参数有：
			 * buttonType：1: 保存按钮 2. 提交按钮
			 * user及userId
			 */
			Empj_BuildingInfoForm buildModel = new Empj_BuildingInfoForm();
			buildModel.setButtonType("1");
			buildModel.setUser(model.getUser());
			buildModel.setUserId(model.getUserId());

			buildModel.setProject(po.getProject());
			buildModel.setCityRegion(po.getCityRegion());
			buildModel.setTheNameOfCityRegion(po.getTheNameOfCityRegion());
			buildModel.setStreetInfo(po.getStreetInfo());
			buildModel.setTheNameOfStreet(po.getTheNameOfStreet());
			buildModel.setTheNameOfProject(po.getTheNameOfProject());
			buildModel.setDevelopCompany(po.getDevelopCompany());
			buildModel.seteCodeOfDevelopCompany(po.geteCodeOfDevelopCompany());

			buildModel.setCreateTimeStamp(po.getCreateTimeStamp());
			buildModel.setLastUpdateTimeStamp(po.getLastUpdateTimeStamp());
			buildModel.setUserStart(po.getUserStart());
			buildModel.setUserUpdate(po.getUserUpdate());
			buildModel.seteCode(po.geteCode());
			buildModel.setTheState(po.getTheState());

			buildModel.setExternalId(po.getExternalId());
			buildModel.seteCodeOfProjectFromPresellSystem(po.geteCodeOfProjectFromPresellSystem());
			buildModel.setTheNameFromPresellSystem(po.getTheNameFromPresellSystem());
			buildModel.seteCodeFromPresellSystem(po.geteCodeFromPresellSystem());
			buildModel.seteCodeFromConstruction(po.geteCodeFromConstruction());
			buildModel.seteCodeFromPublicSecurity(po.geteCodeFromPublicSecurity());
			buildModel.setUnitNumber(po.getUnitNumber());
			buildModel.setBuildingArea(po.getBuildingArea());
			buildModel.setUpfloorNumber(po.getUpfloorNumber());
			buildModel.setDownfloorNumber(po.getDownfloorNumber());
			buildModel.setEndDate(po.getEndDate());

			// 审批操作
			sm_approvalProcessService.execute(po, buildModel, sm_approvalProcess_cfg);

		}
		else
		{
			// 备案人，备案日期，备案状态，审批状态
			po.setApprovalState(S_ApprovalState.Completed);
			po.setBusiState(S_BusiState.HaveRecord);
			po.setUserRecord(model.getUser());
			po.setRecordTimeStamp(System.currentTimeMillis());
			empj_BuildingInfoDao.save(po);
		}
	}

	public Empj_BuildingInfo init(Tb_b_buildingVO vo)
	{
		// 预售系统项目编号 预售项目名称 施工编号（幢号） 公安编号 单元数 地上层数 地下层数 建筑面积（总建筑面积）
		// 住宅面积（套内面积）预售系统楼幢编号
		Empj_BuildingInfo info = new Empj_BuildingInfo();

		info.setExternalId(vo.getExternalId());
		info.seteCodeOfProjectFromPresellSystem(vo.getECodeOfPjFromPS());
		info.setTheNameFromPresellSystem(vo.getTheNameFromPresellSystem());
		info.seteCodeFromPresellSystem(vo.getECodeFromPresellSystem());
		info.seteCodeFromConstruction(vo.getECodeFromConstruction());
		info.seteCodeFromPublicSecurity(vo.getECodeFromPublicSecurity());
		/*
		 * info.setUnitNumber(Integer.parseInt(vo.getUnitNumber()));
		 * info.setBuildingArea(Double.parseDouble(vo.getBuildingArea()));
		 * info.setUpfloorNumber(Double.parseDouble(vo.getUpfloorNumber()));
		 * info.setDownfloorNumber(Double.parseDouble(vo.getDownfloorNumber()));
		 */
		info.setUnitNumber(Integer.parseInt(null == vo.getUnitNumber() ? "1" : vo.getUnitNumber()));
		info.setBuildingArea(Double.parseDouble(null == vo.getBuildingArea() ? "0" : vo.getBuildingArea()));
		info.setUpfloorNumber(Double.parseDouble(null == vo.getUpfloorNumber() ? "1" : vo.getUpfloorNumber()));
		info.setDownfloorNumber(Double.parseDouble(null == vo.getDownfloorNumber() ? "0" : vo.getDownfloorNumber()));
		info.setEndDate(vo.getEndDate());
		info.setRemark(vo.getUnitInfo());
		info.seteCodeFromPresellCert(null == vo.getCertificatenumber() ? "" : vo.getCertificatenumber());// 预售证号

		return info;
	}
}
