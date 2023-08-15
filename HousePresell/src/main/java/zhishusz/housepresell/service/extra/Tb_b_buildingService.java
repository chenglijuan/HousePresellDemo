package zhishusz.housepresell.service.extra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_buildingForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_buildingDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_roomDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.extra.Tb_b_building;
import zhishusz.housepresell.database.po.extra.Tb_b_buildingVO;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_HouseBusiState;
import zhishusz.housepresell.database.po.state.S_LandMortgageState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UnitType;
import zhishusz.housepresell.service.Sm_ApprovalProcessGetService;
import zhishusz.housepresell.service.Sm_ApprovalProcessService;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;

/**
 * 中间库-楼幢取数
 * 
 * @ClassName: Tb_b_buildingService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月26日 上午9:48:35
 * @version V1.0
 *
 */
@Service
@Transactional
public class Tb_b_buildingService
{
	private static final String BUSI_CODE_B = "030102";// 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	private static final String BUSI_CODE_U = "03010204";// 单元编码
	private static final String BUSI_CODE_H = "03010205";// 户室编码
	private static final String BUSI_CODE = "03010201";

	@Autowired
	private Tb_b_buildingDao dao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Tb_b_roomDao roomDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;

	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;// 托管标准
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;// 楼幢账户
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;// 楼幢扩展表
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;// 公共参数查询

	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;

	@SuppressWarnings("unchecked")
	public Properties execute(Tb_b_buildingForm model)
	{
		Properties properties = new MyProperties();
		try
		{
			// 获取请求类型
			String type = model.getType();

			switch (type)
			{
			case "listByProject":
				// // 根据项目rowguid查询楼幢列表
				// String projectid = model.getPROJECTID();
				//
				// if (null == projectid || projectid.trim().isEmpty())
				// {
				// return MyBackInfo.fail(properties, "请选择正确的项目信息");
				// }
				//
				// List<Tb_b_building> list = new ArrayList<Tb_b_building>();
				//
				// list = dao.getBuildingListByProjectId(projectid);
				//
				// properties.put("buildingList", list);

				break;

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
				else
				{

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

					
					String unitRemarks = "";
					// 循环遍历楼幢信息进行保存
					List<Empj_BuildingInfo> gasList = JSON.parseArray(saveBuildingList, Empj_BuildingInfo.class);
					for (Empj_BuildingInfo po : gasList)
					{
						unitRemarks = po.getRemark();
						po.setProject(projectInfo);
						po.setCityRegion(projectInfo.getCityRegion());
						po.setTheNameOfCityRegion(
								null == projectInfo.getCityRegion() ? "" : projectInfo.getCityRegion().getTheName());
						po.setStreetInfo(projectInfo.getStreet());
						po.setTheNameOfStreet(
								null == projectInfo.getStreet() ? "" : projectInfo.getStreet().getTheName());
						po.setTheNameOfProject(projectInfo.getTheName());
						po.setDevelopCompany(projectInfo.getDevelopCompany());
						po.seteCodeOfDevelopCompany(null == projectInfo.getDevelopCompany() ? ""
								: projectInfo.getDevelopCompany().geteCode());

						po.setCreateTimeStamp(System.currentTimeMillis());
						po.setLastUpdateTimeStamp(System.currentTimeMillis());
						po.setUserStart(user);
						po.setUserUpdate(user);
						po.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE_B));
						po.setTheState(S_TheState.Normal);
						po.setRemark("");
						po.setBuildingArea(null==po.getBuildingArea()?0.00:po.getBuildingArea());
						/*
						 * xsz by time 2018-11-23 19:20:23
						 * 导入时托管面积=建筑面积
						 * 
						 */
						po.setEscrowArea(po.getBuildingArea());
						

						// // 业务状态
						// po.setBusiState(S_BusiState.NoRecord);
						// // 审批流程状态
						// po.setApprovalState(S_ApprovalState.WaitSubmit);

						// ==========托管标准信息=start=================
						/*
						 * 查询最新的一条托管标准
						 */
						Tgpj_EscrowStandardVerMngForm mngModel = new Tgpj_EscrowStandardVerMngForm();
						mngModel.setTheState(S_TheState.Normal);
						mngModel.setBusiState(S_BusiState.HaveRecord);

						List<Tgpj_EscrowStandardVerMng> list = new ArrayList<Tgpj_EscrowStandardVerMng>();
						list = tgpj_EscrowStandardVerMngDao.findByPage(tgpj_EscrowStandardVerMngDao
								.getQuery(tgpj_EscrowStandardVerMngDao.getBasicHQL(), mngModel));
						if (null != list && list.size() > 0)
						{
							// 如果查询到有效的托管标准，取最新的一条
							Tgpj_EscrowStandardVerMng verMng = list.get(0);

							po.setEscrowStandardVerMng(verMng);
							if ("0".equals(verMng.getTheType()))
							{
								po.setEscrowStandard(MyString.getInstance().parse(verMng.getAmount()));
							}
							if ("1".equals(verMng.getTheType()))
							{
								po.setEscrowStandard(MyString.getInstance().parse(verMng.getPercentage()));
							}

						}
						else
						{
							return MyBackInfo.fail(properties, "未查询到有效的托管标准信息");
						}

						// ==========托管标准信息=end=================

						// ==========楼幢扩展信息=start=============
						Empj_BuildingExtendInfo empj_BuildingExtendInfo = new Empj_BuildingExtendInfo();

						empj_BuildingExtendInfo.setUserStart(user);
						empj_BuildingExtendInfo.setCreateTimeStamp(System.currentTimeMillis());
						empj_BuildingExtendInfo.setUserUpdate(user);
						empj_BuildingExtendInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
						empj_BuildingExtendInfo.setTheState(S_TheState.Normal);
						empj_BuildingExtendInfo.setLandMortgageState(S_LandMortgageState.No);
						empj_BuildingExtendInfo.setEscrowState(S_EscrowState.UnEscrowState);
						empj_BuildingExtendInfoDao.save(empj_BuildingExtendInfo);

						po.setExtendInfo(empj_BuildingExtendInfo);

						// ==========楼幢扩展信息=end=============

						// ===========楼幢账户信息=start=============
						Tgpj_BuildingAccount tgpj_BuildingAccount = new Tgpj_BuildingAccount();
						tgpj_BuildingAccount.setTheState(S_TheState.Normal);
						// tgpj_BuildingAccount.setBusiState(busiState);
						tgpj_BuildingAccount.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE_B));
						tgpj_BuildingAccount.setUserStart(user);
						tgpj_BuildingAccount.setCreateTimeStamp(System.currentTimeMillis());
						tgpj_BuildingAccount.setUserUpdate(user);
						tgpj_BuildingAccount.setLastUpdateTimeStamp(System.currentTimeMillis());
						tgpj_BuildingAccount.setDevelopCompany(projectInfo.getDevelopCompany());
						if (projectInfo.getDevelopCompany() != null)
						{
							tgpj_BuildingAccount.seteCodeOfDevelopCompany(projectInfo.getDevelopCompany().geteCode());
						}

						tgpj_BuildingAccount.setProject(projectInfo);
						tgpj_BuildingAccount.setTheNameOfProject(projectInfo.getTheName());
						tgpj_BuildingAccount.seteCodeOfBuilding(po.geteCode());
						tgpj_BuildingAccount.setEscrowStandard(po.getEscrowStandard());
						tgpj_BuildingAccount.setEscrowArea(po.getEscrowArea());
						tgpj_BuildingAccount.setBuildingArea(po.getBuildingArea());
						tgpj_BuildingAccount.setOrgLimitedAmount(0.0);
						tgpj_BuildingAccount.setCurrentFigureProgress("0%");
						tgpj_BuildingAccount.setCurrentLimitedRatio(1.0);
						tgpj_BuildingAccount.setNodeLimitedAmount(0.0);
						tgpj_BuildingAccount.setTotalGuaranteeAmount(0.0);
						tgpj_BuildingAccount.setCashLimitedAmount(0.0);
						tgpj_BuildingAccount.setEffectiveLimitedAmount(0.0);
						tgpj_BuildingAccount.setTotalAccountAmount(0.0);
						tgpj_BuildingAccount.setSpilloverAmount(0.0);
						tgpj_BuildingAccount.setPayoutAmount(0.0);
						tgpj_BuildingAccount.setAppliedNoPayoutAmount(0.0);
						tgpj_BuildingAccount.setApplyRefundPayoutAmount(0.0);
						tgpj_BuildingAccount.setRefundAmount(0.0);
						tgpj_BuildingAccount.setCurrentEscrowFund(0.0);
						tgpj_BuildingAccount.setAllocableAmount(0.0);
						tgpj_BuildingAccount.setRecordAvgPriceOfBuildingFromPresellSystem(0.0);
						tgpj_BuildingAccount.setRecordAvgPriceOfBuilding(0.0);
						// tgpj_BuildingAccount.setPaymentLines(paymentLines);
						// tgpj_BuildingAccount.setLogId(logId);

						/*
						 * 支付保证上限比例
						 * 通过公共参数查询：
						 * Parametertype=60
						 */
						Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
						paraModel.setParametertype("60");
						paraModel.setTheState(S_TheState.Normal);
						Sm_BaseParameter sm_BaseParameter = sm_BaseParameterDao.findOneByQuery_T(
								sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));

						if (null == sm_BaseParameter || sm_BaseParameter.getTheName().trim().isEmpty())
						{
							tgpj_BuildingAccount.setPaymentLines(50.00);
						}
						else
						{
							tgpj_BuildingAccount.setPaymentLines(Double.valueOf(sm_BaseParameter.getTheName().trim()));
						}

						tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);

						po.setBuildingAccount(tgpj_BuildingAccount);

						// ==================楼幢账户信息=end==========================

						/*
						 * xsz by time 2018-11-10 13:54:07
						 * 处理施工编号和公安编号：
						 * 如果施工编号为空，则将公安编号赋值为施工编号
						 * 
						 */
						if (null == po.geteCodeFromConstruction() || po.geteCodeFromConstruction().trim().isEmpty())
						{
							po.seteCodeFromConstruction(po.geteCodeFromPublicSecurity());
						}

						Serializable tableId = empj_BuildingInfoDao.save(po);

						// 提交审批流设置
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

						// ==============楼幢扩展信息关联=start============
						empj_BuildingExtendInfo.setBuildingInfo(po);
						empj_BuildingExtendInfoDao.save(empj_BuildingExtendInfo);
						// ==============楼幢扩展信息关联=end============

						// ===============楼幢账户信息关联=start================
						tgpj_BuildingAccount.setBuilding(po);
						tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);
						// ===============楼幢账户信息关联=end================

						/*
						 * 需要对remark字段进行分析，保存楼幢下的单元数据
						 * 住宅单元信息（例如：2,2,2 表示3个单元，每个单元2房间）
						 */
						String unitRemark = unitRemarks;
						if (null != unitRemark && !unitRemark.trim().isEmpty())
						{
							// 存在单元信息时，需要对单元进行判断保存至托管系统
							int n = unitRemark.length() - unitRemark.replaceAll(",", "").length() + 1;

							Empj_UnitInfo unitInfo;
							for (int i = 1; i <= n; i++)
							{
								unitInfo = new Empj_UnitInfo();
								String name = S_UnitType.getMsg(Integer.toString(i));

								po.setTableId(Long.parseLong(tableId.toString()));

								unitInfo.setTheName(name);
								unitInfo.setBuilding(po);
								unitInfo.seteCodeOfBuilding(po.geteCodeFromConstruction());

								unitInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE_U));
								unitInfo.setTheState(S_TheState.Normal);
								unitInfo.setBusiState(S_BusiState.HaveRecord);
								unitInfo.setCreateTimeStamp(System.currentTimeMillis());
								unitInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
								unitInfo.setUserStart(user);
								unitInfo.setUserUpdate(user);

								empj_UnitInfoDao.save(unitInfo);

							}

						}

						/*
						 * 需要对楼幢下的户室信息进行查询并保存
						 */
						List<Empj_HouseInfo> houseList = roomDao.getHouseList(po.getExternalId());

						if (null != houseList && houseList.size() > 0)
						{
							for (Empj_HouseInfo housePo : houseList)
							{

								// 设置关联楼幢信息
								po.setTableId(Long.parseLong(tableId.toString()));
								housePo.setBuilding(po);
								housePo.seteCodeOfBuilding(po.geteCodeFromConstruction());

								String remark = housePo.getRemark();
								Empj_UnitInfo unitInfo;
								// 判断如果字段为空或者是99，则不需要关联单元信息
								if (null != remark && !remark.trim().isEmpty() && !"99".equals(remark))
								{
									String name;
									try
									{
										name = S_UnitType.getMsg(housePo.getRemark());

										// 设置关联单元信息
										Empj_UnitInfoForm unitForm = new Empj_UnitInfoForm();
										unitForm.setBuilding(po);
										unitForm.setTheName(name);

										// 20181009
										// 已确认楼幢信息下的户室关联的单元信息一定存在，所以此时未进行非空校验
										unitInfo = (Empj_UnitInfo) empj_UnitInfoDao.findOneByQuery(
												empj_UnitInfoDao.getQuery(empj_UnitInfoDao.getBasicHQL(), unitForm));

										housePo.setUnitInfo(unitInfo);
										housePo.seteCodeOfUnitInfo(unitInfo.getTheName());
										housePo.setUnitNumber(housePo.getRemark());

									}
									catch (Exception e)
									{

									}

								}
								else
								{
									// 默认排序99
									housePo.setUnitNumber("99");
								}

								/*
								 * xsz by time 2018-11-15 13:42:19
								 * 设置户室的楼盘表信息
								 * rowNumber：所处楼层
								 * colNumber：所在户
								 * rowSpan：默认1
								 * colSpan：默认1
								 */
								String roomId = housePo.getRoomId();
								housePo.setRowNumber(housePo.getFloor().intValue());
								if (null == roomId || roomId.trim().isEmpty())
								{
									housePo.setColNumber(0);
								}
								else
								{
									housePo.setColNumber(Math.abs(
											Integer.parseInt(roomId.substring(roomId.length() - 2, roomId.length()))));
								}

								housePo.setRowSpan(1);
								housePo.setColSpan(1);
								housePo.setBusiState(S_HouseBusiState.Establish);
								housePo.setCreateTimeStamp(System.currentTimeMillis());
								housePo.setLastUpdateTimeStamp(System.currentTimeMillis());
								housePo.setUserStart(user);
								housePo.setUserUpdate(user);
								housePo.setTheState(S_TheState.Normal);
								housePo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE_H));
								housePo.seteCodeFromEscrowSystem(housePo.geteCode());

								housePo.seteCodeFromPublicSecurity(po.geteCodeFromPublicSecurity());

								empj_HouseInfoDao.save(housePo);

							}
						}

					}

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
		info.setUnitNumber(Integer.parseInt(vo.getUnitNumber()));
		info.setBuildingArea(Double.parseDouble(vo.getBuildingArea()));
		info.setUpfloorNumber(Double.parseDouble(vo.getUpfloorNumber()));
		info.setDownfloorNumber(Double.parseDouble(vo.getDownfloorNumber()));
		info.setEndDate(vo.getEndDate());
		info.setRemark(vo.getUnitInfo());
		info.seteCodeFromPresellCert(null==vo.getCertificatenumber()?"":vo.getCertificatenumber());//预售证号

		return info;
	}
}
