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
public class Tg_DepositProjectAnalysis_ViewCopyListService
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

		/**
		 * xsz by time 2018-12-8 15:57:56
		 * 先将数据库中所有数据查询出来
		 * 再根据条件加载
		 */
		Tg_DepositProjectAnalysis_ViewForm allModel = new Tg_DepositProjectAnalysis_ViewForm();
		List<Tg_DepositProjectAnalysis_View> allViewList = null;
		allViewList = tg_DepositProjectAnalysis_ViewDao.findByPage(
				tg_DepositProjectAnalysis_ViewDao.getQuery(tg_DepositProjectAnalysis_ViewDao.getBasicHQL(), allModel));

		if (null == allViewList || allViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效信息");
		}

		Long cityRegionId = model.getCityRegionId();// 区域Id
		// 查询区域
		if (null == cityRegionId || cityRegionId < 0)
		{
			model.setCityRegion(null);
		}
		else
		{
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

		// 区域List
		List<String> areaList = new ArrayList<String>();
		for (Tg_DepositProjectAnalysis_View po : allViewList)
		{
			boolean isContains = areaList.contains(po.getCityRegion());
			if (!isContains)
			{
				areaList.add(po.getCityRegion());
			}
		}

		/*
		 * 正式查询开始
		 */
		// 返回到前端的list
		List<Tg_DepositProjectAnalysis_View> viewList = new ArrayList<Tg_DepositProjectAnalysis_View>();

		try
		{
			String cityRegion = model.getCityRegion();
			/*
			 * 根据是否有区域分条件查询
			 */
			if (null == cityRegion || cityRegion.trim().isEmpty())
			{
				// TODO 不分区域查询
				if (null == queryQuarter && null == queryMonth)
				{
					// 年

					for (String city : areaList)
					{

						Tg_DepositProjectAnalysis_View viewPo1 = new Tg_DepositProjectAnalysis_View();
						viewPo1.setBusiKind("当前");
						viewPo1.setCityRegion(city);

						Tg_DepositProjectAnalysis_View viewPo2 = new Tg_DepositProjectAnalysis_View();
						viewPo2.setBusiKind("同比");
						viewPo2.setCityRegion(city);

						double escrowAreaCount1 = 0.00;
						double preEscrowAreaCount1 = 0.00;

						double escrowAreaCount2 = 0.00;
						double preEscrowAreaCount2 = 0.00;

						// 年
						for (Tg_DepositProjectAnalysis_View po : allViewList)
						{
							// 当前
							if (queryYear.equals(po.getQueryYear()) && city.equals(po.getCityRegion()))
							{
								viewPo1.setEscrowArea((null == viewPo1.getEscrowArea() ? 0.00 : viewPo1.getEscrowArea())
										+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
								viewPo1.setPreEscrowArea(
										(null == viewPo1.getPreEscrowArea() ? 0.00 : viewPo1.getPreEscrowArea())
												+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
							}

							// 计算当前面积总计
							if (queryYear.equals(po.getQueryYear()))
							{
								escrowAreaCount1 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
								preEscrowAreaCount1 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
							}

							// 同比
							if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
									&& city.equals(po.getCityRegion()))
							{
								viewPo2.setEscrowArea((null == viewPo2.getEscrowArea() ? 0.00 : viewPo2.getEscrowArea())
										+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
								viewPo2.setPreEscrowArea(
										(null == viewPo2.getPreEscrowArea() ? 0.00 : viewPo2.getPreEscrowArea())
												+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
							}

							// 计算同比面积总计
							if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear()))
							{
								escrowAreaCount2 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
								preEscrowAreaCount2 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
							}

						}

						// 计算当前已签约托管面积占比
						viewPo1.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
						// 计算当前已预售托管面积占比
						viewPo1.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));

						if (null == viewPo2.getEscrowArea() || viewPo2.getEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
							// 计算同比已签约托管面积
							viewPo2.setEscrowArea(viewPo1.getEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
									- (viewPo2.getEscrowArea() / escrowAreaCount2) * 100));

							// 计算同比已签约托管面积
							viewPo2.setEscrowArea(viewPo1.getEscrowArea() - viewPo2.getEscrowArea());
						}

						if (null == viewPo2.getPreEscrowArea() || viewPo2.getPreEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo2.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
							// 计算同步已预售托管面积
							viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo2.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
											- (viewPo2.getPreEscrowArea() / preEscrowAreaCount2) * 100));
							// 计算同步已预售托管面积
							viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo2.getPreEscrowArea());
						}

						

						viewList.add(viewPo1);
						viewList.add(viewPo2);
					}

				}
				else if (null == queryMonth && null != queryQuarter)
				{
					// 季度
					for (String city : areaList)
					{

						// 季度
						Tg_DepositProjectAnalysis_View viewPo1 = new Tg_DepositProjectAnalysis_View();
						viewPo1.setBusiKind("当前");
						viewPo1.setCityRegion(city);

						Tg_DepositProjectAnalysis_View viewPo2 = new Tg_DepositProjectAnalysis_View();
						viewPo2.setBusiKind("同比");
						viewPo2.setCityRegion(city);

						Tg_DepositProjectAnalysis_View viewPo3 = new Tg_DepositProjectAnalysis_View();
						viewPo3.setBusiKind("环比");
						viewPo3.setCityRegion(city);

						double escrowAreaCount1 = 0.00;
						double preEscrowAreaCount1 = 0.00;

						double escrowAreaCount2 = 0.00;
						double preEscrowAreaCount2 = 0.00;

						double escrowAreaCount3 = 0.00;
						double preEscrowAreaCount3 = 0.00;

						for (Tg_DepositProjectAnalysis_View po : allViewList)
						{
							// 当前
							if (queryYear.equals(po.getQueryYear()) && queryQuarter.equals(po.getQueryQuarter())
									&& city.equals(po.getCityRegion()))
							{
								viewPo1.setEscrowArea((null == viewPo1.getEscrowArea() ? 0.00 : viewPo1.getEscrowArea())
										+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
								viewPo1.setPreEscrowArea(
										(null == viewPo1.getPreEscrowArea() ? 0.00 : viewPo1.getPreEscrowArea())
												+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
							}

							// 计算当前面积总计
							if (queryYear.equals(po.getQueryYear()) && queryQuarter.equals(po.getQueryQuarter()))
							{
								escrowAreaCount1 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
								preEscrowAreaCount1 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
							}

							// 同比
							if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
									&& queryQuarter.equals(po.getQueryQuarter()) && city.equals(po.getCityRegion()))
							{
								viewPo2.setEscrowArea((null == viewPo2.getEscrowArea() ? 0.00 : viewPo2.getEscrowArea())
										+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
								viewPo2.setPreEscrowArea(
										(null == viewPo2.getPreEscrowArea() ? 0.00 : viewPo2.getPreEscrowArea())
												+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
							}

							// 计算同比面积总计
							if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
									&& queryQuarter.equals(po.getQueryQuarter()))
							{
								escrowAreaCount2 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
								preEscrowAreaCount2 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
							}

							// 环比:如果当前季度是第一季度，则取去年的最后一个季度
							String lastQuarter = String.valueOf(Integer.parseInt(queryQuarter) - 1);
							String quarter = lastQuarter;
							String year = queryYear;
							if ("0".equals(lastQuarter))
							{
								quarter = "4";
								year = String.valueOf(Integer.parseInt(year) - 1);
							}
							if (year.equals(po.getQueryYear()) && quarter.equals(po.getQueryQuarter())
									&& city.equals(po.getCityRegion()))
							{
								viewPo3.setEscrowArea((null == viewPo3.getEscrowArea() ? 0.00 : viewPo3.getEscrowArea())
										+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
								viewPo3.setPreEscrowArea(
										(null == viewPo3.getPreEscrowArea() ? 0.00 : viewPo3.getPreEscrowArea())
												+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
							}

							// 计算环比面积总计
							if (year.equals(po.getQueryYear()) && quarter.equals(po.getQueryQuarter()))
							{
								escrowAreaCount3 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
								preEscrowAreaCount3 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
							}

						}

						// 计算当前已签约托管面积占比
						viewPo1.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
						// 计算当前已预售托管面积占比
						viewPo1.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));

						//
						if (null == viewPo2.getEscrowArea() || viewPo2.getEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
							// 计算同比已签约托管面积
							viewPo2.setEscrowArea(viewPo1.getEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
									- (viewPo2.getEscrowArea() / escrowAreaCount2) * 100));

							// 计算同比已签约托管面积
							viewPo2.setEscrowArea(viewPo1.getEscrowArea() - viewPo2.getEscrowArea());
						}

						if (null == viewPo2.getPreEscrowArea() || viewPo2.getPreEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo2.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
							// 计算同步已预售托管面积
							viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo2.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
											- (viewPo2.getPreEscrowArea() / preEscrowAreaCount2) * 100));
							// 计算同步已预售托管面积
							viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo2.getPreEscrowArea());
						}
						
						
						//
						if (null == viewPo3.getEscrowArea() || viewPo3.getEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo3.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
							// 计算同比已签约托管面积
							viewPo3.setEscrowArea(viewPo1.getEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo3.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
									- (viewPo3.getEscrowArea() / escrowAreaCount3) * 100));

							// 计算同比已签约托管面积
							viewPo3.setEscrowArea(viewPo1.getEscrowArea() - viewPo3.getEscrowArea());
						}

						if (null == viewPo3.getPreEscrowArea() || viewPo3.getPreEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo3.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
							// 计算同步已预售托管面积
							viewPo3.setPreEscrowArea(viewPo1.getPreEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo3.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
											- (viewPo3.getPreEscrowArea() / preEscrowAreaCount3) * 100));
							// 计算同步已预售托管面积
							viewPo3.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo3.getPreEscrowArea());
						}
						

						viewList.add(viewPo1);
						viewList.add(viewPo2);
						viewList.add(viewPo3);

					}

				}
				else
				{
					// 月份
					for (String city : areaList)
					{

						// 月份
						Tg_DepositProjectAnalysis_View viewPo1 = new Tg_DepositProjectAnalysis_View();
						viewPo1.setBusiKind("当前");
						viewPo1.setCityRegion(city);

						Tg_DepositProjectAnalysis_View viewPo2 = new Tg_DepositProjectAnalysis_View();
						viewPo2.setBusiKind("同比");
						viewPo2.setCityRegion(city);

						Tg_DepositProjectAnalysis_View viewPo3 = new Tg_DepositProjectAnalysis_View();
						viewPo3.setBusiKind("环比");
						viewPo3.setCityRegion(city);

						double escrowAreaCount1 = 0.00;
						double preEscrowAreaCount1 = 0.00;

						double escrowAreaCount2 = 0.00;
						double preEscrowAreaCount2 = 0.00;

						double escrowAreaCount3 = 0.00;
						double preEscrowAreaCount3 = 0.00;

						for (Tg_DepositProjectAnalysis_View po : allViewList)
						{
							// 当前
							if (queryYear.equals(po.getQueryYear()) && queryMonth.equals(po.getQueryMonth())
									&& city.equals(po.getCityRegion()))
							{
								viewPo1.setEscrowArea((null == viewPo1.getEscrowArea() ? 0.00 : viewPo1.getEscrowArea())
										+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
								viewPo1.setPreEscrowArea(
										(null == viewPo1.getPreEscrowArea() ? 0.00 : viewPo1.getPreEscrowArea())
												+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
							}

							// 计算当前面积总计
							if (queryYear.equals(po.getQueryYear()) && queryMonth.equals(po.getQueryMonth()))
							{
								escrowAreaCount1 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
								preEscrowAreaCount1 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
							}

							// 同比
							if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
									&& queryMonth.equals(po.getQueryMonth()) && city.equals(po.getCityRegion()))
							{
								viewPo2.setEscrowArea((null == viewPo2.getEscrowArea() ? 0.00 : viewPo2.getEscrowArea())
										+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
								viewPo2.setPreEscrowArea(
										(null == viewPo2.getPreEscrowArea() ? 0.00 : viewPo2.getPreEscrowArea())
												+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
							}

							// 计算同比面积总计
							if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
									&& queryMonth.equals(po.getQueryMonth()))
							{
								escrowAreaCount2 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
								preEscrowAreaCount2 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
							}

							// 环比:如果当前季度是第一季度，则取去年的最后一个季度
							String lastMonth = String.valueOf(Integer.parseInt(queryMonth) - 1);
							String month = lastMonth;
							String year = queryYear;
							if ("0".equals(lastMonth))
							{
								month = "12";
								year = String.valueOf(Integer.parseInt(year) - 1);
							}
							else
							{
								if (month.length() == 1)
								{
									month = "0" + month;
								}
							}

							if (year.equals(po.getQueryYear()) && month.equals(po.getQueryMonth())
									&& city.equals(po.getCityRegion()))
							{
								viewPo3.setEscrowArea((null == viewPo3.getEscrowArea() ? 0.00 : viewPo3.getEscrowArea())
										+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
								viewPo3.setPreEscrowArea(
										(null == viewPo3.getPreEscrowArea() ? 0.00 : viewPo3.getPreEscrowArea())
												+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
							}

							// 计算环比面积总计
							if (year.equals(po.getQueryYear()) && month.equals(po.getQueryMonth()))
							{
								escrowAreaCount3 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
								preEscrowAreaCount3 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
							}

						}

						// 计算当前已签约托管面积占比
						viewPo1.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
						// 计算当前已预售托管面积占比
						viewPo1.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));

						//
						if (null == viewPo2.getEscrowArea() || viewPo2.getEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
							// 计算同比已签约托管面积
							viewPo2.setEscrowArea(viewPo1.getEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
									- (viewPo2.getEscrowArea() / escrowAreaCount2) * 100));

							// 计算同比已签约托管面积
							viewPo2.setEscrowArea(viewPo1.getEscrowArea() - viewPo2.getEscrowArea());
						}

						if (null == viewPo2.getPreEscrowArea() || viewPo2.getPreEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo2.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
							// 计算同步已预售托管面积
							viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo2.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
											- (viewPo2.getPreEscrowArea() / preEscrowAreaCount2) * 100));
							// 计算同步已预售托管面积
							viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo2.getPreEscrowArea());
						}
						
						
						//
						if (null == viewPo3.getEscrowArea() || viewPo3.getEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo3.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
							// 计算同比已签约托管面积
							viewPo3.setEscrowArea(viewPo1.getEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo3.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
									- (viewPo3.getEscrowArea() / escrowAreaCount3) * 100));

							// 计算同比已签约托管面积
							viewPo3.setEscrowArea(viewPo1.getEscrowArea() - viewPo3.getEscrowArea());
						}

						if (null == viewPo3.getPreEscrowArea() || viewPo3.getPreEscrowArea() == 0.00)
						{
							// 计算同比已签约托管面积占比
							viewPo3.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
							// 计算同步已预售托管面积
							viewPo3.setPreEscrowArea(viewPo1.getPreEscrowArea());
						}
						else
						{
							// 计算同比已签约托管面积占比
							viewPo3.setPreEscrowAreaRatio(
									String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
											- (viewPo3.getPreEscrowArea() / preEscrowAreaCount3) * 100));
							// 计算同步已预售托管面积
							viewPo3.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo3.getPreEscrowArea());
						}
						

						viewList.add(viewPo1);
						viewList.add(viewPo2);
						viewList.add(viewPo3);

					}

				}

			}
			else
			{
				// TODO 分区域查询
				if (null == queryQuarter && null == queryMonth)
				{
					Tg_DepositProjectAnalysis_View viewPo1 = new Tg_DepositProjectAnalysis_View();
					viewPo1.setBusiKind("当前");
					viewPo1.setCityRegion(cityRegion);

					Tg_DepositProjectAnalysis_View viewPo2 = new Tg_DepositProjectAnalysis_View();
					viewPo2.setBusiKind("同比");
					viewPo2.setCityRegion(cityRegion);

					double escrowAreaCount1 = 0.00;
					double preEscrowAreaCount1 = 0.00;

					double escrowAreaCount2 = 0.00;
					double preEscrowAreaCount2 = 0.00;

					// 年
					for (Tg_DepositProjectAnalysis_View po : allViewList)
					{
						// 当前
						if (queryYear.equals(po.getQueryYear()) && cityRegion.equals(po.getCityRegion()))
						{
							viewPo1.setEscrowArea((null == viewPo1.getEscrowArea() ? 0.00 : viewPo1.getEscrowArea())
									+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
							viewPo1.setPreEscrowArea(
									(null == viewPo1.getPreEscrowArea() ? 0.00 : viewPo1.getPreEscrowArea())
											+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
						}

						// 计算当前面积总计
						if (queryYear.equals(po.getQueryYear()))
						{
							escrowAreaCount1 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
							preEscrowAreaCount1 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
						}

						// 同比
						if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
								&& cityRegion.equals(po.getCityRegion()))
						{
							viewPo2.setEscrowArea((null == viewPo2.getEscrowArea() ? 0.00 : viewPo2.getEscrowArea())
									+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
							viewPo2.setPreEscrowArea(
									(null == viewPo2.getPreEscrowArea() ? 0.00 : viewPo2.getPreEscrowArea())
											+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
						}

						// 计算同比面积总计
						if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear()))
						{
							escrowAreaCount2 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
							preEscrowAreaCount2 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
						}

					}

					// 计算当前已签约托管面积占比
					viewPo1.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
					// 计算当前已预售托管面积占比
					viewPo1.setPreEscrowAreaRatio(String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));

					//
					if (null == viewPo2.getEscrowArea() || viewPo2.getEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
						// 计算同比已签约托管面积
						viewPo2.setEscrowArea(viewPo1.getEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
								- (viewPo2.getEscrowArea() / escrowAreaCount2) * 100));

						// 计算同比已签约托管面积
						viewPo2.setEscrowArea(viewPo1.getEscrowArea() - viewPo2.getEscrowArea());
					}

					if (null == viewPo2.getPreEscrowArea() || viewPo2.getPreEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo2.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
						// 计算同步已预售托管面积
						viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo2.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
										- (viewPo2.getPreEscrowArea() / preEscrowAreaCount2) * 100));
						// 计算同步已预售托管面积
						viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo2.getPreEscrowArea());
					}
					
					viewList.add(viewPo1);
					viewList.add(viewPo2);

				}
				else if (null == queryMonth && null != queryQuarter)
				{
					// 季度
					Tg_DepositProjectAnalysis_View viewPo1 = new Tg_DepositProjectAnalysis_View();
					viewPo1.setBusiKind("当前");
					viewPo1.setCityRegion(cityRegion);

					Tg_DepositProjectAnalysis_View viewPo2 = new Tg_DepositProjectAnalysis_View();
					viewPo2.setBusiKind("同比");
					viewPo2.setCityRegion(cityRegion);

					Tg_DepositProjectAnalysis_View viewPo3 = new Tg_DepositProjectAnalysis_View();
					viewPo3.setBusiKind("环比");
					viewPo3.setCityRegion(cityRegion);

					double escrowAreaCount1 = 0.00;
					double preEscrowAreaCount1 = 0.00;

					double escrowAreaCount2 = 0.00;
					double preEscrowAreaCount2 = 0.00;

					double escrowAreaCount3 = 0.00;
					double preEscrowAreaCount3 = 0.00;

					for (Tg_DepositProjectAnalysis_View po : allViewList)
					{
						// 当前
						if (queryYear.equals(po.getQueryYear()) && queryQuarter.equals(po.getQueryQuarter())
								&& cityRegion.equals(po.getCityRegion()))
						{
							viewPo1.setEscrowArea((null == viewPo1.getEscrowArea() ? 0.00 : viewPo1.getEscrowArea())
									+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
							viewPo1.setPreEscrowArea(
									(null == viewPo1.getPreEscrowArea() ? 0.00 : viewPo1.getPreEscrowArea())
											+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
						}

						// 计算当前面积总计
						if (queryYear.equals(po.getQueryYear()) && queryQuarter.equals(po.getQueryQuarter()))
						{
							escrowAreaCount1 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
							preEscrowAreaCount1 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
						}

						// 同比
						if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
								&& queryQuarter.equals(po.getQueryQuarter()) && cityRegion.equals(po.getCityRegion()))
						{
							viewPo2.setEscrowArea((null == viewPo2.getEscrowArea() ? 0.00 : viewPo2.getEscrowArea())
									+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
							viewPo2.setPreEscrowArea(
									(null == viewPo2.getPreEscrowArea() ? 0.00 : viewPo2.getPreEscrowArea())
											+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
						}

						// 计算同比面积总计
						if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
								&& queryQuarter.equals(po.getQueryQuarter()))
						{
							escrowAreaCount2 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
							preEscrowAreaCount2 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
						}

						// 环比:如果当前季度是第一季度，则取去年的最后一个季度
						String lastQuarter = String.valueOf(Integer.parseInt(queryQuarter) - 1);
						String quarter = lastQuarter;
						String year = queryYear;
						if ("0".equals(lastQuarter))
						{
							quarter = "4";
							year = String.valueOf(Integer.parseInt(year) - 1);
						}
						if (year.equals(po.getQueryYear()) && quarter.equals(po.getQueryQuarter())
								&& cityRegion.equals(po.getCityRegion()))
						{
							viewPo3.setEscrowArea((null == viewPo3.getEscrowArea() ? 0.00 : viewPo3.getEscrowArea())
									+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
							viewPo3.setPreEscrowArea(
									(null == viewPo3.getPreEscrowArea() ? 0.00 : viewPo3.getPreEscrowArea())
											+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
						}

						// 计算环比面积总计
						if (year.equals(po.getQueryYear()) && quarter.equals(po.getQueryQuarter()))
						{
							escrowAreaCount3 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
							preEscrowAreaCount3 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
						}

					}

					// 计算当前已签约托管面积占比
					viewPo1.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
					// 计算当前已预售托管面积占比
					viewPo1.setPreEscrowAreaRatio(String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));

					//
					if (null == viewPo2.getEscrowArea() || viewPo2.getEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
						// 计算同比已签约托管面积
						viewPo2.setEscrowArea(viewPo1.getEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
								- (viewPo2.getEscrowArea() / escrowAreaCount2) * 100));

						// 计算同比已签约托管面积
						viewPo2.setEscrowArea(viewPo1.getEscrowArea() - viewPo2.getEscrowArea());
					}

					if (null == viewPo2.getPreEscrowArea() || viewPo2.getPreEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo2.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
						// 计算同步已预售托管面积
						viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo2.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
										- (viewPo2.getPreEscrowArea() / preEscrowAreaCount2) * 100));
						// 计算同步已预售托管面积
						viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo2.getPreEscrowArea());
					}
					
					
					//
					if (null == viewPo3.getEscrowArea() || viewPo3.getEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo3.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
						// 计算同比已签约托管面积
						viewPo3.setEscrowArea(viewPo1.getEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo3.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
								- (viewPo3.getEscrowArea() / escrowAreaCount3) * 100));

						// 计算同比已签约托管面积
						viewPo3.setEscrowArea(viewPo1.getEscrowArea() - viewPo3.getEscrowArea());
					}

					if (null == viewPo3.getPreEscrowArea() || viewPo3.getPreEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo3.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
						// 计算同步已预售托管面积
						viewPo3.setPreEscrowArea(viewPo1.getPreEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo3.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
										- (viewPo3.getPreEscrowArea() / preEscrowAreaCount3) * 100));
						// 计算同步已预售托管面积
						viewPo3.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo3.getPreEscrowArea());
					}
					

					viewList.add(viewPo1);
					viewList.add(viewPo2);
					viewList.add(viewPo3);

				}
				else
				{
					// 月份
					Tg_DepositProjectAnalysis_View viewPo1 = new Tg_DepositProjectAnalysis_View();
					viewPo1.setBusiKind("当前");
					viewPo1.setCityRegion(cityRegion);

					Tg_DepositProjectAnalysis_View viewPo2 = new Tg_DepositProjectAnalysis_View();
					viewPo2.setBusiKind("同比");
					viewPo2.setCityRegion(cityRegion);

					Tg_DepositProjectAnalysis_View viewPo3 = new Tg_DepositProjectAnalysis_View();
					viewPo3.setBusiKind("环比");
					viewPo3.setCityRegion(cityRegion);

					double escrowAreaCount1 = 0.00;
					double preEscrowAreaCount1 = 0.00;

					double escrowAreaCount2 = 0.00;
					double preEscrowAreaCount2 = 0.00;

					double escrowAreaCount3 = 0.00;
					double preEscrowAreaCount3 = 0.00;

					for (Tg_DepositProjectAnalysis_View po : allViewList)
					{
						// 当前
						if (queryYear.equals(po.getQueryYear()) && queryMonth.equals(po.getQueryMonth())
								&& cityRegion.equals(po.getCityRegion()))
						{
							viewPo1.setEscrowArea((null == viewPo1.getEscrowArea() ? 0.00 : viewPo1.getEscrowArea())
									+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
							viewPo1.setPreEscrowArea(
									(null == viewPo1.getPreEscrowArea() ? 0.00 : viewPo1.getPreEscrowArea())
											+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
						}

						// 计算当前面积总计
						if (queryYear.equals(po.getQueryYear()) && queryMonth.equals(po.getQueryMonth()))
						{
							escrowAreaCount1 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
							preEscrowAreaCount1 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
						}

						// 同比
						if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
								&& queryMonth.equals(po.getQueryMonth()) && cityRegion.equals(po.getCityRegion()))
						{
							viewPo2.setEscrowArea((null == viewPo2.getEscrowArea() ? 0.00 : viewPo2.getEscrowArea())
									+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
							viewPo2.setPreEscrowArea(
									(null == viewPo2.getPreEscrowArea() ? 0.00 : viewPo2.getPreEscrowArea())
											+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
						}

						// 计算同比面积总计
						if (String.valueOf(Integer.parseInt(queryYear) - 1).equals(po.getQueryYear())
								&& queryMonth.equals(po.getQueryMonth()))
						{
							escrowAreaCount2 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
							preEscrowAreaCount2 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
						}

						// 环比:如果当前月是第一月，则取去年的最后一个月
						String lastMonth = String.valueOf(Integer.parseInt(queryMonth) - 1);
						String month = lastMonth;
						String year = queryYear;
						if ("0".equals(lastMonth))
						{
							month = "12";
							year = String.valueOf(Integer.parseInt(year) - 1);
						}
						else
						{
							if (month.length() == 1)
							{
								month = "0" + month;
							}
						}

						if (year.equals(po.getQueryYear()) && month.equals(po.getQueryMonth())
								&& cityRegion.equals(po.getCityRegion()))
						{
							viewPo3.setEscrowArea((null == viewPo3.getEscrowArea() ? 0.00 : viewPo3.getEscrowArea())
									+ (null == po.getEscrowArea() ? 0.00 : po.getEscrowArea()));
							viewPo3.setPreEscrowArea(
									(null == viewPo3.getPreEscrowArea() ? 0.00 : viewPo3.getPreEscrowArea())
											+ (null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea()));
						}

						// 计算环比面积总计
						if (year.equals(po.getQueryYear()) && month.equals(po.getQueryMonth()))
						{
							escrowAreaCount3 += null == po.getEscrowArea() ? 0.00 : po.getEscrowArea();
							preEscrowAreaCount3 += null == po.getPreEscrowArea() ? 0.00 : po.getPreEscrowArea();
						}

					}

					// 计算当前已签约托管面积占比
					viewPo1.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
					// 计算当前已预售托管面积占比
					viewPo1.setPreEscrowAreaRatio(String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));

					//
					if (null == viewPo2.getEscrowArea() || viewPo2.getEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
						// 计算同比已签约托管面积
						viewPo2.setEscrowArea(viewPo1.getEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo2.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
								- (viewPo2.getEscrowArea() / escrowAreaCount2) * 100));

						// 计算同比已签约托管面积
						viewPo2.setEscrowArea(viewPo1.getEscrowArea() - viewPo2.getEscrowArea());
					}

					if (null == viewPo2.getPreEscrowArea() || viewPo2.getPreEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo2.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
						// 计算同步已预售托管面积
						viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo2.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
										- (viewPo2.getPreEscrowArea() / preEscrowAreaCount2) * 100));
						// 计算同步已预售托管面积
						viewPo2.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo2.getPreEscrowArea());
					}
					
					
					//
					if (null == viewPo3.getEscrowArea() || viewPo3.getEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo3.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100));
						// 计算同比已签约托管面积
						viewPo3.setEscrowArea(viewPo1.getEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo3.setEscrowAreaRatio(String.valueOf((viewPo1.getEscrowArea() / escrowAreaCount1) * 100
								- (viewPo3.getEscrowArea() / escrowAreaCount3) * 100));

						// 计算同比已签约托管面积
						viewPo3.setEscrowArea(viewPo1.getEscrowArea() - viewPo3.getEscrowArea());
					}

					if (null == viewPo3.getPreEscrowArea() || viewPo3.getPreEscrowArea() == 0.00)
					{
						// 计算同比已签约托管面积占比
						viewPo3.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100));
						// 计算同步已预售托管面积
						viewPo3.setPreEscrowArea(viewPo1.getPreEscrowArea());
					}
					else
					{
						// 计算同比已签约托管面积占比
						viewPo3.setPreEscrowAreaRatio(
								String.valueOf((viewPo1.getPreEscrowArea() / preEscrowAreaCount1) * 100
										- (viewPo3.getPreEscrowArea() / preEscrowAreaCount3) * 100));
						// 计算同步已预售托管面积
						viewPo3.setPreEscrowArea(viewPo1.getPreEscrowArea() - viewPo3.getPreEscrowArea());
					}
					

					viewList.add(viewPo1);
					viewList.add(viewPo2);
					viewList.add(viewPo3);

				}

			}
		}
		catch (Exception e)
		{
			viewList = new ArrayList<Tg_DepositProjectAnalysis_View>();
		}

		properties.put("tg_DepositProjectAnalysis_ViewList", viewList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

}