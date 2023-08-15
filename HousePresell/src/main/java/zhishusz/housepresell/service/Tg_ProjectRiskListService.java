package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_ProjectRiskForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;

import zhishusz.housepresell.database.dao.Tg_ProjectRiskDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;

import zhishusz.housepresell.database.po.Tg_ProjectRiskView;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service : 项目风险明细表
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_ProjectRiskListService {
	@Autowired
	private Tg_ProjectRiskDao tg_ProjectRiskDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	public Properties execute(Tg_ProjectRiskForm model) {
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 托管满额率
		String keyword = model.getKeyword();
		if (null != keyword && !keyword.trim().isEmpty()) {
			model.setHostingFullRate(keyword);
		}

		// 日期
		String dateRange = model.getDateRange();
		if (null == dateRange || dateRange.trim().isEmpty()) {
			dateRange = null;
		} else {
			dateRange = dateRange.trim();
		}

		// 所属区域
		Long areaId = model.getAreaId();
		if (areaId != null && areaId > 0) {
			// areaName = sm_CityRegionInfoDao.findById(areaId).getTheName();

			Sm_CityRegionInfo cityRegionInfo = sm_CityRegionInfoDao.findById(areaId);
			if (null == cityRegionInfo) {
				areaId = 0L;
			} else {
				areaId = cityRegionInfo.getTableId();
			}
		} else {
			areaId = 0L;
		}

		// 托管项目
		Long managedProjectsId = model.getManagedProjectsId();
		if (managedProjectsId != null && managedProjectsId > 0) {
			// managedProjectName =
			// empj_ProjectInfoDao.findById(managedProjectsId).getTheName();

			Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(managedProjectsId);
			if (null == projectInfo) {
				managedProjectsId = 0L;
			} else {
				managedProjectsId = projectInfo.getTableId();
			}
		} else {
			managedProjectsId = 0L;
		}

		// 查封情况（未签）
		 String attachment = model.getAttachment();
		 if (attachment == null || attachment.trim().isEmpty())
		 {
		 attachment = null;
		 }
		 else
		 {
		 attachment = attachment.trim();
		 }
		 
		// 查封情况（已签）
		 String attachmented = model.getAttachmented();
		 if (attachmented == null || attachmented.trim().isEmpty())
		 {
			 attachmented = null;
		 }
		 else
		 {
			 attachmented = attachmented.trim();
		 }

		// 土地抵押情况 0-未抵押 1-已抵押
		String landMortgage = model.getLandMortgage();
		if (landMortgage == null || landMortgage.trim().isEmpty()) {
			landMortgage = "99";
		} else {
			landMortgage = landMortgage.trim();

			switch (landMortgage) {
			case "未抵押":
				landMortgage = "0";
				break;

			case "已抵押":
				landMortgage = "1";
				break;

			default:
				landMortgage = "99";
				break;
			}

		}

		// 风险评级
		/*
		 * <option value="0">高</option> <option value="1">中</option> <option
		 * value="2">低</option>
		 */
		String riskRating = model.getRiskRating();
		if (riskRating == null || riskRating.trim().isEmpty()) {
			riskRating = "99";
		} else {
			switch (riskRating) {
			case "高":
				riskRating = "0";
				break;

			case "中":
				riskRating = "1";
				break;

			case "低":
				riskRating = "2";
				break;
			default:
				riskRating = "99";
				break;
			}
			riskRating = riskRating.trim();
		}

		// 转换list
		List<Tg_ProjectRiskView> tg_ProjectRiskLists = new ArrayList<Tg_ProjectRiskView>();
		// 接收list
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
		try {
			list = tg_ProjectRiskDao.getTg_ProjectRisk(areaId, managedProjectsId, dateRange, Long.valueOf(landMortgage),
					Long.valueOf(riskRating),model.getUserId(),attachment,attachmented);

		} catch (SQLException e) {
			list = new ArrayList<Map<String, String>>();
			e.printStackTrace();
		}
		System.out.println("掉用存储过程结束：" + System.currentTimeMillis());
		Tg_ProjectRiskView po;
		System.out.println("转换开始：" + System.currentTimeMillis());
		for (Map<String, String> map : list) {
			po = new Tg_ProjectRiskView();
			po.setAlreadyUnsignedContract(map.get("ALREADYUNSIGNEDCONTRACT"));
			po.setAppointedTime(map.get("APPOINTEDTIME"));
			po.setArea(map.get("AREA"));
			po.setAstrict(map.get("ASTRICT"));
			po.setAttachment(map.get("ATTACHMENT"));
			po.setContractFilingRate(map.get("CONTRACTFILINGRATE"));
			po.setContractLoanRatio(map.get("CONTRACTLOANRATIO"));
			po.setCurrentConstruction(map.get("CURRENTCONSTRUCTION"));
			po.setDateOfPresale(map.get("DATEOFPRESALE"));
			po.setDateQuery(map.get("DATEQUERY"));
			po.setFloorBuilding(map.get("FLOORBUILDING"));
			po.setHostingFullRate(map.get("HOSTINGFULLRATE"));
			po.setLandMortgage(map.get("LANDMORTGAGE"));
			po.setManagedArea(map.get("MANAGEDAREA"));
			po.setManagedProjects(map.get("MANAGEDPROJECTS"));
			po.setOtherRisks(map.get("OTHERRISKS"));
			po.setProgressEvaluation(map.get("PROGRESSEVALUATION"));
			po.setRiskRating(map.get("RISKRATING"));
			po.setSignTheEfficiency(map.get("SIGNTHEEFFICIENCY"));
			po.setTableId(Long.valueOf(map.get("TABLEID")));
			po.setTotalOfoverground(map.get("TOTALOFOVERGROUND"));
			po.setUnsignedContract(map.get("UNSIGNEDCONTRACT"));
			po.setUpdateTime(map.get("UPDATETIME"));
			po.setPayguarantee(map.get("PAYGUARANTEE"));

			tg_ProjectRiskLists.add(po);

		}
		System.out.println("转换结束：" + System.currentTimeMillis());
		// 返回list
		List<Tg_ProjectRiskView> tg_ProjectRiskList = new ArrayList<Tg_ProjectRiskView>();
		Integer totalCount = tg_ProjectRiskLists.size();
		Integer totalPage = totalCount / countPerPage;
		if (totalCount > 0) {

			if (totalCount % countPerPage > 0)
				totalPage++;
			if (pageNumber > totalPage && totalPage != 0)
				pageNumber = totalPage;

			// 创建分页坐标
			Integer startTotal = countPerPage * (pageNumber - 1);
			Integer endTotal = countPerPage * pageNumber;

			if (endTotal > totalCount) {
				endTotal = totalCount;
			}

			System.out.println("返回开始：" + System.currentTimeMillis());
			for (int i = startTotal; i < endTotal; i++) {
				tg_ProjectRiskList.add(tg_ProjectRiskLists.get(i));
			}
			System.out.println("返回结束：" + System.currentTimeMillis());
		}

		properties.put("tg_ProjectRiskList", tg_ProjectRiskList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

}
