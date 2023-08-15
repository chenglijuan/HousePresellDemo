package zhishusz.housepresell.database.dao;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.ParameterMode;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import oracle.jdbc.OracleTypes;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_RetainedRightsView2;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.extra.Qs_NodeChangeForeCast_View;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.util.EhCacheUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyInteger;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.OracleOrder;

@Repository
public abstract class BaseDao<T> {
	@Autowired
	private Empj_ProjectInfoDao projectInfoDao;
	@Autowired
	private Empj_UnitInfoDao unitInfoDao;
	@Autowired
	private Empj_HouseInfoDao houseInfoDao;

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("rawtypes")
	private Class clazz;

	private Configuration configuration;
	private StringTemplateLoader stringTemplateLoader;

	@SuppressWarnings("rawtypes")
	public BaseDao() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = (Class) type.getActualTypeArguments()[0];

		this.stringTemplateLoader = new StringTemplateLoader();
		this.configuration = new Configuration(Configuration.getVersion());
		this.configuration.setTemplateLoader(stringTemplateLoader);
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("deprecation")
	public Criteria createCriteria() {
		return getCurrentSession().createCriteria(this.clazz);
	}

	@SuppressWarnings("deprecation")
	public Criteria createCriteria(Class clazz) {
		return getCurrentSession().createCriteria(clazz);
	}

	@SuppressWarnings("unchecked")
	public T findById(Long id) {
		if (id != null && id > 0) {
			return (T) getCurrentSession().get(this.clazz, id);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public T findByIdWithClear(Long id) {
		if (id != null && id > 0) {
			Session ses = getCurrentSession();
			Object object = ses.get(this.clazz, id);
			ses.evict(object);

			return (T) object;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public T findById(Integer id) {
		if (id != null && id > 0) {
			return (T) getCurrentSession().get(this.clazz, id);
		} else {
			return null;
		}
	}

	public Serializable save(T object) {
		Serializable entity = getCurrentSession().save(object);
		getCurrentSession().flush();

		String poName = object.getClass().getSimpleName();

		EhCacheUtil.getInstance().remove(poName);

		return entity;
	}

	public void clear(T object) {
		getCurrentSession().evict(object);
		return;
	}

	public void update(T object) {
		getCurrentSession().update(object);
		getCurrentSession().flush();

		String poName = object.getClass().getSimpleName();

		EhCacheUtil.getInstance().remove(poName);

		return;
	}

	public void delete(T object) {
		getCurrentSession().delete(object);
		getCurrentSession().flush();
	}

	// public void emptyTable(String tableName)
	// {
	// String sql = "TRUNCATE TABLE "+tableName;
	// getCurrentSession().createSQLQuery(sql).executeUpdate();
	// }

	public String getHql(String sqlStr, BaseForm model) {
		String nowTimeStampStr = System.currentTimeMillis() + "";
		stringTemplateLoader.putTemplate(nowTimeStampStr, sqlStr);
		String hqlSql = "";

		try {
			Template template = configuration.getTemplate(nowTimeStampStr, "utf-8");

			// 执行插值，并输出到指定的输出流中
			StringWriter stringWriter = new StringWriter();
			template.process(model, stringWriter);

			hqlSql = stringWriter.toString();
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return hqlSql;
	}

	@SuppressWarnings("rawtypes")
	public Query getQuery(String sqlStr, BaseForm model) {
		String hqlSql = getHql(sqlStr, model);

		// System.out.println(hqlSql);

		if (hqlSql == null || hqlSql.length() == 0) {
			return null;
		} else {
			return getCurrentSession().createQuery(hqlSql).setProperties(model);
		}
	}

	@SuppressWarnings("rawtypes")
	public List findByPage(Query query, Integer pageNumber, Integer countPerPage) {
		if (query == null) {
			return new ArrayList();
		} else {
			if (pageNumber != null && pageNumber > 0) {
				query.setFirstResult(countPerPage * pageNumber - countPerPage);
				query.setMaxResults(countPerPage);
			}

			return query.list();
		}
	}

	@SuppressWarnings("rawtypes")
	public List findByPage(Criteria criteria, Integer pageNumber, Integer countPerPage) {
		if (pageNumber != null && pageNumber > 0) {
			criteria = criteria.setFirstResult(countPerPage * pageNumber - countPerPage).setMaxResults(countPerPage);
		}

		return criteria.list();
	}

	public Integer findByPage_Size(Criteria criteria) {
		return MyInteger.getInstance().parse(criteria.setProjection(Projections.rowCount()).uniqueResult());
	}

	@SuppressWarnings("rawtypes")
	public List findByPage(Query query) {
		return findByPage(query, null, null);
	}

	@SuppressWarnings("rawtypes")
	public List findByPage(Criteria criteria) {
		return findByPage(criteria, null, null);
	}

	@SuppressWarnings("rawtypes")
	public Query getQuery_Size(String sqlStr, BaseForm model) {
		String hqlSql = "select count(*) " + getHql(sqlStr, model);

		// System.out.println(hqlSql);

		if (hqlSql == null || hqlSql.length() == 0) {
			return null;
		} else {
			return getCurrentSession().createQuery(hqlSql).setProperties(model);
		}
	}

	@SuppressWarnings("rawtypes")
	public Query getQuery_Sum(String sqlStr, String sumStr, BaseForm model) {
		String hqlSql = "select sum(" + sumStr + ") " + getHql(sqlStr, model);
		if (hqlSql == null || hqlSql.length() == 0) {
			return null;
		} else {
			return getCurrentSession().createQuery(hqlSql).setProperties(model);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public Object findOneByQuery(Query query) {
		return query.uniqueResult();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T findOneByQuery_T(Query query) {
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("rawtypes")
	public Integer findByPage_Size(Query query) {
		return ((Number) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("rawtypes")
	public Object findByPage_DoubleSum(Query query) {
		return query.uniqueResult();
	}

	@SuppressWarnings("rawtypes")
	public Query getSpecialQuery(String sqlStr, BaseForm model, String queryCondition) {
		String hqlSql = "select " + queryCondition + getHql(sqlStr, model);
		if (hqlSql == null || hqlSql.length() == 0) {
			return null;
		} else {
			return getCurrentSession().createQuery(hqlSql).setProperties(model);
		}
	}

	public void updateBySql(String sql) {
		getCurrentSession().createSQLQuery(sql).executeUpdate();
	}

	protected String getKeyWord(BaseForm baseForm) {
		if (baseForm.getKeyword() == null) {
			return "%%";
		}
		return "%" + baseForm.getKeyword() + "%";
	}

	@SuppressWarnings("unchecked")
	public T findOne(Criteria criteria) {
		return (T) criteria.setMaxResults(1).uniqueResult();
	}

	protected void addCriteriaListOrder(Criteria criteria, BaseForm baseForm) {
		addCriteriaListOrder(criteria, baseForm, null);
	}

	protected void addCriteriaListOrder(Criteria criteria, BaseForm baseForm, ArrayList<String> needPinYinList) {
		String orderBy = baseForm.getOrderBy();
		if (StringUtils.isNotEmpty(orderBy)) {
			String[] split = orderBy.split(" ");
			if (split.length == 2) {
				String paramName = split[1];
				boolean needPinYinOrder = false;
				if (needPinYinList != null) {
					if (needPinYinList.contains(paramName)) {
						needPinYinOrder = true;
					}
				}
				if (split[1].equals("asc")) {

					if (needPinYinOrder) {
						OracleOrder.asc(paramName);
					} else {
						criteria.addOrder(Order.asc(split[0]));
					}
					// criteria.add(Restrictions.sqlRestriction("ORDER BY
					// NLSSORT("+split[0]+",'NLS_SORT = SCHINESE_PINYIN_M')
					// asc"));
				} else if (split[1].equals("desc")) {
					if (needPinYinOrder) {
						OracleOrder.desc(paramName);
					} else {
						criteria.addOrder(Order.desc(split[0]));
					}

					// criteria.add(Restrictions.sqlRestriction("ORDER BY
					// NLSSORT("+split[0]+",'NLS_SORT = SCHINESE_PINYIN_M')
					// desc"));
				}

			}
		}
	}

	protected void addProjectRangeAuthorization(Criteria criteria, BaseForm model) {
		if (model.getProjectInfoIdArr() != null && model.getProjectInfoIdArr().length > 0) {
			if (model.getUserId() != null) {
				criteria.add(Restrictions.or(Restrictions.in("project.tableId", model.getProjectInfoIdArr()),
						Restrictions.eq("userStart.tableId", model.getUser().getTableId())));
			} else {
				criteria.add(Restrictions.in("project.tableId", model.getProjectInfoIdArr()));
			}
		}
	}

	protected void addCityRangeAuthorization(Criteria criteria, BaseForm model) {
		if (model.getCityRegionInfoIdArr() != null && model.getCityRegionInfoIdArr().length > 0) {
			if (model.getUserId() != null) {
				criteria.add(Restrictions.or(Restrictions.in("city.tableId", model.getCityRegionInfoIdArr()),
						Restrictions.eq("userStart.tableId", model.getUser().getTableId())));
			} else {
				criteria.add(Restrictions.in("city.tableId", model.getCityRegionInfoIdArr()));
			}

		}
	}

	protected void addBuildingRangeAuthorization(Criteria criteria, BaseForm model) {

		if (model.getBuildingInfoIdIdArr() != null && model.getBuildingInfoIdIdArr().length > 0) {
			if (model.getUserId() != null) {
				criteria.add(Restrictions.or(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()),
						Restrictions.eq("userStart.tableId", model.getUser().getTableId())));
			} else {
				criteria.add(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()));
			}
		}
	}

	protected void addCompanyLimitRange(Criteria criteria, BaseForm model) {
		Sm_User user = model.getUser();
		if (user == null) {
			return;
		}
		Integer theType = user.getTheType();
		if (theType != null) {
			if (theType.equals(S_UserType.ZhengtaiUser)) {
				return;
			}
		}
		if (user != null) {
			Emmp_CompanyInfo userCompany = user.getCompany();

			/**
			 * xsz by time 2019-1-25 18:00:12 此处直接用用户类型判断是否是正泰用户是不合理的，
			 * 这样做导致了监理公司、合作机构等用户也被当做一般用户处理，造成数据查询问题， 应该改为用用户所属开发企业类型判断
			 * 
			 */
			String theType2 = userCompany.getTheType();
			if (S_CompanyType.Development.equals(theType2)) {
				criteria.add(Restrictions.and(Restrictions.eq("company.tableId", userCompany.getTableId())));
			}

			// if (userCompany != null)
			// {
			// criteria.add(Restrictions.and(Restrictions.eq("company.tableId",
			// userCompany.getTableId())));
			// }
		}
	}

	/***** 数据库取数过程 START ！！！其他的方法请写在上边 *****/

	/**
	 * 收入预测 入账资金趋势预测 取数
	 * 
	 * @param count
	 * @return
	 */
	public Double getDayEndBalanceSum(Integer count) {
		// 调用存储过程
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("getDayEndBalanceSum");
		// 设置输入参数
		pc.registerParameter(0, Integer.class, ParameterMode.IN).bindValue(count);
		// 设置输出参数
		pc.registerParameter(1, Double.class, ParameterMode.OUT);
		// 获取输出参数的值

		Double rt = (Double) pc.getOutputs().getOutputParameterValue(1);

		getCurrentSession().flush();
		// 获取输出参数的值
		return rt;
	}

	/**
	 * 收入预测 定期到期金额取数
	 * 
	 * @param dateStart
	 *            开始时间
	 * @param dateEnd
	 *            结束时间
	 * @param type
	 *            0：工作日 1：节假日
	 * @return
	 */
	public Double getDayEndBalanceSum(Long dateStart, Long dateEnd, String type) {
		// 调用存储过程
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("getDepositDou");
		// 设置输入参数
		pc.registerParameter(0, Long.class, ParameterMode.IN).bindValue(dateStart);
		// 设置输入参数
		pc.registerParameter(1, Long.class, ParameterMode.IN).bindValue(dateEnd);
		// 设置输入参数
		pc.registerParameter(2, String.class, ParameterMode.IN).bindValue(type);
		// 设置输出参数
		pc.registerParameter(3, Double.class, ParameterMode.OUT);
		// 获取输出参数的值
		Double rt = (Double) pc.getOutputs().getOutputParameterValue(3);

		getCurrentSession().flush();
		// 获取输出参数的值
		return rt;
	}

	/**
	 * 支出预测 支出资金趋势预测 取数
	 * 
	 * @param count
	 * @return
	 */
	public Double getPayTrendForecastSum(Integer count) {
		// 调用存储过程
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("getPayTrendForecast");
		// 设置输入参数
		pc.registerParameter(0, Integer.class, ParameterMode.IN).bindValue(count);
		// 设置输出参数
		pc.registerParameter(1, Double.class, ParameterMode.OUT);
		// 获取输出参数的值

		Double rt = (Double) pc.getOutputs().getOutputParameterValue(1);

		getCurrentSession().flush();
		// 获取输出参数的值
		return rt;
	}

	/**
	 * 支出预测 拨付资金预测 取数
	 * 
	 * @return
	 */
	public Double getApplyAmountSum() {
		// 调用存储过程
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("getApplyAmountSum");
		// 设置输出参数
		pc.registerParameter(0, Double.class, ParameterMode.OUT);

		Double rt = (Double) pc.getOutputs().getOutputParameterValue(0);

		getCurrentSession().flush();
		// 获取输出参数的值
		return rt;
	}

	/**
	 * 支出预测 可拨付金额预测 取数
	 * 
	 * @return
	 */
	public Double getPayableFundSum() {
		// 调用存储过程
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("getPayableFundSum");
		// 设置输出参数
		pc.registerParameter(0, Double.class, ParameterMode.OUT);
		Double rt = (Double) pc.getOutputs().getOutputParameterValue(0);
		getCurrentSession().flush();

		// 获取输出参数的值
		return rt;
	}

	/**
	 * 支出预测 节点变更拨付
	 * 
	 * @return
	 */
	public Double getNodeChangePayForecastSum(Long timeStamp) {
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("getNodeChangePayForecastSum");
		// 设置输入参数
		pc.registerParameter(0, Long.class, ParameterMode.IN).bindValue(timeStamp);
		// 设置输出参数
		pc.registerParameter(1, Double.class, ParameterMode.OUT);

		Double rt = (Double) pc.getOutputs().getOutputParameterValue(1);

		getCurrentSession().flush();
		// 获取输出参数的值
		return rt;
	}

	/**
	 * 支出预测 节点变更拨付 2.0
	 * 
	 * @return
	 */
	public Double getNodeChangePayForecastSum(String timeStamp) {
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("get_expforecast_pjdsum");
		// 设置输入参数
		pc.registerParameter(0, String.class, ParameterMode.IN).bindValue(timeStamp);
		// 设置输出参数
		pc.registerParameter(1, Double.class, ParameterMode.OUT);

		Double rt = (Double) pc.getOutputs().getOutputParameterValue(1);

		getCurrentSession().flush();
		// 获取输出参数的值
		return rt;
	}

	/**
	 * 楼幢信息导入
	 * 
	 * @param projectId
	 *            项目tableId
	 * @param externalIdStr
	 *            拼接中间库buidingid
	 * @param userid
	 *            操作人id
	 * @return 导入楼幢tableId
	 * 
	 */
	public String buildingImport(Long projectId, String externalIdStr, Long userid) {
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("PRO_BUILDINGIMPORT");
		// 设置输入参数
		pc.registerParameter(0, Long.class, ParameterMode.IN).bindValue(projectId);
		pc.registerParameter(1, String.class, ParameterMode.IN).bindValue(externalIdStr);
		pc.registerParameter(2, Long.class, ParameterMode.IN).bindValue(userid);
		// 设置输出参数
		pc.registerParameter(3, String.class, ParameterMode.OUT);
		pc.registerParameter(4, String.class, ParameterMode.OUT);
		// 获取输出参数的值
		String isSuccess = (String) pc.getOutputs().getOutputParameterValue(3);
		String result = (String) pc.getOutputs().getOutputParameterValue(4);
		if (null == isSuccess || isSuccess.equals("FALSE") || isSuccess.equals("false")) {
			// 判断是否返回成功
			return ";" + result;
		}

		getCurrentSession().flush();

		return result;
	}

	public Session getOpenSession() {
		return sessionFactory.openSession();
	}

	/**
	 * xsz by time 2018-12-9 13:39:09 按权责发生制查询利息情况统计表
	 * 
	 * @param bankId
	 *            银行Id
	 * @param beginTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public Object callFunction(Long bankId, String beginTime, String endTime) {

		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("PRC_GET_ACCOUNTABILITYENQUIRY");
		// 设置输入参数
		pc.registerParameter(0, Long.class, ParameterMode.IN).bindValue(bankId);
		pc.registerParameter(1, String.class, ParameterMode.IN).bindValue(beginTime);
		pc.registerParameter(2, String.class, ParameterMode.IN).bindValue(endTime);
		// 设置输出参数
		pc.registerParameter(3, Object.class, ParameterMode.OUT);

		// 获取输出参数的值
		Object outputParameterValue = pc.getOutputs().getOutputParameterValue(3);

		getCurrentSession().flush();

		return outputParameterValue;

		// String sql="select fun_get_accountabilityenquiry(?,?,?) from dual";
		// NativeQuery nativeQuery =
		// sessionFactory.getCurrentSession().createNativeQuery(sql);
		// nativeQuery.setParameter(1, null);
		// nativeQuery.setParameter(2, beginTime);
		// nativeQuery.setParameter(3, endTime);
		// return nativeQuery.uniqueResult();
	}

	/**
	 * dcg 业务对账查询时，预生成清单TGPF_BALANCEOFACCOUNT
	 * 
	 * @param count
	 * @return
	 */
	public String insert_BalanceOfAccount(long userId, String BILLTIMESTAMP) {
		// 调用存储过程
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("insert_tgpf_BalanceOfAccount");
		// 设置输入参数
		pc.registerParameter(0, long.class, ParameterMode.IN).bindValue(userId);
		pc.registerParameter(1, String.class, ParameterMode.IN).bindValue(BILLTIMESTAMP);
		// 设置输出参数
		pc.registerParameter(2, String.class, ParameterMode.OUT);
		pc.registerParameter(3, String.class, ParameterMode.OUT);
		// 获取输出参数的值

		String flag = pc.getOutputs().getOutputParameterValue(2).toString();
		String ret = pc.getOutputs().getOutputParameterValue(3).toString();

		getCurrentSession().flush();
		// 获取输出参数的值
		return ret;
	}

	/**
	 * xsz by time 2018-12-10 10:49:22 按权责发生制查询利息情况统计表
	 * 
	 * @param bankId
	 *            开户行Id
	 * @param beginTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> callFunction2(Long inBankBranchId, String beginTime, String endTime)
			throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		try {

			String sql = "{call PRC_GET_ACCOUNTABILITYENQUIRY(?,?,?,?)}";
			CallableStatement sp = connection.prepareCall(sql);

			// 设置参数
			sp.setLong(1, inBankBranchId);
			sp.setString(2, beginTime);
			sp.setString(3, endTime);
			// 游标类型
			sp.registerOutParameter(4, OracleTypes.CURSOR);

			sp.execute(); // 执行存储过程
			ResultSet rs = (ResultSet) sp.getObject(4); // 获取返回的对象,再将对象转为记录集

			while (rs.next()) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("TABLEID", ObjectUtils.toString(rs.getObject(1)));
				resultMap.put("BANK", ObjectUtils.toString(rs.getObject(2)));
				resultMap.put("BANKOFDEPOSIT", ObjectUtils.toString(rs.getObject(3)));
				resultMap.put("DEPOSITPROPERTY", ObjectUtils.toString(rs.getObject(4)));
				resultMap.put("ESCROWACCOUNT", ObjectUtils.toString(rs.getObject(5)));
				resultMap.put("ESCROWACOUNTNAME", ObjectUtils.toString(rs.getObject(6)));

				resultMap.put("RECORDDATE", ObjectUtils.toString(rs.getObject(7)));
				resultMap.put("LOADTIME", ObjectUtils.toString(rs.getObject(8)));
				resultMap.put("EXPIRATIONTIME", ObjectUtils.toString(rs.getObject(9)));
				resultMap.put("AMOUNTDEPOSITED", ObjectUtils.toString(rs.getObject(10)));

				resultMap.put("DEPOSITCEILINGS", ObjectUtils.toString(rs.getObject(11)));
				resultMap.put("INTERESTRATE", ObjectUtils.toString(rs.getObject(12)));
				resultMap.put("FATE", ObjectUtils.toString(rs.getObject(13)));
				resultMap.put("INTEREST", ObjectUtils.toString(rs.getObject(14)));

				list.add(resultMap);
			}

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			connection.close();
			getCurrentSession().flush();
		}
		return list;
	}

	/**
	 * 项目风险明细表调用存储过程查询
	 * 
	 * @param PI_QY
	 *            区域Id
	 * @param PI_PROJECT
	 *            项目Id
	 * @param PI_RQ
	 *            托管日期
	 * @param PI_ISDY
	 *            是否抵押
	 * @param PI_RISKRANK
	 *            风险评级
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> postPZ(String pi_pc, String pi_type, String pi_user) throws SQLException {

		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("prc_insert_tgpz_edit");
		// ProcedureCall pc =
		// getCurrentSession().createStoredProcedureCall("prc_insert_tgpz");

		// 设置输入参数
		pc.registerParameter(0, String.class, ParameterMode.IN).bindValue(pi_pc);
		pc.registerParameter(1, String.class, ParameterMode.IN).bindValue(pi_type);
		pc.registerParameter(2, String.class, ParameterMode.IN).bindValue(pi_user);
		// 设置输出参数
		pc.registerParameter(3, String.class, ParameterMode.OUT);
		pc.registerParameter(4, String.class, ParameterMode.OUT);

		// 获取输出参数的值
		String pi_signValue = pc.getOutputs().getOutputParameterValue(3).toString();
		String po_retValue = pc.getOutputs().getOutputParameterValue(4).toString();

		Map<String, String> returnMap = new HashMap<String, String>();

		returnMap.put("pi_sign", pi_signValue);
		returnMap.put("po_ret", po_retValue);

		getCurrentSession().flush();

		return returnMap;

	}

	/**
	 * 留存权益计算
	 * 
	 * @param tgpf_BuildingRemainRightLogId
	 *            楼栋每日留存权益计算日志主键
	 * @return 返回信息
	 * 
	 */
	public Properties buildingRemainRight(Long tgpf_BuildingRemainRightLogId) {

		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("PRC_Calculation_Retain");
		// 设置输入参数
		pc.registerParameter(0, Long.class, ParameterMode.IN).bindValue(tgpf_BuildingRemainRightLogId);
		// 设置输出参数
		pc.registerParameter(1, String.class, ParameterMode.OUT);
		pc.registerParameter(2, String.class, ParameterMode.OUT);
		// 获取输出参数的值
		String isSuccess = (String) pc.getOutputs().getOutputParameterValue(1);
		String result = (String) pc.getOutputs().getOutputParameterValue(2);
		if (null == isSuccess || isSuccess.equals("FALSE") || isSuccess.equals("false")) {
			MyBackInfo.fail(properties, result);
		}

		getCurrentSession().flush();

		return properties;
	}

	/**
	 * 网银对账预处理
	 * 
	 * @param PI_JZRQ
	 *            记账日期
	 * @return 返回信息
	 * 
	 */
	public Properties PreCompareofWY(Long tgpf_BalanceOfAccountId) {

		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("prc_wydz");

		// 设置输入参数
		pc.registerParameter(0, Long.class, ParameterMode.IN).bindValue(tgpf_BalanceOfAccountId);

		// 设置输出参数
		pc.registerParameter(1, String.class, ParameterMode.OUT);

		// 获取输出参数的值
		String isSuccess = pc.getOutputs().getOutputParameterValue(1).toString();

		if (null == isSuccess || isSuccess.equals("FALSE") || isSuccess.equals("false")) {
			MyBackInfo.fail(properties, isSuccess);
		}

		getCurrentSession().flush();

		return properties;
	}

	/**
	 * 日终结算计算
	 * 
	 * @param PI_JZRQ
	 *            记账日期
	 * @return 返回信息
	 * 
	 */
	public Properties dayEndBalancing(String PI_JZRQ) {

		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("PRC_DayEndBalancing");
		// 设置输入参数
		pc.registerParameter(0, String.class, ParameterMode.IN).bindValue(PI_JZRQ);
		// 设置输出参数
		pc.registerParameter(1, String.class, ParameterMode.OUT);
		pc.registerParameter(2, String.class, ParameterMode.OUT);
		// 获取输出参数的值
		String isSuccess = (String) pc.getOutputs().getOutputParameterValue(1);
		String result = (String) pc.getOutputs().getOutputParameterValue(2);
		if (null == isSuccess || isSuccess.equals("FALSE") || isSuccess.equals("false")) {
			MyBackInfo.fail(properties, result);
		}

		getCurrentSession().flush();

		return properties;
	}

	/**
	 * 楼幢信息更新 同步户室信息
	 * 
	 * @param buildingId
	 *            楼幢tableId
	 * @param userid
	 *            操作人id
	 * @return
	 * 
	 */
	public String buildingUpdate(Long buildingId, Long userId) {
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("PRO_BUILDINGUPDATE");
		// 设置输入参数
		pc.registerParameter(0, Long.class, ParameterMode.IN).bindValue(buildingId);
		pc.registerParameter(1, Long.class, ParameterMode.IN).bindValue(userId);
		// 设置输出参数
		pc.registerParameter(2, String.class, ParameterMode.OUT);
		pc.registerParameter(3, String.class, ParameterMode.OUT);
		// 获取输出参数的值
		String isSuccess = (String) pc.getOutputs().getOutputParameterValue(2);
		String result = (String) pc.getOutputs().getOutputParameterValue(3);
		if (null == isSuccess || isSuccess.equals("FALSE") || isSuccess.equals("false")) {
			// 判断是否返回成功
			return ";" + result;
		}

		getCurrentSession().flush();

		return result;
	}


	/**
	 *
	 *
	 * @param count
	 * @return
	 */
	public Double getZJCDZBALL() {
		// 调用存储过程
		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("F_TC_ZJCDZBALL");
		// 设置输入参数
//		pc.registerParameter(0, Integer.class, ParameterMode.IN).bindValue(count);
		// 设置输出参数
		pc.registerParameter(0, Double.class, ParameterMode.OUT);
		// 获取输出参数的值

		Double rt = (Double) pc.getOutputs().getOutputParameterValue(1);

		getCurrentSession().flush();
		// 获取输出参数的值
		return rt;
	}

	/**
	 * 
	 * @param userId
	 *            用户Id
	 * @param keyWord
	 *            关键字
	 * @param companyId
	 *            企业Id
	 * @param projectId
	 *            项目Id
	 * @param buildingId
	 *            楼幢Id
	 * @param billTimeStart
	 *            开始时间
	 * @param billTimeEnd
	 *            结束时间
	 * @param pageNumber
	 *            页码
	 * @param countPerPage
	 *            每页显示条数
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getRetainedRightsView2(Long userId, String keyWord, Long companyId, Long projectId,
			Long buildingId, String billTimeStart, String billTimeEnd, Integer pageNumber, Integer countPerPage)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_RetainedRightsView(?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;

		try {
			// 设置参数
			sp.setLong(1, userId);
			sp.setString(2, keyWord);
			sp.setString(3, null == companyId ? null : companyId.toString());
			sp.setString(4, null == projectId ? null : projectId.toString());
			sp.setString(5, null == buildingId ? null : buildingId.toString());
			sp.setString(6, billTimeStart);
			sp.setString(7, billTimeEnd);
			sp.setInt(8, pageNumber);
			sp.setInt(9, countPerPage);

			// 游标类型
			sp.registerOutParameter(10, OracleTypes.CURSOR);
			sp.registerOutParameter(11, OracleTypes.INTEGER);
			sp.registerOutParameter(12, OracleTypes.INTEGER);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(10);

			List<Tg_RetainedRightsView2> tg_RetainedRightsList = new ArrayList<Tg_RetainedRightsView2>();
			Tg_RetainedRightsView2 tg_RetainedRightsView = null;

			while (rs.next()) {
				tg_RetainedRightsView = new Tg_RetainedRightsView2();

				// 主键
				tg_RetainedRightsView.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));
				// 留存权益计算日期
				tg_RetainedRightsView.setBilltTimeStamp(ObjectUtils.toString(removeNullString(rs.getObject(2))));
				// 到账日期
				tg_RetainedRightsView.setArrivalTimeStamp(ObjectUtils.toString(removeNullString(rs.getObject(3))));
				// 企业名称
				tg_RetainedRightsView.setSellerName(ObjectUtils.toString(removeNullString(rs.getObject(4))));
				// 项目名称
				tg_RetainedRightsView.setProjectName(ObjectUtils.toString(removeNullString(rs.getObject(5))));
				// 楼幢施工编号
				tg_RetainedRightsView.setEcodeFromConstruction(ObjectUtils.toString(removeNullString(rs.getObject(6))));
				// 单元信息
				tg_RetainedRightsView.setEcodeOfBuildingUnit(ObjectUtils.toString(removeNullString(rs.getObject(7))));
				// 房间号
				tg_RetainedRightsView.setEcodeFromRoom(ObjectUtils.toString(removeNullString(rs.getObject(8))));
				// 买受人名称
				tg_RetainedRightsView.setBuyer(ObjectUtils.toString(removeNullString(rs.getObject(9))));

				// 借款人名称
				tg_RetainedRightsView.setTheNameOfCreditor(ObjectUtils.toString(removeNullString(rs.getObject(10))));
				// 借款人身份证
				tg_RetainedRightsView.setIdNumberOfCreditor(ObjectUtils.toString(removeNullString(rs.getObject(11))));
				// 合同备案号
				tg_RetainedRightsView
						.setEcodeOfContractRecord(ObjectUtils.toString(removeNullString(rs.getObject(12))));
				// 三方协议号
				tg_RetainedRightsView
						.setEcodeoftripleagreement(ObjectUtils.toString(removeNullString(rs.getObject(13))));

				// 实际入账金额
				tg_RetainedRightsView.setActualDepositAmount(
						Double.valueOf(ObjectUtils.toString(null == rs.getObject(14) ? 0.00 : rs.getObject(14))));
				// 按揭入账金额
				tg_RetainedRightsView.setDepositAmountFromloan(
						Double.valueOf(ObjectUtils.toString(null == rs.getObject(15) ? 0.00 : rs.getObject(15))));
				// 留存权益总金额
				tg_RetainedRightsView.setTheAmount(
						Double.valueOf(ObjectUtils.toString(null == rs.getObject(16) ? 0.00 : rs.getObject(16))));
				// 未到期权益金额
				tg_RetainedRightsView.setAmountOfInterestNotdue(
						Double.valueOf(ObjectUtils.toString(null == rs.getObject(17) ? 0.00 : rs.getObject(17))));
				// 到期权益金额
				tg_RetainedRightsView.setAmountOfInterestdue(
						Double.valueOf(ObjectUtils.toString(null == rs.getObject(18) ? 0.00 : rs.getObject(18))));
				// 留存权益系数
				tg_RetainedRightsView.setRemaincoefficient(ObjectUtils.toString(removeNullString(rs.getObject(19))));

				tg_RetainedRightsList.add(tg_RetainedRightsView);
			}

			map.put("tg_RetainedRightsList", tg_RetainedRightsList);

			int totalPage = sp.getInt(11);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(12);

			map.put("totalCount", totalCount);

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
			/*
			 * if(sessionFactory!=null) {
			 * sessionFactory.getCurrentSession().flush(); }
			 */
		}
		return map;
	}

	private Object removeNullString(Object obj) {
		return null == obj ? "" : obj;
	}

	/**
	 * 
	 * @param buildingId
	 *            楼幢
	 * @param nowLimitRatio
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getBldLimitAmountVerAfDtl(Long buildingId, String nowLimitRatio) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_BldLimitAmountVerAfDtl(?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		try {
			// 设置参数
			sp.setLong(1, buildingId);
			sp.setString(2, nowLimitRatio);

			// 游标类型
			sp.registerOutParameter(3, OracleTypes.CURSOR);
			sp.registerOutParameter(4, OracleTypes.VARCHAR);
			sp.registerOutParameter(5, OracleTypes.VARCHAR);
			// 执行存储过程
			sp.execute();

			map.put("sign", sp.getObject(4));
			map.put("info", sp.getObject(5));
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(3);

			List<HashMap<String, Object>> versionList = new ArrayList<>();
			HashMap<String, Object> version = null;

			if (null != rs) {
				while (rs.next()) {
					version = new HashMap<>();

					version.put("limitedAmount", rs.getString("LIMITEDAMOUNT"));
					version.put("stageName", rs.getString("STAGENAME"));
					version.put("tableId", rs.getString("TABLEID"));
					version.put("theName", rs.getString("STAGENAME"));

					versionList.add(version);
				}

				map.put("versionList", versionList);
			}

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return map;
	}

	/**
	 * 一般拨付完成
	 * 
	 * @param AF_ID
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> update_FundAppropriated_Final(Long AF_ID) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call update_FundAppropriated_Final(?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		try {
			// 设置参数
			sp.setLong(1, AF_ID);

			// 游标类型
			sp.registerOutParameter(2, OracleTypes.CURSOR);
			sp.registerOutParameter(3, OracleTypes.VARCHAR);
			sp.registerOutParameter(4, OracleTypes.VARCHAR);
			// 执行存储过程
			sp.execute();

			map.put("sign", sp.getObject(3));
			map.put("info", sp.getObject(4));
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(2);

			HashMap<Long, Double> buildMap = new HashMap<>();

			if (null != rs) {
				while (rs.next()) {
					buildMap.put(rs.getLong("BUILDING"), 0.00);
				}
			}

			map.put("buildMap", buildMap);

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return map;
	}

	/**
	 * 用款申请提交
	 * 
	 * @param AF_ID
	 * @return
	 */
	public Map<String, Object> update_FundAppropriated_Submit(Long AF_ID) {

		Map<String, Object> map = new HashMap<>();

		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("update_FundAppropriated_Submit");
		// 设置输入参数
		pc.registerParameter(0, Long.class, ParameterMode.IN).bindValue(AF_ID);
		// 设置输出参数
		pc.registerParameter(1, String.class, ParameterMode.OUT);
		pc.registerParameter(2, String.class, ParameterMode.OUT);
		// 获取输出参数的值
		String isSuccess = (String) pc.getOutputs().getOutputParameterValue(1);
		String result = (String) pc.getOutputs().getOutputParameterValue(2);

		map.put("sign", isSuccess);
		map.put("info", result);

		getCurrentSession().flush();

		return map;
	}

	/**
	 * 三方协议计量结算列表预加载
	 * 
	 * @param pi_userid
	 * @param pi_companyid
	 * @param pi_starttime
	 * @param pi_endtime
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> query_Coopagreementdtllist(Long pi_userid, Long pi_companyid, Long pi_starttime,
			Long pi_endtime) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Tgxy_CoopAgreementSettleDtl> list = new ArrayList<>();
		Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl = new Tgxy_CoopAgreementSettleDtl();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call query_coopagreementdtllist(?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		try {
			// 设置参数
			sp.setLong(1, pi_userid);
			sp.setLong(2, pi_companyid);
			sp.setLong(3, pi_starttime);
			sp.setLong(4, pi_endtime);

			// 游标类型
			sp.registerOutParameter(5, OracleTypes.CURSOR);
			sp.registerOutParameter(6, OracleTypes.VARCHAR);
			sp.registerOutParameter(7, OracleTypes.VARCHAR);
			sp.registerOutParameter(8, OracleTypes.NUMBER);
			// 执行存储过程
			sp.execute();

			map.put("sign", sp.getObject(6));
			map.put("info", sp.getObject(7));
			map.put("beforeNumbers", sp.getObject(8));
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(5);

			// HashMap<Long,Double> buildMap = new HashMap<>();

			if (null != rs) {
				// Empj_ProjectInfo projectInfo = new Empj_ProjectInfo();
				Empj_HouseInfo houseInfo = new Empj_HouseInfo();
				while (rs.next()) {
					tgxy_CoopAgreementSettleDtl = new Tgxy_CoopAgreementSettleDtl();
					tgxy_CoopAgreementSettleDtl.setTheState(S_TheState.Normal);
					tgxy_CoopAgreementSettleDtl.setCreateTimeStamp(System.currentTimeMillis());
					tgxy_CoopAgreementSettleDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
					// tgxy_CoopAgreementSettleDtl.setRecordTimeStamp(rs.getLong("RECORDTIMESTAMP"));
					// tgxy_CoopAgreementSettleDtl.seteCode(rs.getString("ECODEOFTRIPLEAGREEMENT"));
					// tgxy_CoopAgreementSettleDtl.setAgreementDate(rs.getString("STARTTIMESTAMP"));
					// tgxy_CoopAgreementSettleDtl.setSeller(rs.getString("SellerName"));
					// tgxy_CoopAgreementSettleDtl.setBuyer(rs.getString("BuyerName"));
					//// projectInfo =
					// projectInfoDao.findById(rs.getLong("Project"));
					// tgxy_CoopAgreementSettleDtl.setProject(null);
					// tgxy_CoopAgreementSettleDtl.setTheNameOfProject(rs.getString("TheNameOfProject"));
					// tgxy_CoopAgreementSettleDtl.setBuildingInfo(null);
					// tgxy_CoopAgreementSettleDtl.seteCodeOfBuilding(rs.getString("eCodeOfBuilding"));
					// tgxy_CoopAgreementSettleDtl.seteCodeFromConstruction(rs.getString("eCodeFromConstruction"));

					houseInfo = houseInfoDao.findById(rs.getLong("HOUSE"));
					tgxy_CoopAgreementSettleDtl.setUnitInfo(houseInfo.getUnitInfo());
					tgxy_CoopAgreementSettleDtl.setHouseInfo(houseInfo);
					tgxy_CoopAgreementSettleDtl.setTableId(rs.getLong("tableId"));
					// tgxy_CoopAgreementSettleDtl.setHouseInfoName(rs.getString("UNITROOM"));

					list.add(tgxy_CoopAgreementSettleDtl);

				}
			}

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		map.put("list", list);
		return map;
	}

	/**
	 * 三方协议计量结算保存
	 * 
	 * @param PI_TABLEID
	 * @param PI_USERID
	 * @param PI_COMPANYID
	 * @param PI_STARTTIME
	 * @param PI_ENDTIME
	 * @return
	 */
	public Map<String, Object> QUERY_COOPAGREEMENT_SAVE(Long PI_TABLEID, Long PI_USERID, Long PI_COMPANYID,
			Long PI_STARTTIME, Long PI_ENDTIME) throws SQLException {

		Map<String, Object> map = new HashMap<>();

		// ProcedureCall pc =
		// getCurrentSession().createStoredProcedureCall("QUERY_COOPAGREEMENT_SAVE");
		// // 设置输入参数
		// pc.registerParameter(0, Long.class,
		// ParameterMode.IN).bindValue(PI_TABLEID);
		// pc.registerParameter(1, Long.class,
		// ParameterMode.IN).bindValue(PI_USERID);
		// pc.registerParameter(2, Long.class,
		// ParameterMode.IN).bindValue(PI_COMPANYID);
		// pc.registerParameter(3, Long.class,
		// ParameterMode.IN).bindValue(PI_STARTTIME);
		// pc.registerParameter(4, Long.class,
		// ParameterMode.IN).bindValue(PI_ENDTIME);
		// 设置输出参数
		// pc.registerParameter(5, String.class, ParameterMode.OUT);
		// pc.registerParameter(6, String.class, ParameterMode.OUT);
		// 获取输出参数的值
		// String isSuccess = (String)
		// pc.getOutputs().getOutputParameterValue(5);
		// String result = (String) pc.getOutputs().getOutputParameterValue(6);

		// map.put("sign", isSuccess);
		// map.put("info", result);

		// getCurrentSession().flush();

		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call QUERY_COOPAGREEMENT_SAVE(?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		try {
			// 设置参数
			sp.setLong(1, PI_TABLEID);
			sp.setLong(2, PI_USERID);
			sp.setLong(3, PI_COMPANYID);
			sp.setLong(4, PI_STARTTIME);
			sp.setLong(5, PI_ENDTIME);

			// 游标类型
			sp.registerOutParameter(6, OracleTypes.VARCHAR);
			sp.registerOutParameter(7, OracleTypes.VARCHAR);
			// 执行存储过程
			sp.execute();

			map.put("sign", sp.getObject(6));
			map.put("info", sp.getObject(7));
			// 获取返回的对象,再将对象转为记录集

			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

		return map;
	}

	/**
	 * 存单推送
	 * 
	 * @param str
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> prc_Insert_Cdgl(String str, String userId) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call prc_insert_cdgl(?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		try {
			// 设置参数
			sp.setString(1, str);
			sp.setString(2, userId);

			// 游标类型
			sp.registerOutParameter(3, OracleTypes.VARCHAR);
			sp.registerOutParameter(4, OracleTypes.VARCHAR);
			// 执行存储过程
			sp.execute();

			map.put("sign", sp.getObject(3));
			map.put("info", sp.getObject(4));

			sp.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return map;
	}

	/**
	 * 三方协议计量结算保存
	 * 
	 * @param PI_TABLEID
	 * @param PI_USERID
	 * @param PI_COMPANYID
	 * @param PI_STARTTIME
	 * @param PI_ENDTIME
	 * @return
	 */
	public Map<String, Object> QUERY_COOPAGREEMENT_ACTION(Long PI_TABLEID, Long PI_USERID, String PI_ACTION)
			throws SQLException {

		Map<String, Object> map = new HashMap<>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call QUERY_COOPAGREEMENT_ACTION(?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		try {
			// 设置参数
			sp.setLong(1, PI_TABLEID);
			sp.setLong(2, PI_USERID);
			sp.setString(3, PI_ACTION);

			// 游标类型
			sp.registerOutParameter(4, OracleTypes.VARCHAR);
			sp.registerOutParameter(5, OracleTypes.VARCHAR);
			// 执行存储过程
			sp.execute();

			map.put("sign", sp.getObject(4));
			map.put("info", sp.getObject(5));
			// 获取返回的对象,再将对象转为记录集

			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

		return map;
	}

	/**
	 * 校验受限额度是否被
	 * 
	 * @param PI_TABLEID
	 * @return
	 * @throws SQLException
	 */
	public String PRO_CHECKISREJECT(Long PI_TABLEID) throws SQLException {

		String po_ret = "0";

		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call PRO_CHECKISREJECT(?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		try {
			// 设置参数
			sp.setLong(1, PI_TABLEID);

			// 输出参数
			sp.registerOutParameter(2, OracleTypes.VARCHAR);
			// 执行存储过程
			sp.execute();

			// 获取输出参数
			po_ret = (String) sp.getObject(2);

			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return po_ret;
	}

	/**
	 * 项目部报表-楼幢销售面积查询
	 * 
	 * @param userId
	 *            用户Id
	 * @param keyWord
	 *            关键字
	 * @param companyId
	 *            企业Id
	 * @param projectId
	 *            项目Id
	 * @param buildingId
	 *            楼幢Id
	 * @param pageNumber
	 *            页码
	 * @param countPerPage
	 *            每页显示条数
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> saleAreaForBuildingList(Long userId, String keyWord, Long companyId, Long projectId,
			Long buildingId, Integer pageNumber, Integer countPerPage) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_building_salearea(?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;

		try {
			// 设置参数
			sp.setString(1, null == companyId ? null : companyId.toString());
			sp.setString(2, null == projectId ? null : projectId.toString());
			sp.setString(3, null == buildingId ? null : buildingId.toString());
			sp.setInt(4, pageNumber);
			sp.setInt(5, countPerPage);

			// 游标类型
			sp.registerOutParameter(6, OracleTypes.CURSOR);
			sp.registerOutParameter(7, OracleTypes.INTEGER);
			sp.registerOutParameter(8, OracleTypes.INTEGER);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(6);

			List<Map<String, Object>> listMap = new ArrayList<>();
			Map<String, Object> map_1;

			while (rs.next()) {
				map_1 = new HashMap<>();

				map_1.put("tableId", Long.valueOf(ObjectUtils.toString(removeNullString(rs.getObject(1)))));
				map_1.put("theNameOfCompany", ObjectUtils.toString(removeNullString(rs.getObject(2))));
				map_1.put("theNameOfProject", ObjectUtils.toString(removeNullString(rs.getObject(3))));
				map_1.put("theNameOfBuilding", ObjectUtils.toString(removeNullString(rs.getObject(4))));
				map_1.put("price",
						Double.valueOf(ObjectUtils.toString(null == rs.getObject(5) ? 0.00 : rs.getObject(5))));
				map_1.put("escrowarea",
						Double.valueOf(ObjectUtils.toString(null == rs.getObject(6) ? 0.00 : rs.getObject(6))));
				map_1.put("salemj",
						Double.valueOf(ObjectUtils.toString(null == rs.getObject(7) ? 0.00 : rs.getObject(7))));
				map_1.put("mj", Double.valueOf(ObjectUtils.toString(null == rs.getObject(8) ? 0.00 : rs.getObject(8))));

				listMap.add(map_1);
			}

			map.put("saleAreaForBuildingList", listMap);

			int totalPage = sp.getInt(7);
			map.put("totalPage", totalPage);
			int totalCount = sp.getInt(8);
			map.put("totalCount", totalCount);

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return map;
	}

	/**
	 * 工程进度节点楼幢列表加载
	 * 
	 * @param projectId
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> query_Get_buidling_change(Long projectId) throws SQLException {
		Map<String, Object> map;
		List<Map<String, Object>> list = new ArrayList<>();

		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call get_buidling_change(?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		try {
			// 设置参数
			sp.setLong(1, projectId);
			// 游标类型
			sp.registerOutParameter(2, OracleTypes.CURSOR);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(2);

			if (null != rs) {
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("disabled", rs.getString("DISABLED"));
					map.put("escrowStandard", rs.getString("ESCROWSTANDARD"));
					map.put("escrowStandardName", rs.getString("ESCROWSTANDARDNAME"));
					map.put("upfloorNumber", rs.getString("UPFLOORNUMBER"));
					map.put("eCodeFromConstruction", rs.getString("ECODEFROMCONSTRUCTION"));
					map.put("escrowArea", rs.getString("ESCROWAREA"));
					map.put("tableId", rs.getString("TABLEID"));
					map.put("currentLimitedRatio", rs.getString("CURRENTLIMITEDRATIO"));
					map.put("bldLimitAmountName", rs.getString("BLDLIMITAMOUNTNAME"));
					map.put("limitedAmount", rs.getString("LIMITEDAMOUNT"));
					map.put("deliveryTypeName", rs.getString("DELIVERYTYPENAME"));
					map.put("orgLimitedAmount", rs.getString("ORGLIMITEDAMOUNT"));
					map.put("nowLimitedAmount", rs.getString("NOWLIMITEDAMOUNT"));
					map.put("deliveryType", rs.getString("DELIVERYTYPE"));
					map.put("downfloorNumber", rs.getString("DOWNFLOORNUMBER"));
					map.put("recordAvgPriceOfBuilding", rs.getString("RECORDAVGPRICEOFBUILDING"));
					map.put("signingDate", rs.getString("SIGNINGDATE"));
					map.put("limitedId", rs.getString("LIMITEDID"));
					map.put("busiState", rs.getString("BUSISTATE"));
					map.put("bldLimitAmountId", rs.getString("BLDLIMITAMOUNTID"));
					map.put("projectId", rs.getString("PROJECTID"));

					list.add(map);

				}
			}

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

		return list;
	}

	/**
	 * 合作协议提交更新
	 * 
	 * @param tableId
	 *            主键
	 * @return 返回信息
	 * 
	 */
	public Properties buildingPREsale(Long tableId) {

		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("GET_buildingPREsale");
		// 设置输入参数
		pc.registerParameter(0, Long.class, ParameterMode.IN).bindValue(tableId);
		/*
		 * // 设置输出参数 pc.registerParameter(1, String.class, ParameterMode.OUT);
		 * pc.registerParameter(2, String.class, ParameterMode.OUT); // 获取输出参数的值
		 * String isSuccess = (String)
		 * pc.getOutputs().getOutputParameterValue(1); String result = (String)
		 * pc.getOutputs().getOutputParameterValue(2); if (null == isSuccess ||
		 * isSuccess.equals("FALSE") || isSuccess.equals("false")) {
		 * MyBackInfo.fail(properties, result); }
		 */

		getCurrentSession().flush();

		return properties;
	}

	/**
	 * 基本户、非基本户凭证推送
	 * 
	 * @param str
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> prc_Insert_pzts(String type, String str, String userId) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call prc_insert_pzts(?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		try {
			// 设置参数
			sp.setString(1, type);
			sp.setString(2, str);
			sp.setString(3, userId);

			// 游标类型
			sp.registerOutParameter(4, OracleTypes.VARCHAR);
			sp.registerOutParameter(5, OracleTypes.VARCHAR);
			// 执行存储过程
			sp.execute();

			map.put("sign", sp.getObject(4));
			map.put("info", sp.getObject(5));

			sp.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return map;
	}

	/**
	 * 节点变更预测
	 * 
	 * @param dateStr
	 *            日期
	 * @param pageNumber
	 *            目标页
	 * @param countPerPage
	 *            每页显示记录数
	 * @return
	 * @throws SQLException
	 */
	public List<Qs_NodeChangeForeCast_View> getNodeChangeForeCast(String dateStr, Integer pageNumber,
			Integer countPerPage) throws SQLException {

		List<Qs_NodeChangeForeCast_View> qs_RecordAmount_ViewList = new ArrayList<>();
		Qs_NodeChangeForeCast_View view;
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_QS_NODECHANGEFORECAST_VIEW(?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;

		try {
			// 设置参数
			sp.setString(1, dateStr);
			sp.setInt(2, pageNumber);
			sp.setInt(3, countPerPage);

			// 游标类型
			sp.registerOutParameter(4, OracleTypes.CURSOR);
			sp.registerOutParameter(5, OracleTypes.INTEGER);
			sp.registerOutParameter(6, OracleTypes.INTEGER);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(4);

			while (rs.next()) {
				view = new Qs_NodeChangeForeCast_View();
				/*
				 * //到账日期 tg_RetainedRightsView.setArrivalTimeStamp(ObjectUtils.
				 * toString(removeNullString(rs.getObject(3)))); //按揭入账金额
				 * tg_RetainedRightsView.setDepositAmountFromloan(Double.valueOf
				 * (ObjectUtils.toString(null ==
				 * rs.getObject(15)?0.00:rs.getObject(15))));
				 */

				view.setTABLEID(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));
				view.setCOMMPANYNAME(ObjectUtils.toString(removeNullString(rs.getObject(2))));
				view.setPROJECTNAME(ObjectUtils.toString(removeNullString(rs.getObject(4))));
				view.setBUILDCODE(ObjectUtils.toString(removeNullString(rs.getObject(6))));
				view.setCURRENTESCROWFUND(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(7) ? 0.00 : rs.getObject(7))));
				view.setORGLIMITEDAMOUNT(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(8) ? 0.00 : rs.getObject(8))));
				view.setCASHLIMITEDAMOUNT(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(9) ? 0.00 : rs.getObject(9))));
				view.setCURRENTFIGUREPROGRESS(ObjectUtils.toString(removeNullString(rs.getObject(10))));
				view.setCURRENTLIMITEDRATIO(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(11) ? 0.00 : rs.getObject(11))));
				view.setNODELIMITEDAMOUNT(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(12) ? 0.00 : rs.getObject(12))));
				view.setEFFECTIVELIMITEDAMOUNT(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(13) ? 0.00 : rs.getObject(13))));
				view.setFORECASTNODENAME(ObjectUtils.toString(removeNullString(rs.getObject(14))));
				view.setLIMITEDAMOUNT(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(15) ? 0.00 : rs.getObject(15))));
				view.setNODELIMITAMOUNT(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(16) ? 0.00 : rs.getObject(16))));
				view.setEFFLIMITAMOUNT(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(17) ? 0.00 : rs.getObject(17))));
				view.setAPPAMOUNT(
						new BigDecimal(ObjectUtils.toString(null == rs.getObject(18) ? 0.00 : rs.getObject(18))));

				qs_RecordAmount_ViewList.add(view);
			}

			int totalPage = sp.getInt(5);
			int totalCount = sp.getInt(6);

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return qs_RecordAmount_ViewList;
	}

	/**
	 * 待办预处理
	 * @throws SQLException 
	 */
	public Properties updateAfState() throws SQLException {

		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Connection connection = null;
		CallableStatement sp = null;
		try {
			connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
			String sql = "{call updateafstate()}";
			sp = connection.prepareCall(sql);
			
			sp.execute();
			
			sp.close();
			
		} catch (Exception e) {
			System.out.println("初始化审批流程结束：" + e.getMessage());
		} finally {
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

		return properties;
	}


	/**
	 * 删除对账异常的数据
	 *
	 * @param deletedzdata
	 *            记账日期
	 * @return 返回信息
	 *
	 */
	public Properties deletedzdata(String billTimeStamp,String theNameOfBankAccountEscrowed) {


		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		ProcedureCall pc = getCurrentSession().createStoredProcedureCall("prc_deletedzdata");

		// 设置输入参数
		pc.registerParameter(0, String.class, ParameterMode.IN).bindValue(billTimeStamp);
		// 设置输入参数
		pc.registerParameter(1, String.class, ParameterMode.IN).bindValue(theNameOfBankAccountEscrowed);

		// 设置输出参数
		pc.registerParameter(2, String.class, ParameterMode.OUT);
		// 设置输出参数
		pc.registerParameter(3, String.class, ParameterMode.OUT);

		// 获取输出参数的值
		String isSuccess = pc.getOutputs().getOutputParameterValue(2).toString();

		if (null == isSuccess || isSuccess.equals("FALSE") || isSuccess.equals("false")) {
			MyBackInfo.fail(properties, isSuccess);
		}

		getCurrentSession().flush();

		return properties;
	}

}
