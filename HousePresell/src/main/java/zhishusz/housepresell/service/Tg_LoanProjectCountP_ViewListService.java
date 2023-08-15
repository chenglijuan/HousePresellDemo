package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import zhishusz.housepresell.util.Checker;
//import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管项目统计表（项目部）
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_LoanProjectCountP_ViewListService {
	@Autowired
	private Tg_LoanProjectCountP_ViewDao tg_LoanProjectCountP_ViewDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

//	private MyDouble myDouble = MyDouble.getInstance();

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_LoanProjectCountP_ViewForm model) {
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

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

		Integer totalCount = tg_LoanProjectCountP_ViewDao.findByPage_Size(
				tg_LoanProjectCountP_ViewDao.getQuery_Size(tg_LoanProjectCountP_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tg_LoanProjectCountP_View> tg_LoanProjectCountP_ViewList = new ArrayList<Tg_LoanProjectCountP_View>();
		if (totalCount > 0) {
			tg_LoanProjectCountP_ViewList = tg_LoanProjectCountP_ViewDao.findByPage(
					tg_LoanProjectCountP_ViewDao.getQuery(tg_LoanProjectCountP_ViewDao.getBasicHQL(), model),
					pageNumber, countPerPage);
		}
		// 总计
		Map<String, Object> accountHouseMap = new HashMap<String, Object>();
		/*
		 * Integer acountGroup = 0;// 总计集团 Integer accountCompany = 0;// 总计公司 Integer
		 * accountProject = 0;// 总计项目 Integer accountBuilding = 0;// 总计楼幢 Double
		 * accountEscrowArea = 0.0;// 总计托管面积 Double accountRecordAvgPriceOfBuilding =
		 * 0.0;// 总计楼幢备案均价 Double accountCurrentLimitedAmount = 0.0;// 总计当前受限额度 Double
		 * accountCurrentEscrowFund = 0.0;// 总计托管余额 Double accountAmountOffset = 0.0;//
		 * 总计抵充额度 Double accountRecordHouseNum = 0.0;// 总计备案户数 Double
		 * accountDepositHouseNum = 0.0;// 总计托管户数
		 * 
		 * if (totalCount > 0) { List<Tg_LoanProjectCountP_View> loanProjectCountPList =
		 * tg_LoanProjectCountP_ViewDao.findByPage(
		 * tg_LoanProjectCountP_ViewDao.getQuery(tg_LoanProjectCountP_ViewDao.
		 * getBasicHQL(), model), pageNumber, countPerPage);
		 * 
		 * if (null != loanProjectCountPList && loanProjectCountPList.size() > 0) { //
		 * 获取托管楼幢均价 Double recordAvgPriceOfBuilding =
		 * loanProjectCountPList.get(0).getRecordAvgPriceOfBuilding(); // 当前受限额度 Double
		 * currentLimitedAmount =
		 * loanProjectCountPList.get(0).getCurrentLimitedAmount();
		 * 
		 * Tg_LoanProjectCountP_View loanProjectCountP = new
		 * Tg_LoanProjectCountP_View();
		 * 
		 * loanProjectCountP.setCompanyGroup("小计：");
		 * loanProjectCountP.setRecordAvgPriceOfBuilding(recordAvgPriceOfBuilding);//
		 * 楼幢备案均价 loanProjectCountP.setCurrentLimitedAmount(currentLimitedAmount);//
		 * 当前受限额度 loanProjectCountP.setCityRegion("~");// 区域
		 * loanProjectCountP.setDeliveryType("~");// 交付类型
		 * loanProjectCountP.setUpTotalFloorNumber(null);// 地上总层数
		 * loanProjectCountP.setOrgLimitedAmount(null);// 初始受限额度
		 * loanProjectCountP.setCurrentBuildProgress("~");// 当前建设进度
		 * loanProjectCountP.setCurrentLimitedNote("~");// 当前受限节点
		 * loanProjectCountP.setSumFamilyNumber(null);// 总户数
		 * loanProjectCountP.setSignHouseNum(null);// 签约户数
		 * loanProjectCountP.setIsPresell("~");// 预售证（有/无）
		 * loanProjectCountP.setEscrowAgRecordTime("~");// 合作协议备案时间
		 * 
		 * // 定义集合 List<String> groupNameList = new ArrayList<String>();// 所属集团集合
		 * List<String> companyNameList = new ArrayList<String>();// 开发企业集合 List<String>
		 * projectNameList = new ArrayList<String>();// 托管项目集合
		 * 
		 * for (int i = 0; i < loanProjectCountPList.size(); i++) {
		 * Tg_LoanProjectCountP_View tg_LoanProjectCOuntPpo =
		 * loanProjectCountPList.get(i);
		 * 
		 * String groupName = tg_LoanProjectCOuntPpo.getCompanyGroup();// 集团（原） String
		 * companyName = tg_LoanProjectCOuntPpo.getCompanyName();// 开发企业（原） String
		 * projectName = tg_LoanProjectCOuntPpo.getProjectName();// 托管项目（原）
		 * 
		 * if (i == 0) { tg_LoanProjectCountP_ViewList.add(tg_LoanProjectCOuntPpo);
		 * 
		 * // 获取需要合计的数据 int companyCount = 1;// 开发企业 int projectCount = 1;// 托管项目 int
		 * buildingCount = 1;// 托管楼幢
		 * 
		 * loanProjectCountP.setCompanyName(Integer.toString(companyCount));//
		 * 开发企业->开发企业数量
		 * loanProjectCountP.setProjectName(Integer.toString(projectCount));//
		 * 托管项目->托管项目数量
		 * loanProjectCountP.seteCodeFromConstruction(Integer.toString(buildingCount));/
		 * / 托管楼幢->托管楼幢数量
		 * loanProjectCountP.setEscrowArea(tg_LoanProjectCOuntPpo.getEscrowArea());//
		 * 托管面积 loanProjectCountP.setCurrentEscrowFund(tg_LoanProjectCOuntPpo.
		 * getCurrentEscrowFund());// 托管余额
		 * loanProjectCountP.setAmountOffset(tg_LoanProjectCOuntPpo.getAmountOffset());/
		 * / 抵充额度
		 * loanProjectCountP.setRecordHouseNum(tg_LoanProjectCOuntPpo.getRecordHouseNum(
		 * ));// 备案户数 loanProjectCountP.setDepositHouseNum(tg_LoanProjectCOuntPpo.
		 * getDepositHouseNum());// 托管户数
		 * 
		 * groupNameList.add(groupName); companyNameList.add(companyName);
		 * projectNameList.add(projectName);
		 * 
		 * // 总计 ++acountGroup; ++accountCompany; ++accountProject; ++accountBuilding;
		 * accountEscrowArea = myDouble.doubleAddDouble(accountEscrowArea,
		 * tg_LoanProjectCOuntPpo.getEscrowArea()); accountRecordAvgPriceOfBuilding =
		 * recordAvgPriceOfBuilding; accountCurrentLimitedAmount = currentLimitedAmount;
		 * accountCurrentEscrowFund = myDouble.doubleAddDouble(accountCurrentEscrowFund,
		 * tg_LoanProjectCOuntPpo.getCurrentEscrowFund()); accountAmountOffset =
		 * myDouble.doubleAddDouble(accountAmountOffset,
		 * tg_LoanProjectCOuntPpo.getAmountOffset()); accountRecordHouseNum =
		 * myDouble.doubleAddDouble(accountRecordHouseNum,
		 * tg_LoanProjectCOuntPpo.getRecordHouseNum()); accountDepositHouseNum =
		 * myDouble.doubleAddDouble(accountDepositHouseNum,
		 * tg_LoanProjectCOuntPpo.getDepositHouseNum());
		 * 
		 * if ((i + 1) == loanProjectCountPList.size()) {
		 * tg_LoanProjectCountP_ViewList.add(loanProjectCountP);
		 * 
		 * accountHouseMap.put("acountGroup", acountGroup);// 集团
		 * accountHouseMap.put("accountCompany", accountCompany);// 公司
		 * accountHouseMap.put("accountProject", accountProject);// 托管项目
		 * accountHouseMap.put("accountBuilding", accountBuilding);// 托管楼幢
		 * accountHouseMap.put("accountEscrowArea", accountEscrowArea);// 托管面积
		 * accountHouseMap.put("accountRecordAvgPriceOfBuilding",
		 * accountRecordAvgPriceOfBuilding);// 楼幢备案均价
		 * accountHouseMap.put("accountCurrentLimitedAmount",
		 * accountCurrentLimitedAmount);// 当前受限额度
		 * accountHouseMap.put("accountCurrentEscrowFund", accountCurrentEscrowFund);//
		 * 托管余额 accountHouseMap.put("accountAmountOffset", accountAmountOffset);// 抵充额度
		 * accountHouseMap.put("accountRecordHouseNum", accountRecordHouseNum);// 备案户数
		 * accountHouseMap.put("accountDepositHouseNum", accountDepositHouseNum);// 托管户数
		 * } } else { // 循环所属集团进行判断 int groupSize = groupNameList.size();
		 * 
		 * for (int j = 0; j < groupSize; j++) { String group = groupNameList.get(j);
		 * 
		 * String groupNameNew = tg_LoanProjectCOuntPpo.getCompanyGroup();// 集团（新）
		 * 
		 * if (groupNameNew.equals(group)) { int companyCount =
		 * Integer.parseInt(loanProjectCountP.getCompanyName());// 开发企业数量 int
		 * projectCount = Integer.parseInt(loanProjectCountP.getProjectName());// 托管项目数量
		 * int buildingCount =
		 * Integer.parseInt(loanProjectCountP.geteCodeFromConstruction());// 托管楼幢数量
		 * Double escrowArea = loanProjectCountP.getEscrowArea();// 托管面积小计 Double
		 * currentEscrowFund = loanProjectCountP.getCurrentEscrowFund();// 托管余额小计 Double
		 * amountOffset = loanProjectCountP.getAmountOffset();// 抵充额度小计 Double
		 * recordHouseNum = loanProjectCountP.getRecordHouseNum();// 备案户数小计 Double
		 * depositHouseNum = loanProjectCountP.getDepositHouseNum();// 托管户数小计
		 * 
		 * String companyNameNew = tg_LoanProjectCOuntPpo.getCompanyName();// 开发企业（新）
		 * 
		 * // 循环所属企业进行判断 int companySize = companyNameList.size();
		 * 
		 * for (int k = 0; k < companySize; k++) { String company =
		 * companyNameList.get(k);
		 * 
		 * if (companyNameNew.equals(company)) { String projectNameNew =
		 * tg_LoanProjectCOuntPpo.getProjectName();// 托管项目（新）
		 * 
		 * // 循环所属项目进行判断 int projectSize = projectNameList.size();
		 * 
		 * for (int l = 0; l < projectSize; l++) { String project =
		 * projectNameList.get(l);
		 * 
		 * if (projectNameNew.equals(project)) { // 总计 ++accountBuilding;
		 * 
		 * ++buildingCount; } else { if ((l + 1) < projectSize) { continue; }
		 * 
		 * // 总计 ++accountProject; ++accountBuilding;
		 * 
		 * projectNameList.add(projectName); ++projectCount; ++buildingCount; } } } else
		 * { if ((k + 1) < companySize) { continue; }
		 * 
		 * // 总计 ++accountCompany; ++accountBuilding;
		 * 
		 * companyNameList.add(companyName); ++companyCount; ++buildingCount; } }
		 * tg_LoanProjectCountP_ViewList.add(tg_LoanProjectCOuntPpo);
		 * 
		 * // 总计 accountEscrowArea = myDouble.doubleAddDouble(accountEscrowArea,
		 * tg_LoanProjectCOuntPpo.getEscrowArea()); accountCurrentEscrowFund =
		 * myDouble.doubleAddDouble(accountCurrentEscrowFund,
		 * tg_LoanProjectCOuntPpo.getCurrentEscrowFund()); accountAmountOffset =
		 * myDouble.doubleAddDouble(accountAmountOffset,
		 * tg_LoanProjectCOuntPpo.getAmountOffset()); accountRecordHouseNum =
		 * myDouble.doubleAddDouble(accountRecordHouseNum,
		 * tg_LoanProjectCOuntPpo.getRecordHouseNum()); accountDepositHouseNum =
		 * myDouble.doubleAddDouble(accountDepositHouseNum,
		 * tg_LoanProjectCOuntPpo.getDepositHouseNum());
		 * 
		 * // 小计 escrowArea = myDouble.doubleAddDouble(escrowArea,
		 * tg_LoanProjectCOuntPpo.getEscrowArea());// 托管面积 currentEscrowFund =
		 * myDouble.doubleAddDouble(currentEscrowFund,
		 * tg_LoanProjectCOuntPpo.getCurrentEscrowFund());// 托管余额 amountOffset =
		 * myDouble.doubleAddDouble(amountOffset,
		 * tg_LoanProjectCOuntPpo.getAmountOffset());// 抵充额度 recordHouseNum =
		 * myDouble.doubleAddDouble(recordHouseNum,
		 * tg_LoanProjectCOuntPpo.getRecordHouseNum());// 备案户数 depositHouseNum =
		 * myDouble.doubleAddDouble(depositHouseNum,
		 * tg_LoanProjectCOuntPpo.getDepositHouseNum());// 托管户数
		 * 
		 * loanProjectCountP.setCompanyName(Integer.toString(companyCount));//
		 * 开发企业->开发企业数量
		 * loanProjectCountP.setProjectName(Integer.toString(projectCount));//
		 * 托管项目->托管项目数量
		 * loanProjectCountP.seteCodeFromConstruction(Integer.toString(buildingCount));/
		 * / 托管楼幢->托管楼幢数量 loanProjectCountP.setEscrowArea(escrowArea);// 托管面积
		 * loanProjectCountP.setCurrentEscrowFund(currentEscrowFund);// 托管余额
		 * loanProjectCountP.setAmountOffset(amountOffset);// 抵充额度
		 * loanProjectCountP.setRecordHouseNum(recordHouseNum);// 备案户数
		 * loanProjectCountP.setDepositHouseNum(depositHouseNum);// 托管户数/
		 * 
		 * if ((i + 1) == loanProjectCountPList.size()) {
		 * tg_LoanProjectCountP_ViewList.add(loanProjectCountP);
		 * 
		 * // 总计 accountHouseMap.put("acountGroup", acountGroup);// 集团
		 * accountHouseMap.put("accountCompany", accountCompany);// 公司
		 * accountHouseMap.put("accountProject", accountProject);// 托管项目
		 * accountHouseMap.put("accountBuilding", accountBuilding);// 托管楼幢
		 * accountHouseMap.put("accountEscrowArea", accountEscrowArea);// 托管面积
		 * accountHouseMap.put("accountRecordAvgPriceOfBuilding",
		 * accountRecordAvgPriceOfBuilding);// 楼幢备案均价
		 * accountHouseMap.put("accountCurrentLimitedAmount",
		 * accountCurrentLimitedAmount);// 当前受限额度
		 * accountHouseMap.put("accountCurrentEscrowFund", accountCurrentEscrowFund);//
		 * 托管余额 accountHouseMap.put("accountAmountOffset", accountAmountOffset);// 抵充额度
		 * accountHouseMap.put("accountRecordHouseNum", accountRecordHouseNum);// 备案户数
		 * accountHouseMap.put("accountDepositHouseNum", accountDepositHouseNum);// 托管户数
		 * } } else { if ((j + 1) < groupSize) { continue; }
		 * 
		 * // 总计 ++acountGroup; ++accountCompany; ++accountProject; ++accountBuilding;
		 * accountEscrowArea = myDouble.doubleAddDouble(accountEscrowArea,
		 * tg_LoanProjectCOuntPpo.getEscrowArea()); accountRecordAvgPriceOfBuilding =
		 * recordAvgPriceOfBuilding; accountCurrentLimitedAmount = currentLimitedAmount;
		 * accountCurrentEscrowFund = myDouble.doubleAddDouble(accountCurrentEscrowFund,
		 * tg_LoanProjectCOuntPpo.getCurrentEscrowFund()); accountAmountOffset =
		 * myDouble.doubleAddDouble(accountAmountOffset,
		 * tg_LoanProjectCOuntPpo.getAmountOffset()); accountRecordHouseNum =
		 * myDouble.doubleAddDouble(accountRecordHouseNum,
		 * tg_LoanProjectCOuntPpo.getRecordHouseNum()); accountDepositHouseNum =
		 * myDouble.doubleAddDouble(accountDepositHouseNum,
		 * tg_LoanProjectCOuntPpo.getDepositHouseNum());
		 * 
		 * tg_LoanProjectCountP_ViewList.add(loanProjectCountP);
		 * 
		 * tg_LoanProjectCountP_ViewList.add(tg_LoanProjectCOuntPpo);
		 * 
		 * loanProjectCountP = new Tg_LoanProjectCountP_View();
		 * loanProjectCountP.setCompanyGroup("小计：");
		 * loanProjectCountP.setRecordAvgPriceOfBuilding(recordAvgPriceOfBuilding);//
		 * 楼幢备案均价 loanProjectCountP.setCurrentLimitedAmount(currentLimitedAmount);//
		 * 当前受限额度 loanProjectCountP.setCityRegion("~");// 区域
		 * loanProjectCountP.setDeliveryType("~");// 交付类型
		 * loanProjectCountP.setUpTotalFloorNumber(null);// 地上总层数
		 * loanProjectCountP.setOrgLimitedAmount(null);// 初始受限额度
		 * loanProjectCountP.setCurrentBuildProgress("~");// 当前建设进度
		 * loanProjectCountP.setCurrentLimitedNote("~");// 当前受限节点
		 * loanProjectCountP.setSumFamilyNumber(null);// 总户数
		 * loanProjectCountP.setSignHouseNum(null);// 签约户数
		 * loanProjectCountP.setIsPresell("~");// 预售证（有/无）
		 * loanProjectCountP.setEscrowAgRecordTime("~");// 合作协议备案时间
		 * 
		 * int companyCount = 1;// 开发企业 int projectCount = 1;// 托管项目 int buildingCount =
		 * 1;// 托管楼幢
		 * 
		 * loanProjectCountP.setCompanyName(Integer.toString(companyCount));//
		 * 开发企业->开发企业数量
		 * loanProjectCountP.setProjectName(Integer.toString(projectCount));//
		 * 托管项目->托管项目数量
		 * loanProjectCountP.seteCodeFromConstruction(Integer.toString(buildingCount));/
		 * / 托管楼幢->托管楼幢数量
		 * loanProjectCountP.setEscrowArea(tg_LoanProjectCOuntPpo.getEscrowArea());//
		 * 托管面积 loanProjectCountP.setCurrentEscrowFund(tg_LoanProjectCOuntPpo.
		 * getCurrentEscrowFund());// 托管余额
		 * loanProjectCountP.setAmountOffset(tg_LoanProjectCOuntPpo.getAmountOffset());
		 * // 抵充额度
		 * loanProjectCountP.setRecordHouseNum(tg_LoanProjectCOuntPpo.getRecordHouseNum(
		 * ));// 备案户数 loanProjectCountP.setDepositHouseNum(tg_LoanProjectCOuntPpo.
		 * getDepositHouseNum());// 托管户数
		 * 
		 * groupNameList.add(groupName); companyNameList.add(companyName);
		 * projectNameList.add(projectName);
		 * 
		 * if ((i + 1) == loanProjectCountPList.size()) {
		 * tg_LoanProjectCountP_ViewList.add(loanProjectCountP);
		 * 
		 * // 总计 accountHouseMap.put("acountGroup", acountGroup);// 集团
		 * accountHouseMap.put("accountCompany", accountCompany);// 公司
		 * accountHouseMap.put("accountProject", accountProject);// 托管项目
		 * accountHouseMap.put("accountBuilding", accountBuilding);// 托管楼幢
		 * accountHouseMap.put("accountEscrowArea", accountEscrowArea);// 托管面积
		 * accountHouseMap.put("accountRecordAvgPriceOfBuilding",
		 * accountRecordAvgPriceOfBuilding);// 楼幢备案均价
		 * accountHouseMap.put("accountCurrentLimitedAmount",
		 * accountCurrentLimitedAmount);// 当前受限额度
		 * accountHouseMap.put("accountCurrentEscrowFund", accountCurrentEscrowFund);//
		 * 托管余额 accountHouseMap.put("accountAmountOffset", accountAmountOffset);// 抵充额度
		 * accountHouseMap.put("accountRecordHouseNum", accountRecordHouseNum);// 备案户数
		 * accountHouseMap.put("accountDepositHouseNum", accountDepositHouseNum);// 托管户数
		 * } } } } }
		 * 
		 * } }
		 */

		properties.put("tg_LoanProjectCountP_ViewList", tg_LoanProjectCountP_ViewList);
		properties.put("accountHouseMap", accountHouseMap);

		properties.put("keyword", keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
