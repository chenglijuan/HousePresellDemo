package zhishusz.housepresell.service.extra;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.extra.Qs_ProjectEscrowAmount_ViewForm;
import zhishusz.housepresell.database.dao.extra.Qs_ProjectEscrowAmount_ViewDao;
import zhishusz.housepresell.database.po.extra.Qs_ProjectEscrowAmount_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Qs_ProjectEscrowAmount_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/**
 * 统计报表-项目托管资金收支情况表-导出Excel
 * 
 * @ClassName: Qs_ProjectEscrowAmount_ViewExportExcelService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月20日 上午11:45:48
 * @version V1.0
 *
 */
@Service
@Transactional
public class Qs_ProjectEscrowAmount_ViewExportExcelService {
	private static final String excelName = "项目托管资金收支情况表";
	@Autowired
	private Qs_ProjectEscrowAmount_ViewDao qs_ProjectEscrowAmount_ViewDao;
	
	private MyDouble dplan = MyDouble.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Qs_ProjectEscrowAmount_ViewForm model) {
		Properties properties = new MyProperties();

		Integer pageNumber = 1;
		Integer countPerPage = 6000000;

		String keyword = model.getKeyword();
		String userId=String.valueOf(model.getUserId());//用戶id
		String theNameOfCompany = model.getTheNameOfCompany();// 开发企业
		String theNameOfProject = model.getTheNameOfProject();// 项目
		if(null == model.getRecordDateStart()||model.getRecordDateStart().trim().isEmpty()||null == model.getRecordDateEnd() || model.getRecordDateEnd().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请先选择日期！");
		}
		String recordDateStart = model.getRecordDateStart().trim();// 入账日期
		String recordDateEnd = model.getRecordDateEnd().trim();// 入账日期

		String cityRegionId = model.getCityRegionId(); // 所属区域
//		System.out.println("cityRegionId="+cityRegionId);

		// 转换list
		List<Qs_ProjectEscrowAmount_View> qs_ProjectEscrowAmount_ViewList = new ArrayList<Qs_ProjectEscrowAmount_View>();
		// 接收map

		Map<String, Object> retmap = new HashMap<String, Object>();
		// System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
		try {
			retmap = qs_ProjectEscrowAmount_ViewDao.getProjectEscrowAmount_View(userId,theNameOfCompany, theNameOfProject,
					keyword, recordDateStart, recordDateEnd, pageNumber, countPerPage,cityRegionId);
			qs_ProjectEscrowAmount_ViewList = (List<Qs_ProjectEscrowAmount_View>) retmap
					.get("qs_ProjectEscrowAmount_ViewList");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 返回list
		List<Qs_ProjectEscrowAmount_View> qs_ProjectEscrowAmount_ViewListAfter = new ArrayList<Qs_ProjectEscrowAmount_View>();

		// 合計
		if (qs_ProjectEscrowAmount_ViewList.size() > 0) {

			int len = qs_ProjectEscrowAmount_ViewList.size();
			Map<String, Qs_ProjectEscrowAmount_View> companyMap = new HashMap<String, Qs_ProjectEscrowAmount_View>();
			String companyName = "";
			Map<String, Qs_ProjectEscrowAmount_View> cityRegionnameMap = new HashMap<String, Qs_ProjectEscrowAmount_View>();
			String cityRegionname = "";
			
			//总计
			Qs_ProjectEscrowAmount_View qs_TotalEscrowAmount_View=new Qs_ProjectEscrowAmount_View();
			
			// 设置不需要合计字段
			qs_TotalEscrowAmount_View.setSerialNumber("-");
			qs_TotalEscrowAmount_View.setRecordDate("-");
			qs_TotalEscrowAmount_View.setTheNameOfCompany("总计");
			qs_TotalEscrowAmount_View.setTheNameOfProject("-");
			qs_TotalEscrowAmount_View.seteCodeFromConstruction("-");
			qs_TotalEscrowAmount_View.setRemarkNote("-");
			qs_TotalEscrowAmount_View.setCityRegionname("-");

			// 设置初始值
			qs_TotalEscrowAmount_View.setLoansCountHouse(0);
			qs_TotalEscrowAmount_View.setIncome(0.00);
			qs_TotalEscrowAmount_View.setPayout(0.00);
			qs_TotalEscrowAmount_View.setCurrentFund(0.00);
			qs_TotalEscrowAmount_View.setSpilloverAmount(0.00);
			
			for (int i = 0; i < len; i++) {
				Qs_ProjectEscrowAmount_View qs_ProjectEscrowAmount_View = qs_ProjectEscrowAmount_ViewList.get(i);
				String companyCurrentName = qs_ProjectEscrowAmount_View.getTheNameOfCompany();
				if (companyMap.containsKey(companyCurrentName)) {
					Qs_ProjectEscrowAmount_View qs_Company = companyMap.get(companyCurrentName);
					qs_Company = addView(qs_Company, qs_ProjectEscrowAmount_View);
					companyMap.put(companyCurrentName, qs_Company);
				} else {
					if (!"".equals(companyName)) {
						Qs_ProjectEscrowAmount_View qs_CompanyBefore = companyMap.get(companyName);
						qs_ProjectEscrowAmount_ViewListAfter.add(qs_CompanyBefore);
					}

					Qs_ProjectEscrowAmount_View qs_Company = initCompanyView();
					qs_Company = addView(qs_Company, qs_ProjectEscrowAmount_View);
					companyName = companyCurrentName;
					companyMap.put(companyCurrentName, qs_Company);
				}

				String cityCurrentName = qs_ProjectEscrowAmount_View.getCityRegionname();
				//System.out.println("cityCurrentName="+cityCurrentName);
				//如果包含
				if(cityRegionnameMap.containsKey(cityCurrentName)){
					Qs_ProjectEscrowAmount_View qs_cityview = cityRegionnameMap.get(cityCurrentName);
					qs_cityview = addView(qs_cityview, qs_ProjectEscrowAmount_View);
					cityRegionnameMap.put(cityCurrentName, qs_cityview);
				}else {
					if (!"".equals(cityRegionname)) {
						Qs_ProjectEscrowAmount_View qs_CompanyBefore = cityRegionnameMap.get(cityRegionname);
						qs_ProjectEscrowAmount_ViewListAfter.add(qs_CompanyBefore);
					}

					// 如果不包含
					Qs_ProjectEscrowAmount_View qs_cityview = initCityRegionView(cityCurrentName);
					qs_cityview = addView(qs_cityview, qs_ProjectEscrowAmount_View);


					cityRegionname = cityCurrentName;
					cityRegionnameMap.put(cityCurrentName, qs_cityview);
				}

				// 直接插入
				qs_TotalEscrowAmount_View= addView(qs_TotalEscrowAmount_View, qs_ProjectEscrowAmount_View);
				
				qs_ProjectEscrowAmount_ViewListAfter.add(qs_ProjectEscrowAmount_View);
			}
			if (!"".equals(companyName)) {
				qs_ProjectEscrowAmount_ViewListAfter.add(companyMap.get(companyName));
			}
			if (!"".equals(cityRegionname)) {
				qs_ProjectEscrowAmount_ViewListAfter.add(cityRegionnameMap.get(cityRegionname));
			}

			qs_ProjectEscrowAmount_ViewListAfter.add(qs_TotalEscrowAmount_View);

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Qs_ProjectEscrowAmount_View");// 文件在项目中的相对路径
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

			// 自定义字段别名
			writer.addHeaderAlias("ordinal", "序号");
			writer.addHeaderAlias("theNameOfCompany", "开发企业");
			writer.addHeaderAlias("theNameOfProject", "项目名称");
			writer.addHeaderAlias("cityRegionname", "所属区域");
			writer.addHeaderAlias("eCodeFromConstruction", "楼幢号");
			writer.addHeaderAlias("loansCountHouse", "放款户数");
			writer.addHeaderAlias("income", "托管收入（元）");
			writer.addHeaderAlias("payout", "托管支出（元）");
			writer.addHeaderAlias("currentFund", "余额（元）");
			writer.addHeaderAlias("spilloverAmount", "溢出资金（元）");

			List<Qs_ProjectEscrowAmount_ViewExportExcelVO> qs_ProjectEscrowAmount_ViewExportExcelVOList = formart(
					qs_ProjectEscrowAmount_ViewListAfter);

			// 一次性写出内容，使用默认样式
			writer.write(qs_ProjectEscrowAmount_ViewExportExcelVOList);

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

			writer.close();

			properties.put("fileName", strNewFileName);
			properties.put("fileURL", relativeDir + strNewFileName);
			properties.put("fullFileName", saveFilePath);

		} else {
			return MyBackInfo.fail(properties, "未查询到有效数据");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}


	public Qs_ProjectEscrowAmount_View initCityRegionView(String cityCurrentName) {
		/*
		 * xsz by time 2018-9-6 16:45:22 ===》需要合计信息字段： 放款户数（loansCountHouse）、
		 * 托管收入（income）、 托管支出（payout）、 余额（currentFund）、 溢出资金（spilloverAmount）
		 * ===》不需要合计信息字段： 序号（serialNumber）、 入账日期（recordDate）、 开发企业（theNameOfCompany）、
		 * 项目名称（theNameOfProject）、 楼幢号（eCodeFromConstruction）、 备注（remarkNote）、
		 *
		 */

		Qs_ProjectEscrowAmount_View qp = new Qs_ProjectEscrowAmount_View();
		// 设置不需要合计字段
		qp.setSerialNumber("-");
		qp.setRecordDate("-");
		qp.setTheNameOfCompany("小计");
		qp.setTheNameOfProject("-");
		qp.seteCodeFromConstruction("-");
		qp.setRemarkNote("-");
		qp.setCityRegionname(cityCurrentName);

		// 设置初始值
		qp.setLoansCountHouse(0);
		qp.setIncome(0.00);
		qp.setPayout(0.00);
		qp.setCurrentFund(0.00);
		qp.setSpilloverAmount(0.00);

		return qp;
	}

	List<Qs_ProjectEscrowAmount_ViewExportExcelVO> formart(
			List<Qs_ProjectEscrowAmount_View> qs_ProjectEscrowAmount_ViewList) {
		List<Qs_ProjectEscrowAmount_ViewExportExcelVO> list = new ArrayList<Qs_ProjectEscrowAmount_ViewExportExcelVO>();
		int ordinal = 0;
		for (Qs_ProjectEscrowAmount_View po : qs_ProjectEscrowAmount_ViewList) {
			++ordinal;
			Qs_ProjectEscrowAmount_ViewExportExcelVO vo = new Qs_ProjectEscrowAmount_ViewExportExcelVO();

			vo.setCurrentFund(po.getCurrentFund());
			vo.seteCodeFromConstruction(po.geteCodeFromConstruction());
			vo.setIncome(po.getIncome());
			vo.setLoansCountHouse(po.getLoansCountHouse());
			vo.setOrdinal(ordinal);
			vo.setPayout(po.getPayout());
			vo.setSpilloverAmount(po.getSpilloverAmount());
			vo.setTheNameOfCompany(po.getTheNameOfCompany());
			vo.setTheNameOfProject(po.getTheNameOfProject());
			vo.setCityRegionname(po.getCityRegionname());

			list.add(vo);

		}

		return list;
	}

	public Qs_ProjectEscrowAmount_View initCompanyView() {
		/*
		 * xsz by time 2018-9-6 16:45:22 ===》需要合计信息字段： 放款户数（loansCountHouse）、
		 * 托管收入（income）、 托管支出（payout）、 余额（currentFund）、 溢出资金（spilloverAmount）
		 * ===》不需要合计信息字段： 序号（serialNumber）、 入账日期（recordDate）、 开发企业（theNameOfCompany）、
		 * 项目名称（theNameOfProject）、 楼幢号（eCodeFromConstruction）、 备注（remarkNote）、
		 * 
		 */

		Qs_ProjectEscrowAmount_View qp = new Qs_ProjectEscrowAmount_View();
		// 设置不需要合计字段
		qp.setSerialNumber("-");
		qp.setRecordDate("-");
		qp.setTheNameOfCompany("合计");
		qp.setTheNameOfProject("-");
		qp.seteCodeFromConstruction("-");
		qp.setRemarkNote("-");

		// 设置初始值
		qp.setLoansCountHouse(0);
		qp.setIncome(0.00);
		qp.setPayout(0.00);
		qp.setCurrentFund(0.00);
		qp.setSpilloverAmount(0.00);

		return qp;
	}

	public Qs_ProjectEscrowAmount_View addView(Qs_ProjectEscrowAmount_View qp, Qs_ProjectEscrowAmount_View qv) {
		// 放款户数
		qp.setLoansCountHouse(qp.getLoansCountHouse() + qv.getLoansCountHouse());
		// 托管收入
		qp.setIncome(dplan.doubleAddDouble(qp.getIncome(), qv.getIncome()));
		// 托管支出
		qp.setPayout(dplan.doubleAddDouble(qp.getPayout(), qv.getPayout()));
		// 余额
		qp.setCurrentFund(dplan.doubleAddDouble(qp.getCurrentFund(), qv.getCurrentFund()));
		// 溢出资金
		qp.setSpilloverAmount(dplan.doubleAddDouble(qp.getSpilloverAmount(), qv.getSpilloverAmount()));

		return qp;
	}
}
