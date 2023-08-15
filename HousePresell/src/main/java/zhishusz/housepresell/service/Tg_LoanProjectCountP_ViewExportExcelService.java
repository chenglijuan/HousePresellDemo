package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Tg_LoanProjectCountP_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_LoanProjectCountP_ViewDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_LoanProjectCountP_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_LoanProjectCountP_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管项目统计表 - 导出Excel（项目部）
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_LoanProjectCountP_ViewExportExcelService
{
	private static final String excelName = "托管项目统计表（项目部）";

	@Autowired
	private Tg_LoanProjectCountP_ViewDao tg_LoanProjectCountP_ViewDao;

	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;

	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	private MyDouble myDouble = MyDouble.getInstance();

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_LoanProjectCountP_ViewForm model)
	{
		Properties properties = new MyProperties();

		// 获取查询条件
		// 获取查询条件
				String keyword = model.getKeyword();// 关键字
				Long cityRegionId = model.getCityRegionId();// 区域Id
				Long companyId = model.getCompanyId();// 企业Id
				Long projectId = model.getProjectId();// 项目Id

				String companyGroup = model.getCompanyGroup();// 集团
				String recordDateStart = model.getRecordDateStart().trim();// 合作协议备案时间（结束）
				String recordDateEnd = model.getRecordDateEnd().trim();// 合作协议备案时间（结束）		

				if (null == recordDateStart || recordDateStart.length() == 0) {
					model.setRecordDateStart(null);
				} else {
					model.setRecordDateStart(recordDateStart);
				}
				if (null == recordDateEnd || recordDateEnd.length() == 0) {
					model.setRecordDateEnd(null);
				} else {
					model.setRecordDateEnd(recordDateEnd);
				}
				if (null == companyGroup || companyGroup.length() == 0) {
					model.setCompanyGroup(null);
				} else {
					model.setCompanyGroup(companyGroup.trim());
				}

				if (null == cityRegionId || cityRegionId == 0) {
					model.setCityRegion(null);
				} else {
					Sm_CityRegionInfo sm_CityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
					model.setCityRegion(sm_CityRegionInfo.getTheName());
				}

				if (null == companyId || companyId == 0) {
					model.setCompanyName(null);
				} else {
					Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(companyId);
					model.setCompanyName(emmp_CompanyInfo.getTheName());
				}

				if (null == projectId || projectId == 0) {
					model.setProjectName(null);
				} else {
					Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
					model.setProjectName(empj_ProjectInfo.getTheName());
				}

				if (null == keyword || keyword.length() == 0) {
					model.setKeyword(null);
				} else {
					model.setKeyword("%" + keyword + "%");
				}

//		List<Tg_LoanProjectCountP_View> tg_LoanProjectCountP_ViewList = new ArrayList<Tg_LoanProjectCountP_View>();

		List<Tg_LoanProjectCountP_View> loanProjectCountPList = tg_LoanProjectCountP_ViewDao
				.findByPage(tg_LoanProjectCountP_ViewDao.getQuery(tg_LoanProjectCountP_ViewDao.getBasicHQL(), model));

		if (null != loanProjectCountPList && loanProjectCountPList.size() > 0)
		{
			if (loanProjectCountPList.size() > 7000)
			{
				return MyBackInfo.fail(properties, "查询结果过多，无法导出请进行适当筛选");
			}

		/*	// 获取托管楼幢均价
			Double recordAvgPriceOfBuilding = loanProjectCountPList.get(0).getRecordAvgPriceOfBuilding();
			// 当前受限额度
			Double currentLimitedAmount = loanProjectCountPList.get(0).getCurrentLimitedAmount();

			Tg_LoanProjectCountP_View loanProjectCountP = new Tg_LoanProjectCountP_View();

			loanProjectCountP.setCompanyGroup("小计：");
			loanProjectCountP.setRecordAvgPriceOfBuilding(recordAvgPriceOfBuilding);// 楼幢备案均价
			loanProjectCountP.setCurrentLimitedAmount(currentLimitedAmount);// 当前受限额度
			loanProjectCountP.setCityRegion("~");// 区域
			loanProjectCountP.setDeliveryType("~");// 交付类型
			loanProjectCountP.setUpTotalFloorNumber(null);// 地上总层数
			loanProjectCountP.setOrgLimitedAmount(null);// 初始受限额度
			loanProjectCountP.setCurrentBuildProgress("~");// 当前建设进度
			loanProjectCountP.setCurrentLimitedNote("~");// 当前受限节点
			loanProjectCountP.setSumFamilyNumber(null);// 总户数
			loanProjectCountP.setSignHouseNum(null);// 签约户数
			loanProjectCountP.setIsPresell("~");// 预售证（有/无）
			loanProjectCountP.setEscrowAgRecordTime("~");// 合作协议备案时间

			// 定义集合
			List<String> groupNameList = new ArrayList<String>();// 所属集团集合
			List<String> companyNameList = new ArrayList<String>();// 开发企业集合
			List<String> projectNameList = new ArrayList<String>();// 托管项目集合

			for (int i = 0; i < loanProjectCountPList.size(); i++)
			{
				Tg_LoanProjectCountP_View tg_LoanProjectCOuntPpo = loanProjectCountPList.get(i);

				String groupName = tg_LoanProjectCOuntPpo.getCompanyGroup();// 集团（原）
				String companyName = tg_LoanProjectCOuntPpo.getCompanyName();// 开发企业（原）
				String projectName = tg_LoanProjectCOuntPpo.getProjectName();// 托管项目（原）

				if (i == 0)
				{
					tg_LoanProjectCountP_ViewList.add(tg_LoanProjectCOuntPpo);

					// 获取需要合计的数据
					int companyCount = 1;// 开发企业
					int projectCount = 1;// 托管项目
					int buildingCount = 1;// 托管楼幢

					loanProjectCountP.setCompanyName(Integer.toString(companyCount));// 开发企业->开发企业数量
					loanProjectCountP.setProjectName(Integer.toString(projectCount));// 托管项目->托管项目数量
					loanProjectCountP.seteCodeFromConstruction(Integer.toString(buildingCount));// 托管楼幢->托管楼幢数量
					loanProjectCountP.setEscrowArea(tg_LoanProjectCOuntPpo.getEscrowArea());// 托管面积
					loanProjectCountP.setCurrentEscrowFund(tg_LoanProjectCOuntPpo.getCurrentEscrowFund());// 托管余额
					loanProjectCountP.setAmountOffset(tg_LoanProjectCOuntPpo.getAmountOffset());// 抵充额度
					loanProjectCountP.setRecordHouseNum(tg_LoanProjectCOuntPpo.getRecordHouseNum());// 备案户数
					loanProjectCountP.setDepositHouseNum(tg_LoanProjectCOuntPpo.getDepositHouseNum());// 托管户数

					groupNameList.add(groupName);
					companyNameList.add(companyName);
					projectNameList.add(projectName);

					if ((i + 1) == loanProjectCountPList.size())
					{
						tg_LoanProjectCountP_ViewList.add(loanProjectCountP);
					}
				}
				else
				{
					// 循环所属集团进行判断
					int groupSize = groupNameList.size();

					for (int j = 0; j < groupSize; j++)
					{
						String group = groupNameList.get(j);

						String groupNameNew = tg_LoanProjectCOuntPpo.getCompanyGroup();// 集团（新）

						if (groupNameNew.equals(group))
						{
							int companyCount = Integer.parseInt(loanProjectCountP.getCompanyName());// 开发企业数量
							int projectCount = Integer.parseInt(loanProjectCountP.getProjectName());// 托管项目数量
							int buildingCount = Integer.parseInt(loanProjectCountP.geteCodeFromConstruction());// 托管楼幢数量
							Double escrowArea = loanProjectCountP.getEscrowArea();// 托管面积小计
							Double currentEscrowFund = loanProjectCountP.getCurrentEscrowFund();// 托管余额小计
							Double amountOffset = loanProjectCountP.getAmountOffset();// 抵充额度小计
							Double recordHouseNum = loanProjectCountP.getRecordHouseNum();// 备案户数小计
							Double depositHouseNum = loanProjectCountP.getDepositHouseNum();// 托管户数小计

							String companyNameNew = tg_LoanProjectCOuntPpo.getCompanyName();// 开发企业（新）

							// 循环所属企业进行判断
							int companySize = companyNameList.size();

							for (int k = 0; k < companySize; k++)
							{
								String company = companyNameList.get(k);

								if (companyNameNew.equals(company))
								{
									String projectNameNew = tg_LoanProjectCOuntPpo.getProjectName();// 托管项目（新）

									// 循环所属项目进行判断
									int projectSize = projectNameList.size();

									for (int l = 0; l < projectSize; l++)
									{
										String project = projectNameList.get(l);

										if (projectNameNew.equals(project))
										{
											// 总计
											++buildingCount;
										}
										else
										{
											if ((l + 1) < projectSize)
											{
												continue;
											}

											projectNameList.add(projectName);
											++projectCount;
											++buildingCount;
										}
									}
								}
								else
								{
									if ((k + 1) < companySize)
									{
										continue;
									}

									companyNameList.add(companyName);
									++companyCount;
									++buildingCount;
								}
							}
							tg_LoanProjectCountP_ViewList.add(tg_LoanProjectCOuntPpo);

							// 小计
							escrowArea = myDouble.doubleAddDouble(escrowArea, tg_LoanProjectCOuntPpo.getEscrowArea());// 托管面积
							currentEscrowFund = myDouble.doubleAddDouble(currentEscrowFund,
									tg_LoanProjectCOuntPpo.getCurrentEscrowFund());// 托管余额
							amountOffset = myDouble.doubleAddDouble(amountOffset,
									tg_LoanProjectCOuntPpo.getAmountOffset());// 抵充额度
							recordHouseNum = myDouble.doubleAddDouble(recordHouseNum,
									tg_LoanProjectCOuntPpo.getRecordHouseNum());// 备案户数
							depositHouseNum = myDouble.doubleAddDouble(depositHouseNum,
									tg_LoanProjectCOuntPpo.getDepositHouseNum());// 托管户数

							loanProjectCountP.setCompanyName(Integer.toString(companyCount));// 开发企业->开发企业数量
							loanProjectCountP.setProjectName(Integer.toString(projectCount));// 托管项目->托管项目数量
							loanProjectCountP.seteCodeFromConstruction(Integer.toString(buildingCount));// 托管楼幢->托管楼幢数量
							loanProjectCountP.setEscrowArea(escrowArea);// 托管面积
							loanProjectCountP.setCurrentEscrowFund(currentEscrowFund);// 托管余额
							loanProjectCountP.setAmountOffset(amountOffset);// 抵充额度
							loanProjectCountP.setRecordHouseNum(recordHouseNum);// 备案户数
							loanProjectCountP.setDepositHouseNum(depositHouseNum);// 托管户数/

							if ((i + 1) == loanProjectCountPList.size())
							{
								tg_LoanProjectCountP_ViewList.add(loanProjectCountP);
							}
						}
						else
						{
							if ((j + 1) < groupSize)
							{
								continue;
							}

							tg_LoanProjectCountP_ViewList.add(loanProjectCountP);

							tg_LoanProjectCountP_ViewList.add(tg_LoanProjectCOuntPpo);

							loanProjectCountP = new Tg_LoanProjectCountP_View();
							loanProjectCountP.setCompanyGroup("小计：");
							loanProjectCountP.setRecordAvgPriceOfBuilding(recordAvgPriceOfBuilding);// 楼幢备案均价
							loanProjectCountP.setCurrentLimitedAmount(currentLimitedAmount);// 当前受限额度
							loanProjectCountP.setCityRegion("~");// 区域
							loanProjectCountP.setDeliveryType("~");// 交付类型
							loanProjectCountP.setUpTotalFloorNumber(null);// 地上总层数
							loanProjectCountP.setOrgLimitedAmount(null);// 初始受限额度
							loanProjectCountP.setCurrentBuildProgress("~");// 当前建设进度
							loanProjectCountP.setCurrentLimitedNote("~");// 当前受限节点
							loanProjectCountP.setSumFamilyNumber(null);// 总户数
							loanProjectCountP.setSignHouseNum(null);// 签约户数
							loanProjectCountP.setIsPresell("~");// 预售证（有/无）
							loanProjectCountP.setEscrowAgRecordTime("~");// 合作协议备案时间

							int companyCount = 1;// 开发企业
							int projectCount = 1;// 托管项目
							int buildingCount = 1;// 托管楼幢

							loanProjectCountP.setCompanyName(Integer.toString(companyCount));// 开发企业->开发企业数量
							loanProjectCountP.setProjectName(Integer.toString(projectCount));// 托管项目->托管项目数量
							loanProjectCountP.seteCodeFromConstruction(Integer.toString(buildingCount));// 托管楼幢->托管楼幢数量
							loanProjectCountP.setEscrowArea(tg_LoanProjectCOuntPpo.getEscrowArea());// 托管面积
							loanProjectCountP.setCurrentEscrowFund(tg_LoanProjectCOuntPpo.getCurrentEscrowFund());// 托管余额
							loanProjectCountP.setAmountOffset(tg_LoanProjectCOuntPpo.getAmountOffset());// 抵充额度
							loanProjectCountP.setRecordHouseNum(tg_LoanProjectCOuntPpo.getRecordHouseNum());// 备案户数
							loanProjectCountP.setDepositHouseNum(tg_LoanProjectCOuntPpo.getDepositHouseNum());// 托管户数

							groupNameList.add(groupName);
							companyNameList.add(companyName);
							projectNameList.add(projectName);

							if ((i + 1) == loanProjectCountPList.size())
							{
								tg_LoanProjectCountP_ViewList.add(loanProjectCountP);
							}
						}
					}
				}
			}*/

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil
					.createRelativePathWithDate("Tg_LoanProjectCountP_ViewExportExcelService");// 文件在项目中的相对路径
			String localPath = directoryUtil.getProjectRoot();// 项目路径

			String saveDirectory = localPath + relativeDir;// 文件在服务器文件系统中的完整路径

			if (saveDirectory.contains("%20"))
			{
				saveDirectory = saveDirectory.replace("%20", " ");
			}

			directoryUtil.mkdir(saveDirectory);

			String strNewFileName = excelName + "-"
					+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

			String saveFilePath = saveDirectory + strNewFileName;

			// 通过工具类创建writer
			ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);

			// 自定义字段别名
			writer.addHeaderAlias("ordinal", "");

			writer.addHeaderAlias("companyGroup", "集团");
			writer.addHeaderAlias("companyName", "开发企业");
			writer.addHeaderAlias("cityRegion", "区域");
			writer.addHeaderAlias("projectName", "托管项目");
			writer.addHeaderAlias("eCodeFromConstruction", "托管楼幢");

			writer.addHeaderAlias("deliveryType", "交付类型");
			writer.addHeaderAlias("upTotalFloorNumber", "地上总层数");
			writer.addHeaderAlias("escrowArea", "托管面积");
			writer.addHeaderAlias("recordAvgPriceOfBuilding", "托管楼幢备案均价");
			writer.addHeaderAlias("orgLimitedAmount", "初始受限额度");

			writer.addHeaderAlias("currentLimitedAmount", "当前受限额度");
			writer.addHeaderAlias("currentBuildProgress", "当前建设进度");
			writer.addHeaderAlias("currentLimitedNote", "当前受限节点");
			writer.addHeaderAlias("currentEscrowFund", "托管余额");
			writer.addHeaderAlias("amountOffset", "抵充额度");

			writer.addHeaderAlias("sumFamilyNumber", "总户数");
			writer.addHeaderAlias("signHouseNum", "签约户数");
			writer.addHeaderAlias("recordHouseNum", "备案户数");
			writer.addHeaderAlias("depositHouseNum", "托管户数");
			writer.addHeaderAlias("isPresell", "预售证（有/无）");

			writer.addHeaderAlias("escrowAgRecordTime", "合作协议备案时间");

			List<Tg_LoanProjectCountP_ViewExportExcelVO> list = formart(loanProjectCountPList);
			// 一次性写出内容，使用默认样式
			writer.write(list);

			// 关闭writer，释放内存
			writer.flush();
			
			writer.autoSizeColumn(0, true);
			writer.autoSizeColumn(1, true);
			writer.autoSizeColumn(2, true);
			writer.autoSizeColumn(3, true);
			writer.autoSizeColumn(4, true);
			
			writer.autoSizeColumn(5, true);
			writer.autoSizeColumn(6, true);
			writer.autoSizeColumn(7, true);
			writer.autoSizeColumn(8, true);
			writer.autoSizeColumn(9, true);
			
			writer.autoSizeColumn(10, true);
			writer.autoSizeColumn(11, true);
			writer.autoSizeColumn(12, true);
			writer.autoSizeColumn(13, true);
			writer.autoSizeColumn(14, true);
			
			writer.autoSizeColumn(15, true);
			writer.autoSizeColumn(16, true);
			writer.autoSizeColumn(17, true);
			writer.autoSizeColumn(18, true);
			writer.autoSizeColumn(19, true);
			
			writer.autoSizeColumn(20, true);
			writer.autoSizeColumn(21, true);
			
			writer.close();

			properties.put("fileName", strNewFileName);
			properties.put("fileURL", relativeDir + strNewFileName);
			properties.put("fullFileName", saveFilePath);
		}
		else
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据。");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);

		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * po 格式化
	 * 
	 * @param tg_LoanProjectCountP_ViewList
	 * @return
	 */
	List<Tg_LoanProjectCountP_ViewExportExcelVO> formart(List<Tg_LoanProjectCountP_View> tg_LoanProjectCountP_ViewList)
	{
		List<Tg_LoanProjectCountP_ViewExportExcelVO> tg_LoanProjectCountP_ViewExportExcelList = new ArrayList<Tg_LoanProjectCountP_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_LoanProjectCountP_View tg_LoanProjectCountP_View : tg_LoanProjectCountP_ViewList)
		{
			++ordinal;
			Tg_LoanProjectCountP_ViewExportExcelVO tg_LoanProjectCountP_ViewExportExcel = new Tg_LoanProjectCountP_ViewExportExcelVO();
			tg_LoanProjectCountP_ViewExportExcel.setOrdinal(ordinal);

			tg_LoanProjectCountP_ViewExportExcel.setCompanyGroup(tg_LoanProjectCountP_View.getCompanyGroup());
			tg_LoanProjectCountP_ViewExportExcel.setCompanyName(tg_LoanProjectCountP_View.getCompanyName());
			tg_LoanProjectCountP_ViewExportExcel.setCityRegion(tg_LoanProjectCountP_View.getCityRegion());
			tg_LoanProjectCountP_ViewExportExcel.setProjectName(tg_LoanProjectCountP_View.getProjectName());
			tg_LoanProjectCountP_ViewExportExcel
					.setECodeFromConstruction(tg_LoanProjectCountP_View.geteCodeFromConstruction());
			tg_LoanProjectCountP_ViewExportExcel.setDeliveryType(tg_LoanProjectCountP_View.getDeliveryType());
			tg_LoanProjectCountP_ViewExportExcel
					.setUpTotalFloorNumber(tg_LoanProjectCountP_View.getUpTotalFloorNumber());
			tg_LoanProjectCountP_ViewExportExcel.setEscrowArea(tg_LoanProjectCountP_View.getEscrowArea());
			tg_LoanProjectCountP_ViewExportExcel
					.setRecordAvgPriceOfBuilding(tg_LoanProjectCountP_View.getRecordAvgPriceOfBuilding());
			tg_LoanProjectCountP_ViewExportExcel.setOrgLimitedAmount(tg_LoanProjectCountP_View.getOrgLimitedAmount());
			tg_LoanProjectCountP_ViewExportExcel
					.setCurrentLimitedAmount(tg_LoanProjectCountP_View.getCurrentLimitedAmount());
			tg_LoanProjectCountP_ViewExportExcel
					.setCurrentBuildProgress(tg_LoanProjectCountP_View.getCurrentBuildProgress());
			tg_LoanProjectCountP_ViewExportExcel
					.setCurrentLimitedNote(tg_LoanProjectCountP_View.getCurrentLimitedNote());
			tg_LoanProjectCountP_ViewExportExcel.setCurrentEscrowFund(tg_LoanProjectCountP_View.getCurrentEscrowFund());
			tg_LoanProjectCountP_ViewExportExcel.setAmountOffset(tg_LoanProjectCountP_View.getAmountOffset());
			tg_LoanProjectCountP_ViewExportExcel.setSumFamilyNumber(tg_LoanProjectCountP_View.getSumFamilyNumber());
			tg_LoanProjectCountP_ViewExportExcel.setSignHouseNum(tg_LoanProjectCountP_View.getSignHouseNum());
			tg_LoanProjectCountP_ViewExportExcel.setRecordHouseNum(tg_LoanProjectCountP_View.getRecordHouseNum());
			tg_LoanProjectCountP_ViewExportExcel.setDepositHouseNum(tg_LoanProjectCountP_View.getDepositHouseNum());
			tg_LoanProjectCountP_ViewExportExcel.setIsPresell(tg_LoanProjectCountP_View.getIsPresell());
			tg_LoanProjectCountP_ViewExportExcel
					.setEscrowAgRecordTime(tg_LoanProjectCountP_View.getEscrowAgRecordTime());

			tg_LoanProjectCountP_ViewExportExcelList.add(tg_LoanProjectCountP_ViewExportExcel);
		}
		return tg_LoanProjectCountP_ViewExportExcelList;
	}
}
