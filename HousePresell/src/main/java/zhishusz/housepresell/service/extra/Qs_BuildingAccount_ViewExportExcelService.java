package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.extra.Qs_BuildingAccount_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.extra.Qs_BuildingAccount_ViewDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.extra.Qs_BuildingAccount_View;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Qs_BuildingAccount_ViewEportExcelVO;
import zhishusz.housepresell.exportexcelvo.Qs_BuildingAccount_ViewEportExcelVO2;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service操作：楼幢账户表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Qs_BuildingAccount_ViewExportExcelService {
	private static final String excelName = "楼幢账户表";
	@Autowired
	private Qs_BuildingAccount_ViewDao qs_BuildingAccount_ViewDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;// 开发企业
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;// 楼幢

	@SuppressWarnings("unchecked")
	public Properties execute(Qs_BuildingAccount_ViewForm model) {
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String theNameOfCompany = model.getTheNameOfCompany();// 开发企业
		String theNameOfProject = model.getTheNameOfProject();// 项目
		String ecodeFromConstruction = model.geteCodeFromConstruction();// 施工楼幢
		
		String sourceType = model.getSourceType();//来源页面

		if (null != theNameOfCompany && theNameOfCompany.trim().isEmpty()) {
			model.setTheNameOfCompany(null);
		} else {
			Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao
					.findById(Long.parseLong(null == theNameOfCompany ? "0" : theNameOfCompany));

			model.setTheNameOfCompany(null == companyInfo ? null : companyInfo.getTheName());
		}
		
		//用户判断
		Integer userType=model.getUser().getTheType();
		if(userType==2) 
		{
			//普通机构用户
			String compnayType = model.getUser().getCompany().getTheType();
			if(S_CompanyType.Development.equals(compnayType)||S_CompanyType.Witness.equals(compnayType))
			{
				model.setTheNameOfCompany(model.getUser().getCompany().getTheName());
			}
			
		}

		if (null != theNameOfProject && theNameOfProject.trim().isEmpty()) {
			model.setTheNameOfProject(null);
		} else {
			Empj_ProjectInfo projectInfo = empj_ProjectInfoDao
					.findById(Long.parseLong(null == theNameOfProject ? "0" : theNameOfProject));

			model.setTheNameOfProject(null == projectInfo ? null : projectInfo.getTheName());
		}

		if (null != ecodeFromConstruction && ecodeFromConstruction.trim().isEmpty()) {
			model.seteCodeFromConstruction(null);
		} else {
			Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao
					.findById(Long.parseLong(null == ecodeFromConstruction ? "0" : ecodeFromConstruction));

			model.seteCodeFromConstruction(null == buildingInfo ? null : buildingInfo.geteCodeFromConstruction());
		}

		if (keyword != null) {
			model.setKeyword("%" + keyword + "%");
		}

		Integer totalCount = qs_BuildingAccount_ViewDao.findByPage_Size(
				qs_BuildingAccount_ViewDao.getQuery_Size(qs_BuildingAccount_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Qs_BuildingAccount_View> qs_BuildingAccount_ViewList;
		if (totalCount > 0) {
			qs_BuildingAccount_ViewList = qs_BuildingAccount_ViewDao
					.findByPage(qs_BuildingAccount_ViewDao.getQuery(qs_BuildingAccount_ViewDao.getBasicHQL(), model));

			/*
			 * xsz by time 2018-9-6 09:26:21 ===》需要合计信息字段： 托管面积（escrowArea）、
			 * 建筑面积（buildingArea）、 初始受限额度（orgLimitedAmount）、 当前受限额度（currentLimitedAmount）、
			 * 总入账金额（totalAccountAmount）、 溢出金额（spilloverAmount）、 已拨付金额（payoutAmount）、
			 * 已申请未拨付金额（appliedNoPayoutAmount）、 已申请退款未拨付金额（applyRefundPayoutAmount）、
			 * 已退款金额（refundAmount）、 当前托管余额（currentEscrowFund）、 可划拨金额（allocableAmount）
			 * 支付保证金额（ZfbzPrice） 现金受限额度（XjsxPrice） 有效受限额度（YxsxPrice）
			 * 
			 * ===》不需要合计信息字段： 开发企业（theNameOfCompany）、 项目（theNameOfProject）、
			 * 施工楼幢（eCodeFromConstruction）、 托管标准（escrowStandard）、 交付类型（deliveryType）、
			 * 当前形象进度（currentFigureProgress）、 当前受限比例（currentLimitedRatio）、
			 * 预售系统楼幢住宅备案均价（recordAvgPriceBldPS）、 楼幢住宅备案均价（recordAvgPriceOfBuilding）
			 * 
			 */

			Qs_BuildingAccount_View ba = new Qs_BuildingAccount_View();
			// 设置不需要合计字段
			ba.setTheNameOfCompany("-");
			ba.setTheNameOfProject("-");
			ba.seteCodeFromConstruction("-");
			ba.setEscrowStandard("-");
			ba.setDeliveryType("-");
			ba.setCurrentFigureProgress("-");
			ba.setCurrentLimitedRatio(null);
			ba.setRecordAvgPriceBldPS(null);
			ba.setRecordAvgPriceOfBuilding(null);
			ba.setCityregion("-");
			ba.setIspresell("-");
			ba.setEscrowagrecordTime("-");

			// 设置初始值
			ba.setEscrowArea(0.00);
			ba.setBuildingArea(0.00);
			ba.setOrgLimitedAmount(0.00);
			ba.setCurrentLimitedAmount(0.00);
			ba.setTotalAccountAmount(0.00);
			ba.setSpilloverAmount(0.00);
			ba.setPayoutAmount(0.00);
			ba.setAppliedNoPayoutAmount(0.00);
			ba.setApplyRefundPayoutAmount(0.00);
			ba.setRefundAmount(0.00);
			ba.setCurrentEscrowFund(0.00);
			ba.setAllocableAmount(0.00);
			ba.setZfbzPrice(0.00);
			ba.setXjsxPrice(0.00);
			ba.setYxsxPrice(0.00);
			
			ba.setSumfamilyNumber(0);
			ba.setSignhouseNum(0);
			ba.setRecordhouseNum(0);
			ba.setDeposithouseNum(0);

			/*
			 * double类型计算
			 * 
			 * doubleAddDouble 加 doubleSubtractDouble 减 doubleMultiplyDouble 乘 div 除
			 * getShort() 保留小数位
			 */
			MyDouble dplan = MyDouble.getInstance();

			for (Qs_BuildingAccount_View qv : qs_BuildingAccount_ViewList) {
				// 托管面积
				ba.setEscrowArea(dplan.doubleAddDouble(ba.getEscrowArea(), qv.getEscrowArea()));
				// 建筑面积
				ba.setBuildingArea(dplan.doubleAddDouble(ba.getBuildingArea(), qv.getBuildingArea()));
				// 初始受限额度
				ba.setOrgLimitedAmount(dplan.doubleAddDouble(ba.getOrgLimitedAmount(), qv.getOrgLimitedAmount()));
				// 当前受限额度
				ba.setCurrentLimitedAmount(
						dplan.doubleAddDouble(ba.getCurrentLimitedAmount(), qv.getCurrentLimitedAmount()));
				// 总入账金额
				ba.setTotalAccountAmount(dplan.doubleAddDouble(ba.getTotalAccountAmount(), qv.getTotalAccountAmount()));
				// 溢出金额
				ba.setSpilloverAmount(dplan.doubleAddDouble(ba.getSpilloverAmount(), qv.getSpilloverAmount()));
				// 已拨付金额
				ba.setPayoutAmount(dplan.doubleAddDouble(ba.getPayoutAmount(), qv.getPayoutAmount()));
				// 已申请未拨付金额
				ba.setAppliedNoPayoutAmount(
						dplan.doubleAddDouble(ba.getAppliedNoPayoutAmount(), qv.getAppliedNoPayoutAmount()));
				// 已申请退款未拨付金额
				ba.setApplyRefundPayoutAmount(
						dplan.doubleAddDouble(ba.getApplyRefundPayoutAmount(), qv.getApplyRefundPayoutAmount()));
				// 已退款金额
				ba.setRefundAmount(dplan.doubleAddDouble(ba.getRefundAmount(), qv.getRefundAmount()));
				// 当前托管余额
				ba.setCurrentEscrowFund(dplan.doubleAddDouble(ba.getCurrentEscrowFund(), qv.getCurrentEscrowFund()));
				// 可划拨金额
				ba.setAllocableAmount(dplan.doubleAddDouble(ba.getAllocableAmount(), qv.getAllocableAmount()));

				// 支付保证金额
				ba.setZfbzPrice(dplan.doubleAddDouble(ba.getZfbzPrice(), qv.getZfbzPrice()));
				// 现金受限额度
				ba.setXjsxPrice(dplan.doubleAddDouble(ba.getXjsxPrice(), qv.getXjsxPrice()));
				// 有效受限额度
				ba.setYxsxPrice(dplan.doubleAddDouble(ba.getYxsxPrice(), qv.getYxsxPrice()));
				
				ba.setSumfamilyNumber(ba.getSumfamilyNumber() + (null == qv.getSumfamilyNumber()?0:qv.getSumfamilyNumber()));
				ba.setSignhouseNum(ba.getSignhouseNum() + (null == qv.getSignhouseNum()?0:qv.getSignhouseNum()));
				ba.setRecordhouseNum(ba.getRecordhouseNum() + (null == qv.getRecordhouseNum()?0:qv.getRecordhouseNum()));
				ba.setDeposithouseNum(ba.getDeposithouseNum() + (null == qv.getDeposithouseNum()?0:qv.getDeposithouseNum()));

			}

			// 合计信息入列表
			ba.setTheNameOfCompany("合计");
			qs_BuildingAccount_ViewList.add(ba);

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Qs_BuildingAccount_View");// 文件在项目中的相对路径
			String localPath = directoryUtil.getProjectRoot();// 项目路径

			String saveDirectory = localPath + relativeDir;// 文件在服务器文件系统中的完整路径

			if (saveDirectory.contains("%20")) {
				saveDirectory = saveDirectory.replace("%20", " ");
			}

			directoryUtil.mkdir(saveDirectory);

			String strNewFileName = excelName + "-"
					+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

			String saveFilePath = saveDirectory + strNewFileName;

			// 通过工具类创建writer
			ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);
			
//			if(sourceType.isEmpty())
			if(S_CompanyType.Cooperation.equals(model.getUser().getCompany().getTheType())||S_CompanyType.Zhengtai.equals(model.getUser().getCompany().getTheType()))
			{
				// 自定义字段别名
				writer.addHeaderAlias("ordinal", "序号");
				writer.addHeaderAlias("theNameOfCompany", "开发企业");
				writer.addHeaderAlias("theNameOfProject", "项目");
				
				writer.addHeaderAlias("cityregion", "区域");
				
				writer.addHeaderAlias("eCodeFromConstruction", "施工楼幢");
				writer.addHeaderAlias("theNameOfCompany", "开发企业");
				
				writer.addHeaderAlias("sumfamilyNumber", "总户数");
				writer.addHeaderAlias("signhouseNum", "签约户数");
				writer.addHeaderAlias("recordhouseNum", "备案户数");
				writer.addHeaderAlias("deposithouseNum", "托管户数");
				writer.addHeaderAlias("ispresell", "预售证");
				writer.addHeaderAlias("escrowagrecordTime", "合作协议备案时间");
				
				writer.addHeaderAlias("upFloorNumber", "地上楼层数");
				writer.addHeaderAlias("escrowStandard", "托管标准");
				writer.addHeaderAlias("escrowArea", "托管面积");
				writer.addHeaderAlias("deliveryType", "房屋类型");
				writer.addHeaderAlias("recordAvgPriceOfBuilding", "楼幢住宅备案均价");
				writer.addHeaderAlias("orgLimitedAmount", "初始受限额度（元）");
				writer.addHeaderAlias("currentFigureProgress", "当前形象进度");
				writer.addHeaderAlias("currentLimitedRatio", "当前受限比例（%）");
				writer.addHeaderAlias("currentLimitedAmount", "当前受限额度（元）");
				writer.addHeaderAlias("zfbzPrice", "支付保证金额（元）");
				writer.addHeaderAlias("xjsxPrice", "现金受限额度（元）");
				writer.addHeaderAlias("yxsxPrice", "有效受限额度（元）");
				writer.addHeaderAlias("totalAccountAmount", "总入账金额（元）");
				writer.addHeaderAlias("spilloverAmount", "溢出金额（元）");
				writer.addHeaderAlias("payoutAmount", "已拨付金额（元）");
				writer.addHeaderAlias("currentEscrowFund", "当前托管余额（元）");
				writer.addHeaderAlias("allocableAmount", "可划拨金额（元）");
				writer.addHeaderAlias("appliedNoPayoutAmount", "已申请未拨付金额（元）");
				writer.addHeaderAlias("applyRefundPayoutAmount", "已申请退款未拨付金额（元）");
				writer.addHeaderAlias("refundAmount", "已退款金额（元）");
				writer.addHeaderAlias("recordAvgPriceBldPS", "预售系统楼幢住宅备案均价");
				writer.addHeaderAlias("buildingArea", "建筑面积");
				
				

				List<Qs_BuildingAccount_ViewEportExcelVO> list = formart(qs_BuildingAccount_ViewList);

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
				writer.autoSizeColumn(22, true);
				writer.autoSizeColumn(23, true);
				writer.autoSizeColumn(24, true);
				
				writer.autoSizeColumn(25, true);
				
				writer.autoSizeColumn(26, true);
				writer.autoSizeColumn(27, true);
				writer.autoSizeColumn(28, true);
				writer.autoSizeColumn(29, true);
				writer.autoSizeColumn(30, true);
				writer.autoSizeColumn(31, true);
				writer.autoSizeColumn(32, true);
				writer.autoSizeColumn(33, true);

				writer.close();
			}
			else
			{
				// 自定义字段别名
				writer.addHeaderAlias("ordinal", "序号");
				writer.addHeaderAlias("theNameOfProject", "项目");
				writer.addHeaderAlias("eCodeFromConstruction", "施工楼幢");
				writer.addHeaderAlias("theNameOfCompany", "开发企业");
				
				writer.addHeaderAlias("upFloorNumber", "地上楼层数");
				writer.addHeaderAlias("escrowArea", "托管面积");
				writer.addHeaderAlias("escrowStandard", "托管标准");
				writer.addHeaderAlias("deliveryType", "房屋类型");
				writer.addHeaderAlias("recordAvgPriceOfBuilding", "楼幢住宅备案均价");
				writer.addHeaderAlias("orgLimitedAmount", "初始受限额度（元）");
				writer.addHeaderAlias("currentFigureProgress", "当前形象进度");
				writer.addHeaderAlias("currentLimitedRatio", "当前受限比例（%）");
				writer.addHeaderAlias("currentLimitedAmount", "当前节点受限额度（元）");
				writer.addHeaderAlias("zfbzPrice", "支付保证金额（元）");
				writer.addHeaderAlias("xjsxPrice", "现金受限额度（元）");
				writer.addHeaderAlias("yxsxPrice", "有效受限额度（元）");
				writer.addHeaderAlias("totalAccountAmount", "总入账金额（元）");
				writer.addHeaderAlias("spilloverAmount", "溢出金额（元）");
				writer.addHeaderAlias("payoutAmount", "已拨付金额（元）");
				writer.addHeaderAlias("currentEscrowFund", "当前托管余额（元）");
				writer.addHeaderAlias("allocableAmount", "可划拨金额（元）");
				writer.addHeaderAlias("appliedNoPayoutAmount", "已申请未拨付金额（元）");
				writer.addHeaderAlias("applyRefundPayoutAmount", "已申请退款未拨付金额（元）");
				writer.addHeaderAlias("refundAmount", "已退款金额（元）");
				writer.addHeaderAlias("recordAvgPriceBldPS", "预售系统楼幢住宅备案均价");
				writer.addHeaderAlias("buildingArea", "建筑面积");
				
				List<Qs_BuildingAccount_ViewEportExcelVO2> list = formart2(qs_BuildingAccount_ViewList);

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
				writer.autoSizeColumn(22, true);
				writer.autoSizeColumn(23, true);
				writer.autoSizeColumn(24, true);
				writer.autoSizeColumn(25, true);

				writer.close();
			}

			

			properties.put("fileURL", relativeDir + strNewFileName);

		} else {
			return MyBackInfo.fail(properties, "未查询到有效数据");
		}

		properties.put("qs_BuildingAccount_ViewList", qs_BuildingAccount_ViewList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	List<Qs_BuildingAccount_ViewEportExcelVO> formart(List<Qs_BuildingAccount_View> qs_BuildingAccount_ViewList) {

		List<Qs_BuildingAccount_ViewEportExcelVO> list = new ArrayList<Qs_BuildingAccount_ViewEportExcelVO>();

		int ordinal = 0;
		for (Qs_BuildingAccount_View po : qs_BuildingAccount_ViewList) {
			++ordinal;
			Qs_BuildingAccount_ViewEportExcelVO vo = new Qs_BuildingAccount_ViewEportExcelVO();

			vo.setAllocableAmount(po.getAllocableAmount());
			vo.setAppliedNoPayoutAmount(po.getAppliedNoPayoutAmount());
			vo.setApplyRefundPayoutAmount(po.getApplyRefundPayoutAmount());
			vo.setBuildingArea(po.getBuildingArea());
			vo.setCurrentEscrowFund(po.getCurrentEscrowFund());
			vo.setCurrentFigureProgress(po.getCurrentFigureProgress());
			vo.setCurrentLimitedAmount(po.getCurrentLimitedAmount());
			vo.setCurrentLimitedRatio(po.getCurrentLimitedRatio());
			vo.setDeliveryType(po.getDeliveryType());
			vo.seteCodeFromConstruction(po.geteCodeFromConstruction());
			vo.setUpFloorNumber(po.getUpFloorNumber());
			vo.setEscrowArea(po.getEscrowArea());
			vo.setEscrowStandard(po.getEscrowStandard());
			vo.setOrdinal(ordinal);
			vo.setOrgLimitedAmount(po.getOrgLimitedAmount());
			vo.setPayoutAmount(po.getPayoutAmount());
			vo.setRecordAvgPriceBldPS(po.getRecordAvgPriceBldPS());
			vo.setRecordAvgPriceOfBuilding(po.getRecordAvgPriceOfBuilding());
			vo.setRefundAmount(po.getRefundAmount());
			vo.setSpilloverAmount(po.getSpilloverAmount());
			vo.setTheNameOfCompany(po.getTheNameOfCompany());
			vo.setTheNameOfProject(po.getTheNameOfProject());
			vo.setTotalAccountAmount(po.getTotalAccountAmount());
			vo.setZfbzPrice(po.getZfbzPrice());
			vo.setXjsxPrice(po.getXjsxPrice());
			vo.setYxsxPrice(po.getYxsxPrice());
			
			vo.setCityregion(po.getCityregion());
			vo.setSumfamilyNumber(po.getSumfamilyNumber());
			vo.setSignhouseNum(po.getSignhouseNum());
			vo.setRecordhouseNum(po.getRecordhouseNum());
			vo.setDeposithouseNum(po.getDeposithouseNum());
			vo.setIspresell(po.getIspresell());
			vo.setEscrowagrecordTime(po.getEscrowagrecordTime());
			

			list.add(vo);
		}

		return list;

	}
	
	List<Qs_BuildingAccount_ViewEportExcelVO2> formart2(List<Qs_BuildingAccount_View> qs_BuildingAccount_ViewList) {

		List<Qs_BuildingAccount_ViewEportExcelVO2> list = new ArrayList<Qs_BuildingAccount_ViewEportExcelVO2>();

		int ordinal = 0;
		for (Qs_BuildingAccount_View po : qs_BuildingAccount_ViewList) {
			++ordinal;
			Qs_BuildingAccount_ViewEportExcelVO2 vo = new Qs_BuildingAccount_ViewEportExcelVO2();

			vo.setAllocableAmount(po.getAllocableAmount());
			vo.setAppliedNoPayoutAmount(po.getAppliedNoPayoutAmount());
			vo.setApplyRefundPayoutAmount(po.getApplyRefundPayoutAmount());
			vo.setBuildingArea(po.getBuildingArea());
			vo.setCurrentEscrowFund(po.getCurrentEscrowFund());
			vo.setCurrentFigureProgress(po.getCurrentFigureProgress());
			vo.setCurrentLimitedAmount(po.getCurrentLimitedAmount());
			vo.setCurrentLimitedRatio(po.getCurrentLimitedRatio());
			vo.setDeliveryType(po.getDeliveryType());
			vo.seteCodeFromConstruction(po.geteCodeFromConstruction());
			vo.setUpFloorNumber(po.getUpFloorNumber());
			vo.setEscrowArea(po.getEscrowArea());
			vo.setEscrowStandard(po.getEscrowStandard());
			vo.setOrdinal(ordinal);
			vo.setOrgLimitedAmount(po.getOrgLimitedAmount());
			vo.setPayoutAmount(po.getPayoutAmount());
			vo.setRecordAvgPriceBldPS(po.getRecordAvgPriceBldPS());
			vo.setRecordAvgPriceOfBuilding(po.getRecordAvgPriceOfBuilding());
			vo.setRefundAmount(po.getRefundAmount());
			vo.setSpilloverAmount(po.getSpilloverAmount());
			vo.setTheNameOfCompany(po.getTheNameOfCompany());
			vo.setTheNameOfProject(po.getTheNameOfProject());
			vo.setTotalAccountAmount(po.getTotalAccountAmount());
			vo.setZfbzPrice(po.getZfbzPrice());
			vo.setXjsxPrice(po.getXjsxPrice());
			vo.setYxsxPrice(po.getYxsxPrice());
			
			list.add(vo);
		}

		return list;

	}

}
