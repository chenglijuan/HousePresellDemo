package zhishusz.housepresell.service.extra;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.extra.Qs_ProjectEscrowAmount_ViewForm;
import zhishusz.housepresell.database.dao.extra.Qs_ProjectEscrowAmount_ViewDao;
import zhishusz.housepresell.database.po.extra.Qs_ProjectEscrowAmount_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：项目托管资金收支情况表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Qs_ProjectEscrowAmount_ViewListService {
	@Autowired
	private Qs_ProjectEscrowAmount_ViewDao qs_ProjectEscrowAmount_ViewDao;

	private MyDouble dplan = MyDouble.getInstance();

	@SuppressWarnings("unchecked")
	public Properties execute(Qs_ProjectEscrowAmount_ViewForm model) {
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		String keyword = model.getKeyword();
		String userId=String.valueOf(model.getUserId());//用戶id
		String theNameOfCompany = model.getTheNameOfCompany();// 开发企业
		String theNameOfProject = model.getTheNameOfProject();// 项目
		
		/**
		 * xsz y time 2019-5-29 17:59:05
		 * 2561 托管项目资金收支情况表 提醒 去除
		 * ======start===========
		 */
		/*if(null == model.getRecordDateStart()||model.getRecordDateStart().trim().isEmpty()||null == model.getRecordDateEnd() || model.getRecordDateEnd().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请先选择日期！");
		}*/
		/**
		 * xsz y time 2019-5-29 17:59:05
		 * 2561 托管项目资金收支情况表 提醒 去除
		 * ======end===========
		 */
		
		String recordDateStart = model.getRecordDateStart().trim();// 入账日期
		String recordDateEnd = model.getRecordDateEnd().trim();// 入账日期
		String cityRegionId = model.getCityRegionId();  //区域id

		// 转换list
		List<Qs_ProjectEscrowAmount_View> qs_ProjectEscrowAmount_ViewList = new ArrayList<Qs_ProjectEscrowAmount_View>();
		// 接收map

		Map<String, Object> retmap = new HashMap<String, Object>();
		Integer totalPage = 0;
		Integer totalCount = 0;
		// System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
		try {
			retmap = qs_ProjectEscrowAmount_ViewDao.getProjectEscrowAmount_View(userId,theNameOfCompany, theNameOfProject,
					keyword, recordDateStart, recordDateEnd, pageNumber, countPerPage,cityRegionId);
			totalPage = (Integer) retmap.get("totalPage");
			totalCount = (Integer) retmap.get("totalCount");
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

			Map<String, Qs_ProjectEscrowAmount_View> cityRegionnameMap = new HashMap<String, Qs_ProjectEscrowAmount_View>();
			String companyName = "";
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
			qs_TotalEscrowAmount_View.setCityRegionname("");

			// 设置初始值
			qs_TotalEscrowAmount_View.setLoansCountHouse(0);
			qs_TotalEscrowAmount_View.setIncome(0.00);
			qs_TotalEscrowAmount_View.setPayout(0.00);
			qs_TotalEscrowAmount_View.setCurrentFund(0.00);
			qs_TotalEscrowAmount_View.setSpilloverAmount(0.00);
			
//			String cityRegionnametemp = "";
			
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
				}else{
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
			
		}

		properties.put("qs_ProjectEscrowAmount_ViewList", qs_ProjectEscrowAmount_ViewListAfter);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

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
		qp.setCityRegionname("-");

		// 设置初始值
		qp.setLoansCountHouse(0);
		qp.setIncome(0.00);
		qp.setPayout(0.00);
		qp.setCurrentFund(0.00);
		qp.setSpilloverAmount(0.00);

		return qp;
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

	public Qs_ProjectEscrowAmount_View initProjectView(String CompanyName) {
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
		qp.setTheNameOfCompany(CompanyName);
		qp.setTheNameOfProject("小计");
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
