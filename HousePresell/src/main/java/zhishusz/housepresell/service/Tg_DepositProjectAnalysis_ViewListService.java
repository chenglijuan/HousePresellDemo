package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_DepositProjectAnalysis_ViewForm;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_DepositProjectAnalysis_ViewDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_DepositProjectAnalysis_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管项目情况分析表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_DepositProjectAnalysis_ViewListService
{

	@Autowired
	private Tg_DepositProjectAnalysis_ViewDao tg_DepositProjectAnalysis_ViewDao;

	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	private MyDatetime formart = MyDatetime.getInstance();

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_DepositProjectAnalysis_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		Long cityRegionId = model.getCityRegionId();// 区域Id

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null == cityRegionId || cityRegionId < 1)
		{
			model.setCityRegion(null);
		}
		else
		{
			// 查询区域
			Sm_CityRegionInfo regionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			if (null == regionInfo)
			{
				return MyBackInfo.fail(properties, "未查询到有效的区域信息，请核实");
			}
			else
			{
				model.setCityRegion(regionInfo.getTheName());
			}
		}

		String queryYear = null;
		String queryQuarter = null;
		String queryMonth = null;

		queryYear = model.getQueryYear();
		queryQuarter = model.getQueryQuarter();
		queryMonth = model.getQueryMonth();

		// 如果年份为空，默认取当前年份
		if (null == queryYear || queryYear.trim().isEmpty())
		{
			long currentTimeMillis = System.currentTimeMillis();
			String dateToString = formart.dateToString(currentTimeMillis);
			queryYear = dateToString.split("-")[0];// 获取当前时间的年份
		}

		// 判断季度是否存在
		if (null == queryQuarter || queryQuarter.trim().isEmpty())
		{
			queryQuarter = null;
		}

		// 如果月份不为空，则将季度置为空（有月份的情况下以月份查询）
		if (null != queryMonth && !queryMonth.trim().isEmpty())
		{
			queryQuarter = null;
		}
		else
		{
			queryMonth = null;
		}

		model.setQueryMonth(queryMonth);
		model.setQueryQuarter(queryQuarter);
		model.setQueryYear(queryYear);

		Integer totalCount = tg_DepositProjectAnalysis_ViewDao.findByPage_Size(tg_DepositProjectAnalysis_ViewDao
				.getQuery_Size(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		// 返回到前端的list
		List<Tg_DepositProjectAnalysis_View> viewList = new ArrayList<Tg_DepositProjectAnalysis_View>();

		List<Tg_DepositProjectAnalysis_View> tg_DepositProjectAnalysis_ViewList = null;
		if (totalCount > 0)
		{
			tg_DepositProjectAnalysis_ViewList = tg_DepositProjectAnalysis_ViewDao.findByPage(
					tg_DepositProjectAnalysis_ViewDao.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));
		}
		else
		{
			tg_DepositProjectAnalysis_ViewList = new ArrayList<Tg_DepositProjectAnalysis_View>();
		}

		if (null != tg_DepositProjectAnalysis_ViewList && tg_DepositProjectAnalysis_ViewList.size() > 0)
		{

			String cityRegion = model.getCityRegion();
			/*
			 * 如果有查询到数据
			 * 1：按照年份查询，需要将同一区域的同年所有数据累计
			 * 2：按照季度查询，需要将同一区域的同季度所有数据累计
			 * 3：按照月份查询，则不需要累计
			 * 
			 * 需要判断查询条件中是否根据区域条件查询
			 * 
			 */

			Tg_DepositProjectAnalysis_View viewPo = new Tg_DepositProjectAnalysis_View();

			// 建立累加字段
			// 区域
			String cityRegionPo = "";
			// 已签约托管面积（㎡）
			Double escrowArea = 0.00;
			// 已签约托管面积区域占比（%）
			String escrowAreaRatio = "";
			// 已预售托管面积（㎡）
			Double preEscrowArea = 0.00;
			// 已预售托管面积区域占比（%）
			String preEscrowAreaRatio = "";

			List<Tg_DepositProjectAnalysis_View> areaCountList = new ArrayList<Tg_DepositProjectAnalysis_View>();
			/**
			 * 
			 * 1.先查询条件查询出来有多少个区域
			 * 2.再根据区域加载数据
			 * 
			 */
			//全区域已签约托管面积（㎡）
			Double escrowAreaCount = 0.00;
			// 已预售托管面积（㎡）
			Double preEscrowAreaCount = 0.00;
			
			List<String> areaList = new ArrayList<String>();
			if (null == queryQuarter && null == queryMonth)
			{
				// 以年份查询
				// 是否根据区域查询
				if (null == cityRegion)
				{

					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						// 如果区域列表中未存在此区域，则将区域字段加入列表中
						boolean isContains = areaList.contains(po.getCityRegion());
						if (!isContains)
						{
							areaList.add(po.getCityRegion());
						}
					}

				}
				else
				{
					areaList.add(cityRegion);
				}

				// 根据区域和年份加载数据
				String nowYear = model.getQueryYear();
				String lastYear = String.valueOf(Integer.parseInt(nowYear) - 1);
				
				Tg_DepositProjectAnalysis_ViewForm areaMmodel = new Tg_DepositProjectAnalysis_ViewForm();
				areaMmodel.setCityRegion(null);
				areaMmodel.setQueryYear(nowYear);
				areaCountList = tg_DepositProjectAnalysis_ViewDao
						.findByPage(tg_DepositProjectAnalysis_ViewDao
								.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), areaMmodel));
				
				
				//计算同期总面积
				escrowAreaCount = 0.00;
				preEscrowAreaCount = 0.00;
				for (Tg_DepositProjectAnalysis_View po : areaCountList)
				{
					escrowAreaCount += null==po.getEscrowArea()?0.00:po.getEscrowArea();
					preEscrowAreaCount += null==po.getPreEscrowArea()?0.00:po.getPreEscrowArea();
				}
				for (String city : areaList)
				{
					model = new Tg_DepositProjectAnalysis_ViewForm();
					model.setCityRegion(city);
					model.setQueryYear(nowYear);
					
					tg_DepositProjectAnalysis_ViewList = tg_DepositProjectAnalysis_ViewDao
							.findByPage(tg_DepositProjectAnalysis_ViewDao
									.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));
					
					
					/*
					 * 根据区域计算累加数值
					 * // 区域
			String cityRegionPo = "";
			// 已签约托管面积（㎡）
			Double escrowArea = 0.00;
			// 已签约托管面积区域占比（%）
			String escrowAreaRatio = "";
			// 已预售托管面积（㎡）
			Double preEscrowArea = 0.00;
			// 已预售托管面积区域占比（%）
			String preEscrowAreaRatio = "";
					 */
					viewPo = new Tg_DepositProjectAnalysis_View();
					viewPo.setBusiKind("当前");
					viewPo.setCityRegion(city);
					
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						po.setBusiKind("当前");
						viewPo.setEscrowArea((null==viewPo.getEscrowArea()?0.00:viewPo.getEscrowArea())+(null==po.getEscrowArea()?0.00:po.getEscrowArea()));
						viewPo.setEscrowAreaRatio(po.getEscrowAreaRatio());
						viewPo.setPreEscrowArea((null==viewPo.getPreEscrowArea()?0.00:viewPo.getPreEscrowArea())+(null==po.getPreEscrowArea()?0.00:po.getPreEscrowArea()));
						viewPo.setPreEscrowAreaRatio(po.getPreEscrowAreaRatio());
					}
					
					//计算已签约托管面积区域占比（%）
					viewPo.setEscrowAreaRatio(String.valueOf((viewPo.getEscrowArea()/escrowAreaCount)*100));
					//计算已预售托管面积区域占比（%）
					viewPo.setPreEscrowAreaRatio(String.valueOf((viewPo.getPreEscrowArea()/preEscrowAreaCount)*100));
					
					viewList.add(viewPo);

					// 同比
					model.setQueryYear(lastYear);
					tg_DepositProjectAnalysis_ViewList = tg_DepositProjectAnalysis_ViewDao
							.findByPage(tg_DepositProjectAnalysis_ViewDao
									.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));

					viewPo = new Tg_DepositProjectAnalysis_View();
					viewPo.setBusiKind("同比");
					viewPo.setCityRegion(city);
					
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						po.setBusiKind("同比");
						viewPo.setEscrowArea((null==viewPo.getEscrowArea()?0.00:viewPo.getEscrowArea())+(null==po.getEscrowArea()?0.00:po.getEscrowArea()));
						viewPo.setEscrowAreaRatio(po.getEscrowAreaRatio());
						viewPo.setPreEscrowArea((null==viewPo.getPreEscrowArea()?0.00:viewPo.getPreEscrowArea())+(null==po.getPreEscrowArea()?0.00:po.getPreEscrowArea()));
						viewPo.setPreEscrowAreaRatio(po.getPreEscrowAreaRatio());
					}

					viewList.add(viewPo);

				}

			}
			else if (null == queryMonth && null != queryQuarter)
			{
				// 以季度查询
				// 是否根据区域查询
				if (null == cityRegion)
				{
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						// 如果区域列表中未存在此区域，则将区域字段加入列表中
						boolean isContains = areaList.contains(po.getCityRegion());
						if (!isContains)
						{
							areaList.add(po.getCityRegion());
						}
					}
				}
				else
				{
					areaList.add(cityRegion);
				}

				// 根据区域和年份+季度加载数据
				String nowYear = model.getQueryYear();
				String lastYear = String.valueOf(Integer.parseInt(nowYear) - 1);

				// 环比（季度）：如果当前季度是第一季度，则取去年的最后一个季度
				String nowQuarter = model.getQueryQuarter();

				for (String city : areaList)
				{
					model = new Tg_DepositProjectAnalysis_ViewForm();
					model.setCityRegion(city);
					model.setQueryYear(nowYear);
					model.setQueryQuarter(model.getQueryQuarter());

					tg_DepositProjectAnalysis_ViewList = tg_DepositProjectAnalysis_ViewDao
							.findByPage(tg_DepositProjectAnalysis_ViewDao
									.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));
					
					viewPo = new Tg_DepositProjectAnalysis_View();
					viewPo.setBusiKind("当前");
					viewPo.setCityRegion(city);
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						po.setBusiKind("当前");
						viewPo.setEscrowArea((null==viewPo.getEscrowArea()?0.00:viewPo.getEscrowArea())+(null==po.getEscrowArea()?0.00:po.getEscrowArea()));
						viewPo.setEscrowAreaRatio(po.getEscrowAreaRatio());
						viewPo.setPreEscrowArea((null==viewPo.getPreEscrowArea()?0.00:viewPo.getPreEscrowArea())+(null==po.getPreEscrowArea()?0.00:po.getPreEscrowArea()));
						viewPo.setPreEscrowAreaRatio(po.getPreEscrowAreaRatio());
					}
					viewList.add(viewPo);

					// 同比
					model.setQueryYear(lastYear);
					tg_DepositProjectAnalysis_ViewList = tg_DepositProjectAnalysis_ViewDao
							.findByPage(tg_DepositProjectAnalysis_ViewDao
									.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));
					
					viewPo = new Tg_DepositProjectAnalysis_View();
					viewPo.setBusiKind("同比");
					viewPo.setCityRegion(city);
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						po.setBusiKind("同比");
						viewPo.setEscrowArea((null==viewPo.getEscrowArea()?0.00:viewPo.getEscrowArea())+(null==po.getEscrowArea()?0.00:po.getEscrowArea()));
						viewPo.setEscrowAreaRatio(po.getEscrowAreaRatio());
						viewPo.setPreEscrowArea((null==viewPo.getPreEscrowArea()?0.00:viewPo.getPreEscrowArea())+(null==po.getPreEscrowArea()?0.00:po.getPreEscrowArea()));
						viewPo.setPreEscrowAreaRatio(po.getPreEscrowAreaRatio());
					}
					viewList.add(viewPo);

					// 环比:如果当前季度是第一季度，则取去年的最后一个季度
					String lastQuarter = String.valueOf(Integer.parseInt(nowQuarter) - 1);
					if ("0".equals(lastQuarter))
					{
						model.setQueryQuarter("4");
						model.setQueryYear(lastYear);
					}
					else
					{
						model.setQueryQuarter(nowQuarter);
						model.setQueryYear(nowYear);
					}
					model.setQueryYear(lastYear);
					tg_DepositProjectAnalysis_ViewList = tg_DepositProjectAnalysis_ViewDao
							.findByPage(tg_DepositProjectAnalysis_ViewDao
									.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));
					
					viewPo = new Tg_DepositProjectAnalysis_View();
					viewPo.setBusiKind("环比");
					viewPo.setCityRegion(city);
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						po.setBusiKind("环比");
						viewPo.setEscrowArea((null==viewPo.getEscrowArea()?0.00:viewPo.getEscrowArea())+(null==po.getEscrowArea()?0.00:po.getEscrowArea()));
						viewPo.setEscrowAreaRatio(po.getEscrowAreaRatio());
						viewPo.setPreEscrowArea((null==viewPo.getPreEscrowArea()?0.00:viewPo.getPreEscrowArea())+(null==po.getPreEscrowArea()?0.00:po.getPreEscrowArea()));
						viewPo.setPreEscrowAreaRatio(po.getPreEscrowAreaRatio());
					}
					viewList.add(viewPo);
				}
			}
			else
			{
				// 以月份查询
				// 是否根据区域查询
				if (null == cityRegion)
				{
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						// 如果区域列表中未存在此区域，则将区域字段加入列表中
						boolean isContains = areaList.contains(po.getCityRegion());
						if (!isContains)
						{
							areaList.add(po.getCityRegion());
						}
					}
				}
				else
				{
					areaList.add(cityRegion);
				}

				// 根据区域和年份+月份加载数据
				String nowYear = model.getQueryYear();
				String lastYear = String.valueOf(Integer.parseInt(nowYear) - 1);

				// 环比
				String nowMonth = model.getQueryMonth();

				for (String city : areaList)
				{
					model = new Tg_DepositProjectAnalysis_ViewForm();
					model.setCityRegion(city);
					model.setQueryYear(nowYear);
					model.setQueryQuarter(model.getQueryQuarter());

					tg_DepositProjectAnalysis_ViewList = tg_DepositProjectAnalysis_ViewDao
							.findByPage(tg_DepositProjectAnalysis_ViewDao
									.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));
					
					viewPo = new Tg_DepositProjectAnalysis_View();
					viewPo.setBusiKind("当前");
					viewPo.setCityRegion(city);
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						po.setBusiKind("当前");
						viewPo.setEscrowArea((null==viewPo.getEscrowArea()?0.00:viewPo.getEscrowArea())+(null==po.getEscrowArea()?0.00:po.getEscrowArea()));
						viewPo.setEscrowAreaRatio(po.getEscrowAreaRatio());
						viewPo.setPreEscrowArea((null==viewPo.getPreEscrowArea()?0.00:viewPo.getPreEscrowArea())+(null==po.getPreEscrowArea()?0.00:po.getPreEscrowArea()));
						viewPo.setPreEscrowAreaRatio(po.getPreEscrowAreaRatio());
					}
					viewList.add(viewPo);

					// 同比
					model.setQueryYear(lastYear);
					tg_DepositProjectAnalysis_ViewList = tg_DepositProjectAnalysis_ViewDao
							.findByPage(tg_DepositProjectAnalysis_ViewDao
									.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));
					
					viewPo = new Tg_DepositProjectAnalysis_View();
					viewPo.setBusiKind("同比");
					viewPo.setCityRegion(city);
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						po.setBusiKind("同比");
						viewPo.setEscrowArea((null==viewPo.getEscrowArea()?0.00:viewPo.getEscrowArea())+(null==po.getEscrowArea()?0.00:po.getEscrowArea()));
						viewPo.setEscrowAreaRatio(po.getEscrowAreaRatio());
						viewPo.setPreEscrowArea((null==viewPo.getPreEscrowArea()?0.00:viewPo.getPreEscrowArea())+(null==po.getPreEscrowArea()?0.00:po.getPreEscrowArea()));
						viewPo.setPreEscrowAreaRatio(po.getPreEscrowAreaRatio());
					}
					viewList.add(viewPo);

					// 环比：如果当前月是1月，则取去年的12月份
					String lastMonth = String.valueOf(Integer.parseInt(nowMonth) - 1);
					if ("0".equals(lastMonth))
					{
						model.setQueryMonth("12");
						model.setQueryYear(lastYear);
					}
					else
					{
						if(lastMonth.length()==1){
							lastMonth = "0"+lastMonth;
						}
						model.setQueryMonth(lastMonth);
						model.setQueryYear(nowYear);
					}
					tg_DepositProjectAnalysis_ViewList = tg_DepositProjectAnalysis_ViewDao
							.findByPage(tg_DepositProjectAnalysis_ViewDao
									.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));
					
					viewPo = new Tg_DepositProjectAnalysis_View();
					viewPo.setBusiKind("环比");
					viewPo.setCityRegion(city);
					for (Tg_DepositProjectAnalysis_View po : tg_DepositProjectAnalysis_ViewList)
					{
						po.setBusiKind("环比");
						viewPo.setEscrowArea((null==viewPo.getEscrowArea()?0.00:viewPo.getEscrowArea())+(null==po.getEscrowArea()?0.00:po.getEscrowArea()));
						viewPo.setEscrowAreaRatio(po.getEscrowAreaRatio());
						viewPo.setPreEscrowArea((null==viewPo.getPreEscrowArea()?0.00:viewPo.getPreEscrowArea())+(null==po.getPreEscrowArea()?0.00:po.getPreEscrowArea()));
						viewPo.setPreEscrowAreaRatio(po.getPreEscrowAreaRatio());
					}
					viewList.add(viewPo);

				}
			}

		}
		else
		{
			// 未查询到数据
			viewList = new ArrayList<>();
		}

		properties.put("tg_DepositProjectAnalysis_ViewList", viewList);

		properties.put("keyword", keyword);
//		properties.put(S_NormalFlag.totalPage, totalPage);
//		properties.put(S_NormalFlag.pageNumber, pageNumber);
//		properties.put(S_NormalFlag.countPerPage, countPerPage);
//		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * 格式化返回数据 增加同比环笔数据
	 * 
	 * @param tg_DepositProjectAnalysis_ViewList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Tg_DepositProjectAnalysis_View> formart(
			List<Tg_DepositProjectAnalysis_View> tg_DepositProjectAnalysis_ViewList)
	{
		// 定义返回数据集合
		List<Tg_DepositProjectAnalysis_View> list = new ArrayList<Tg_DepositProjectAnalysis_View>();

		/*
		 * 判断查询结果是否为空
		 * 为空 定义虚拟数据集合
		 * 不为空 循环数据增加同比和环笔数据
		 */
		if (null != tg_DepositProjectAnalysis_ViewList && tg_DepositProjectAnalysis_ViewList.size() > 0)
		{
			for (Tg_DepositProjectAnalysis_View tg_DepositProjectAnalysis_View : tg_DepositProjectAnalysis_ViewList)
			{
				tg_DepositProjectAnalysis_View.setBusiKind("当前");
				list.add(tg_DepositProjectAnalysis_View);

				String currentTIme = tg_DepositProjectAnalysis_View.getDepositTime();

				/* 查询同比 获取当前数据的托管时间-1年 进行查询获取数据 */
				// 获取当前时间前一年时间
				Tg_DepositProjectAnalysis_ViewForm model = new Tg_DepositProjectAnalysis_ViewForm();
				model.setCityRegion(tg_DepositProjectAnalysis_View.getCityRegion());
				String yearBefore = formart.getSpecifiedYearBefore(currentTIme);
				model.setDepositTime(yearBefore);

				List<Tg_DepositProjectAnalysis_View> depositProjectAnalysisList = tg_DepositProjectAnalysis_ViewDao
						.findByPage(tg_DepositProjectAnalysis_ViewDao
								.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), model));
				if (null == depositProjectAnalysisList || depositProjectAnalysisList.size() < 1)
				{
					Tg_DepositProjectAnalysis_View depositProjectAnalysis = new Tg_DepositProjectAnalysis_View();
					depositProjectAnalysis.setTableId(0L);
					depositProjectAnalysis.setBusiKind("同比");
					depositProjectAnalysis.setCityRegion(model.getCityRegion());
					depositProjectAnalysis.setDepositTime("-");
					depositProjectAnalysis.setEscrowArea(0.00);
					depositProjectAnalysis.setEscrowAreaRatio("-");
					depositProjectAnalysis.setPreEscrowArea(0.00);
					depositProjectAnalysis.setPreEscrowAreaRatio("-");

					list.add(depositProjectAnalysis);
				}
				else
				{
					for (Tg_DepositProjectAnalysis_View analysis : depositProjectAnalysisList)
					{
						analysis.setBusiKind("同比");
						list.add(analysis);
					}
				}

				/* 查询环比 获取当前数据的托管时间-1月 进行查询获取数据 */
				// 获取当前时间前一月时间
				String monthBefore = formart.getSpecifiedMonthBefore(currentTIme);
				Tg_DepositProjectAnalysis_ViewForm form = new Tg_DepositProjectAnalysis_ViewForm();
				form.setCityRegion(tg_DepositProjectAnalysis_View.getCityRegion());
				form.setDepositTime(monthBefore);

				List<Tg_DepositProjectAnalysis_View> depositList = tg_DepositProjectAnalysis_ViewDao
						.findByPage(tg_DepositProjectAnalysis_ViewDao
								.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), form));

				if (null == depositList || depositList.size() < 1)
				{
					Tg_DepositProjectAnalysis_View depositProjectAnalysis = new Tg_DepositProjectAnalysis_View();
					depositProjectAnalysis.setTableId(0L);
					depositProjectAnalysis.setBusiKind("环比");
					depositProjectAnalysis.setCityRegion(form.getCityRegion());
					depositProjectAnalysis.setDepositTime("-");
					depositProjectAnalysis.setEscrowArea(0.00);
					depositProjectAnalysis.setEscrowAreaRatio("-");
					depositProjectAnalysis.setPreEscrowArea(0.00);
					depositProjectAnalysis.setPreEscrowAreaRatio("-");

					list.add(depositProjectAnalysis);
				}
				else
				{
					for (Tg_DepositProjectAnalysis_View analysis : depositList)
					{
						analysis.setBusiKind("环比");
						list.add(analysis);
					}
				}
			}
		}
		return list;
	}
}
